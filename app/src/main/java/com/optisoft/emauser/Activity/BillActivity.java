package com.optisoft.emauser.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.optisoft.emauser.Adapter.CurrentBillAdapter;
import com.optisoft.emauser.HelperClasses.CommonPrefrence;
import com.optisoft.emauser.Model.BillModel;
import com.optisoft.emauser.Model.ResponseModel;
import com.optisoft.emauser.Model.UserModel;
import com.optisoft.emauser.R;
import com.optisoft.emauser.Webservices.CallApi;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;

import java.lang.reflect.Type;
import java.util.ArrayList;

import static com.optisoft.emauser.HelperClasses.ApiConstant.CURRENT_TAG;
import static com.optisoft.emauser.HelperClasses.ApiConstant.EXCEPTION_TAG;
import static com.optisoft.emauser.HelperClasses.ApiConstant.INTENT_TAG;

public class BillActivity extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView recyclerView;
    private ArrayList<BillModel> arraList;
    private CurrentBillAdapter adapter;
    private boolean flag = true;
    private ImageView drawer;
    private TextView title;
    private Gson gson = new Gson();

    private LinearLayout nodata;
    private String tag = CURRENT_TAG;
    private CommonPrefrence commonPrefrence = new CommonPrefrence();
    private UserModel userModel = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill);


        userModel = commonPrefrence.getUserLoginSharedPref(this);

        final Intent intent = getIntent();
        if (intent.hasExtra(INTENT_TAG)){
            tag = intent.getStringExtra(INTENT_TAG);
        }

        drawer = (ImageView)findViewById(R.id.drawer);
        title = (TextView)findViewById(R.id.title);
        if (tag.equalsIgnoreCase(CURRENT_TAG)){
            title.setText("Cureent Bills");
        }else {
            title.setText("Previous Bills");
        }

        nodata = (LinearLayout)findViewById(R.id.nodata);
        recyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        arraList = new ArrayList<>();



        drawer.setOnClickListener(this);

        load(0);
    }

    private void load(int index){
        CallApi callApi=new CallApi();
        callApi.requestCurrentBillList(BillActivity.this, userModel.getUserId(), tag);
    }

    public void customToast(String s) {
        Toast.makeText(getApplication(), s, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.drawer:
                finish();
                break;
        }
    }

    public void responseCurrentBillList(ResponseModel model){
        try {
            if(model.getStatus()==1)
            {
                JSONArray jsonArray = new JSONArray(gson.toJson(model.getResponse()));
                Type type=new TypeToken<ArrayList<BillModel>>(){}.getType();
                arraList = gson.fromJson(jsonArray.toString(), type);
                adapter = new CurrentBillAdapter(this, arraList, tag);
                recyclerView.setAdapter(adapter);
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


}
