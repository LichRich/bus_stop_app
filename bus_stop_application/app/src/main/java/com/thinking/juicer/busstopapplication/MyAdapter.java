package com.thinking.juicer.busstopapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.thinking.juicer.busstopapplication.items.BusRouteItem;

import java.util.ArrayList;
import java.util.List;

public class MyAdapter extends RecyclerView.Adapter {

    ArrayList<BusRouteItem>items;
    Context context;

    public MyAdapter(ArrayList<BusRouteItem>items,Context context){
        this.items = items;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(context).inflate(R.layout.recycler_item,parent,false);

        VH vh = new VH(itemView);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        VH vh = (VH) holder;

        BusRouteItem item = items.get(position);
        vh.busNum.setText(item.getBusNum());
    }



    @Override
    public int getItemCount() {
        return items.size();
    }

    class VH extends RecyclerView.ViewHolder{

        TextView busNum;

        public VH(View itemView){
            super(itemView);

            busNum = itemView.findViewById(R.id.bs_num);

            itemView.setOnClickListener(new View.OnClickListener() { //카드뷰를 클릭했을 때
                @Override
                public void onClick(View view) {


                }
            });
        }
    }
}
