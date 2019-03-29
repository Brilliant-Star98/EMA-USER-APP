package com.optisoft.emauser.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.optisoft.emauser.Adapter.VehicleHistoryAdapter;
import com.optisoft.emauser.Adapter.VehicleListAdapter;
import com.optisoft.emauser.HelperClasses.CommonPrefrence;
import com.optisoft.emauser.Model.ResponseModel;
import com.optisoft.emauser.Model.UserModel;
import com.optisoft.emauser.R;
import com.optisoft.emauser.Webservices.CallApi;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

import static com.optisoft.emauser.HelperClasses.ApiConstant.EXCEPTION_TAG;

public class VehicleHistoryActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView img_back;
    private ListView listView;
    private String car_plate, date_from, date_to;
    VehicleHistoryAdapter vehicleHistoryAdapter;
    private CommonPrefrence commonPrefrence = new CommonPrefrence();
    private UserModel userModel = null;
    private Gson gson = new Gson();
    private CallApi callApi = new CallApi();
    public  ArrayList<HashMap<String, String>> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_history);

        car_plate = getIntent().getStringExtra("car_plate");
        date_from = getIntent().getStringExtra("date_from");
        date_to = getIntent().getStringExtra("date_to");

        userModel = commonPrefrence.getUserLoginSharedPref(this);
        load();

        listView = (ListView) findViewById(R.id.listview_vehicle_history);
        list = new ArrayList<HashMap<String, String>>();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
                view.animate().setDuration(800).alpha(0)
                        .withEndAction(new Runnable() {
                            @Override
                            public void run() {
                                //list.remove(item);
                                //vehicleListAdapter.notifyDataSetChanged();
                                view.setAlpha(1);
                            }
                        });
            }
        });
        img_back = (ImageView) findViewById(R.id.img_back_2);
        img_back.setOnClickListener(this);
    }

    private void load() {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("car_plate", car_plate);
        map.put("date_from", date_from);
        map.put("date_to", date_to);
        callApi.getCarHistory(VehicleHistoryActivity.this, map);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.img_back_2:
                finish();
                break;
        }
    }

    public void showHistoryList(ResponseModel model) {
        try {
            customToast(model.getMessage());
            if (model.getStatus()==1)
            {
                JSONArray jsonArray = new JSONArray(gson.toJson(model.getResponse()));
                Type type=new TypeToken<ArrayList<HashMap<String, String>>>(){}.getType();
                ArrayList<HashMap<String, String>> list_model = gson.fromJson(jsonArray.toString(), type);

                //Add a header to the listView
                LayoutInflater inflater = getLayoutInflater();
                ViewGroup header = (ViewGroup)inflater.inflate(R.layout.listing_my_vehicle_history_header, listView, false);
                listView.addHeaderView(header);

                vehicleHistoryAdapter = new VehicleHistoryAdapter(this, list_model);
                listView.setAdapter(vehicleHistoryAdapter);
            }

        }catch (Exception e){
            Log.e(EXCEPTION_TAG, e.getMessage());
            customToast(e.getMessage());
        }
    }

    public void customToast(String s) {
        Toast.makeText(getApplication(), s, Toast.LENGTH_LONG).show();
    }
}
