package com.example.traveler.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.traveler.R;
import com.example.traveler.db.City;
import com.example.traveler.db.County;
import com.example.traveler.db.Province;
import com.example.traveler.gson.Weather;
import com.example.traveler.pojo.Attraction;
import com.example.traveler.util.HttpUtil;
import com.example.traveler.util.JudgePermissionUtil;
import com.example.traveler.util.RetrofitHelper;
import com.example.traveler.util.Utility;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.http.GET;

public class Fragment1 extends Fragment implements View.OnClickListener {
    public LocationClient mLocationClient = null;
    TextView text_home;
    TextView text_wendu;
    TextView text_tianqi;
    ImageView img_today;
    ImageView search;
    Map<String, Integer> weather_icon_list = new HashMap<String, Integer>();
    private View view;
    private MyLocationListener myListener = new MyLocationListener();
    private String p, c, d;
    private BaseQuickAdapter<Attraction, BaseViewHolder> adapter;
    private RecyclerView recyclerView;
    private Province selectedProvince;
    private City selectedCity;
    private String WeatherId;
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String info = (String) msg.obj;
            String re[] = info.split(";");
            if (msg.what == 1) {
                text_tianqi.setText(re[1]);
                text_wendu.setText(re[0]);
                text_home.setText(d);
                img_today.setImageResource(weather_icon_list.get(judgeWeather(re[1])));
            }

        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_fragment1, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        judgePermission();
        initRecycler();
    }

    private void initRecycler() {
        recyclerView = view.findViewById(R.id.fragment_home_recycler);
        LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext());
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
                ImageView imageView = helper.getView(R.id.recycler_home_img);
                String u = "";
                try {
                    u = java.net.URLDecoder.decode(item.getImageurl(), "UTF-8");
                    Log.d("转化的url", u);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                Glide.with(getActivity()).load(u).into(imageView);

            }
        };
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(getActivity(), AttractionActivity.class);
                Attraction attraction = (Attraction) adapter.getItem(position);
                intent.putExtra("attraction", attraction);
                startActivity(intent);
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
                    Log.d("jso", "" + jsonArray);
                    List<Attraction> attractionsList;
                    attractionsList = new Gson().fromJson(jsonArray.toString(), new TypeToken<List<Attraction>>() {
                    }.getType());
//                    for(int i=0;i<jsonArray.length();i++){
//                        Attraction s = new Gson().fromJson(jsonArray.getJSONObject(i).toString(),Attraction.class);
//                        attractionsList.add(s);
//                    }
                    Log.d("num", "此数组一共有" + attractionsList.size());
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

    public void initWeatherIconList() {
        weather_icon_list.put("晴", R.mipmap.weather_qing);
        weather_icon_list.put("阴", R.mipmap.weather_ying);
        weather_icon_list.put("多云", R.mipmap.weather_duoyun);
        weather_icon_list.put("小雨", R.mipmap.weather_xiaoyu);
        weather_icon_list.put("有雨", R.mipmap.weather_dayu);
        weather_icon_list.put("雷", R.mipmap.weather_lei);
    }

    public String judgeWeather(String w) {
        String weather = w;
        if (w.contains("雨") && w != "小雨")
            weather = "有雨";
        if (w.contains("云"))
            weather = "多云";
        if (w.contains("雷"))
            weather = "雷";
        return weather;
    }

    private void judgePermission() {
        if (JudgePermissionUtil.judgePermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) &&
                JudgePermissionUtil.judgePermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) &&
                JudgePermissionUtil.judgePermission(getActivity(), Manifest.permission.READ_PHONE_STATE)
                ) {
            getLocationByBDMap();
            Log.d("eee", "有权限定位");
            //如果存在定位权限，则开始接下来的工作
        } else
            JudgePermissionUtil.setPermission(getActivity(),
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.READ_PHONE_STATE});
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1:
                Log.d("ddd", "sss");
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getLocationByBDMap();

                    Log.d("true", "点击确定，获得权限，开始工作");

                } else {
                    Toast.makeText(getActivity(), "你拒绝了应用获取地理位置信息", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
        }
    }

    private void initWeather() {
        RelativeLayout weather_layout = view.findViewById(R.id.weather_layout);
        weather_layout.setOnClickListener(this);
        search = view.findViewById(R.id.btn_search);
        search.setOnClickListener(this);
        text_home = view.findViewById(R.id.fragment_home_name);
        text_wendu = view.findViewById(R.id.fragment_home_today_wendu);
        text_tianqi = view.findViewById(R.id.fragment_home_today_tianqi);
        img_today = view.findViewById(R.id.fragment_home_today_img);
        Log.d("ee", "initweather");
        queryProvinces();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.weather_layout:
                Intent intent = new Intent(getActivity(), WeatherActivity.class);
                intent.putExtra("weather_id", WeatherId);
                startActivity(intent);
                break;

            case R.id.btn_search:
                Intent intent1 = new Intent(getActivity(), SearchDemo.class);
                startActivity(intent1);
                break;
        }
    }

    private void getLocationByBDMap() {
        mLocationClient = new LocationClient(view.getContext());
        //声明LocationClient类
        mLocationClient.registerLocationListener(myListener);

        LocationClientOption option = new LocationClientOption();
        option.setIsNeedAddress(true);
        //如果开发者需要获得当前点的地址信息，此处必须为true
        mLocationClient.setLocOption(option);
        //mLocationClient为第二步初始化过的LocationClient对象
        //需将配置好的LocationClientOption对象，通过setLocOption方法传递给LocationClient对象使用
        mLocationClient.start();

    }

    private void queryProvinces() {
        /*
      获取省市列表，传入weatherId
     */
        List<Province> provinceList = DataSupport.findAll(Province.class);
        if (provinceList.size() > 0) {
            for (Province province : provinceList) {
                if (p.contains(province.getProvinceName())) {
                    selectedProvince = province;
                    queryCities();
                }
            }
        } else {
            String address = "http://guolin.tech/api/china";
            queryFromServer(address, "province");
        }
    }

    private void queryCities() {
        List<City> cityList = DataSupport.where("provinceid = ?", String.valueOf(selectedProvince.getId())).find(City.class);
        if (cityList.size() > 0) {
            for (City city : cityList) {
                if (c.contains(city.getCityName())) {
                    selectedCity = city;
                    queryCounties();
                }
            }
        } else {
            int provinceCode = selectedProvince.getProvinceCode();
            String address = "http://guolin.tech/api/china/" + provinceCode;
            queryFromServer(address, "city");
        }
    }

    /**
     * 查询选中市内所有的县，优先从数据库查询，如果没有查询到再去服务器上查询。
     */
    private void queryCounties() {
        List<County> countyList = DataSupport.where("cityid = ?", String.valueOf(selectedCity.getId())).find(County.class);
        if (countyList.size() > 0) {
            for (County county : countyList) {
                if (d.contains(county.getCountyName())) {
                    WeatherId = county.getWeatherId();
                    Log.d("ee", "我们拿到了id" + WeatherId);
                    requestWeather(WeatherId);
                }
            }

        } else {
            int provinceCode = selectedProvince.getProvinceCode();
            int cityCode = selectedCity.getCityCode();
            String address = "http://guolin.tech/api/china/" + provinceCode + "/" + cityCode;
            queryFromServer(address, "county");
        }
    }

    private void queryFromServer(String address, final String type) {
        HttpUtil.sendOkHttpRequest(address, new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseText = response.body().string();
                boolean result = false;
                if ("province".equals(type)) {
                    result = Utility.handleProvinceResponse(responseText);
                } else if ("city".equals(type)) {
                    result = Utility.handleCityResponse(responseText, selectedProvince.getId());
                } else if ("county".equals(type)) {
                    result = Utility.handleCountyResponse(responseText, selectedCity.getId());
                }
                if (result) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if ("province".equals(type)) {
                                queryProvinces();
                            } else if ("city".equals(type)) {
                                queryCities();
                            } else if ("county".equals(type)) {
                                queryCounties();
                            }
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                // 通过runOnUiThread()方法回到主线程处理逻辑
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getContext(), "加载失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    public void requestWeather(final String weatherId) {
        String weatherUrl = "http://guolin.tech/api/weather?cityid=" + weatherId + "&key=cfb54226dec748afa9f7ddb6f48c795f";
        HttpUtil.sendOkHttpRequest(weatherUrl, new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseText = response.body().string();
                final Weather weather = Utility.handleWeatherResponse(responseText);
                String degree = weather.now.temperature + "℃";
                String weatherInfo = weather.now.more.info;
                Message message = handler.obtainMessage();
                message.what = 1;
                message.obj = degree + ";" + weatherInfo;
                handler.sendMessage(message);
            }

            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }
        });
    }

    public interface DataService {
        @GET("attractions/all")
        retrofit2.Call<ResponseBody> search();
    }

    public class MyLocationListener extends BDAbstractLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            p = location.getProvince();    //获取省份
            c = location.getCity();    //获取城市
            d = location.getDistrict();
            Log.d("eee", "定位" + d);
            initWeather();
            initWeatherIconList();
            getAttractionData();
        }
    }

}

