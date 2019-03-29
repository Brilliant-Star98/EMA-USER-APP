package com.optisoft.emauser.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.optisoft.emauser.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class VehicleListAdapter extends BaseAdapter {

    private Activity activity;
    private ArrayList<String> vehicleList;

    public VehicleListAdapter(Activity activity,
                              ArrayList<String> vehicleList) {
        this.activity = activity;
        this.vehicleList = vehicleList;
    }

    @Override
    public int getCount() {
        return this.vehicleList.size();
    }
    @Override
    public String getItem(int index) {
        return this.vehicleList.get(index);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        if (row == null){
            LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.listing_my_vehicle_items, parent, false);
            TextView textView = (TextView) row.findViewById(R.id.item_car_plate);
            textView.setText(vehicleList.get(position));
        }
        //String vehicle = getItem(position);
//        Picasso.with(activity)
//                //.load(IMAGE_BASE_URL+orderModel.getImage_url())
//                .load(R.drawable.user)
//                .placeholder(R.drawable.user)
//                .error(R.drawable.user)
//                .into( viewHolder.img_service);
        return row;
    }
}