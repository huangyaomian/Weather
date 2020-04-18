package com.hym.weather.city_manager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hym.weather.R;

import java.util.List;


public class DeleteCityAdapter extends BaseAdapter {

    Context context;
    List<String> mDatas;
    List<String> deleteCity;


    public DeleteCityAdapter(Context context, List<String> mDatas, List<String> deleteCity) {
        this.context = context;
        this.mDatas = mDatas;
        this.deleteCity = deleteCity;
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public Object getItem(int i) {
        return mDatas.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder = null;
        if (view==null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_deletecity,null);
            holder = new ViewHolder(view);
            view.setTag(holder);
        }else {
            holder = (ViewHolder) view.getTag();
        }
        final String city = mDatas.get(i);
        holder.tv.setText(city);
        holder.iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDatas.remove(city);
                deleteCity.add(city);
            //刪除了提示適配器更新
                notifyDataSetChanged();
                DeleteCityActivity.isDeleteDialog =true;
            }
        });
        return view;
    }

    class ViewHolder{
        TextView tv;
        ImageView iv;

        public ViewHolder(View itemView) {
            tv = itemView.findViewById(R.id.item_delete_tv);
            iv = itemView.findViewById(R.id.item_delete_iv);
        }
    }
}
