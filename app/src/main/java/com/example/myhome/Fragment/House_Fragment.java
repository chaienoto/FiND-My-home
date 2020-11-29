package com.example.myhome.Fragment;


import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myhome.Adapter.house_adapter;
import com.example.myhome.Model.House;
import com.example.myhome.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import java.util.ArrayList;
import javax.annotation.Nullable;

public class House_Fragment extends Fragment implements house_adapter.OnItemClickedListener  {
    RecyclerView recyclerView;
    ArrayList<House> houses = new ArrayList<House>();
    String dID,pID,dName;
    public House_Fragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_house_, container, false);
        recyclerView=view.findViewById(R.id.house_fragment_recycleview);

        Bundle b= getArguments();
        if (b != null){
            dID=b.getString("dID");
            dName=b.getString("dName");
            pID=b.getString("pID");
        }
        getActivity().setTitle(getActivity().getTitle()+" - "+dName);
        getRealtimeData(pID,dID);
        return view;
    }

    public void getRealtimeData(final String pID, final String dID) {

        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference collectionReference = db.collection(pID+"/"+dID+"/house");
        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable final FirebaseFirestoreException e) {
                if (!queryDocumentSnapshots.isEmpty())
                for (final QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                    if (doc.getId() != null) {
                        final String house = doc.getId();
                        System.out.println(house);
                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                        DocumentReference documentReference = db.collection(pID).document(dID).collection("house").document(house);
                        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                            @Override
                            public void onEvent(@Nullable final DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                                if (documentSnapshot != null && documentSnapshot.exists()) {
                                    final String address=documentSnapshot.get("house_address").toString();
                                    final String price=documentSnapshot.get("house_price").toString();
                                    final String detail=documentSnapshot.get("house_detail").toString();
                                    StorageReference storageRef = FirebaseStorage.getInstance().getReference();
                                    final ArrayList<String> list = (ArrayList<String>) documentSnapshot.get("house_picture_id");
                                    if (!list.isEmpty()) {
                                        storageRef.child("images/" + list.get(0)).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                            @Override
                                            public void onSuccess(Uri uri) {
                                                Uri picture=uri;
                                                houses.add(new House(house,address,price,detail,picture,pID+"/"+dID+"/house/"+documentSnapshot.getId()));
                                                showData(houses);
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                            }
                                        });
                                    } else {
                                        houses.add(new House(house,address,price,detail,null,pID+"/"+dID+"/house/"+documentSnapshot.getId()));
                                        showData(houses);
                                    }

                                }
                            }
                        });
                    }
                }
            }
        });
    }
    private void showData(ArrayList<House> list){
        house_adapter adapter = new house_adapter(getActivity(),houses, House_Fragment.this);
        LinearLayoutManager manager = new LinearLayoutManager(House_Fragment.this.getContext());
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onItemClick(String path) {
        FragmentManager manager = getActivity().getSupportFragmentManager();
        Fragment fragment = null;
        try {
            fragment = (Fragment) Room_info_Fragment.class.newInstance();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (java.lang.InstantiationException e) {
            e.printStackTrace();
        }
        Bundle bundle = new Bundle();
        bundle.putString("path", path);
        fragment.setArguments(bundle);
        manager.beginTransaction().replace(R.id.flContent,fragment ).commit();

    }
}
