package com.vrcorp.resepmasakan;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.hhl.gridpagersnaphelper.GridPagerSnapHelper;
import com.vrcorp.resepmasakan.adapter.BahanAdapter;
import com.vrcorp.resepmasakan.adapter.CariAdapter;
import com.vrcorp.resepmasakan.adapter.HomeAdapter;
import com.vrcorp.resepmasakan.adapter.SmallAdapter;
import com.vrcorp.resepmasakan.adapter.StepAdapter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import io.supercharge.shimmerlayout.ShimmerLayout;

public class CariActivity extends AppCompatActivity {
    String urlnya, txtJudul;
    String Nama, gambara;
    private ActionBar toolbar;
    TextView jdul;
    LinearLayout no_result;
    private ArrayList<String> cariArray = new ArrayList<>();
    private ArrayList<String> gambarArray = new ArrayList<>();
    private ArrayList<String> urlArray = new ArrayList<>();
    RecyclerView rc_cari;
    int data=0;
    ShimmerLayout sh_cari;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cari);
        toolbar = getSupportActionBar();
        toolbar.hide();
        Intent intent = getIntent();
        urlnya = intent.getStringExtra("url");
        txtJudul =  intent.getStringExtra("judul");
        rc_cari = findViewById(R.id.rc_cari);
        sh_cari = findViewById(R.id.shimmer_cari);
        jdul = findViewById(R.id.cari_judul);
        jdul.setText(txtJudul);
        no_result = findViewById(R.id.no_result);
        new CardGet().execute();
        sh_cari.startShimmerAnimation();

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
            // -------------- RECENTT ---------------
            //----------------
            Elements mElementDataSize = mBlogPagination.select("div[class=date-posts] div[class=post-outer]");
            // Locate the content attribute
            int mElementSize = mElementDataSize.size();
            int max = 0;
            if(mElementSize>10){
                max=10;
            }else{
                max=mElementSize;
            }
            data = mElementSize;
            //System.out.println("jumlah data"+mElementSize);
            for (int i = 0; i < max; i++) {
                //Judul
                Elements ElemenJudul = mElementDataSize.select("h3[class=post-title entry-title]").eq(i);
                String Nama= ElemenJudul.text();
                //gambar
                Elements elGambar = mElementDataSize.select("div[class=post-body entry-content]").eq(i);
                String gambara = elGambar.select("img").eq(0).attr("src");
                Elements elKat = mElementDataSize.select("div[class=post-footer-line post-footer-line-2] span").eq(i);
                String ket = elKat.select("a").eq(0).text().trim();
                String urlPosting = ElemenJudul.select("a").eq(0).attr("href");
                //STATUS
                Elements elKata = mElementDataSize.select("p.fbquote").eq(i);
                String kata = elKata.text().trim();
                gambarArray.add(gambara);
                cariArray.add(Nama);
                urlArray.add(urlPosting);
            }
            //---------------------------
            //--------------------------
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

            //--------------RECENT -
            //----------------------
            CariAdapter mDataAdapter = new CariAdapter( CariActivity.this, cariArray, urlArray, gambarArray);
            RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(CariActivity.this.getApplicationContext(),1, LinearLayoutManager.VERTICAL, false);
            //attachToRecyclerView
            if(data>0){
                rc_cari.setLayoutManager(mLayoutManager);
                rc_cari.setAdapter(mDataAdapter);
                sh_cari.stopShimmerAnimation();
                sh_cari.setVisibility(View.GONE);
            }else{
                jdul.setVisibility(View.GONE);
                sh_cari.stopShimmerAnimation();
                sh_cari.setVisibility(View.GONE);
                no_result.setVisibility(View.VISIBLE);
            }

            //--------------------------
            //-------------------------

        }

    }
    @Override
    public void onResume() {
        super.onResume();
        sh_cari.startShimmerAnimation();
    }

    @Override
    public void onPause() {
        sh_cari.stopShimmerAnimation();
        super.onPause();
    }
}
