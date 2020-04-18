package com.hym.weather.city_manager;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.hym.weather.R;
import com.hym.weather.db.DBManager;

import java.util.ArrayList;
import java.util.List;

public class DeleteCityActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView errorIv,rightIv;
    ListView deleteLv;
    List<String> mDatas;
    List<String> deleteCitys;
    private DeleteCityAdapter deleteCityAdapter;
    public static boolean isDeleteDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_city);
        errorIv = findViewById(R.id.delete_iv_error);
        rightIv = findViewById(R.id.delete_iv_right);
        deleteLv = findViewById(R.id.delete_lv);
        mDatas = new ArrayList<>();
        deleteCitys = new ArrayList<>();
        //設置點擊監聽事件
        errorIv.setOnClickListener(this);
        rightIv.setOnClickListener(this);
        //適配器的設置
        deleteCityAdapter = new DeleteCityAdapter(this, mDatas, deleteCitys);
        deleteLv.setAdapter(deleteCityAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        final List<String> cityList = DBManager.queryAllCityName();
        mDatas.addAll(cityList);
        deleteCityAdapter.notifyDataSetChanged();
        isDeleteDialog=false;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.delete_iv_error:
                if (isDeleteDialog) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("提示信息").setMessage("您確定要捨棄更改嗎？").setPositiveButton("捨棄更改", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            finish();
                        }
                    });
                    builder.setNeutralButton("取消",null);
                    builder.create().show();
                }else {
                    finish();
                }
                break;
            case R.id.delete_iv_right:
                for (int i = 0; i < deleteCitys.size(); i++) {
                    String city = deleteCitys.get(i);
                    //調用刪除城市的函數
                    DBManager.deleteInfoCity(city);
                }
                finish();
                break;
        }
    }
}
