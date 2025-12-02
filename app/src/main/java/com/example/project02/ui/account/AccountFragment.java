package com.example.project02.ui.account;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.example.project02.ui.LoginActivity;
import com.example.project02.R;
import com.example.project02.Database.PantryManagerRepository;
import com.example.project02.Database.Entities.User;

public class AccountFragment extends Fragment {

    private PantryManagerRepository repository;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_account, container, false);
        
        repository = new PantryManagerRepository(requireActivity().getApplication());

//        final TextView textView = root.findViewById(R.id.text_account);
//        textView.setText("This is the Account fragment");

        Button logoutButton = root.findViewById(R.id.logout_button);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });

        Button deleteAccountButton = root.findViewById(R.id.delete_account_button);
        deleteAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = getActivity().getIntent().getStringExtra("username");
                if (username != null) {
                    User user = repository.getUserByUsername(username);
                    if (user != null) {
                        repository.deleteUser(user);
                        repository.deleteUserLogs(user);
                        Intent intent = new Intent(getActivity(), LoginActivity.class);
                        startActivity(intent);
                        getActivity().finish();
                    }
                }
            }
        });

        return root;
    }
}
