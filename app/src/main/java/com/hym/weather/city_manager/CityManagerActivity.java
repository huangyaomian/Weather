package com.hym.weather.city_manager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.hym.weather.R;
import com.hym.weather.db.DBManager;
import com.hym.weather.db.DatabaseBean;


import java.util.ArrayList;
import java.util.List;

public class CityManagerActivity extends AppCompatActivity implements View.OnClickListener {
    ImageView addIv,backIv,deleteIv;
    ListView cityLv;
    RelativeLayout outLayout;
    List<DatabaseBean> mDatas;//顯示列表數據源
    private CityManagerAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_manager);
        addIv = findViewById(R.id.city_iv_add);
        backIv = findViewById(R.id.city_iv_back);
        deleteIv = findViewById(R.id.city_iv_delete);
        cityLv = findViewById(R.id.city_lv);
        outLayout = findViewById(R.id.city_out_layout);
        mDatas = new ArrayList<>();
        Log.d("hym", "onCreate: "+mDatas.toString());
        //设置点击监听事件
        addIv.setOnClickListener(this);
        backIv.setOnClickListener(this);
        deleteIv.setOnClickListener(this);
        //设置适配器
        adapter = new CityManagerAdapter(this, mDatas);
        cityLv.setAdapter(adapter);
    }

    //獲取數據庫當中真是數據源，添加到原有數據源當中，提示適配器更新
    @Override
    protected void onResume() {
        super.onResume();
        Log.d("hym", "进入onResume方法 ");
        List<DatabaseBean> databaseBeans = DBManager.queryAllInfo();
        mDatas.clear();
        mDatas.addAll(databaseBeans);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()){
            case R.id.city_iv_add:
                int cityCount = DBManager.getCityCount();
                if (cityCount<5){
                    intent = new Intent(this, SearchCityActivity.class);
                    startActivity(intent);
                }else {
                    Toast.makeText(this,"存储城市数量已达上限，请删除后在增加！",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.city_iv_back:
                finish();
                break;
            case R.id.city_iv_delete:
                intent = new Intent(this, DeleteCityActivity.class);
                startActivity(intent);
                break;

        }

    }


}
