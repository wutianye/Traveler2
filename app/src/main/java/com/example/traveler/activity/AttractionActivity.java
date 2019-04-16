package com.example.traveler.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.traveler.R;
import com.example.traveler.pojo.Attraction;
import com.example.traveler.pojo.Comment;
import com.example.traveler.pojo.User;
import com.example.traveler.util.RetrofitHelper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

import static com.jaeger.library.StatusBarUtil.setTranslucentForCoordinatorLayout;

public class AttractionActivity extends AppCompatActivity implements View.OnClickListener{

    private ImageView imageStar;
    private int isStar = 0;
    private ImageView map_btn;
    private ImageView comment_btn;
    private BaseQuickAdapter<Comment, BaseViewHolder> adapter;
    private RecyclerView recyclerView;
    private Attraction a;
    private long starId;
    List<Comment> attractionsList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attraction);
        initView();
        initCommentRecycler();
        getStar();
        getCommentData();
    }

    private void initView(){
        //StatusBarUtil.setTranslucentForImageView(AttractionActivity.this, 0, imageView);
        //StatusBarUtil.setTransparent(AttractionActivity.this);
        setTranslucentForCoordinatorLayout(AttractionActivity.this, 0);
        imageStar = findViewById(R.id.attraction_star);
        imageStar.setOnClickListener(this);
        map_btn = findViewById(R.id.attraction_map);
        map_btn.setOnClickListener(this);
        comment_btn = findViewById(R.id.attraction_comment);
        comment_btn.setOnClickListener(this);

        //接收数据
        a = (Attraction) getIntent().getSerializableExtra("attraction");
        initAttractionData();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.attraction_star:
                if (isStar == 0) {
                    imageStar.setImageResource(R.mipmap.star2);
                    addStar();

                } else {
                    imageStar.setImageResource(R.mipmap.star);
                    delStar();
                }
                break;

            case R.id.attraction_comment:
                ReplyDialogFragment replyDialogFragment = new ReplyDialogFragment();
                Bundle bundle = new Bundle();
                bundle.putString("attrname", a.getAttr_name());
                replyDialogFragment.setArguments(bundle);
                replyDialogFragment.attach(new ReplyDialogFragment.CommentInterface() {
                    @Override
                    public void add(String t) {
                        attractionsList.add(new Comment((long) 1, "test", t));
                        adapter.setNewData(attractionsList);
                        // recyclerView.scrollToPosition(adapter.getItemCount()-1);
                    }
                });
                replyDialogFragment.show(getFragmentManager(),"comment");

                break;
            case R.id.attraction_map:
                break;
        }
    }

    private void initAttractionData() {
        TextView txt_name = findViewById(R.id.attraction_name);
        TextView txt_about = findViewById(R.id.attraction_about);
        TextView txt_address = findViewById(R.id.attraction_address);
        ImageView img = findViewById(R.id.attraction_img);
        txt_name.setText(a.getAttr_name());
        txt_about.setText(a.getIntroduction());
        txt_address.setText(a.getStatus());
        String u = "";
        try {
            u = java.net.URLDecoder.decode(a.getImageurl(), "UTF-8");
            Log.d("转化的url", u);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        Glide.with(this).load(u).into(img);
        if (isStar == 0)
            imageStar.setImageResource(R.mipmap.star);
        else
            imageStar.setImageResource(R.mipmap.star2);

    }


    private void initCommentRecycler() {
//        List<Comment> list = new ArrayList<>();
//        list.add(new Comment((long)1,new User((long) 1,"aaa"),"很好玩，很好看"));
//        list.add(new Comment((long)2,new User((long) 2,"bbb"),"很好玩，很好看啊"));
//        list.add(new Comment((long)3,new User((long) 3,"ccc"),"很好玩，很好看"));
//        list.add(new Comment((long)3,new User((long) 3,"ccc"),"很好玩，很好看"));
//        list.add(new Comment((long)3,new User((long) 3,"ccc"),"很好玩，很好看"));
//        list.add(new Comment((long)3,new User((long) 3,"ccc"),"很好玩，很好看"));
//        list.add(new Comment((long)3,new User((long) 3,"ccc"),"很好玩，很好看"));
//        list.add(new Comment((long)3,new User((long) 3,"ccc"),"很好玩，很好看"));
        recyclerView = findViewById(R.id.attraction_recycler);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new BaseQuickAdapter<Comment, BaseViewHolder>(R.layout.recycler_comment, new ArrayList<Comment>()) {
            @Override
            protected void convert(BaseViewHolder helper, Comment item) {
                helper.setText(R.id.item_comment_tv_userName, item.getPublisher())
                        .setText(R.id.item_comment_tv_content,item.getContent());

            }
        };
        recyclerView.setAdapter(adapter);

    }

    private void getStar() {
        Retrofit retrofit = RetrofitHelper.getInstence();
        StarService dataService = retrofit.create(StarService.class);
        retrofit2.Call<ResponseBody> call = dataService.search("test", a.getAttr_name());
        call.enqueue(new retrofit2.Callback<ResponseBody>() {
            @Override
            public void onResponse(retrofit2.Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                if (response.body() == null)
                    return;
                try {
                    JSONArray jsonArray = new JSONArray(response.body().string());
                    if (jsonArray.length() > 0) {
                        starId = jsonArray.getLong(0);
                        Log.d("starId", starId + "");
                        isStar = 1;
                    } else
                        isStar = 0;
                    if (isStar == 1)
                        imageStar.setImageResource(R.mipmap.star2);
                    else
                        imageStar.setImageResource(R.mipmap.star);
                } catch (JSONException | IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(retrofit2.Call<ResponseBody> call, Throwable t) {
                Log.d("fail", "获取数据失败");
            }
        });
    }

    private void getCommentData() {
        Retrofit retrofit = RetrofitHelper.getInstence();
        DataService dataService = retrofit.create(DataService.class);
        retrofit2.Call<ResponseBody> call = dataService.search(a.getAttr_name());
        call.enqueue(new retrofit2.Callback<ResponseBody>() {
            @Override
            public void onResponse(retrofit2.Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                Log.d("connect_success", "success");
                if (response.body() == null)
                    return;
                try {
                    JSONObject jsonObject = new JSONObject(response.body().string());
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    attractionsList = new Gson().fromJson(jsonArray.toString(), new TypeToken<List<Comment>>() {
                    }.getType());
                    adapter.setNewData(attractionsList);
                } catch (JSONException | IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(retrofit2.Call<ResponseBody> call, Throwable t) {
                Log.d("fail", "获取数据失败");
            }
        });
    }

    private void addStar() {
        Retrofit retrofit = RetrofitHelper.getInstence();
        AddStarService addStarService = retrofit.create(AddStarService.class);
        retrofit2.Call<ResponseBody> call = addStarService.search("test", a.getAttr_name());
        call.enqueue(new retrofit2.Callback<ResponseBody>() {
            @Override
            public void onResponse(retrofit2.Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                Log.d("connect_success", "success");
                isStar = 1;
            }

            @Override
            public void onFailure(retrofit2.Call<ResponseBody> call, Throwable t) {
                Log.d("fail", "收藏失败");
                isStar = 0;
            }
        });
    }

    private void delStar() {
        Retrofit retrofit = RetrofitHelper.getInstence();
        DelStarService delStarService = retrofit.create(DelStarService.class);
        retrofit2.Call<ResponseBody> call = delStarService.search(starId);
        call.enqueue(new retrofit2.Callback<ResponseBody>() {
            @Override
            public void onResponse(retrofit2.Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                Log.d("connect_success", "success");
                isStar = 0;
            }

            @Override
            public void onFailure(retrofit2.Call<ResponseBody> call, Throwable t) {
                Log.d("fail", "取消收藏失败");
            }
        });
    }


    public interface DataService {
        @GET("Comments/queryCommentByAttr/{name}")
        retrofit2.Call<ResponseBody> search(@Path("name") String s);
    }

    public interface StarService {
        @GET("Collections/confirmCollection/{username},{attrname}")
        retrofit2.Call<ResponseBody> search(@Path("username") String s, @Path("attrname") String n);
    }

    public interface AddStarService {
        @POST("Collections/addCollection/")
        retrofit2.Call<ResponseBody> search(@Query("username") String s, @Query("attrname") String n);
    }

    public interface DelStarService {
        @GET("Collections/deleteCollection/{id}")
        retrofit2.Call<ResponseBody> search(@Path("id") Long id);
    }

}
