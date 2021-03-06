package com.example.traveler.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.traveler.R;
import com.example.traveler.pojo.Attraction;
import com.example.traveler.searchview.ICallBack;
import com.example.traveler.searchview.SearchView;
import com.example.traveler.searchview.bCallBack;
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
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.http.GET;
import retrofit2.http.Path;


/**
 * Created by Carson_Ho on 17/8/11.
 */

public class SearchDemo extends AppCompatActivity {

    // 1. 初始化搜索框变量
    private SearchView searchView;
    private BaseQuickAdapter<Attraction, BaseViewHolder> adapter;
    private RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initSearchView();
        initRecycler();
    }

    private void initSearchView() {
        // 2. 绑定视图
        setContentView(R.layout.activity_search);

        // 3. 绑定组件
        searchView = (SearchView) findViewById(R.id.search_view);

        // 4. 设置点击搜索按键后的操作（通过回调接口）
        // 参数 = 搜索框输入的内容
        searchView.setOnClickSearch(new ICallBack() {
            @Override
            public void SearchAciton(String string) {
                Log.d("我收到了", string);
                searchForData(string);
            }
        });

        // 5. 设置点击返回按键后的操作（通过回调接口）
        searchView.setOnClickBack(new bCallBack() {
            @Override
            public void BackAciton() {
                finish();
            }
        });
    }

    private void initRecycler() {
        recyclerView = findViewById(R.id.search_result_recycler);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);

        recyclerView.setLayoutManager(layoutManager);
        adapter = new BaseQuickAdapter<Attraction, BaseViewHolder>(R.layout.recycler_home, new ArrayList<Attraction>()) {
            @Override
            protected void convert(BaseViewHolder helper, Attraction item) {
                helper.setText(R.id.recycler_home_tv, item.getAttr_name())
                        .setText(R.id.recycler_home_peoplenum, item.getP_num())
                        .setText(R.id.recycler_home_open, item.getOpen_time())
                        .setText(R.id.recycler_home_close, item.getClose_time());
                ImageView imageView = helper.getView(R.id.recycler_home_img);
                String u = "";
                try {
                    u = java.net.URLDecoder.decode(item.getImageurl(), "UTF-8");
                    Log.d("转化的url", u);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                Glide.with(SearchDemo.this).load(u).into(imageView);

            }
        };
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(SearchDemo.this, AttractionActivity.class);
                Attraction attraction = (Attraction) adapter.getItem(position);
                intent.putExtra("attraction", attraction);
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(adapter);
    }
    private void searchForData(String text) {
        Retrofit retrofit = RetrofitHelper.getInstence();
        DataService dataService = retrofit.create(DataService.class);
//        try {
//            text   =   URLEncoder.encode(text,   "utf-8");
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
        retrofit2.Call<ResponseBody> call = dataService.search(text);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    JSONObject jsonObject = new JSONObject(response.body().string());
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    Log.d("jso", "" + jsonArray);
                    List<Attraction> attractionsList = new Gson().fromJson(jsonArray.toString(), new TypeToken<List<Attraction>>() {
                    }.getType());
                    adapter.setNewData(attractionsList);
                } catch (JSONException | IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    public interface DataService {
        @GET("attractions/queryAttraction/{name}")
        retrofit2.Call<ResponseBody> search(@Path("name") String name);
    }

}