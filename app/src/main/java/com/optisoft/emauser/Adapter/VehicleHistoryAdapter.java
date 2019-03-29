package com.optisoft.emauser.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.optisoft.emauser.R;

import java.util.ArrayList;
import java.util.HashMap;

public class VehicleHistoryAdapter extends BaseAdapter {
    private Activity activity;
    private ArrayList<HashMap<String, String>> historyList;

    public VehicleHistoryAdapter(Activity activity,
                                 ArrayList<HashMap<String, String>> list) {
        this.activity = activity;
        this.historyList = list;
    }

    @Override
    public int getCount() {
        return historyList.size();
    }
    @Override
    public Object getItem(int index) {
        return historyList.get(index);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private class ViewHolder {
        TextView status_view;
        TextView time_view;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null)
        {
            LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.listing_my_vehicle_history, parent, false);
            holder = new ViewHolder();
            holder.status_view = (TextView) convertView.findViewById(R.id.item_car_status);
            holder.time_view = (TextView) convertView.findViewById(R.id.item_car_time);
            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) convertView.getTag();
        }

        HashMap<String, String> map = historyList.get(position);
        String cStatus = map.get("car_status");
        if (cStatus.equals("1"))
            holder.status_view.setText("In");
        else if (cStatus.equals("2"))
            holder.status_view.setText("Out");
        holder.time_view.setText(map.get("car_time"));
        return convertView;
    }
}
