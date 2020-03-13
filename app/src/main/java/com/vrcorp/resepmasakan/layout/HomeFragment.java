package com.vrcorp.resepmasakan.layout;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.vrcorp.resepmasakan.R;
import com.vrcorp.resepmasakan.adapter.HomeAdapter;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;

import okhttp3.internal.http.StatusLine;

import static com.android.volley.VolleyLog.TAG;


public class HomeFragment extends Fragment {
    ProgressDialog dialog;
    SharedPreferences sharedpreferences;
    TextView coba;
    RecyclerView order_list;
    View view;
    SearchView cari;
    SwipeRefreshLayout refresh;
    RecyclerView mRecyclerView;
    Document mBlogDocument  = null, cardDoc = null;
    private ArrayList<String> card_gambar = new ArrayList<>();
    private ArrayList<String> card_kategori = new ArrayList<String>();
    private ArrayList<String> card_judul = new ArrayList<>();
    private ArrayList<String> card_url = new ArrayList<>();
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
        cari.setQueryHint("Cari Konjugasi");
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

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                return false;
            }
        });
        dialog = new ProgressDialog(getActivity());
        dialog.setCancelable(false);
        dialog.setMessage("Memuat data ....");
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
        dialog.show();
        return view;
    }
    private class CardGet extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            // NO CHANGES TO UI TO BE DONE HERE
            String url = "https://resepdemo.blogspot.com/";
            Document mBlogPagination = null;
            try {
                mBlogPagination = Jsoup.parse(new URL(url),50000);
            } catch (IOException e) {
                e.printStackTrace();
            }
            //mBlogPagination.outputSettings().prettyPrint(false);
            Log.d(TAG, "doInBackground: "+mBlogPagination);
            System.out.println(mBlogPagination);
            // Using Elements to get the Meta data
            Elements mElementDataSize = mBlogPagination.select("div[class=date-posts] div[class=post-outer]");
            // Locate the content attribute
            int mElementSize = mElementDataSize.size();
            int max = 0;
            if(mElementSize>10){
                max=10;
            }else{
                max=mElementSize;
            }
            System.out.println("jumlah data"+mElementSize);
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
            System.out.println("url"+card_url);
            System.out.println("kat"+card_kategori);
            System.out.println("judul"+card_judul);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            //This is where we update the UI with the acquired data
            // Set description into TextView
            Log.d(TAG, "onPostExecute: "+result);
            HomeAdapter mDataAdapter = new HomeAdapter( card_judul, card_kategori, card_gambar, card_url);
            RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity().getApplicationContext(),2);
            mRecyclerView.setLayoutManager(mLayoutManager);
            mRecyclerView.setAdapter(mDataAdapter);
            dialog.dismiss();
        }

    }


}