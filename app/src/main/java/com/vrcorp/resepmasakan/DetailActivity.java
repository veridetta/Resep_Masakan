package com.vrcorp.resepmasakan;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.hhl.gridpagersnaphelper.GridPagerSnapHelper;
import com.vrcorp.resepmasakan.adapter.BahanAdapter;
import com.vrcorp.resepmasakan.adapter.HomeAdapter;
import com.vrcorp.resepmasakan.adapter.SmallAdapter;
import com.vrcorp.resepmasakan.adapter.StepAdapter;
import com.vrcorp.resepmasakan.layout.HomeFragment;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class DetailActivity extends AppCompatActivity {
    private ActionBar toolbar;
    boolean doubleBackToExitPressedOnce = false;
    Document mBlogDocument  = null, cardDoc = null;
    private ArrayList<String> bahanArray = new ArrayList<>();
    private ArrayList<String> stepArray= new ArrayList<String>();
    private ArrayList<String> nomorArray= new ArrayList<String>();
    RecyclerView bahan, step;
    String urlnya;
    TextView judul;
    ImageView bg,imgBack;
    String gambara, Nama;
    ProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        toolbar = getSupportActionBar();
        toolbar.hide();
        judul= findViewById(R.id.judul_detail);
        bg = findViewById(R.id.bg_detail);
        bahan = findViewById(R.id.rc_bahan);
        step = findViewById(R.id.rc_langkah);
        Intent intent = getIntent();
        urlnya = intent.getStringExtra("url");
        dialog = new ProgressDialog(this);
        dialog.setCancelable(false);
        dialog.setMessage("Memuat data ....");
        new CardGet().execute();
        dialog.show();
    }
    private class CardGet extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            // NO CHANGES TO UI TO BE DONE HERE
            String url = urlnya;
            Document mBlogPagination = null;
            try {
                mBlogPagination = Jsoup.parse(new URL(url),50000);
            } catch (IOException e) {
                e.printStackTrace();
            }
            //mBlogPagination.outputSettings().prettyPrint(false);
            //Log.d(TAG, "doInBackground: "+mBlogPagination);
            System.out.println(mBlogPagination);
            // Using Elements to get the Meta data
            // -------------- RECENTT ---------------
            //----------------
            Elements mElementDataSize = mBlogPagination.select("div[class=post hentry uncustomized-post-template]");
            // Locate the content attribute
            int mElementSize = mElementDataSize.size();
            int max = 0;
            if(mElementSize>10){
                max=10;
            }else{
                max=mElementSize;
            }
            //System.out.println("jumlah data"+mElementSize);
            for (int i = 0; i < max; i++) {
                //Judul
                Elements ElemenJudul = mElementDataSize.select("h3[class=post-title entry-title]").eq(i);
                Nama= ElemenJudul.text();
                //gambar
                Elements elGambar = mElementDataSize.select("div[class=post-body entry-content]").eq(i);
                gambara = elGambar.select("img").eq(0).attr("src");
                Elements elKat = mElementDataSize.select("div[class=post-footer-line post-footer-line-2] span").eq(i);
                String label = elKat.select("a").eq(0).text().trim();
                Elements BahanSize = mBlogPagination.select("div[class=post-body entry-content] ol.list-unstyled li");
                System.out.println("jumlah data"+BahanSize.size());
                for(int s=0;s<BahanSize.size();s++){
                    Elements div = BahanSize.select("div").eq(s);
                    String bahan = div.select("div").eq(0).text().trim();
                    bahanArray.add(bahan);
                }
                Elements StepSize = mBlogPagination.select("div[class=post-body entry-content] div[class=ingredient__details] ol.numbered-list li");
                for(int b=0;b<StepSize.size();b++){
                    Elements dov = StepSize.select("div").eq(b);
                    String step = dov.select("div").eq(0).text().trim();
                    stepArray.add(step);
                    nomorArray.add(String.valueOf(b+1));
                }
            }
            //---------------------------
            //--------------------------

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            //This is where we update the UI with the acquired data
            // Set description into TextView
            Log.d("EAEA", "onPostExecute: "+result);
            //--------------RECENT -
            //----------------------
            System.out.println("setep"+stepArray+Nama+gambara);
            System.out.println("bahan"+bahanArray);
            System.out.println("nomor"+nomorArray);
            Glide.with(bg)
                    .load(Uri.parse(gambara))
                    .apply(RequestOptions.centerCropTransform())
                    .into(new SimpleTarget<Drawable>() {
                        @Override
                        public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                           bg.setImageDrawable(resource);
                        }
                    });
            BahanAdapter mDataAdapter = new BahanAdapter(bahanArray);
            RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(DetailActivity.this, 1, GridLayoutManager.VERTICAL, false);
            //attachToRecyclerView
            bahan.setLayoutManager(mLayoutManager);
            bahan.setAdapter(mDataAdapter);
            //--------------------------
            StepAdapter stepData = new StepAdapter(stepArray, nomorArray);
            RecyclerView.LayoutManager stepLayout = new GridLayoutManager(DetailActivity.this, 1, GridLayoutManager.VERTICAL, false);
            //attachToRecyclerView
            step.setLayoutManager(stepLayout);
            step.setAdapter(stepData);
            //--------------------------
            dialog.dismiss();
        }

    }
}
