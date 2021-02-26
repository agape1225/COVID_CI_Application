package com.cookandroid.k_project;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.cookandroid.k_project.Header;
import com.cookandroid.k_project.R;


public class Setting extends Fragment {

    LinearLayout buttongroup;
    Header Header;

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
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        DrawerLayout drawerLayout;
        Button verbtn, noticebtn, informationbtn;
        TextView tvuser;
        ViewGroup rootview = (ViewGroup) inflater.inflate(R.layout.setting, container, false);
        drawerLayout = rootview.findViewById(R.id.drawer_layout);

        verbtn = (Button) rootview.findViewById(R.id.btnver);
        noticebtn = (Button) rootview.findViewById(R.id.btnnotice);
        informationbtn = (Button) rootview.findViewById(R.id.btninformation);

        buttongroup=(LinearLayout)Header.findViewById(R.id.buttongroup);
        buttongroup.setVisibility(View.GONE);

        verbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                Verinfo verinfo = new Verinfo();
                transaction.replace(R.id.container, verinfo);
                transaction.commit();

            }
        });
        noticebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                Notice notice = new Notice();
                transaction.replace(R.id.container, notice);
                transaction.commit();
            }
        });
        informationbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                InformationManage informationManage = new InformationManage();
                transaction.replace(R.id.container, informationManage);
                transaction.commit();
            }
        });


        return rootview;
    }
}