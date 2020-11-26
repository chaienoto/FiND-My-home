package com.example.myhome.Fragment;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myhome.Adapter.picture_adapter;
import com.example.myhome.Api;
import com.example.myhome.LoginActivity;
import com.example.myhome.MapsActivity;
import com.example.myhome.R;
import com.example.myhome.Store;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

public class Room_info_Fragment extends Fragment {
    TextView h_name, h_phone, h_address, h_price, h_water, h_electric, h_P_lot, h_net, h_service, h_detail,h_city, h_district, h_type;
    FloatingActionButton h_map,h_note,h_call;
    RecyclerView h_recycleview;
    ArrayList<Uri> picture = new ArrayList<Uri>();
    String path,dID,hID,uID;
    public Room_info_Fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_room_info, container, false);
        h_name = view.findViewById(R.id.h_name);
        h_phone = view.findViewById(R.id.h_phone);
        h_address = view.findViewById(R.id.h_address);
        h_price = view.findViewById(R.id.h_price);
        h_water = view.findViewById(R.id.h_water);
        h_electric = view.findViewById(R.id.h_electric);
        h_P_lot = view.findViewById(R.id.h_P_lot);
        h_net = view.findViewById(R.id.h_net);
        h_service = view.findViewById(R.id.h_service);
        h_detail = view.findViewById(R.id.h_detail);
        h_city = view.findViewById(R.id.h_city);
        h_district = view.findViewById(R.id.h_district);
        h_type = view.findViewById(R.id.h_type);
        h_map = view.findViewById(R.id.h_map);
        h_note = view.findViewById(R.id.h_note);
        h_call = view.findViewById(R.id.h_call);
        h_recycleview = view.findViewById(R.id.h_recycleview);

        Bundle b= getArguments();
        if (b != null)path=b.getString("path");
        // chạy lên database
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        // truy cập vào đường dẫn
        DocumentReference documentReference = db.document(path);
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable final DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if (documentSnapshot != null && documentSnapshot.exists()) {
                    h_P_lot.setText(documentSnapshot.get("house_P_lot").toString());
                    h_phone.setText(documentSnapshot.get("house_phone").toString());
                    h_address.setText(documentSnapshot.get("house_address").toString());
                    h_price.setText(documentSnapshot.get("house_price").toString());
                    h_water.setText(documentSnapshot.get("house_water").toString());
                    h_electric.setText(documentSnapshot.get("house_electric").toString());
                    h_net.setText(documentSnapshot.get("house_net").toString());
                    h_service.setText(documentSnapshot.get("house_service").toString());
                    h_detail.setText(documentSnapshot.get("house_detail").toString());
                    h_city.setText(documentSnapshot.get("house_city").toString());
                    h_district.setText(documentSnapshot.get("house_district").toString());
                    h_type.setText(documentSnapshot.get("house_type").toString());
                    h_name.setText(documentSnapshot.get("house_owner").toString());
                    ArrayList<String> list = (ArrayList<String>) documentSnapshot.get("house_picture_id");
                   // System.out.println(list.toString());
                    for (String doc:list) {
                        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
                        storageRef.child("images/" + doc).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                picture.add(uri);
                                picture_adapter adapter = new picture_adapter(Room_info_Fragment.this.getContext(), picture);
                                LinearLayoutManager manager = new LinearLayoutManager(Room_info_Fragment.this.getContext(), LinearLayoutManager.HORIZONTAL, false);
                                h_recycleview.setLayoutManager(manager);
                                h_recycleview.setAdapter(adapter);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                            }
                        });
                    }
                    h_map.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(Room_info_Fragment.this.getActivity(),MapsActivity.class);
                            intent.putExtra("address", documentSnapshot.get("house_address") + ","+documentSnapshot.get("house_district") + "," + documentSnapshot.get("house_city") );
                            startActivity(intent);
                        }
                    });
                    h_call.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View arg0) {
                            try {
                                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                                callIntent.setData(Uri.parse("tel:"+documentSnapshot.get("house_phone")));
                                startActivity(callIntent);
                            } catch (Exception e){
                                Log.e("Demo application", "Failed to invoke call", e);
                            }
                        }
                    });
                    h_note.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (Store.login){
                                new Api().updateLikedHouse(path, new Api.OnCompleteAddLikedHouse() {
                                    @Override
                                    public void onComplete(Boolean status) {
                                       if (status)  Toast.makeText(getContext(), "Thanh Cong", Toast.LENGTH_SHORT).show();
                                       else  Toast.makeText(getContext(), "That bai", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            } else {
                                Toast.makeText(getContext(), "Bạn Phải đăng nhập để thực hiện chức năng này", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(Room_info_Fragment.this.getActivity(),LoginActivity.class);
                                startActivity(intent);
                            }
                        }
                    });
                }
            }
        });


        return view;
    }

}
