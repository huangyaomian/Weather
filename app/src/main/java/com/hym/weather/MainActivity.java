package com.hym.weather;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.hym.weather.city_manager.CityManagerActivity;
import com.hym.weather.db.DBManager;
import com.hym.weather.fragment.CityFragmentPagerAdapter;
import com.hym.weather.fragment.CityWeatherFragment;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ViewPager.OnClickListener{

    private ImageView addCityIv,moreTv;
    private LinearLayout pointLayout;
    private ViewPager mainVp;
    CityFragmentPagerAdapter adapter;
    //viewpager的数据源
    List<Fragment> fragmentList;
    //表示需要显示的城市的集合
    List<String> cityList;
    //表示viewpager的页数指数器显示的集合
    List<ImageView> imgList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addCityIv = findViewById(R.id.main_iv_add);
        moreTv = findViewById(R.id.main_iv_more);
        pointLayout = findViewById(R.id.main_layout_point);
        mainVp = findViewById(R.id.main_vp);
        //添加點擊事件
        addCityIv.setOnClickListener(this);
        moreTv.setOnClickListener(this);

        fragmentList = new ArrayList<>();
        cityList = DBManager.queryAllCityName();
        imgList = new ArrayList<>();

        if (cityList.size() == 0) {
            cityList.add("深圳");
        }
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
}
