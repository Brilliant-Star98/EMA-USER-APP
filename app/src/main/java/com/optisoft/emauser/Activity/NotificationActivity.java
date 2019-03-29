package com.optisoft.emauser.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.optisoft.emauser.Adapter.NotificationListAdapter;
import com.optisoft.emauser.HelperClasses.CommonPrefrence;
import com.optisoft.emauser.Model.NotificationModel;
import com.optisoft.emauser.Model.ResponseModel;
import com.optisoft.emauser.Model.UserModel;
import com.optisoft.emauser.R;
import com.optisoft.emauser.Webservices.CallApi;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;

import java.lang.reflect.Type;
import java.util.ArrayList;

import static com.optisoft.emauser.HelperClasses.ApiConstant.EXCEPTION_TAG;

public class NotificationActivity extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView recyclerView;
    NotificationListAdapter notificationAdapter;
    private ImageView img_back;
    private ArrayList<NotificationModel> list = null;
    private UserModel userModel = null;
    private Gson gson = new Gson();
    private CommonPrefrence commonPrefrence = new CommonPrefrence();
    private Animation animationUp, animationDown;
    private LinearLayoutManager mLayoutManager;
    private LinearLayout nodata;
    public boolean isAgent = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        userModel = commonPrefrence.getUserLoginSharedPref(this);
        if (userModel.getRoleId().equals("2")){
            isAgent = true;
        }else {
            isAgent = false;
        }

        animationUp = AnimationUtils.loadAnimation(this, R.anim.list_slide_up);
        animationDown = AnimationUtils.loadAnimation(this, R.anim.list_slide_down);


        nodata = (LinearLayout)findViewById(R.id.nodata);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        img_back    = (ImageView) findViewById(R.id.drawer);

        list = new ArrayList<NotificationModel>();
        recyclerView.setHasFixedSize(true);
        notificationAdapter = new NotificationListAdapter(this,list,animationUp,animationDown);
        mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(notificationAdapter);

        img_back.setOnClickListener(this);

        load(0);
    }
    private void load(int index){

        CallApi callApi=new CallApi();
        if (isAgent){
            callApi.contactRequestList(NotificationActivity.this, userModel.getUserId());
        }else {
            callApi.notificationHistory(NotificationActivity.this, userModel.getUserId());
        }
    }


    public void customToast(String s) {
        Toast.makeText(getApplication(), s, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.drawer:
                finish();
                break;
        }
    }

    public void notificationHistory(ResponseModel model){
        try {
            if(model.getStatus()==1)
            {
                JSONArray jsonArray = new JSONArray(gson.toJson(model.getResponse()));
                Type type=new TypeToken<ArrayList<NotificationModel>>(){}.getType();
                ArrayList<NotificationModel> data = gson.fromJson(jsonArray.toString(), type);
                list.addAll(data);
                notificationAdapter.notifyDataChanged();

                if (list.size() == 0){
                    nodata.setVisibility(View.VISIBLE);
                }else {
                    nodata.setVisibility(View.GONE);
                }
            }else {
                nodata.setVisibility(View.VISIBLE);
                customToast(model.getMessage());
            }
        }catch (Exception e){
            nodata.setVisibility(View.VISIBLE);
            Log.e(EXCEPTION_TAG, e.getMessage());
            customToast(e.getMessage());
        }

    }
}
