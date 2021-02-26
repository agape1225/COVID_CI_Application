package com.cookandroid.k_project;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;


public class Verinfo extends Fragment {

    Header Header;
    LinearLayout buttongroup;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Header = (Header) getActivity();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Header = null;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        DrawerLayout drawerLayout;
        //String username=((Chart)Chart.Chart).username;;
        TextView tvuser;
        ViewGroup rootview = (ViewGroup)inflater.inflate(R.layout.verinfo,container,false);
        drawerLayout = rootview.findViewById(R.id.drawer_layout);
        buttongroup=(LinearLayout)Header.findViewById(R.id.buttongroup);
        buttongroup.setVisibility(View.GONE);
        return rootview;
    }
}