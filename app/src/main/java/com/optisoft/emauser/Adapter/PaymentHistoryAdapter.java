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

public class PaymentHistoryAdapter extends BaseAdapter {


    private List<PaymentModel> PaymentModelList;
    private Activity activity;
    boolean isliked = false;

    static class PaymentModelViewHolder {

        TextView bill_id, transaction_date, amount,status;
        ImageView img_service;
        LinearLayout ly_onclick;
    }

    public PaymentHistoryAdapter(Activity activity, ArrayList<PaymentModel> PaymentModelList) {
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
        final PaymentModelViewHolder viewHolder;
        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.listing_payment_history, parent, false);
            viewHolder = new PaymentModelViewHolder();
            viewHolder.bill_id = (TextView) row.findViewById(R.id.bill_number);
            viewHolder.amount= (TextView) row.findViewById(R.id.amount);
            viewHolder.transaction_date = (TextView) row.findViewById(R.id.amountdate);
            viewHolder.status= (TextView) row.findViewById(R.id.amountstatus);
            viewHolder.img_service = (ImageView) row.findViewById(R.id.img_payment);

            row.setTag(viewHolder);
        } else {
            viewHolder = (PaymentModelViewHolder)row.getTag();
        }
        PaymentModel paymentModel = getItem(position);

        viewHolder.bill_id.setText("#"+paymentModel.getTransaction_id());
        viewHolder.amount.setText("$"+paymentModel.getAmount());
        viewHolder.transaction_date.setText(paymentModel.getTransaction_date());
        //viewHolder.status.setText(paymentModel.getStatus());
        if(paymentModel.getStatus().equalsIgnoreCase("1")){
            viewHolder.status.setText("Successfull");
        }else
        {
            viewHolder.status.setText("Failed");
        }


        Picasso.with(activity)
                //.load(IMAGE_BASE_URL+orderModel.getImage_url())
                .load(R.drawable.user)
                .placeholder(R.drawable.user)
                .error(R.drawable.user)
                .into( viewHolder.img_service);

        return row;
    }

}
