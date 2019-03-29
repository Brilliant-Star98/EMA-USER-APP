package com.optisoft.emauser.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.optisoft.emauser.Adapter.DistressMessageAdapter;
import com.optisoft.emauser.HelperClasses.CommonPrefrence;
import com.optisoft.emauser.Model.DistressModel;
import com.optisoft.emauser.Model.ResponseModel;
import com.optisoft.emauser.Model.UserModel;
import com.optisoft.emauser.R;
import com.optisoft.emauser.Webservices.CallApi;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.optisoft.emauser.HelperClasses.ApiConstant.EXCEPTION_TAG;

public class DistressMessageActivity extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView recyclerView;
    private ArrayList<DistressModel> arraList;
    private DistressMessageAdapter adapter;
    private ImageView drawer;
    private CommonPrefrence commonPrefrence = new CommonPrefrence();
    public boolean isAgent = false;
    private UserModel userModel = null;
    private CallApi callApi = new CallApi();
    private Gson gson = new Gson();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_distress_message);

        if (commonPrefrence.getDistressSharedPref(this) == null){
            finish();
        }
        userModel = commonPrefrence.getUserLoginSharedPref(this);
        if (userModel.getRoleId().equals("2")){
            isAgent = true;
        }
        ArrayList<String> mList = commonPrefrence.getDistressSharedPref(this);

        drawer = (ImageView)findViewById(R.id.drawer);
        recyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        arraList = new ArrayList<>();
        drawer.setOnClickListener(this);

        List<String> strings = new ArrayList<>();

        try {
            for (int i = 0; i < mList.size(); i++){
                Type type=new TypeToken<DistressModel>(){}.getType();
                DistressModel model = gson.fromJson(mList.get(i), type);
                if (!strings.contains(model.getId())){
                    arraList.add(model);
                    strings.add(model.getId());
                }
            }
        }catch (Exception e){
            finish();
        }
        adapter = new DistressMessageAdapter(this, arraList);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
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

    @Override
    protected void onDestroy() {
        commonPrefrence.setDistressSharedPref(this, null);
        super.onDestroy();
    }

    public void sendNotifiocationToAll(int position) {
        DistressModel model = arraList.get(position);
        HashMap<String, String> map = new HashMap<>();
        map.put("user_id", model.getUser_id());
        map.put("agent_id", userModel.getUserId());
        map.put("message_id", model.getId());
        callApi.requestSendDistressToAll(this, map, position);
    }

    public void responseSendDistressToAll(ResponseModel body, int pos) {
        try {
            customToast(body.getMessage());
            if (body.getStatus() == 1){
                arraList.get(pos).setStatus("1");
                adapter.notifyItemChanged(pos);
            }
        }catch (Exception e){
            Log.e(EXCEPTION_TAG, e.getMessage());
            customToast(e.getMessage());
        }
    }
}
