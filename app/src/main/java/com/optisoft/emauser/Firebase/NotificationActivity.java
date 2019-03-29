package com.optisoft.emauser.Firebase;

import android.app.NotificationManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.optisoft.emauser.HelperClasses.CommonPrefrence;
import com.optisoft.emauser.Model.ResponseModel;
import com.optisoft.emauser.Model.UserModel;
import com.optisoft.emauser.R;
import com.optisoft.emauser.Webservices.CallApi;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class NotificationActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String NOTIFICATION_ID = "NOTIFICATION_ID";
    private TextView title, message, allow, close;
    private ImageView closeimg;
    private int notiId = -1;

    private CommonPrefrence commonPrefrence = new CommonPrefrence();
    private UserModel usersModel = new UserModel();
    private CallApi callApi = new CallApi();
    private String booking_id = "";
    private String type = "", user;
    private String visiterData = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification2);

        usersModel = commonPrefrence.getUserLoginSharedPref(this);


        title = (TextView) findViewById(R.id.notification_title);
        message = (TextView) findViewById(R.id.message);
        close = (TextView) findViewById(R.id.close_btn);
        allow = (TextView) findViewById(R.id.allow);
        closeimg = (ImageView) findViewById(R.id.close);


        closeimg.setOnClickListener(this);
        close.setOnClickListener(this);
        allow.setOnClickListener(this);

        try {
            Intent intent = getIntent();
            String titleStr = intent.getStringExtra("title");
            String messageStr = intent.getStringExtra("message");
            notiId = intent.getIntExtra(NOTIFICATION_ID, -1);

            //customToast("Title : "+titleStr+"\nMessage : "+messageStr);

            title.setText(titleStr);
            message.setText(messageStr);

            if (intent.hasExtra("type")){
                type = intent.getStringExtra("type");
                user = intent.getStringExtra("user");
                if (type.equals("VISITER")){
                    visiterData = intent.getStringExtra("data");
                    allow.setVisibility(View.VISIBLE);
                    close.setText("Reject");
                }
            }
        }catch (Exception e){
            Log.e("EXCEPTION", e.getMessage());
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.close:
                closeNotification();
                break;

            case R.id.close_btn:
                if (type.isEmpty()){
                    closeNotification();
                }else {
                    allowVisiter(2);
                }
                break;
            case R.id.allow:
                allowVisiter(1);
                break;
        }
    }

    private void allowVisiter(int i) {
        if (visiterData.isEmpty()){
            finish();
        }
        try {
            //{"visitor_name":"hemraj","visit_to":"Ajay Jangid","user_id":"114","gender":"Male","date":"06\/14\/2018","time_in":"03:15","time_out":"16:20","mobile":"8239929306","vical_no":"RJ45 JP 4585","guard_id":"110"}
            JSONObject object = new JSONObject(visiterData);
            String visiterId = object.getString("id");
            HashMap<String, String> map = new HashMap<>();
            map.put("visiter_id", visiterId);
            map.put("visit_to", object.getString("visit_to"));
            map.put("visitor_name", object.getString("visitor_name"));
            map.put("user_id", object.getString("user_id"));
            map.put("guard_id", object.getString("guard_id"));
            map.put("status", i+"");
            callApi.requestReplyVisiter(this, map);
        } catch (JSONException e) {
            e.printStackTrace();
            finish();
        }
    }

    public void closeNotification(){
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        manager.cancel(notiId);
        finish();
    }

    public void customToast(String message) {
        Toast.makeText(getApplication(), message, Toast.LENGTH_LONG).show();
    }

    public void requestReplyVisiter(ResponseModel body) {
        try {
            customToast(body.getMessage());
            if (body.getStatus()==1){
                finish();
            }
        }catch (Exception e){
            customToast(e.getMessage());
        }
    }
}
