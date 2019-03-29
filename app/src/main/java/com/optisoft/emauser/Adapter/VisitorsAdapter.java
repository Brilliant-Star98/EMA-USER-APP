package com.optisoft.emauser.Adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.optisoft.emauser.Model.VisitorModel;
import com.optisoft.emauser.R;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import static com.optisoft.emauser.HelperClasses.ApiConstant.IMAGE_URL;

public class VisitorsAdapter extends RecyclerView.Adapter<VisitorsAdapter.ViewHolder>  {

    private final List<VisitorModel> mValues;
    private Context context;
    private String tag;
    private Gson gson=new Gson();

    public VisitorsAdapter(Context context, List<VisitorModel> items) {
        this.mValues = items;
        this.context = context;
        this.tag = tag;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_visitor, parent, false);
        return new ViewHolder(view);


    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        holder.mItem = mValues.get(position);
        holder.mTitle.setText(holder.mItem.getVisitor_name());
        holder.mInDate.setText(convertDate(holder.mItem.getDate_in()));
        holder.mPerson.setText(holder.mItem.getNum_person());
        //holder.mUserName.setText(holder.mItem.getVisit_to());

        Picasso.with(context)
                .load(IMAGE_URL+holder.mItem.getImage())
                .placeholder(R.drawable.user)
                .error(R.drawable.user)
                .into(holder.mImage);
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mTitle, mPerson, mInDate, mStatus;
        public ImageView mImage;
        public VisitorModel mItem;
        public CardView main_card;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mTitle = (TextView) view.findViewById(R.id.title);
            //mUserName = (TextView) view.findViewById(R.id.user_name);
            mPerson = (TextView) view.findViewById(R.id.user_count);
            mInDate = (TextView) view.findViewById(R.id.in_date);
            mStatus = (TextView) view.findViewById(R.id.status);
            mImage = (ImageView) view.findViewById(R.id.image);
        }

        @Override
        public String toString()
        {
            return super.toString() + " '" + mTitle.getText()+ "'";
        }
    }

    public String convertDate(String ds1){
        String ds2 = "";
        SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat sdf2 = new SimpleDateFormat("MMM d, yyyy");
        try {
            ds2 = sdf2.format(sdf1.parse(ds1));
        } catch (ParseException e) {
            e.printStackTrace();
            return ds1;
        }

        return ds2;
    }
}


