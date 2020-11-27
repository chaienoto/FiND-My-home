package com.example.myhome.Fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myhome.Adapter.one_item_text_Adapter;
import com.example.myhome.R;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import javax.annotation.Nullable;


public class District_Fragment extends Fragment {
    RecyclerView recyclerView;
    String pID,pName;
    public District_Fragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_district, container, false);
        recyclerView=view.findViewById(R.id.district_fragment_recycleview);
        // lấy id của cái tỉnh
        Bundle b= getArguments();
        if (b != null){
            pID=b.getString("pID");
            pName=b.getString("pName");
        }
        getActivity().setTitle(pName);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference collectionReference=db.collection(pID);
        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                ArrayList<String> dName = new ArrayList<String>();
                ArrayList<String> dID = new ArrayList<String>();
                for (QueryDocumentSnapshot doc: queryDocumentSnapshots){
                    if (doc.getId() != null) {
                        dID.add(doc.getId());
                        dName.add(String.valueOf(doc.get("name")));
                    }
                }

                one_item_text_Adapter adapter = new one_item_text_Adapter(District_Fragment.this.getContext(),dName,dID);
                LinearLayoutManager manager = new LinearLayoutManager(District_Fragment.this.getContext());
                recyclerView.setLayoutManager(manager);
                recyclerView.setAdapter(adapter);



                adapter.setOnItemClickedListener(new one_item_text_Adapter.OnItemClickedListener() {
                    @Override
                    public void onItemClick(String ID,String Name) {

                        FragmentManager manager = getActivity().getSupportFragmentManager();
                        try {

                            Fragment fragment = (Fragment) House_Fragment.class.newInstance();

                            Bundle bundle= new Bundle();
                            bundle.putString("dID",ID);
                            bundle.putString("dName",Name);
                            bundle.putString("pID",pID);
                            fragment.setArguments(bundle);

                            manager.beginTransaction().replace(R.id.flContent,fragment ).commit();
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        } catch (java.lang.InstantiationException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });

        return view;
    }



}
