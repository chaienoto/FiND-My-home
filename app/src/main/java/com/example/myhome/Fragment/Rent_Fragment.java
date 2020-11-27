package com.example.myhome.Fragment;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.myhome.Adapter.house_adapter;
import com.example.myhome.DB_help;
import com.example.myhome.Model.House;
import com.example.myhome.R;
import com.example.myhome.Store;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
import javax.annotation.Nullable;

public class Rent_Fragment extends Fragment implements house_adapter.OnItemClickedListener{
    RecyclerView recyclerView;
    Button rent;
    String ID;
    ArrayList<House> houses = new ArrayList<House>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_rent, container, false);
        recyclerView=view.findViewById(R.id.rent_recycleview);
        rent=view.findViewById(R.id.rent);
        getData();
        rent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    FragmentManager manager = getActivity().getSupportFragmentManager();
                    try {
                        Fragment fragment = (Fragment) Create_house_Fragment.class.newInstance();
                        manager.beginTransaction().replace(R.id.flContent,fragment ).commit();
                    } catch (IllegalAccessException e1) {
                        e1.printStackTrace();
                    } catch (java.lang.InstantiationException e1) {
                        e1.printStackTrace();
                    }
            }
        });
        return view;
    }

    private void getData() {
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        final DocumentReference documentReference = db.document("user/"+Store.id+"/store/rent");
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                ArrayList<String> rent_house_ids= (ArrayList<String>) documentSnapshot.get("rent_house_id");
                if (!rent_house_ids.isEmpty()) {
                    final ArrayList<House> houses = new ArrayList<House>();
                    for (final String path : rent_house_ids) {
                        new DB_help().getSummaryHouseInfo(path, new DB_help.onGetHouseSummaryInfo() {
                            @Override
                            public void onComplete(House house) {
                                houses.add(house);
                                house_adapter adapter = new house_adapter(Rent_Fragment.this.getContext(),houses, Rent_Fragment.this);
                                LinearLayoutManager manager = new LinearLayoutManager(Rent_Fragment.this.getContext());
                                recyclerView.setLayoutManager(manager);
                                recyclerView.setAdapter(adapter);
                            }
                        });
                    }
                } else Toast.makeText(getActivity(), "You don't have any room for rent", Toast.LENGTH_SHORT).show();
            }
        });
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
