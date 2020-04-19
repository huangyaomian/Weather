package com.hym.weather.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;

import android.location.LocationManager;
import android.util.Log;

import com.google.gson.Gson;
import com.hym.weather.bean.CityNameBean;
import com.hym.weather.bean.WeatherBean;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.List;

public class LocationUtils {
    public  String cityName = null;   //城市名
    private  String path1 = "http://api.map.baidu.com/geocoder?output=json&location=";
    private  String path2 = "&ak=esNPFDwwsXWtsQfw4NMNmur1";
    private Context context;

    public LocationUtils(Context context) {
        this.context = context;
    }

    //获取location对象
    @SuppressLint("MissingPermission")
    public Location getlocation() {
        //用于获取Location对象，以及其他
        LocationManager locationManager;
        String serviceName = Context.LOCATION_SERVICE;
        //实例化一个LocationManager对象
        locationManager = (LocationManager) context.getSystemService(serviceName);
        //provider的类型
        String provider = LocationManager.GPS_PROVIDER;

        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_LOW);    //低精度   高精度：ACCURACY_FINE
        criteria.setAltitudeRequired(false);       //不要求海拔
        criteria.setBearingRequired(false);       //不要求方位
        criteria.setCostAllowed(false);      //不允许产生资费
        criteria.setPowerRequirement(Criteria.POWER_LOW);   //低功耗

        //通过最后一次的地理位置来获取Location对象
        Location location = locationManager.getLastKnownLocation(provider);
        return location;
    }

    private Location getLastKnownLocation(LocationManager locationManager) {
        List<String> providers = locationManager.getProviders(true);
        Location bestLocation = null;
        for (String provider : providers) {
            @SuppressLint("MissingPermission") Location l = locationManager.getLastKnownLocation(provider);
            if (l == null) {
                continue;
            }
            if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                // Found best last known location: %s", l);
                bestLocation = l;
            }
        }
        return bestLocation;
    }


    //根据location对象回去当前城市名称
    public String getCityName() {
        Location location =  getlocation();
//        String path = path1 +location.getAltitude()+","+location.getLongitude() +  path2;
        String path = "https://api.map.baidu.com/geocoder?output=json&location=39.913542,116.379763&ak=esNPFDwwsXWtsQfw4NMNmur1";
        RequestParams params = new RequestParams(path);
        String result = null;
        try {
            result = x.http().getSync(params,String.class);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
       /* Callback.Cancelable result = null;

        result = x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                CityNameBean cityNameBean = new Gson().fromJson(result, CityNameBean.class);
                cityName = cityNameBean.getResult().getAddressComponent().getCity();
                Log.e("hymm","LocationUtils:cityName"+cityName);
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
        });*/

        Log.e("hymm","LocationUtils111111:"+result);

        return cityName;
    }


}
