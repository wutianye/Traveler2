package com.example.traveler.activity;

import android.app.DialogFragment;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import com.example.traveler.R;

public class ReplyDialogFragment extends DialogFragment {

    InputMethodManager inputManager;
    private TextView tv_reply;
    private TextView btn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.dialog_reply, container,false);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.dialog);
        setCancelable(true);
        tv_reply = v.findViewById(R.id.dialog_reply_ed_comment);
        btn = v.findViewById(R.id.dialog_reply_publish_comment);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                publish();
            }
        });

        WindowManager.LayoutParams params = getDialog().getWindow().getAttributes();
        params.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
        getDialog().getWindow().setAttributes(params);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        // 这句必须加，否则会有间距
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {

        tv_reply.setFocusable(true);
        tv_reply.requestFocus();
     //调用系统输入法

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                inputManager = (InputMethodManager) tv_reply.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.showSoftInput(tv_reply, 0);
            }
        },200);



    }

    private void publish(){

        //String content = tv_reply.getText().toString();
        inputManager.hideSoftInputFromWindow(tv_reply.getWindowToken(), 0);
        dismiss();

    }

    @Override
    public void onStart(){
        super.onStart();
        getDialog().getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        Window window = getDialog().getWindow();
        WindowManager.LayoutParams lp= window.getAttributes();
        lp.dimAmount =0f;
        lp.flags |= WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(lp);
    }

}
