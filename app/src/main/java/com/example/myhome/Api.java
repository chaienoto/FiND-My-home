package com.example.myhome;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.myhome.Interface.IApi;
import com.example.myhome.Interface.ILogin;
import com.example.myhome.Interface.ISignUp;
import com.example.myhome.Model.House;
import com.example.myhome.Model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Api implements IApi {
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();


    @Override
    public void createUser(String email, String pass, final User user, final ISignUp callback) {
        mAuth.createUserWithEmailAndPassword(email, pass)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Map<String, Object> like_init = new HashMap<>();
                            Map<String, Object> rent_init = new HashMap<>();
                            like_init.put("like_house_id", Arrays.asList());
                            rent_init.put("rent_house_id", Arrays.asList());
                            Store.id = mAuth.getCurrentUser().getUid();
                            new DB_help().initialNewUser(user, like_init, rent_init);
                            callback.onCreateUserSuccessful();
                        } else
                            callback.onCreateUserFail(task.getException());
                    }
                });

    }

    @Override
    public void login(String id, String pass, final ILogin callback) {
        mAuth.signInWithEmailAndPassword(id, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) callback.loginSuccess(mAuth.getCurrentUser().getEmail());
                else callback.loginFail(task.getException());
            }
        });
    }

    @Override
    public void getCurrentUserData(final OnCompleteGetUserData callback) {
        Log.d("User name", "fetching");
        if (mAuth.getCurrentUser() != null){
            Store.id = mAuth.getCurrentUser().getUid();
            new DB_help().getUserData(mAuth.getCurrentUser().getUid(), new DB_help.OnGetUserData() {
                @Override
                public void onComplete(User e) {
                    Store.id = mAuth.getCurrentUser().getUid();
                    callback.onCompleteGetData(e);
                }
            });
        }

    }

    @Override
    public void getLikedHouse(final OnCompleteGetLikedHouse callback) {
        new DB_help().getLikedHouse(new DB_help.OnGetLikedData() {
            @Override
            public void onComplete(ArrayList<House> houses, ArrayList<String> s) {
                callback.onComplete(houses,s);
            }
        });
    }

    @Override
    public void updateLikedHouse(String s, OnCompleteAddLikedHouse callback) {
        Log.d("updateLikedHouse", s);
        if (new DB_help().updateLikedHouse(s)) callback.onComplete(true);
        else callback.onComplete(false);

    }

    public interface OnCompleteGetUserData {
        void onCompleteGetData(User user);
    }
    public interface OnCompleteGetLikedHouse {
        void onComplete(ArrayList<House> list, ArrayList<String> s);
    }
    public interface OnCompleteAddLikedHouse {
        void onComplete(Boolean status);
    }
}



//    public void createUser(String e, String p, final User user, final ISignUp callback) {
//
//
//    }
//    public void login(String id, String pass, final ILogin callback){
//
//    }
//
//
//    public User getCurrentUserData() {
//        return null;
//    }
//
//    public Boolean isUserLogin() {
//
//            return true;
//        else {
//            AccessToken accessToken = AccessToken.getCurrentAccessToken();
//            return accessToken != null && !accessToken.isExpired();
//        }
//    }

