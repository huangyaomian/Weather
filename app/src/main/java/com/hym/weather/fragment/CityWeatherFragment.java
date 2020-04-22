package com.hym.weather.fragment;


import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.icu.util.BuddhistCalendar;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.text.style.BulletSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hym.weather.R;
import com.hym.weather.bean.WeatherBean;
import com.hym.weather.db.DBManager;
import com.hym.weather.utils.CircleTransform;
import com.hym.weather.utils.RoundTransform;
import com.hym.weather.utils.loading.LoadingViewManager;
import com.squareup.picasso.Picasso;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class CityWeatherFragment extends BaseFragment implements View.OnClickListener {
    private TextView tempTv,cityTv,conditionTv,windTv,tempRangeTv,dateTv,clothTndexTv,carIndexTv,colIndexTv,sportIndexTv,raysIndexTv;
    private ImageView dayIv;
    private LinearLayout futureLayout;
    private ScrollView outLayout;
    private SharedPreferences pref;
    private int bgNum;
    List<WeatherBean.ResultsBean.IndexBean> indexList;
    private String url1 = "http://api.map.baidu.com/telematics/v3/weather?location=";
    private String url2 = "&output=json&ak=FkPhtMBK0HTIQNh7gG4cNUttSTyr0nzo";

    String city;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_city_weather, container, false);
        initView(view);
        exchangeBg();//设置壁纸
        //可以通過activity傳值獲取到當前fragment加載的是哪個地方的天氣情況
        Bundle bundle = getArguments();
        city = bundle.getString("city");
        int isShow = bundle.getInt("isShow");
        String url = url1 + city + url2;
        if ( isShow == 1) {
            LoadingViewManager.with(this).setHintText("加载天气中").setAnimationStyle("BallClipRotatePulseIndicator").build();
        }
        //調用父類獲取數據的方法
        loadData(url);
        return view;
    }

    @Override
    public void onSuccess(String result) {
        Log.d("hymmm",result);
        //解析並展示數據
        parseShowData(result);
        super.onSuccess(result);
        //更新数据
        int i = DBManager.updateInfoByCity(city, result);
        if (i<=0) {
            //更新数据库失败，说明没有这条程序是信息，增加这个城市记录
            DBManager.addCityInfo(city,result);
        }
    }



    @Override
    public void onError(Throwable ex, boolean isOnCallback) {
        super.onError(ex, isOnCallback);
        if (!TextUtils.isEmpty(city)) {
            String s = DBManager.queryInfoByCity(city);
            if (!TextUtils.isEmpty(s)) {
                parseShowData(s);
            }
        }
    }

    private void parseShowData(String result) {
        //使用gson解析數據
        WeatherBean weatherBean = new Gson().fromJson(result, WeatherBean.class);
        WeatherBean.ResultsBean resultsBean = weatherBean.getResults().get(0);
        //獲取指標信息集合列表
        indexList = resultsBean.getIndex();
        //設置textview，日期和城市
        dateTv.setText(weatherBean.getDate());
        cityTv.setText(resultsBean.getCurrentCity());
        //獲取今日天氣情況
        WeatherBean.ResultsBean.WeatherDataBean todayDataBean = resultsBean.getWeather_data().get(0);
        windTv.setText(todayDataBean.getWind());
        tempRangeTv.setText(todayDataBean.getTemperature());
        conditionTv.setText(todayDataBean.getWeather());
        //獲取實時天氣溫度情況，需要處理字符串
        String[] split = todayDataBean.getDate().split("：");
        String todayTemp = split[1].replace(")", "");
        tempTv.setText(todayTemp);
        //設置顯示天氣情況圖片
        Picasso.with(getActivity())
                .load(todayDataBean.getDayPictureUrl())
                .resize(120,120)
                .centerCrop()
                .transform(new RoundTransform(20))
                .into(dayIv);
        //獲取未來三天的天氣情況，加載到layout當中
        List<WeatherBean.ResultsBean.WeatherDataBean> futureList = resultsBean.getWeather_data();
        futureList.remove(0);
        futureLayout.removeAllViews();
        for (int i = 0; i < futureList.size(); i++) {
            View itemView = LayoutInflater.from(getActivity()).inflate(R.layout.item_main_center, null);
            itemView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            futureLayout.addView(itemView);
            TextView idateTv = itemView.findViewById(R.id.item_center_tv_date);
            TextView iconTv = itemView.findViewById(R.id.item_center_tv_con);
            TextView itemprangeTv = itemView.findViewById(R.id.item_center_tv_temp);
            ImageView iIv = itemView.findViewById(R.id.item_center_iv);
            WeatherBean.ResultsBean.WeatherDataBean dataBean =  futureList.get(i);
            idateTv.setText(dataBean.getDate());
            iconTv.setText(dataBean.getWeather());
            itemprangeTv.setText(dataBean.getTemperature());
            Picasso.with(getActivity()).load(dataBean.getDayPictureUrl()).into(iIv);
            
        }

    }

    private void initView(View view) {
        tempTv = view.findViewById(R.id.frag_tv_currenttemp);
        cityTv = view.findViewById(R.id.frag_tv_city);
        conditionTv = view.findViewById(R.id.frag_tv_condition);
        windTv = view.findViewById(R.id.frag_tv_wind);
        tempRangeTv = view.findViewById(R.id.frag_tv_temprange);
        dateTv = view.findViewById(R.id.frag_tv_date);
        clothTndexTv = view.findViewById(R.id.frag_index_dress);
        carIndexTv = view.findViewById(R.id.frag_index_car);
        colIndexTv = view.findViewById(R.id.frag_index_cold);
        sportIndexTv = view.findViewById(R.id.frag_index_sport);
        raysIndexTv = view.findViewById(R.id.frag_index_light);
        outLayout = view.findViewById(R.id.frag_out_layout);
        dayIv = view.findViewById(R.id.frag_iv_icon);
        futureLayout = view.findViewById(R.id.frag_center_layout);

        //設置監聽事件
        clothTndexTv.setOnClickListener(this);
        carIndexTv.setOnClickListener(this);
        colIndexTv.setOnClickListener(this);
        sportIndexTv.setOnClickListener(this);
        raysIndexTv.setOnClickListener(this);
        tempTv.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        WeatherBean.ResultsBean.IndexBean indexBean;
        String msg = null;
        switch (view.getId()){
            case R.id.frag_index_dress:
                builder.setTitle("穿衣指數");
                indexBean = indexList.get(0);
                msg = indexBean.getZs() + "\n" + indexBean.getDes();
                builder.setMessage(msg);
                builder.setPositiveButton("确定", null);
                builder.create().show();
                break;
            case R.id.frag_index_car:
                builder.setTitle("洗车指数");
                indexBean = indexList.get(1);
                msg = indexBean.getZs() + "\n" + indexBean.getDes();
                builder.setMessage(msg);
                builder.setPositiveButton("确定", null);
                builder.create().show();
                break;
            case R.id.frag_index_cold:
                builder.setTitle("感冒指数");
                indexBean = indexList.get(2);
                msg = indexBean.getZs() + "\n" + indexBean.getDes();
                builder.setMessage(msg);
                builder.setPositiveButton("确定", null);
                builder.create().show();
                break;
            case R.id.frag_index_sport:
                builder.setTitle("运动指数");
                indexBean = indexList.get(3);
                msg = indexBean.getZs() + "\n" + indexBean.getDes();
                builder.setMessage(msg);
                builder.setPositiveButton("确定", null);
                builder.create().show();
                break;
            case R.id.frag_index_light:
                builder.setTitle("紫外线指数");
                indexBean = indexList.get(4);
                msg = indexBean.getZs() + "\n" + indexBean.getDes();
                builder.setMessage(msg);
                builder.setPositiveButton("确定", null);
                builder.create().show();
                break;
            case R.id.frag_tv_currenttemp:
                Toast.makeText(getContext(),"更新成功！",Toast.LENGTH_SHORT).show();
                String url = url1 + cityTv.getText() + url2;
                LoadingViewManager.with(this).setHintText("加载天气中").setAnimationStyle("BallClipRotatePulseIndicator").build();
                //調用父類獲取數據的方法
                loadData(url);
                break;
        }

    }

    //换壁纸的函数
    public void exchangeBg(){
        pref = getActivity().getSharedPreferences("bg_pref", MODE_PRIVATE);
        bgNum = pref.getInt("bg", 2);
        switch (bgNum){
            case 0:
                outLayout.setBackgroundResource(R.mipmap.bg);
                break;
            case 1:
                outLayout.setBackgroundResource(R.mipmap.bg5);
                break;
            case 2:
                outLayout.setBackgroundResource(R.mipmap.bg3);
                break;
        }
    }
}
