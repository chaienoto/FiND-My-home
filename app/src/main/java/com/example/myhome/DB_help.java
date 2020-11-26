package com.example.myhome;

import com.example.myhome.Model.User;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import javax.annotation.Nullable;

public class DB_help {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference userHelper = db.collection("user");

    public void newUserInitial(String uid, User user) {
        userHelper.document(uid).set(user);
    }
    public void getUserName(String uid){
        userHelper.document(uid).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {

            }
        });
    }


}
