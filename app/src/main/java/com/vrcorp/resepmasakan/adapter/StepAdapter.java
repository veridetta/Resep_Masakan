package com.vrcorp.resepmasakan.adapter;

import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.vrcorp.resepmasakan.R;

import java.util.ArrayList;

public class StepAdapter extends RecyclerView.Adapter<StepAdapter.MyViewHolder> {
    private ArrayList<String> judulList = new ArrayList<>();
    private ArrayList<String> photoList = new ArrayList<>();


    public StepAdapter(ArrayList<String> judulList,ArrayList<String> photoList) {

        this.judulList = judulList;
        this.photoList = photoList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txtIsi, step;

        public MyViewHolder(View view) {
            super(view);
            //this.bg = view.findViewById(R.id.bg_img);
            txtIsi= view.findViewById(R.id.bahan_detail);
            step= view.findViewById(R.id.nomor_detail);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_step, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        holder.txtIsi.setText(judulList.get(position));
        holder.step.setText(photoList.get(position));
    }

    @Override
    public int getItemCount() {
        return judulList.size();
    }
}
