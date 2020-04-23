package com.hym.weather;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.google.gson.Gson;
import com.hym.weather.bean.CityNameBean;
import com.hym.weather.city_manager.CityManagerActivity;
import com.hym.weather.db.DBManager;
import com.hym.weather.fragment.CityFragmentPagerAdapter;
import com.hym.weather.fragment.CityWeatherFragment;
import com.hym.weather.utils.LocationUtils;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ViewPager.OnClickListener{

    private ImageView addCityIv,moreTv;
    private LinearLayout pointLayout;
    RelativeLayout outLayout;
    private ViewPager mainVp;
    private CityFragmentPagerAdapter adapter;
    //viewpager的数据源
    List<Fragment> fragmentList;
    //表示需要显示的城市的集合
    List<String> cityList = DBManager.queryAllCityName();
    //表示viewpager的页数指数器显示的集合
    List<ImageView> imgList;
    private SharedPreferences pref;
    private int bgNum;

    private String mLatitudeStr,mLongitudeStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        LoadingViewManager.with(this).setHintText("加载天气中").setAnimationStyle("BallClipRotatePulseIndicator").build();

//        LoadingViewManager.with(this).setHintText("加载中").setAnimationStyle("BallClipRotatePulseIndicator").build();
        if (cityList.size() == 0) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
            LocationUtils location = new LocationUtils(this);
            if (location.getlocation() == null) {
                cityList.add("北京");
                code();//代码提取
            }else {
                BDLocationListener listener = new BDLocationListener() {
                    @Override
                    public void onReceiveLocation(BDLocation bdLocation) {
                        mLatitudeStr = Double.toString(bdLocation.getLatitude());
                        mLongitudeStr = Double.toString(bdLocation.getLongitude());
                    }
                };
                Log.e("hymm","getLongitude" +mLatitudeStr);
                String path = "https://api.map.baidu.com/geocoder?output=json&location=" + mLatitudeStr +","+ mLongitudeStr +"&ak=esNPFDwwsXWtsQfw4NMNmur1";
                RequestParams params = new RequestParams(path);
                x.http().get(params, new Callback.CommonCallback<String>() {
                    @Override
                    public void onSuccess(String result) {
//                    LoadingViewManager.dismiss(true);
                        CityNameBean cityNameBean = new Gson().fromJson(result, CityNameBean.class);
                        String cityName = cityNameBean.getResult().getAddressComponent().getCity();
                        cityList.add(cityName);
                        code();//代码提取
                    }

                    @Override
                    public void onError(Throwable ex, boolean isOnCallback) {

                    }

                    @Override
                    public void onCancelled(CancelledException cex) {

                    }

                    @Override
                    public void onFinished() {

                    }
                });
            }
        }else {
            code();
        }

    }

    private void code() {

        //初始化空间
        initView();
        //设置壁纸
        exchangeBg();
        //添加點擊事件
        addCityIv.setOnClickListener(MainActivity.this);
        moreTv.setOnClickListener(MainActivity.this);

        fragmentList = new ArrayList<>();
        imgList = new ArrayList<>();


        //因为可能搜索界面点击跳转到此界面会传值
        Intent intent = getIntent();
        String city = intent.getStringExtra("city");
        if (!cityList.contains(city) && !TextUtils.isEmpty(city)) {
            cityList.add(city);
        }
        //初始换viewpager页面的方法
        initPager();
        adapter = new CityFragmentPagerAdapter(getSupportFragmentManager(), FragmentStatePagerAdapter.POSITION_UNCHANGED, fragmentList);
        mainVp.setAdapter(adapter);
        //创建小圆点指示器
        initPoint();
        //设置默认显示最后一个添加城市的信息
        mainVp.setCurrentItem(fragmentList.size()-1);

        //设置viewpager页面监听
        setPagerListener();
    }

    //

    //初始化控件
    private void initView() {
        addCityIv = findViewById(R.id.main_iv_add);
        moreTv = findViewById(R.id.main_iv_more);
        pointLayout = findViewById(R.id.main_layout_point);
        outLayout = findViewById(R.id.main_out_layout);
        mainVp = findViewById(R.id.main_vp);
    }

    private void setPagerListener() {
        //设置监听事件
        mainVp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                //
                for (int i = 0; i < imgList.size(); i++) {
                    imgList.get(i).setImageResource(R.mipmap.a1);
                }
                imgList.get(position).setImageResource(R.mipmap.a2);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void initPoint() {
        //创建小圆点viewpager页面指示器的函数
        for (int i = 0; i < fragmentList.size(); i++) {
            ImageView pIv = new ImageView(this);
            pIv.setImageResource(R.mipmap.a1);
            pIv.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) pIv.getLayoutParams();
            lp.setMargins(0,0,20,0);
            imgList.add(pIv);
            pointLayout.addView(pIv);
        }
        imgList.get(imgList.size()-1).setImageResource(R.mipmap.a2);

    }

    private void initPager() {
        /*创建fragment对象，添加到viewpager的数据源当中*/
        for (int i = 0; i < cityList.size(); i++) {
            CityWeatherFragment cityWeatherFragment = new CityWeatherFragment();
            Bundle bundle = new Bundle();
            bundle.putString("city", cityList.get(i));
            if (i == cityList.size() - 1) {
                bundle.putInt("isShow",1);
            }else {
                bundle.putInt("isShow",2);
            }

            cityWeatherFragment.setArguments(bundle);
            fragmentList.add(cityWeatherFragment);
        }
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent();
        switch (view.getId()){
            case R.id.main_iv_add:
                intent.setClass(this, CityManagerActivity.class);
                break;
            case R.id.main_iv_more:
                intent.setClass(this, MoreActivity.class);
                break;
        }
        startActivity(intent);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        //獲取數據庫當中還剩下的城市集合
        List<String> list = DBManager.queryAllCityName();
        if (list.size() == 0){
            list.add("北京");
        }
        cityList.clear();//重新加載之前，清空原本數據源
        cityList.addAll(list);
        //剩餘城市也要創建對應的fragement頁面
        fragmentList.clear();
        initPager();
        adapter.notifyDataSetChanged();
        //頁面數量發生改變，治時期的數量也會發生變化，重新設置添加指示器
        imgList.clear();
        pointLayout.removeAllViews();//將佈局中所有元素全部移除
        initPoint();
        mainVp.setCurrentItem(fragmentList.size()-1);

    }

    //换壁纸的函数
    public void exchangeBg(){
        pref = getSharedPreferences("bg_pref", MODE_PRIVATE);
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
