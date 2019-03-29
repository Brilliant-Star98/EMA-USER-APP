package com.optisoft.emauser.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.optisoft.emauser.HelperClasses.CommonPrefrence;
import com.optisoft.emauser.Model.ResponseModel;
import com.optisoft.emauser.Model.UserModel;
import com.optisoft.emauser.R;
import com.optisoft.emauser.Webservices.CallApi;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

import java.util.HashMap;

import static com.optisoft.emauser.HelperClasses.ApiConstant.EXCEPTION_TAG;
import static com.optisoft.emauser.HelperClasses.ApiConstant.INTENT_TAG;
import static com.optisoft.emauser.HelperClasses.ApiConstant.PLACE_PICKER_REQUEST;

public class ContactUsActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView drawer;
    private LinearLayout ly_location;
    private TextView btn_submit, title, tv_dis_msg, select_location, selected_location;
    private EditText et_subject, et_message;
    private UserModel userModel = null;
    private CommonPrefrence commonPrefrence = new CommonPrefrence();
    String flagActivity = "";

    private String lat = "", lng = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);

        userModel = commonPrefrence.getUserLoginSharedPref(this);

        Intent intent = getIntent();
        if (intent.hasExtra(INTENT_TAG)){
            flagActivity = intent.getStringExtra(INTENT_TAG);
        }

        ly_location = (LinearLayout) findViewById(R.id.ly_location);
        title = (TextView) findViewById(R.id.title);
        select_location   = (TextView) findViewById(R.id.select_location);
        selected_location = (TextView) findViewById(R.id.selected_location);
        tv_dis_msg = (TextView) findViewById(R.id.tv_dis_msg);
        et_message = (EditText) findViewById(R.id.et_message);
        et_subject = (EditText) findViewById(R.id.et_subject);
        btn_submit = (TextView) findViewById(R.id.btn_submit);
        drawer     = (ImageView) findViewById(R.id.drawer);
        drawer.setOnClickListener(this);
        btn_submit.setOnClickListener(this);
        select_location.setOnClickListener(this);

        if (flagActivity.equals("contact")){
            title.setText("Contact Us");
            tv_dis_msg.setVisibility(View.GONE);
            ly_location.setVisibility(View.GONE);
        }else {
            title.setText("Distress Message");
            tv_dis_msg.setVisibility(View.VISIBLE);
            ly_location.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.drawer:
                finish();
                break;
            case R.id.btn_submit:
                callSubmitContact();
                break;
            case R.id.select_location:
                callLocationPicker();
                break;
        }
    }

    private void callLocationPicker(){
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
        try {
            startActivityForResult(builder.build(this), PLACE_PICKER_REQUEST);
        } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(data, this);
                lat = String.valueOf(place.getLatLng().latitude);
                lng = String.valueOf(place.getLatLng().longitude);
                selected_location.setText(lat+", "+lng);
            }
        }
    }


    private void callSubmitContact() {
        String subjectStr = et_subject.getText().toString();
        String msgStr     = et_message.getText().toString();

        if (subjectStr.isEmpty()){
            et_subject.setError("Enter subject");
            et_subject.requestFocus();
            return;
        }
        if (msgStr.isEmpty()){
            et_message.setError("Write you message...");
            et_message.requestFocus();
            return;
        }

        if (!flagActivity.equals("contact") && lat.isEmpty()){
            customToast("Please Select Distress Location");
            return;
        }

        HashMap<String, String> map = new HashMap<>();
        map.put("subject", subjectStr);
        map.put("message", msgStr);
        map.put("user_id", userModel.getUserId());
        map.put("agent_id", userModel.getAgent_id());
        map.put("lat", lat);
        map.put("lng", lng);
        CallApi callApi = new CallApi();
        callApi.requestContactUs(ContactUsActivity.this, map, flagActivity);
    }

    public void customToast(String s) {
        Toast.makeText(getApplication(), s, Toast.LENGTH_LONG).show();
    }

    public void requestContactUs(ResponseModel model){
        try {
            customToast(model.getMessage());
            if(model.getStatus()==1)
            {
                et_message.setText("");
                et_subject.setText("");
                selected_location.setText("");
                lat = "";
                lng = "";
            }
        }catch (Exception e){
            Log.e(EXCEPTION_TAG, e.getMessage());
            customToast(e.getMessage());
        }

    }
}
