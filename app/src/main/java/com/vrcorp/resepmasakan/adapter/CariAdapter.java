package com.vrcorp.resepmasakan.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.vrcorp.resepmasakan.DetailActivity;
import com.vrcorp.resepmasakan.R;
import com.vrcorp.resepmasakan.db.DBHelper;

import java.util.ArrayList;

public class CariAdapter extends RecyclerView.Adapter<CariAdapter.MyViewHolder> {
private ArrayList<String> judulList = new ArrayList<>();
private ArrayList<String> photoList = new ArrayList<>();
private ArrayList<String> urlList = new ArrayList<>();
    private ArrayList<String> tipeList = new ArrayList<>();
    int success=0, favoritStatus=0;
    DBHelper helper;
private Context context;


public CariAdapter(Context context, ArrayList<String> judulList,
                   ArrayList<String> urlList,
                   ArrayList<String> photoList,ArrayList<String> tipeList) {
        this.context = context;
        this.judulList = judulList;
        this.photoList = photoList;
        this.urlList = urlList;
        this.tipeList = tipeList;
        }

public class MyViewHolder extends RecyclerView.ViewHolder {
    TextView  txtJudul;
    ImageView photoResep, favimg;
    LinearLayout cardCari, lyfav;
    public MyViewHolder(View view) {
        super(view);
        //this.bg = view.findViewById(R.id.bg_img);
        txtJudul = view.findViewById(R.id.judul_cari);
        cardCari= view.findViewById(R.id.card_cari);
        photoResep = view.findViewById(R.id.img_cari);
        favimg = view.findViewById(R.id.img_fav_cari);
        lyfav = view.findViewById(R.id.fav_fav);
    }
}

    @Override
    public CariAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_cari, parent, false);
        return new CariAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final CariAdapter.MyViewHolder holder, final int position) {
        holder.txtJudul.setText(judulList.get(position));
        Glide.with(holder.photoResep.getContext())
                .load(Uri.parse(photoList.get(position)))
                .apply(RequestOptions.centerCropTransform())
                .into(new SimpleTarget<Drawable>() {
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        holder.photoResep.setImageDrawable(resource);
                    }
                });
        holder.cardCari.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra("url",urlList.get(position));
                context.startActivity(intent);
            }
        });
        if(tipeList.get(position).equals("fav")){
            holder.lyfav.setVisibility(View.VISIBLE);
            helper = new DBHelper(context);
            success = helper.cekFav(urlList.get(position));
            if(success>0){
                Glide.with(holder.favimg)
                        .load(context.getResources()
                                .getIdentifier("fav", "drawable", context.getPackageName()))
                        .into(new SimpleTarget<Drawable>() {
                            @Override
                            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                                holder.favimg.setImageDrawable(resource);
                            }
                        });
                favoritStatus=1;
            }

        }else{
            holder.lyfav.setVisibility(View.GONE);
        }
        holder.lyfav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(favoritStatus>0){
                    Glide.with(holder.favimg)
                            .load(context.getResources()
                                    .getIdentifier("nofav", "drawable", context.getPackageName()))
                            .into(new SimpleTarget<Drawable>() {
                                @Override
                                public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                                    holder.favimg.setImageDrawable(resource);
                                }
                            });
                    favoritStatus=0;
                }else{
                    Glide.with(holder.favimg)
                            .load(context.getResources()
                                    .getIdentifier("fav", "drawable", context.getPackageName()))
                            .into(new SimpleTarget<Drawable>() {
                                @Override
                                public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                                    holder.favimg.setImageDrawable(resource);
                                }
                            });
                    helper.insertIntoDB(1,judulList.get(position),photoList.get(position),urlList.get(position),"","","1");
                    favoritStatus=1;
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return judulList.size();
    }
}