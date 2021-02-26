package com.cookandroid.k_project;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.cookandroid.k_project.Header;
import com.cookandroid.k_project.R;

public class Selfdiagnosis extends Fragment {

    Header Header;
    Button ChildBtn;
    Button AdultBtn;
    //FrameLayout container;

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

        View view = (View)inflater.inflate(R.layout.selfdiagnosis,container,false);
        ChildBtn = (Button) view.findViewById(R.id.student);
        AdultBtn = (Button) view.findViewById(R.id.newActivity);
        //container = (FrameLayout) view.findViewById(R.id.container);

        ChildBtn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                Aframe aframe = new Aframe();
                transaction.replace(R.id.container,aframe);
                transaction.commit();
            }
        });
        AdultBtn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                Bframe bframe = new Bframe();
                transaction.replace(R.id.container,bframe);
                transaction.commit();
            }
        });
        return view;

    }
}