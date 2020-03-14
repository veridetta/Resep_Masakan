package com.vrcorp.resepmasakan;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.vrcorp.resepmasakan.db.DBHelper;
import com.vrcorp.resepmasakan.layout.HomeFragment;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import io.supercharge.shimmerlayout.ShimmerLayout;

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
    ImageView bg,imgBack, imgFav;
    CardView share, fav;
    String gambara, Nama;
    LinearLayout shareLY;
    ProgressDialog dialog;
    ShimmerLayout sh_bahan, sh_step;
    DBHelper helper;
    int success=0, favoritStatus=0;
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
        shareLY = findViewById(R.id.content_share);
        Intent intent = getIntent();
        urlnya = intent.getStringExtra("url");
        sh_bahan = findViewById(R.id.shimmer_bahan);
        sh_step = findViewById(R.id.shimmer_step);
        share = findViewById(R.id.card_share);
        fav = findViewById(R.id.card_fav);
        imgFav = findViewById(R.id.img_fav);
        helper = new DBHelper(this);
        success = helper.cekFav(urlnya);
        if(success>0){
            Glide.with(imgFav)
                    .load(getResources()
                            .getIdentifier("fav", "drawable", this.getPackageName()))
                    .into(new SimpleTarget<Drawable>() {
                        @Override
                        public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                            imgFav.setImageDrawable(resource);
                        }
                    });
            favoritStatus=1;
        }
        fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(favoritStatus>0){
                    Glide.with(imgFav)
                            .load(getResources()
                                    .getIdentifier("nofav", "drawable", getPackageName()))
                            .into(new SimpleTarget<Drawable>() {
                                @Override
                                public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                                    imgFav.setImageDrawable(resource);
                                }
                            });
                    helper.deletDB(urlnya);
                    favoritStatus=0;
                }else{
                    Glide.with(imgFav)
                            .load(getResources()
                                    .getIdentifier("fav", "drawable", getPackageName()))
                            .into(new SimpleTarget<Drawable>() {
                                @Override
                                public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                                    imgFav.setImageDrawable(resource);
                                }
                            });
                    helper.insertIntoDB(1,Nama,gambara,urlnya,"","","1");
                    favoritStatus=1;
                }
            }
        });
        //dialog = new ProgressDialog(this);
        //dialog.setCancelable(false);
        //dialog.setMessage("Memuat data ....");
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout f = shareLY;
                f.setDrawingCacheEnabled(true);
                f.buildDrawingCache(true);
                Bitmap saveBm = Bitmap.createBitmap(f.getDrawingCache());
                f.setDrawingCacheEnabled(false);
                String bitmapPath = MediaStore.Images.Media.insertImage(f.getContext().getContentResolver(),
                        saveBm,"Resep memasak "+Nama, "Sumber Aplikasi Resep Makanan");
                /*Uri bitmapUri = Uri.parse(bitmapPath);
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("image/png");
                intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.putExtra(Intent.EXTRA_STREAM, bitmapUri );
                v.getContext().startActivity(Intent.createChooser(intent , "Share"));*/
                Intent shareIntent;

                Uri bmpUri = Uri.parse(bitmapPath);
                shareIntent = new Intent(android.content.Intent.ACTION_SEND);
                shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                shareIntent.putExtra(Intent.EXTRA_STREAM, bmpUri);
                shareIntent.putExtra(Intent.EXTRA_TEXT,"Download aplikasi Resep Memasak secara gratis " + "https://play.google.com/store/apps/details?id=" +getPackageName());
                shareIntent.setType("image/png");
                startActivity(Intent.createChooser(shareIntent,"Share with"));
            }
        });
        new CardGet().execute();
        //dialog.show();
        sh_bahan.startShimmerAnimation();
        sh_step.startShimmerAnimation();
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
                int c=1;
                Elements StepSize = mBlogPagination.select("div[class=post-body entry-content] div[class=ingredient__details] ol.numbered-list li");
                for(int b=0;b<StepSize.size();b++){
                    Elements dov = StepSize.select("div.mb-2").eq(b);
                    String step = dov.text().trim();
                    stepArray.add(step);
                    nomorArray.add(String.valueOf(c));
                    c++;
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
            judul.setText(Nama);
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
            sh_bahan.stopShimmerAnimation();
            sh_bahan.setVisibility(View.GONE);
            //--------------------------
            StepAdapter stepData = new StepAdapter(stepArray, nomorArray);
            RecyclerView.LayoutManager stepLayout = new GridLayoutManager(DetailActivity.this, 1, GridLayoutManager.VERTICAL, false);
            //attachToRecyclerView
            step.setLayoutManager(stepLayout);
            step.setAdapter(stepData);
            sh_step.stopShimmerAnimation();
            sh_step.setVisibility(View.GONE);
            //--------------------------
            //dialog.dismiss();
        }

    }
    @Override
    public void onResume() {
        super.onResume();
        sh_step.startShimmerAnimation();
        sh_bahan.startShimmerAnimation();
    }

    @Override
    public void onPause() {
        sh_bahan.stopShimmerAnimation();
        sh_step.stopShimmerAnimation();
        super.onPause();
    }
    @Override
    public void onDestroy() {
        helper.close();
        super.onDestroy();
    }
}
