package com.example.project02.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.example.project02.Database.Entities.User;

public class UserViewModel extends ViewModel {
    private final MutableLiveData<User> selectedUser = new MutableLiveData<>();

    public void selectUser(User user) {
        selectedUser.setValue(user);
    }

    public LiveData<User> getSelectedUser() {
        return selectedUser;
    }
}
