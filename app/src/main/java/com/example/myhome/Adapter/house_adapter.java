package com.example.myhome.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myhome.Model.House;
import com.example.myhome.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class house_adapter extends  RecyclerView.Adapter<house_adapter.ViewHolder>{
    Context context;
    ArrayList<House> houses;
    OnItemClickedListener listener;

    public house_adapter(Context context, ArrayList<House> houses, OnItemClickedListener listener) {
        this.context = context;
        this.houses = houses;
        this.listener = listener;
    }

//    public house_adapter(Context context, ArrayList<House> houses) {
//        this.context = context;
//        this.houses = houses;
//    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inf= LayoutInflater.from(viewGroup.getContext());
        View view=inf.inflate(R.layout.one_item_house,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {
        final House data = houses.get(i);
        if (data.img != null)
        Picasso.with(context).load(data.img).into(viewHolder.room_img);
        viewHolder.room_address.setText("Địa Chỉ: "+data.address);
        viewHolder.room_price.setText("Giá Phòng: "+data.price);
        viewHolder.room_detail.setText("Mô Tả: "+data.detail);
        viewHolder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(data.path);
            }
        });
    }

    @Override
    public int getItemCount() {
        return houses.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView room_img;
        TextView room_address,room_detail,room_price;
        LinearLayout layout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            room_img=itemView.findViewById(R.id.room_img);
            room_address=itemView.findViewById(R.id.room_address);
            room_price=itemView.findViewById(R.id.room_price);
            room_detail=itemView.findViewById(R.id.room_detail);
            layout=itemView.findViewById(R.id.one_item_room);
        }
    }

    public interface OnItemClickedListener {
        void onItemClick(String path);
    }

}

