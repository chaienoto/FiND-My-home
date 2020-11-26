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


public class Like_Fragment extends Fragment {
    RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_like, container, false);
        recyclerView=view.findViewById(R.id.like_fragment_recycleview);
        new Api().getLikedHouse(new Api.OnCompleteGetLikedHouse() {
            @Override
            public void onComplete(ArrayList<House> list, final ArrayList<String> idPath) {
                if (list.isEmpty()) return;
                Log.d("OnGetUserData", list.toString());
                house_adapter adapter = new house_adapter(Like_Fragment.this.getContext(),list);
                LinearLayoutManager manager = new LinearLayoutManager(Like_Fragment.this.getContext());
                recyclerView.setLayoutManager(manager);
                recyclerView.setAdapter(adapter);
                adapter.setOnItemClickedListener(new house_adapter.OnItemClickedListener() {
                    @Override
                    public void onItemClick(int ID) {
                        FragmentManager manager = getActivity().getSupportFragmentManager();
                        //phải Try catch chỗ này mới ko báo lỗi
                        try {
                            Fragment fragment = (Fragment) Room_info_Fragment.class.newInstance();
                            // đóng gói ID lấy đc từ adapter
                            Bundle bundle= new Bundle();
                            bundle.putString("path",idPath.get(ID));
                            fragment.setArguments(bundle);
                            manager.beginTransaction().replace(R.id.flContent,fragment ).commit();
                        } catch (IllegalAccessException e1) {
                            e1.printStackTrace();
                        } catch (java.lang.InstantiationException e1) {
                            e1.printStackTrace();
                        }
                    }
                });
            }
        });
//


        return view;
    }

}
