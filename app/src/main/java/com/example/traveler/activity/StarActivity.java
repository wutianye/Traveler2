package com.example.traveler.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.example.traveler.util.RetrofitHelper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.http.GET;

public class StarActivity extends AppCompatActivity {
    private BaseQuickAdapter<Attraction, BaseViewHolder> adapter;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_star);
        initRecycler();
    }

    private void initRecycler() {
        recyclerView = findViewById(R.id.fragment_home_recycler);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        final List<Integer> imageList = new ArrayList<>();
        imageList.add(R.drawable.tu1);
        imageList.add(R.drawable.tu2);
        imageList.add(R.drawable.tu3);

        recyclerView.setLayoutManager(layoutManager);
        adapter = new BaseQuickAdapter<Attraction, BaseViewHolder>(R.layout.recycler_home, new ArrayList<Attraction>()) {
            @Override
            protected void convert(BaseViewHolder helper, Attraction item) {
                helper.setText(R.id.recycler_home_tv, item.getAttr_name())
                        .setText(R.id.recycler_home_peoplenum, item.getP_num())
                        .setText(R.id.recycler_home_open, item.getOpen_time())
                        .setText(R.id.recycler_home_close, item.getClose_time());
                ImageView imageView = findViewById(R.id.recycler_home_img);
                Glide.with(StarActivity.this).load(item.getImageurl()).into(imageView);

            }
        };
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(StarActivity.this, AttractionActivity.class);
                Attraction attraction = (Attraction) adapter.getItem(position);
                intent.putExtra("attraction", attraction);
            }
        });
        recyclerView.setAdapter(adapter);
    }

    private void getAttractionData() {
        Retrofit retrofit = RetrofitHelper.getInstence();
        DataService dataService = retrofit.create(DataService.class);
        retrofit2.Call<ResponseBody> call = dataService.search();
        call.enqueue(new retrofit2.Callback<ResponseBody>() {
            @Override
            public void onResponse(retrofit2.Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                Log.d("connect_success", "success");
                if (response.body() == null)
                    return;
                try {
                    JSONObject jsonObject = new JSONObject(response.body().string());
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    List<Attraction> attractionsList;
                    attractionsList = new Gson().fromJson(jsonArray.toString(), new TypeToken<List<Attraction>>() {
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

    public interface DataService {
        @GET("attractions/all")
        retrofit2.Call<ResponseBody> search();
    }
}
