package com.example.myhome.Fragment;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.myhome.Adapter.house_adapter;
import com.example.myhome.Api;
import com.example.myhome.Model.House;
import com.example.myhome.R;
import java.util.ArrayList;


public class Like_Fragment extends Fragment implements house_adapter.OnItemClickedListener {
    RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view= inflater.inflate(R.layout.fragment_like, container, false);
        recyclerView=view.findViewById(R.id.like_fragment_recycleview);

        new Api().getLikedHouse(new Api.OnCompleteGetLikedHouse() {
            @Override
            public void onComplete(ArrayList<House> list) {
                if (list.isEmpty()) return;
                house_adapter adapter = new house_adapter(getActivity(),list, Like_Fragment.this);
                LinearLayoutManager manager = new LinearLayoutManager(getActivity());
                recyclerView.setLayoutManager(manager);
                recyclerView.setAdapter(adapter);
            }
        });

        return view;
    }

    @Override
    public void onItemClick(String path)  {
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
