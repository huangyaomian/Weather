package com.hym.weather.fragment;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hym.weather.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class CityWeatherFragment extends BaseFragment implements View.OnClickListener {
    private TextView tempTv,cityTv,conditionTv,windTv,tempRangeTv,dateTv,clothTndexTv,carIndexTv,colIndexTv,sportIndexTv,raysIndexTv;
    private ImageView dayIv;
    private LinearLayout futureLayout;
    private String url1 = "http://api.map.baidu.com/telematics/v3/weather?location=";
    private String url2 = "&output=json&ak=FkPhtMBK0HTIQNh7gG4cNUttSTyr0nzo";

    public CityWeatherFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_city_weather, container, false);
        initView(view);
        return view;
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
        switch (view.getId()){
            case R.id.frag_index_dress:

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
