package com.example.myhome;

import androidx.annotation.NonNull;

import com.example.myhome.Interface.ISignUp;
import com.example.myhome.Model.User;
import com.facebook.AccessToken;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Api {
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    DB_help db = new DB_help();

    public void createUser(String e, String p, final User user, final ISignUp callback) {
        mAuth.createUserWithEmailAndPassword(e,p)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            db.newUserInitial(mAuth.getCurrentUser().getUid(), user);
                            callback.onCreateUserSuccessful();
                        }
                         else
                            callback.onCreateUserFail(task.getException());
                    }
                });

    }


    public void getCurrentUserData() {

    }

    public Boolean isUserLogin() {
        if (mAuth.getCurrentUser().getUid() !=null ) return true;
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        if (accessToken != null && !accessToken.isExpired()) return true;
        return false;
    }
}
