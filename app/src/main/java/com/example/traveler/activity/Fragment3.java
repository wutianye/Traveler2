package com.example.traveler.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.traveler.R;
import com.example.traveler.user.Login;

public class Fragment3 extends Fragment implements View.OnClickListener {
    private TextView txt_login;
    private View view;
    private RelativeLayout layout_star;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_fragment3, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
    }

    private void judgeLoginStatus() {

    }

    private void initView() {
        txt_login = view.findViewById(R.id.me_txt_loginflag);
        txt_login.setOnClickListener(this);
        layout_star = view.findViewById(R.id.me_star);
        layout_star.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.me_txt_loginflag:
                Intent intent = new Intent(getActivity(), Login.class);
                startActivity(intent);
                break;
            case R.id.me_star:
                intent = new Intent(getActivity(), Login.class);
                startActivity(intent);
                break;
        }
    }
}

