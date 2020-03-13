package com.vrcorp.resepmasakan.adapter;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.recyclerview.widget.RecyclerView;

import com.vrcorp.resepmasakan.R;

import java.util.ArrayList;

public class BahanAdapter extends RecyclerView.Adapter<BahanAdapter.MyViewHolder> {
    private ArrayList<String> judulList = new ArrayList<>();


    public BahanAdapter(ArrayList<String> judulList) {

        this.judulList = judulList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txtIsi;
        ImageView imgSamping;

        public MyViewHolder(View view) {
            super(view);
            //this.bg = view.findViewById(R.id.bg_img);
            txtIsi= view.findViewById(R.id.bahan_detail);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_bahan, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        holder.txtIsi.setText(judulList.get(position));
    }

    @Override
    public int getItemCount() {
        return judulList.size();
    }
}