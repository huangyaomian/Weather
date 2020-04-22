package com.hym.weather.fragment;

import android.util.Log;

import androidx.fragment.app.Fragment;

import com.hym.weather.utils.loading.LoadingViewManager;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;


/*
* xutils加載網路數據的步驟
* 1.聲明整體模塊
* 2.執行網絡請求操作
* */
public class BaseFragment extends Fragment implements Callback.CommonCallback<String> {

    public  void loadData(String path){
        Log.d("hymmm",path);
        RequestParams params = new RequestParams(path);
        x.http().get(params, this);
    }

    //獲取數據成功時，會回調的接口
    @Override
    public void onSuccess(String result) {
        Log.d("hymmm","LoadingViewManager.isShowing()="+LoadingViewManager.isShowing());
        LoadingViewManager.dismiss(true);
    }

    //獲取數據失敗時，會回調接口
    @Override
    public void onError(Throwable ex, boolean isOnCallback) {

    }

    //取消請求時，會回調接口
    @Override
    public void onCancelled(CancelledException cex) {

    }

    //完成請求時，會回調接口
    @Override
    public void onFinished() {
    }


}
