package com.example.project02.util;

import android.content.Context;
import android.content.Intent;

import com.example.project02.AddFoodActivity;

public class IntentFactory {

    private IntentFactory() {}

    public static Intent getAddFoodActivityIntent(Context context) {
        return new Intent(context, AddFoodActivity.class);
    }

}
