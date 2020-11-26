package com.example.myhome;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.myhome.Interface.IDB_Helper;
import com.example.myhome.Model.House;
import com.example.myhome.Model.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Map;

import javax.annotation.Nullable;

public class DB_help implements IDB_Helper {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    DocumentReference userHelper = db.collection("user").document(Store.id);
    DocumentReference likeHelper = db.collection("user").document(Store.id).collection("store").document("liked");
    DocumentReference rentHelper = db.collection("user").document(Store.id).collection("store").document("rent");

    @Override
    public void initialNewUser(User user, Map<String, Object> like_init, Map<String, Object> rent_init) {
            Log.d("Store: ",Store.id);
            userHelper.set(user);
            likeHelper.set(like_init);
            rentHelper.set(rent_init);
    }


    @Override
    public void getUserData(String uid, final OnGetUserData callback){
        userHelper.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if (documentSnapshot.exists()){
                    final String name=documentSnapshot.get("Name").toString();
                    final String phoneNumber=documentSnapshot.get("phoneNumber").toString();
                    final String imageUrl=documentSnapshot.get("imageUrl").toString();
                    User user = new User(name,phoneNumber,imageUrl);
                    callback.onComplete(user);
                }

            }
        });
    }

    @Override
    public void getLikedHouse(final OnGetLikedData callback) {
        likeHelper.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if (documentSnapshot.exists()){
                    final ArrayList<String> like_house_id = (ArrayList<String>) documentSnapshot.get("like_house_id");
                    if (like_house_id != null) {
                        final ArrayList<House> houses = new ArrayList<House>();
                        for (final String s : like_house_id) {
                            DocumentReference houseRef = db.document(s);
                            houseRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                                @Override
                                public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                                    if (documentSnapshot != null && documentSnapshot.exists()) {
                                        final String house = documentSnapshot.getId();
                                        final String address=documentSnapshot.get("house_address").toString();
                                        final String price=documentSnapshot.get("house_price").toString();
                                        final String detail=documentSnapshot.get("house_detail").toString();
                                        ArrayList<String> list = (ArrayList<String>) documentSnapshot.get("house_picture_id");
                                        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
                                        storageRef.child("images/" + list.get(0)).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                            @Override
                                            public void onSuccess(Uri uri) {
                                                houses.add(new House(house,address,price,detail,uri));
                                                callback.onComplete(houses,like_house_id);
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                            }
                                        });
                                    }
                                }
                            });
                        }

                    } else callback.onComplete(null, null);

                } else callback.onComplete(null, null);
            }
        });


    }

    @Override
    public boolean updateLikedHouse(String s) {
        return likeHelper.update("like_house_id", FieldValue.arrayUnion(s)).isSuccessful();
    }

    public interface OnGetUserData{void onComplete(User user);
    }
    public interface OnGetLikedData{void onComplete(ArrayList<House> houses, ArrayList<String> idPath);
    }

}
