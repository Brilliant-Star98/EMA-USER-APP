package com.optisoft.emauser.Adapter;

import android.os.CountDownTimer;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.optisoft.emauser.Activity.NotificationActivity;
import com.optisoft.emauser.Model.NotificationModel;
import com.optisoft.emauser.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by OptiSoft on 2/11/2017.
 */

public class NotificationListAdapter extends RecyclerView.Adapter {

    public final int TYPE_MOVIE = 0;
    public final int TYPE_LOAD = 1;

    OnLoadMoreListener loadMoreListener;
    boolean isLoading = false, isMoreDataAvailable = true;

    private final NotificationActivity context;
    private ArrayList<NotificationModel> list;

    private Animation animationUp, animationDown;
    private final int COUNTDOWN_RUNNING_TIME = 500;


    public static class ListViewHolder extends RecyclerView.ViewHolder{

        public TextView date, title, message, tv_title, phone;
        public ImageView more_icon;
        public ScrollView scrollView;
        public View mView;

        public ListViewHolder(View v) {
            super(v);
            mView = v;
            date = (TextView) v.findViewById(R.id.date);
            title = (TextView) v.findViewById(R.id.title);
            tv_title = (TextView) v.findViewById(R.id.tv_title);
            phone = (TextView) v.findViewById(R.id.mobile);
            message = (TextView) v.findViewById(R.id.message);
            more_icon = (ImageView) v.findViewById(R.id.more_icon);
            scrollView = (ScrollView) v.findViewById(R.id.scrollView);
        }

    }

    public static class ProgressViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;

        public ProgressViewHolder(View v) {super(v);
            progressBar = (ProgressBar) v.findViewById(R.id.progressBar1);
        }
    }


    public NotificationListAdapter(NotificationActivity context, ArrayList<NotificationModel> list, Animation animationUp, Animation animationDown){
        this.list = list;
        this.context = context;
        this.animationDown = animationDown;
        this.animationUp = animationUp;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder vh;
        if (viewType == TYPE_MOVIE) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notification_list, parent, false);

            vh = new ListViewHolder(v);
        } else {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.progressbar_item, parent, false);

            vh = new ProgressViewHolder(v);
        }
        return vh;
    }

    @Override
    public int getItemViewType(int position) {
        //if(list.get(position).type.equals("list")){
        if(true){
            return TYPE_MOVIE;
        }else{
            return TYPE_LOAD;
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {

        if(position>=getItemCount()-1 && isMoreDataAvailable && !isLoading && loadMoreListener!=null){
            isLoading = true;
            loadMoreListener.onLoadMore();
        }

        if(getItemViewType(position)==TYPE_MOVIE){
            final NotificationModel bean = list.get(position);


            if (context.isAgent){
                ((ListViewHolder) holder).phone.setVisibility(View.VISIBLE);
                ((ListViewHolder) holder).phone.setText("+"+bean.getCountry_code()+"-"+bean.getMobile());
                ((ListViewHolder) holder).date.setText(convertDate(bean.getCreated_date()));
                ((ListViewHolder) holder).title.setText(bean.getFname()+" "+bean.getLname());
                ((ListViewHolder) holder).tv_title.setText(bean.getTitle());
                ((ListViewHolder) holder).message.setText(bean.getMessage());
            }else {
                ((ListViewHolder) holder).phone.setVisibility(View.GONE);
                ((ListViewHolder) holder).date.setText(convertDate(bean.getCreated_date()));
                ((ListViewHolder) holder).title.setText(bean.getTitle());
                ((ListViewHolder) holder).message.setText(bean.getMessage());
            }


            ((ListViewHolder) holder).scrollView.setTag(bean.getId());

            ((ListViewHolder) holder).more_icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (((ListViewHolder) holder).scrollView.getTag().equals(bean.getId())){
                        if (((ListViewHolder) holder).scrollView.isShown()) {
                            ((ListViewHolder) holder).scrollView.startAnimation(animationUp);

                            CountDownTimer countDownTimerStatic = new CountDownTimer(COUNTDOWN_RUNNING_TIME, 16) {
                                @Override
                                public void onTick(long millisUntilFinished) {
                                }

                                @Override
                                public void onFinish() {
                                    ((ListViewHolder) holder).scrollView.setVisibility(View.GONE);
                                }
                            };
                            countDownTimerStatic.start();
                            ((ListViewHolder) holder).more_icon.setImageResource(R.drawable.plus);

                        } else {
                            ((ListViewHolder) holder).more_icon.setImageResource(R.drawable.minus);

                            ((ListViewHolder) holder).scrollView.setVisibility(View.VISIBLE);
                            ((ListViewHolder) holder).scrollView.startAnimation(animationDown);

                        }
                    }
                }
            });
        }

    }

    public void setMoreDataAvailable(boolean moreDataAvailable) {
        isMoreDataAvailable = moreDataAvailable;
    }

    /* notifyDataSetChanged is final method so we can't override it
         call adapter.notifyDataChanged(); after update the list
         */
    public void notifyDataChanged(){
        notifyDataSetChanged();
        isLoading = false;
    }


    public interface OnLoadMoreListener{
        void onLoadMore();
    }

    public void setLoadMoreListener(OnLoadMoreListener loadMoreListener) {
        this.loadMoreListener = loadMoreListener;
    }


    public void removeItem(int position) {
        list.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, list.size());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public String convertDate(String ds1){
        String ds2 = "";
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
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

