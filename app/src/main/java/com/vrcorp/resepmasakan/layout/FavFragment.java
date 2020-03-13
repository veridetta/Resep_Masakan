package com.vrcorp.resepmasakan.layout;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vrcorp.resepmasakan.R;


public class FavFragment extends Fragment {

    View view;

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
        return view;
    }


}
