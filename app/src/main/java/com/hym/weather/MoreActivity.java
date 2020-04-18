package com.hym.weather;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.hym.weather.db.DBManager;

import static java.util.ResourceBundle.clearCache;

public class MoreActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView bgTv,cacheTv,versionTv,shareTv;
    ImageView backIv;
    RadioGroup exbgRg;
    private SharedPreferences pref;

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
        pref = getSharedPreferences("bg_pref", MODE_PRIVATE);

        String versionName = getVersionName();
        versionTv.setText("当前版本：   v" + versionName);
        setRGListerer();


    }

    private void setRGListerer() {
        //设置改变背景图片的单选按钮的监听
        exbgRg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                //获取目前的默认壁纸
                int bg = pref.getInt("bg", 0);
                SharedPreferences.Editor editor = pref.edit();
                Intent intent = new Intent(MoreActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                switch (checkedId){
                    case R.id.more_rg_green:
                        if (bg == 0) {
                            Toast.makeText(MoreActivity.this,"您选择的为当前背景，无需改变！",Toast.LENGTH_SHORT).show();
                            return;
                        }
                        editor.putInt("bg",0);
                        editor.commit();
                        break;
                    case R.id.more_rg_pink:
                        if (bg == 1) {
                            Toast.makeText(MoreActivity.this,"您选择的为当前背景，无需改变！",Toast.LENGTH_SHORT).show();
                            return;
                        }
                        editor.putInt("bg",1);
                        editor.commit();
                        break;
                    case R.id.more_rg_bule:
                        if (bg == 2) {
                            Toast.makeText(MoreActivity.this,"您选择的为当前背景，无需改变！",Toast.LENGTH_SHORT).show();
                            return;
                        }
                        editor.putInt("bg",2);
                        editor.commit();
                        break;
                }
                startActivity(intent);
            }
        });
    }

    private String getVersionName() {
        //获取应用的版本名称
        PackageManager packageManager = getPackageManager();
        String versionName = null;
        PackageInfo info = null;
        try {
            info = packageManager.getPackageInfo(getPackageName(), 0);
            versionName = info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionName;
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
                shareSoftwareMsg("说天气app是一款超萌超可爱的天气预报软件，画面简约，播报天气非常精准，快来下载吧！");
                break;
            case R.id.more_tv_exchagedg:
                if (exbgRg.getVisibility() == View.VISIBLE) {
                    exbgRg.setVisibility(View.GONE);
                }else {
                    exbgRg.setVisibility(View.VISIBLE);
                }
                break;

        }
    }

    private void shareSoftwareMsg(String s) {
        //分享软件的函数
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT,s);
        startActivity(Intent.createChooser(intent,"分享至"));
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
