package com.vrcorp.resepmasakan.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.vrcorp.resepmasakan.R;

import java.util.ArrayList;

public class
HomeAdapter extends RecyclerView.Adapter<HomeAdapter.MyViewHolder> {
    private ArrayList<String> judulList = new ArrayList<>();
    private ArrayList<String> kategoriList = new ArrayList<>();
    private ArrayList<String> photoList = new ArrayList<>();
    private ArrayList<String> urlList = new ArrayList<>();


    public HomeAdapter(ArrayList<String> judulList,
                       ArrayList<String> kategoriList,
                       ArrayList<String> photoList,
                       ArrayList<String> urlList) {

        this.judulList = judulList;
        this.kategoriList = kategoriList;
        this.photoList = photoList;
        this.urlList = urlList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txtKategori, txtJudul;
        ImageView photoResep;
        CardView cardBaru;
        public MyViewHolder(View view) {
            super(view);
            //this.bg = view.findViewById(R.id.bg_img);
            txtKategori = view.findViewById(R.id.ketgori_home);
            txtJudul = view.findViewById(R.id.judul_home);
            cardBaru = view.findViewById(R.id.card_home);
            photoResep = view.findViewById(R.id.img_home);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.content_list, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        holder.txtKategori.setText(kategoriList.get(position));
        holder.txtJudul.setText(judulList.get(position));
        RequestOptions options = new RequestOptions();
        options.centerCrop();
        Glide.with(holder.photoResep.getContext())
                .load(Uri.parse(photoList.get(position)))
                .apply(new RequestOptions()
                        .centerCrop().fitCenter())
                .into(new SimpleTarget<Drawable>() {
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        holder.photoResep.setImageDrawable(resource);
                    }
                });


    }

    @Override
    public int getItemCount() {
        return judulList.size();
    }
}
