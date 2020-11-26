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
import com.example.myhome.Model.City;
import com.example.myhome.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class city_adapter extends RecyclerView.Adapter<city_adapter.ViewHolder> {
    Context context;
    ArrayList<City> cities = new ArrayList<>();
    private OnItemClickedListener onItemClickedListener;

    public city_adapter(Context context, ArrayList<City> cities) {
        this.context = context;
        this.cities = cities;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inf= LayoutInflater.from(viewGroup.getContext());
        View view=inf.inflate(R.layout.one_item_city,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int position ) {
        final City city = cities.get(position);
        viewHolder.name_city.setText(city.cityName);
        Picasso.with(context).load(city.cityImg).resize(180, 180)
                .centerCrop().into(viewHolder.img_city);
        viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickedListener != null) {
                    onItemClickedListener.onItemClick(city.cityId, city.cityName);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return cities.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout cardView;
        ImageView img_city;
        TextView name_city;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img_city=itemView.findViewById(R.id.img_city);
            name_city=itemView.findViewById(R.id.name_city);
            cardView=itemView.findViewById(R.id.one_item_city);
        }
    }
    public interface OnItemClickedListener {
        void onItemClick(String ID, String Name);
    }

    public void setOnItemClickedListener(OnItemClickedListener onItemClickedListener) {
        this.onItemClickedListener = onItemClickedListener;
    }
}
