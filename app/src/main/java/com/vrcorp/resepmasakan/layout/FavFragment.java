package com.vrcorp.resepmasakan.layout;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vrcorp.resepmasakan.CariActivity;
import com.vrcorp.resepmasakan.R;
import com.vrcorp.resepmasakan.adapter.CariAdapter;
import com.vrcorp.resepmasakan.db.DBHelper;
import com.vrcorp.resepmasakan.db.DBModel;

import java.util.ArrayList;
import java.util.List;

import io.supercharge.shimmerlayout.ShimmerLayout;


public class FavFragment extends Fragment {

    View view;
    RecyclerView rc_fav;
    LinearLayout ly_fav, no_result;
    TextView favJudul;
    DBHelper helper;
    List<DBModel> dbList;
    int totalData;
    int mentok=0;
    ShimmerLayout sh_fav;
     ArrayList<String> cariArray = new ArrayList<>();
     ArrayList<String> gambarArray = new ArrayList<>();
     ArrayList<String> urlArray = new ArrayList<>();
     ArrayList<String> jenisList = new ArrayList<>();
    public FavFragment() {
        // Required empty public constructor
    }


    public static FavFragment newInstance(String param1, String param2) {
        FavFragment fragment = new FavFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_fav, container, false);
        rc_fav = view.findViewById(R.id.rc_fav);
        no_result = view.findViewById(R.id.no_result);
        favJudul = view.findViewById(R.id.fav_judul);
        sh_fav = view.findViewById(R.id.shimmer_fav);
        sh_fav.startShimmerAnimation();
        //Cursor cursor = db.fetchMahasiswa(tnim);
        helper = new DBHelper(getContext());
        totalData = helper.getTotalFav();
        dbList= new ArrayList<DBModel>();
        dbList = helper.getFromDB();
        System.out.println("List"+totalData);
        if(totalData<1){
            favJudul.setVisibility(View.GONE);
            sh_fav.stopShimmerAnimation();
            sh_fav.setVisibility(View.GONE);
            rc_fav.setVisibility(View.GONE);
            no_result.setVisibility(View.VISIBLE);
        }else{
            for (int i = 0; i < totalData; i++) {
                cariArray.add(dbList.get(i).getJudul());
                gambarArray.add(dbList.get(i).getGambar());
                urlArray.add(dbList.get(i).getUrl());
                jenisList.add("fav");
                System.out.println("Masuk while "+urlArray);
                if(totalData - i == 1){
                    mentok=1;
                }
            }
            if(mentok>0){
                CariAdapter mDataAdapter = new CariAdapter( getActivity(), cariArray, urlArray, gambarArray,jenisList);
                RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity().getApplicationContext(),1, LinearLayoutManager.VERTICAL, false);
                rc_fav.setLayoutManager(mLayoutManager);
                rc_fav.setAdapter(mDataAdapter);
                sh_fav.stopShimmerAnimation();
                sh_fav.setVisibility(View.GONE);
                System.out.println("Mentok"+jenisList);
                System.out.println("Mentok"+gambarArray);
                System.out.println("Mentok"+cariArray);

            }

        }
        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        sh_fav.startShimmerAnimation();
    }

    @Override
    public void onPause() {
        sh_fav.stopShimmerAnimation();
        super.onPause();
    }

}
