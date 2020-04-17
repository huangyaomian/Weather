package com.hym.weather;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.hym.weather.db.DBManager;

import static java.util.ResourceBundle.clearCache;

public class MoreActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView bgTv,cacheTv,versionTv,shareTv;
    ImageView backIv;
    RadioGroup exbgRg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more);
        bgTv = findViewById(R.id.more_tv_exchagedg);
        cacheTv = findViewById(R.id.more_tv_cache);
        versionTv = findViewById(R.id.more_tv_version);
        shareTv = findViewById(R.id.more_tv_share);
        backIv = findViewById(R.id.more_iv_back);
        exbgRg = findViewById(R.id.more_rg);

        bgTv.setOnClickListener(this);
        cacheTv.setOnClickListener(this);
        shareTv.setOnClickListener(this);
        backIv.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.more_iv_back:
                finish();
                break;
            case R.id.more_tv_cache:
                myClearCache();
                break;

            case R.id.more_tv_share:

                break;
            case R.id.more_tv_exchagedg:

                break;

        }
    }

    //清除數據庫中所有的城市信息
    private void myClearCache() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("提示信息").setMessage("確定要刪除緩存嗎？").setPositiveButton("確定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                DBManager.deleteAllInfo();
            }
        }).setNeutralButton("取消",null);
        builder.create().show();
    }
}
