package com.optisoft.emauser.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.optisoft.emauser.Model.PaymentModel;
import com.optisoft.emauser.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by OptiSoft_A on 2/3/2018.
 */

public class MyDuesAdapter extends BaseAdapter {


    private List<PaymentModel> PaymentModelList;
    private Activity activity;
    boolean isliked = false;

    static class PaymentModelViewHolder {

        TextView bill_number, series, amount;
        ImageView img_service;
        LinearLayout ly_onclick;
    }

    public MyDuesAdapter(Activity activity, ArrayList<PaymentModel> PaymentModelList) {
        this.activity = activity;
        this.PaymentModelList = PaymentModelList;
    }


    @Override
    public int getCount() {
        return this.PaymentModelList.size();
    }
    @Override
    public PaymentModel getItem(int index) {
        return this.PaymentModelList.get(index);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        final MyDuesAdapter.PaymentModelViewHolder viewHolder;
        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.listing_my_dues_items, parent, false);
            viewHolder = new MyDuesAdapter.PaymentModelViewHolder();
            viewHolder.bill_number = (TextView) row.findViewById(R.id.bill_number);
            viewHolder.series = (TextView) row.findViewById(R.id.series);
            viewHolder.amount = (TextView) row.findViewById(R.id.amount);
            viewHolder.img_service = (ImageView) row.findViewById(R.id.img_payment);

            row.setTag(viewHolder);
        } else {
            viewHolder = (MyDuesAdapter.PaymentModelViewHolder)row.getTag();
        }
        PaymentModel paymentModel = getItem(position);

       // viewHolder.bill_number.setText(paymentModel.getBill_number());
        // viewHolder.services.setText(paymentModel.getSpecialist());
        //viewHolder.series.setText(paymentModel.getSeries());
        //viewHolder.amount.setText(paymentModel.getPrice());
        Picasso.with(activity)
                //.load(IMAGE_BASE_URL+orderModel.getImage_url())
                .load(R.drawable.user)
                .placeholder(R.drawable.user)
                .error(R.drawable.user)
                .into( viewHolder.img_service);

        return row;
    }


}