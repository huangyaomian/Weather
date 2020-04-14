package com.hym.weather.fragment;


import android.app.AlertDialog;
import android.icu.util.BuddhistCalendar;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.hym.weather.R;
import com.hym.weather.bean.WeatherBean;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class CityWeatherFragment extends BaseFragment implements View.OnClickListener {
    private TextView tempTv,cityTv,conditionTv,windTv,tempRangeTv,dateTv,clothTndexTv,carIndexTv,colIndexTv,sportIndexTv,raysIndexTv;
    private ImageView dayIv;
    private LinearLayout futureLayout;
    List<WeatherBean.ResultsBean.IndexBean> indexList;
    private String url1 = "http://api.map.baidu.com/telematics/v3/weather?location=";
    private String url2 = "&output=json&ak=FkPhtMBK0HTIQNh7gG4cNUttSTyr0nzo";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_city_weather, container, false);
        initView(view);

        //可以通過activity傳值獲取到當前fragment加載的是哪個地方的天氣情況
        Bundle bundle = getArguments();
        String city = bundle.getString("city");
        String url = url1 + city + url2;
        //調用父類獲取數據的方法
        loadData(url);

        return view;
    }

    @Override
    public void onSuccess(String result) {
        super.onSuccess(result);
        //解析並展示數據
        parseShowData(result);
    }



    @Override
    public void onError(Throwable ex, boolean isOnCallback) {
        super.onError(ex, isOnCallback);
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
        Picasso.with(getActivity()).load(todayDataBean.getDayPictureUrl()).into(dayIv);
        //獲取未來三天的天氣情況，加載到layout當中
        List<WeatherBean.ResultsBean.WeatherDataBean> futureList = resultsBean.getWeather_data();
        futureList.remove(0);
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

        dayIv = view.findViewById(R.id.frag_iv_icon);
        futureLayout = view.findViewById(R.id.frag_center_layout);

        //設置監聽事件
        clothTndexTv.setOnClickListener(this);
        carIndexTv.setOnClickListener(this);
        colIndexTv.setOnClickListener(this);
        sportIndexTv.setOnClickListener(this);
        raysIndexTv.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        switch (view.getId()){
            case R.id.frag_index_dress:
                builder.setTitle("穿衣指數");
                WeatherBean.ResultsBean.IndexBean indexBean = indexList.get(0);
                indexBean.getZs() + "\n" + indexBean.getDes();
                break;
            case R.id.frag_index_car:

                break;
            case R.id.frag_index_cold:

                break;
            case R.id.frag_index_sport:

                break;
            case R.id.frag_index_light:

                break;
        }
    }
}
