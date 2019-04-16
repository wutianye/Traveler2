package com.example.traveler.activity;

import android.app.DialogFragment;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import com.example.traveler.R;
import com.example.traveler.pojo.Comment;
import com.example.traveler.util.RetrofitHelper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public class ReplyDialogFragment extends DialogFragment {

    InputMethodManager inputManager;
    private TextView tv_reply;
    private TextView btn;
    private String attrname;
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

        Bundle bundle = getArguments();
        attrname = bundle.getString("attrname");
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
        submitComment();
        //String content = tv_reply.getText().toString();
        inputManager.hideSoftInputFromWindow(tv_reply.getWindowToken(), 0);
        commentInterface.add(tv_reply.getText().toString());
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

    private void submitComment() {
        Retrofit retrofit = RetrofitHelper.getInstence();
        CommitService service = retrofit.create(CommitService.class);
        retrofit2.Call<ResponseBody> call = service.search("test", tv_reply.getText().toString(), attrname);
        call.enqueue(new retrofit2.Callback<ResponseBody>() {
            @Override
            public void onResponse(retrofit2.Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                Log.d("connect_success", "success");
            }

            @Override
            public void onFailure(retrofit2.Call<ResponseBody> call, Throwable t) {
                Log.d("fail", "提交数据失败");
            }
        });
    }

    public interface CommitService {
        @POST("Comments/addComment")
        retrofit2.Call<ResponseBody> search(@Query("publisher") String p, @Query("content") String c, @Query("attrname") String a);
    }

    public interface CommentInterface {
        public void add(String t);
    }

    CommentInterface commentInterface;

    public void attach(CommentInterface cinterface) {
        this.commentInterface = cinterface;
    }

}
