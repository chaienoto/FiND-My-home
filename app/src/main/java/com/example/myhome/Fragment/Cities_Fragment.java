package com.example.myhome.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.myhome.Adapter.city_adapter;
import com.example.myhome.Model.City;
import com.example.myhome.R;
import java.util.ArrayList;

public class Cities_Fragment extends Fragment {
    RecyclerView recyclerView;
    ArrayList<City> cities = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //anh xa
        View view= inflater.inflate(R.layout.fragment_cities, container, false);
        recyclerView=view.findViewById(R.id.cities_fragment_recycleview);

        cities.add(new City("Hà Nội", "hanoi", R.drawable.hn));
        cities.add(new City("Đà Nẵng", "danang", R.drawable.dn));
        cities.add(new City("TP.Hồ Chí Minh", "hcm", R.drawable.hcm));
        cities.add(new City("TP.Buôn Mê Thuột", "bmt", R.drawable.bmt));
        cities.add(new City("Cần Thơ", "cantho", R.drawable.ct));

        //do du lieu len recycleview
        final city_adapter adapter = new city_adapter(getContext(),cities);
        GridLayoutManager manager = new GridLayoutManager(Cities_Fragment.this.getContext(),2);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);

        // bắt sự kiện click vào từng mục (void này phải viết thêm trong adapter)
        adapter.setOnItemClickedListener(new city_adapter.OnItemClickedListener() {
            @Override
            public void onItemClick(String ID, String Name) {
                FragmentManager manager = getActivity().getSupportFragmentManager();
                try {
                    //phải Try catch chỗ này mới ko báo lỗi
                    Fragment fragment = (Fragment) District_Fragment.class.newInstance();
                    // đóng gói pID lấy đc từ adapter
                    Bundle bundle= new Bundle();
                    bundle.putString("pID",ID);
                    bundle.putString("pName",Name);
                    fragment.setArguments(bundle);
                    //chuyển Fragment _data
                    manager.beginTransaction().replace(R.id.flContent,fragment ).commit();
                    // ko cần quan tâm 2 cái catch bên dưới
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (java.lang.InstantiationException e) {
                    e.printStackTrace();
                }
            }
        });
        return view;
    }

}
