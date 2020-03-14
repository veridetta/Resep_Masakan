package com.vrcorp.resepmasakan.layout;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;


import com.hhl.gridpagersnaphelper.GridPagerSnapHelper;
import com.vrcorp.resepmasakan.CariActivity;
import com.vrcorp.resepmasakan.R;
import com.vrcorp.resepmasakan.adapter.HomeAdapter;
import com.vrcorp.resepmasakan.adapter.SmallAdapter;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import io.supercharge.shimmerlayout.ShimmerLayout;


public class HomeFragment extends Fragment {
    ProgressDialog dialog;
    SharedPreferences sharedpreferences;
    TextView coba;
    ShimmerLayout sh_recent, sh_pop,sh_ay;
    View view;
    SearchView cari;
    SwipeRefreshLayout refresh;
    RecyclerView mRecyclerView,popRc, ayamRc;
    Document mBlogDocument  = null, cardDoc = null;
    private ArrayList<String> card_gambar = new ArrayList<>();
    private ArrayList<String> card_kategori = new ArrayList<String>();
    private ArrayList<String> card_judul = new ArrayList<>();
    private ArrayList<String> card_url = new ArrayList<>();
    //--------------------------
    //-----------------------
    private ArrayList<String> pop_gambar = new ArrayList<>();
    private ArrayList<String> pop_kategori = new ArrayList<String>();
    private ArrayList<String> pop_judul = new ArrayList<>();
    private ArrayList<String> pop_url = new ArrayList<>();
    //-------------------
    //-------------------
    private ArrayList<String> ay_gambar = new ArrayList<>();
    private ArrayList<String> ay_kategori = new ArrayList<String>();
    private ArrayList<String> ay_judul = new ArrayList<>();
    private ArrayList<String> ay_url = new ArrayList<>();
    public HomeFragment() {
        // Required empty public constructor
    }


    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_home, container, false);
        //noData = view.findViewById(R.id.no_data);
        sharedpreferences = getActivity().getSharedPreferences("resepMakanan", Context.MODE_PRIVATE);
        //string_id = sharedpreferences.getString("id", null);
        cari = view.findViewById(R.id.cari_input);
        coba = view.findViewById(R.id.coba);
        //refresh = view.findViewById(R.id.swiperefresh);
        //refresh.setRefreshing(true);
        mRecyclerView= view.findViewById(R.id.rc_home);
        mRecyclerView.setHasFixedSize(true);
        popRc= view.findViewById(R.id.rc_populer);
        popRc.setHasFixedSize(true);
        ayamRc= view.findViewById(R.id.rc_ayam);
        ayamRc.setHasFixedSize(true);
        sh_recent = view.findViewById(R.id.shimmer_recent_update);
        sh_pop = view.findViewById(R.id.shimmer_populer);
        sh_ay= view.findViewById(R.id.shimmer_ayam);
        cari.setQueryHint("Masukan Kata Kunci");
        cari.onActionViewExpanded();
        cari.setIconified(true);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                cari.clearFocus();
            }
        }, 300);
        cari.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                String cariVal = cari.getQuery().toString();
                String urlnya = "https://resepdemo.blogspot.com/search?q="+cariVal;
                if(cariVal.length()<3){
                    Toast.makeText(getActivity(),"Minimal kata minimal 3 huruf",Toast.LENGTH_LONG).show();
                }else if(cariVal.length()>10){
                    Toast.makeText(getActivity(),"Maksimal 10 huruf",Toast.LENGTH_LONG).show();
                }else{
                    Intent intent = new Intent(getActivity(), CariActivity.class);
                    intent.putExtra("url",urlnya);
                    intent.putExtra("judul","Hasil cari dari kata "+cariVal);
                    startActivity(intent);
                }

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                return false;
            }
        });
        //dialog = new ProgressDialog(getActivity());
        //dialog.setCancelable(false);
        //dialog.setMessage("Memuat data ....");
        /*refresh.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        dialog.show();
                        new CardGet().execute();
                    }
                }
        );*/
        new CardGet().execute();
        //dialog.show();
        sh_recent.startShimmerAnimation();
        sh_pop.startShimmerAnimation();
        sh_ay.startShimmerAnimation();
        return view;
    }
    private class CardGet extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            // NO CHANGES TO UI TO BE DONE HERE
            String url = "https://resepdemo.blogspot.com/";
            String urlPopuler ="https://resepdemo.blogspot.com/search/label/rekomendasi";
            String urlAyam = "https://resepdemo.blogspot.com/search/label/ayam";
            Document mBlogPagination = null;
            Document pagPopuler = null;
            Document pageAyam = null;
            try {
                mBlogPagination = Jsoup.parse(new URL(url),50000);
                pagPopuler = Jsoup.parse(new URL(urlPopuler),50000);
                pageAyam = Jsoup.parse(new URL(urlAyam),50000);
            } catch (IOException e) {
                e.printStackTrace();
            }
            //mBlogPagination.outputSettings().prettyPrint(false);
            //Log.d(TAG, "doInBackground: "+mBlogPagination);
            //System.out.println(mBlogPagination);
            // Using Elements to get the Meta data
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
                card_gambar.add(gambara);
                card_kategori.add(ket);
                card_judul.add(Nama);
                card_url.add(urlPosting);
            }
            //---------------------------
            //--------------------------
            //----POP
            //------------
            Elements populerSize = pagPopuler.select("div[class=date-posts] div[class=post-outer]");
            // Locate the content attribute
            int mpopulerSize = populerSize.size();
            if(mpopulerSize>10){
                max=10;
            }else{
                max=mpopulerSize;
            }
            //System.out.println("jumlah data"+mElementSize);
            for (int ip = 0; ip < max; ip++) {
                //Judul
                Elements popJudul = populerSize.select("h3[class=post-title entry-title]").eq(ip);
                String popNama= popJudul.text();
                //gambar
                Elements popGambar = populerSize.select("div[class=post-body entry-content]").eq(ip);
                String popGambara = popGambar.select("img").eq(0).attr("src");
                Elements popKat = populerSize.select("div[class=post-footer-line post-footer-line-2] span").eq(ip);
                String popKet = popKat.select("a").eq(0).text().trim();
                String popUrl = popJudul.select("a").eq(0).attr("href");
                pop_gambar.add(popGambara);
                pop_kategori.add(popKet);
                pop_judul.add(popNama);
                pop_url.add(popUrl);
            }
            //---------------------------
            //--------------------------
            //----AYAM
            //------------
            Elements ayamsize = pageAyam.select("div[class=date-posts] div[class=post-outer]");
            // Locate the content attribute
            int mayamsize = ayamsize.size();
            if(mayamsize>10){
                max=10;
            }else{
                max=mayamsize;
            }
            //System.out.println("jumlah data"+mElementSize);
            for (int iy = 0; iy < max; iy++) {
                //Judul
                Elements ayJudul = ayamsize.select("h3[class=post-title entry-title]").eq(iy);
                String ayNama= ayJudul.text();
                //gambar
                Elements ayGambar = ayamsize.select("div[class=post-body entry-content]").eq(iy);
                String ayGambara = ayGambar.select("img").eq(0).attr("src");
                Elements ayKat = ayamsize.select("div[class=post-footer-line post-footer-line-2] span").eq(iy);
                String ayKet = ayKat.select("a").eq(0).text().trim();
                String ayUrl = ayJudul.select("a").eq(0).attr("href");
                ay_gambar.add(ayGambara);
                ay_kategori.add(ayKet);
                ay_judul.add(ayNama);
                ay_url.add(ayUrl);
            }
            //---------------------------
            //--------------------------
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            //This is where we update the UI with the acquired data
            // Set description into TextView
            //Log.d(TAG, "onPostExecute: "+result);
            //--------------RECENT -
            //----------------------
            HomeAdapter mDataAdapter = new HomeAdapter( getActivity(), card_judul, card_kategori, card_gambar, card_url);
            RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity().getApplicationContext(),1, LinearLayoutManager.HORIZONTAL, false);
            //attachToRecyclerView
            GridPagerSnapHelper gridPagerSnapHelper = new GridPagerSnapHelper();
            gridPagerSnapHelper.setRow(1).setColumn(1);
            gridPagerSnapHelper.attachToRecyclerView(mRecyclerView);

            mRecyclerView.setLayoutManager(mLayoutManager);
            mRecyclerView.setAdapter(mDataAdapter);
            sh_recent.stopShimmerAnimation();
            sh_recent.setVisibility(View.GONE);
            //--------------------------
            //-------------------------
            //--------------RECENT -
            //----------------------
            SmallAdapter popDataAdapter = new SmallAdapter( getActivity(),pop_judul, pop_kategori, pop_gambar, pop_url);
            RecyclerView.LayoutManager poplayoutManager = new GridLayoutManager(getActivity().getApplicationContext(),1, LinearLayoutManager.HORIZONTAL, false);
            //attachToRecyclerView
            GridPagerSnapHelper popGrid = new GridPagerSnapHelper();
            popGrid.setRow(1).setColumn(2);
            popGrid.attachToRecyclerView(popRc);

            popRc.setLayoutManager(poplayoutManager);
            popRc.setAdapter(popDataAdapter);
            sh_pop.stopShimmerAnimation();
            sh_pop.setVisibility(View.GONE);
            //--------------------------
            //-------------------------
            //--------------AYAM -
            //----------------------
            SmallAdapter ayamDataAdapter = new SmallAdapter( getActivity(),ay_judul, ay_kategori, ay_gambar, ay_url);
            RecyclerView.LayoutManager ayamLayoutManager = new GridLayoutManager(getActivity().getApplicationContext(),1, LinearLayoutManager.HORIZONTAL, false);
            //attachToRecyclerView
            GridPagerSnapHelper ayamGrid = new GridPagerSnapHelper();
            ayamGrid.setRow(1).setColumn(2);
            ayamGrid.attachToRecyclerView(ayamRc);

            ayamRc.setLayoutManager(ayamLayoutManager);
            ayamRc.setAdapter(ayamDataAdapter);
            sh_ay.stopShimmerAnimation();
            sh_ay.setVisibility(View.GONE);
            //--------------------------
            //-------------------------
            //dialog.dismiss();
        }

    }
    @Override
    public void onResume() {
        super.onResume();
        sh_recent.startShimmerAnimation();
        sh_pop.startShimmerAnimation();
        sh_ay.startShimmerAnimation();
    }

    @Override
    public void onPause() {
        sh_recent.stopShimmerAnimation();
        sh_pop.stopShimmerAnimation();
        sh_ay.stopShimmerAnimation();
        super.onPause();
    }

}