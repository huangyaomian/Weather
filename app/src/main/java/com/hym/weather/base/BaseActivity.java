package com.hym.weather.base;

import androidx.appcompat.app.AppCompatActivity;

import com.hym.weather.utils.loading.LoadingViewManager;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

public class BaseActivity extends AppCompatActivity implements Callback.CommonCallback<String> {

    public  void loadData(String path){
        LoadingViewManager.with(this).setHintText("BaseActivity加载天气中").setAnimationStyle("BallClipRotatePulseIndicator").build();
        RequestParams params = new RequestParams(path);
        x.http().get(params, this);
    }

    @Override
    public void onSuccess(String result) {
        LoadingViewManager.dismiss(true);
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
}
