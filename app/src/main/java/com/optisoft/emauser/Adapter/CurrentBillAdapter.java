package com.optisoft.emauser.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.optisoft.emauser.Activity.BillSummaryActivity;
import com.optisoft.emauser.Activity.MpesaActivity;
import com.optisoft.emauser.Model.BillModel;
import com.optisoft.emauser.R;
import com.google.gson.Gson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import static com.optisoft.emauser.HelperClasses.ApiConstant.INTENT_TAG;

public class CurrentBillAdapter extends RecyclerView.Adapter<CurrentBillAdapter.ViewHolder>  {

    private final List<BillModel> mValues;
    private Context context;
    private String tag;
    private Gson gson=new Gson();

    public CurrentBillAdapter(Context context, List<BillModel> items, String tag) {
        this.mValues = items;
        this.context = context;
        this.tag = tag;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.current_bill_item, parent, false);
        return new ViewHolder(view);


    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        holder.mItem = mValues.get(position);
        holder.mTitle.setText(holder.mItem.getBill_title() + " Bill");
        String currentMonth = holder.mItem.getBill_month();
        String currentMonth1=holder.mItem.getDue_date();
        holder.mPrice.setText(holder.mItem.getBill_amount());
        holder.mGenerated.setText(holder.mItem.getBill_generated_date());
        holder.mLast.setText(convertDate1(holder.mItem.getDue_date()));

        holder.mMonth.setText(convertDate(currentMonth));
        holder.mMonth.setText(convertDate1(currentMonth1));





        if (holder.mItem.getStatus().equals("0")){
            holder.mPay.setVisibility(View.VISIBLE);
            holder.mPayed.setVisibility(View.GONE);
        }else {
            holder.mPay.setVisibility(View.GONE);
            holder.mPayed.setVisibility(View.VISIBLE);
        }
        holder.mPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context, MpesaActivity.class);
                intent.putExtra(INTENT_TAG, gson.toJson(holder.mItem));
                context.startActivity(intent);
            }
        });

        holder.main_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context, BillSummaryActivity.class);
                intent.putExtra(INTENT_TAG, gson.toJson(holder.mItem));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mTitle, mPay, mPayed, mMonth,mPrice,mGenerated,mLast;
        public ImageView im;
        public BillModel mItem;
        public CardView main_card;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mGenerated=(TextView)view.findViewById(R.id.generated_date);
            mLast=(TextView)view.findViewById(R.id.last_date);
            im=(ImageView) view.findViewById(R.id.image);
            mMonth=(TextView) view.findViewById(R.id.month);
            mPrice=(TextView)view.findViewById(R.id.tv_price);
            mTitle = (TextView) view.findViewById(R.id.title);
            mPay = (TextView) view.findViewById(R.id.pay_now);
            mPayed = (TextView) view.findViewById(R.id.payed);
            main_card = (CardView) view.findViewById(R.id.main_card);
        }

        @Override
        public String toString()
        {
            return super.toString() + " '" + mTitle.getText()+ "'";
        }
    }

    public String convertDate(String ds1){
        String ds2 = "";
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdf2 = new SimpleDateFormat("MMMM yyyy");
        try {
            ds2 = sdf2.format(sdf1.parse(ds1));
        } catch (ParseException e) {
            e.printStackTrace();
            return ds1;
        }

        return ds2;
    }

    public String convertDate1(String ds1){
        String ds2 = "";
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdf2 = new SimpleDateFormat("MMM dd, yyyy");
        try {
            ds2 = sdf2.format(sdf1.parse(ds1));
        } catch (ParseException e) {
            e.printStackTrace();
            return ds1;
        }

        return ds2;
    }
}


