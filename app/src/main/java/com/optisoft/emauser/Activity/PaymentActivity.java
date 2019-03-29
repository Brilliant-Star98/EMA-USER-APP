package com.optisoft.emauser.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.optisoft.emauser.Adapter.PaymentHistoryAdapter;
import com.optisoft.emauser.HelperClasses.CommonPrefrence;
import com.optisoft.emauser.Model.PaymentModel;
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


public class PaymentActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView img_back;
    private Gson gson = new Gson();
    private PaymentHistoryAdapter adapter;
    private RecyclerView recyclerView;
    private ArrayList<PaymentModel> arraList;
    private UserModel userModel = null;
    private CommonPrefrence commonPrefrence = new CommonPrefrence();
    private ListView listview;
    private LinearLayout nodata;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        userModel = commonPrefrence.getUserLoginSharedPref(this);

        nodata = (LinearLayout)findViewById(R.id.nodata);
        img_back    = (ImageView) findViewById(R.id.drawer);
        listview = (ListView)findViewById(R.id.listview);

        img_back.setOnClickListener(this);
        load(0);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.drawer:
                finish();
                break;
        }
    }
    public void paymentHistory(ResponseModel model){
        try {
            if(model.getStatus()==1)
            {
                JSONArray jsonArray = new JSONArray(gson.toJson(model.getResponse()));
                Type type=new TypeToken<ArrayList<PaymentModel>>(){}.getType();
                arraList = gson.fromJson(jsonArray.toString(), type);
                adapter = new PaymentHistoryAdapter(this, arraList);
                listview.setAdapter(adapter);
                adapter.notifyDataSetChanged();

                if (arraList.size() == 0){
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
    private void load(int index){

        CallApi callApi=new CallApi();
        callApi.paymentHistory(PaymentActivity.this, userModel.getUserId());
    }
    public void customToast(String s) {
        Toast.makeText(getApplication(), s, Toast.LENGTH_LONG).show();
    }
}
