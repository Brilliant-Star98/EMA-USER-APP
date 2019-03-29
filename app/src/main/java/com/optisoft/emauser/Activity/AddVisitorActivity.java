package com.optisoft.emauser.Activity;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.optisoft.emauser.HelperClasses.CommonPrefrence;
import com.optisoft.emauser.HelperClasses.DatePickerFragment;
import com.optisoft.emauser.HelperClasses.SearchableSpinner;
import com.optisoft.emauser.HelperClasses.TimePickerFragment;
import com.optisoft.emauser.Model.DropDownUsers;
import com.optisoft.emauser.Model.ResponseModel;
import com.optisoft.emauser.Model.UserModel;
import com.optisoft.emauser.Model.VisitorModel;
import com.optisoft.emauser.R;
import com.optisoft.emauser.Webservices.CallApi;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import okhttp3.MediaType;
import okhttp3.RequestBody;

import static com.optisoft.emauser.Activity.ProfileActivity.EXTERNAL_STORAGE_PERMISSION_CONSTANT;
import static com.optisoft.emauser.HelperClasses.ApiConstant.EXCEPTION_TAG;
import static com.optisoft.emauser.HelperClasses.ApiConstant.INTENT_TAG;
import static com.optisoft.emauser.HelperClasses.ApiConstant.convert_time;
import static com.optisoft.emauser.HelperClasses.ApiConstant.hideKeyboardFrom;

public class AddVisitorActivity extends AppCompatActivity implements View.OnClickListener, View.OnTouchListener {

    private static final String TAG = "VISITOR";
    private static final int CAMERA_REQUEST = 1888;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;


    private UserModel userModel = null;
    private CommonPrefrence commonPrefrence = new CommonPrefrence();
    private ImageView drawer, imageView;
    private RadioButton male, female;
    private SearchableSpinner spinner_visit_to;
    private TextView submit, tv_title, status;
    private EditText et_name, et_mobile, et_date_in, et_date_out, et_time_in, et_time_out,et_vehical_num, et_num_person, time, date;
    private String nameStr, mobileStr, genderStr, dateInStr, dateOutStr, timeInStr, timeOutStr,vehicalStr, personStr, selectedUserId = "", selectedUserName;
    private CallApi callApi = new CallApi();
    private Gson gson = new Gson();
    private File destination = null;
    private boolean isAdd = true;
    private boolean isEdit = false;
    private VisitorModel visitorModel = null;
    private String visitorId = "";
    private ScrollView scrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_visitor);

        userModel = commonPrefrence.getUserLoginSharedPref(this);

        if (getIntent().hasExtra(INTENT_TAG)){
            isAdd = false;
            String temp = getIntent().getStringExtra(INTENT_TAG);
            Type type=new TypeToken<VisitorModel>(){}.getType();
            visitorModel = gson.fromJson(temp, type);
        }

        scrollView = (ScrollView) findViewById(R.id.scrollView);
        spinner_visit_to = (SearchableSpinner) findViewById(R.id.spinner_visit_to);
        spinner_visit_to.setDefaultText("Search Name");
        spinner_visit_to.setInvalidTextColor(getResources().getColor(R.color.white));
        spinner_visit_to.setSelectionListener(new SearchableSpinner.OnSelectionListener() {
            @Override
            public void onSelect(int spinnerId, int position, DropDownUsers value) {
                selectedUserId = value.getUserId();
                selectedUserName = value.toString();
                Log.i("Select1", "Position : " + position + " : Value : " + value.toString() + " : " + spinnerId);
            }
        });

        male       = (RadioButton) findViewById(R.id.radioMale);
        female     = (RadioButton) findViewById(R.id.radioFemale);
        et_name        = (EditText) findViewById(R.id.et_name);
        et_mobile      = (EditText) findViewById(R.id.et_mobile);
        et_date_in     = (EditText) findViewById(R.id.et_date_in);
        et_date_out    = (EditText) findViewById(R.id.et_date_out);
        et_time_in     = (EditText) findViewById(R.id.et_time_in);
        et_time_out    = (EditText) findViewById(R.id.et_time_out);
        et_vehical_num = (EditText) findViewById(R.id.et_vehical_num);
        et_num_person  = (EditText) findViewById(R.id.et_num_person);

        tv_title   = (TextView) findViewById(R.id.tv_title);
        status     = (TextView) findViewById(R.id.status);
        submit     = (TextView) findViewById(R.id.submit);
        drawer     = (ImageView) findViewById(R.id.drawer);
        imageView     = (ImageView) findViewById(R.id.imageView);
        drawer.setOnClickListener(this);
        submit.setOnClickListener(this);
        imageView.setOnClickListener(this);
        et_date_in.setOnClickListener(this);
        et_date_out.setOnClickListener(this);
        et_time_in.setOnClickListener(this);
        et_time_out.setOnClickListener(this);

        if (!isAdd){
            setData();
        }
        loadData();

        scrollView.setOnTouchListener(this);
    }

    private void setData() {
        if (visitorModel == null){
            return;
        }
        status.setVisibility(View.VISIBLE);
        tv_title.setText("Visitor Detail");

        selectedUserId = visitorModel.getUser_id();
        selectedUserName = visitorModel.getVisit_to();
        spinner_visit_to.setText(visitorModel.getVisit_to());
        et_name.setText(visitorModel.getVisitor_name());
        et_num_person.setText(visitorModel.getNum_person());
        et_vehical_num.setText(visitorModel.getVical_no());
        et_mobile.setText(visitorModel.getMobile());
        et_time_out.setText(visitorModel.getTime_out());
        et_time_in.setText(visitorModel.getTime_in());
        et_date_out.setText(visitorModel.getDate_out());
        et_date_in.setText(visitorModel.getDate_in());
        if (visitorModel.getGender().equalsIgnoreCase("Male")){
            male.setChecked(true);
        }else {
            female.setChecked(true);
        }

       if (!visitorModel.getStatus().equals("0")){
           spinner_visit_to.setEnabled(false);
           et_num_person.setEnabled(false);
           et_vehical_num.setEnabled(false);
           et_mobile.setEnabled(false);
           et_name.setEnabled(false);
           et_name.setEnabled(false);
           et_time_in.setEnabled(false);
           et_date_in.setEnabled(false);
           male.setEnabled(false);
           female.setEnabled(false);
       }

        if (!visitorModel.getStatus().equals("1")){
            et_time_out.setEnabled(false);
            et_date_out.setEnabled(false);
        }

        if (visitorModel.getStatus().equals("0") || visitorModel.getStatus().equals("1")){
            isEdit = true;
            visitorId = visitorModel.getId();
            submit.setVisibility(View.VISIBLE);
        }else {
            submit.setVisibility(View.GONE);
        }

        switch (visitorModel.getStatus()){

            case "0":
                status.setBackgroundColor(Color.parseColor("#f1a92d"));
                status.setText("Pendding");
                break;

            case "1":
                status.setBackgroundColor(Color.parseColor("#16b728"));
                status.setText("Accepted");
                submit.setText("Close");
                break;

            case "2":
                status.setBackgroundColor(Color.parseColor("#ff0000"));
                status.setText("Rejected");
                break;

            case "3":
                status.setBackgroundColor(Color.parseColor("#5a5858"));
                status.setText("Closed");
                break;

            case "4":
                status.setBackgroundColor(Color.parseColor("#000000"));
                status.setText("Deleted");
                break;

            default:
        }

    }

    private void loadData() {
        callApi.requestGuardUsers(this, userModel.getAgent_id());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.drawer:
                finish();
                break;

            case R.id.submit:
                submitVisitorData();
                break;

            case R.id.imageView:
                if (isAdd)
                selectImage();
                break;

            case R.id.et_date_in:
                showDatePicker(et_date_in);
                break;

            case R.id.et_date_out:
                showDatePicker(et_date_out);
                break;

            case R.id.et_time_in:
                showTimePicker(et_time_in);
                break;

            case R.id.et_time_out:
                showTimePicker(et_time_out);
                break;

            default:
                return;
        }
    }

    private void showTimePicker(EditText et) {
        time = et;
        TimePickerFragment time = new TimePickerFragment();
        time.setCallBack(onTime);
        time.show(getSupportFragmentManager(),"Time Picker");
    }

    TimePickerDialog.OnTimeSetListener onTime = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
            time.setText(convert_time(hourOfDay) + ":" + convert_time(minute) );
        }
    };

    private void showDatePicker(EditText et) {
        date = et;
        DatePickerFragment date = new DatePickerFragment();
        Calendar calender = Calendar.getInstance();
        Bundle args = new Bundle();
        args.putInt("year", calender.get(Calendar.YEAR));
        args.putInt("month", calender.get(Calendar.MONTH));
        args.putInt("day", calender.get(Calendar.DAY_OF_MONTH));
        args.putBoolean("isDisableNext", true);
        date.setArguments(args);
        date.setCallBack(ondate);
        date.show(getSupportFragmentManager(), "Date Picker");
    }

    DatePickerDialog.OnDateSetListener ondate = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            String dateStr =  convert_time(dayOfMonth)+"-"+convert_time(monthOfYear+1)+"-"+year;
            date.setText(dateStr);
        }
    };

    private void submitVisitorData() {
        nameStr = et_name.getText().toString();
        mobileStr = et_mobile.getText().toString();
        dateInStr = et_date_in.getText().toString();
        dateOutStr = et_date_out.getText().toString();
        timeInStr = et_time_in.getText().toString();
        timeOutStr = et_time_out.getText().toString();
        vehicalStr = et_vehical_num.getText().toString();
        personStr = et_num_person.getText().toString();

        if (male.isChecked()){
            genderStr = "Male";
        }else {
            genderStr = "Female";
        }

        if (selectedUserId.isEmpty()){
            spinner_visit_to.setError("Select one ");
            spinner_visit_to.requestFocus();
            return;
        }
        if (nameStr.isEmpty()){
            et_name.setError("Enter Name");
            et_name.requestFocus();
            return;
        }
        if (mobileStr.isEmpty()){
            et_mobile.setError("Enter Mobile Number");
            et_mobile.requestFocus();
            return;
        }
        if (dateInStr.isEmpty()){
            et_date_in.setError("Select Entry Date");
            et_date_in.requestFocus();
            return;
        }
/*        if (dateOutStr.isEmpty()){
            et_date_out.setError("Select Exit Date");
            et_date_out.requestFocus();
            return;
        }*/
        if (timeInStr.isEmpty()){
            et_time_in.setError("Select Entry Time");
            et_time_in.requestFocus();
            return;
        }
/*        if (timeOutStr.isEmpty()){
            et_time_out.setError("Select Exit Time");
            et_time_out.requestFocus();
            return;
        }*/
        if (personStr.isEmpty()){
            et_num_person.setError("Enter Number of persons");
            et_num_person.requestFocus();
            return;
        }

        if (Integer.parseInt(personStr) > 5){
            et_num_person.setError("5 person allowed in single entry");
            et_num_person.requestFocus();
            return;
        }

        HashMap<String, RequestBody> map = new HashMap<>();
        map.put("guard_id", RequestBody.create(MediaType.parse("text/plain"), userModel.getUserId()));
        map.put("agent_id", RequestBody.create(MediaType.parse("text/plain"), userModel.getAgent_id()));
        map.put("user_id", RequestBody.create(MediaType.parse("text/plain"), selectedUserId));
        map.put("user_name", RequestBody.create(MediaType.parse("text/plain"), selectedUserName));
        map.put("visitor_name", RequestBody.create(MediaType.parse("text/plain"), nameStr));
        map.put("mobile", RequestBody.create(MediaType.parse("text/plain"), mobileStr));
        map.put("date_in", RequestBody.create(MediaType.parse("text/plain"), dateInStr));
        map.put("date_out", RequestBody.create(MediaType.parse("text/plain"), dateOutStr));
        map.put("time_in", RequestBody.create(MediaType.parse("text/plain"), timeInStr));
        map.put("time_out", RequestBody.create(MediaType.parse("text/plain"), timeOutStr));
        map.put("vehical_num", RequestBody.create(MediaType.parse("text/plain"), vehicalStr));
        map.put("person", RequestBody.create(MediaType.parse("text/plain"), personStr));
        map.put("gender", RequestBody.create(MediaType.parse("text/plain"), genderStr));
        try {
            if (destination != null) {
                File temp = saveBitmapToFile(destination);
                if (temp == null) {
                    customToast("Visitor Image not selected correctly");
                    return;
                }
                map.put("image\"; filename=\"" + temp.getName(), RequestBody.create(MediaType.parse("image*//*"), temp));
            }
        } catch (Exception e) {
            Log.d("IMAGE_ERROR", e.getMessage());
            customToast("Profile Image not selected correctly");
            return;
        }

        if (isEdit){
            map.put("visitor_id", RequestBody.create(MediaType.parse("text/plain"), visitorId));
            if (visitorModel.getStatus().equals("0")){
                map.put("status", RequestBody.create(MediaType.parse("text/plain"), "0"));
            }else {
                map.put("status", RequestBody.create(MediaType.parse("text/plain"), "3"));
            }

            callApi.requestUpdateVisitor(this, map);
        }else {
            callApi.requestEntryVisitor(this, map);
        }
    }

    public void customToast(String message) {
        Toast.makeText(getApplication(), message, Toast.LENGTH_LONG).show();
    }

    public void responseGuardUsers(ResponseModel body) {
        try {

            if (body.getStatus() == 1){
                JSONArray jsonArray = new JSONArray(gson.toJson(body.getResponse()));
                Type type=new TypeToken<ArrayList<DropDownUsers>>(){}.getType();
                ArrayList<DropDownUsers> list = gson.fromJson(jsonArray.toString(), type);
                spinner_visit_to.setData(list);

            }else {
                customToast(body.getMessage());
            }
        }catch (Exception e){
            customToast(e.getMessage());
            Log.e(EXCEPTION_TAG, e.getMessage());
        }
    }

    public void responseEntryVisitor(ResponseModel body) {
        try {
            if (body.getStatus() == 1){
                customToast(body.getMessage());
                Intent intent = new Intent(this, AddVisitorActivity.class);
                startActivity(intent);
                finish();
            }else {
                customToast(body.getMessage());
            }
        }catch (Exception e){
            customToast(e.getMessage());
            Log.e(EXCEPTION_TAG, e.getMessage());
        }
    }


    private void selectImage() {
        if (isStoragePermissionGranted()) {
            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(cameraIntent, CAMERA_REQUEST);
        }
    }

    public  boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED && checkSelfPermission(android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG,"Permission is granted");
                return true;
            } else {

                Log.v(TAG,"Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, EXTERNAL_STORAGE_PERMISSION_CONSTANT);
                return false;
            }
        }
        else {
            Log.v(TAG,"Permission is granted");
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_PERMISSION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            } else {
                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
            }

        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            thumbnail.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
            destination = new File(Environment.getExternalStorageDirectory(), System.currentTimeMillis() + ".jpg");
            FileOutputStream fo;
            try {
                destination.createNewFile();
                fo = new FileOutputStream(destination);
                fo.write(bytes.toByteArray());
                fo.close();
                imageView.setImageBitmap(thumbnail);
            } catch (FileNotFoundException e) {
                customToast(e.getMessage());
                e.printStackTrace();
            } catch (IOException e) {
                customToast(e.getMessage());
                e.printStackTrace();
            }
        }
    }

    public File saveBitmapToFile(File file) {
        try {
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            o.inSampleSize = 6;
            FileInputStream inputStream = new FileInputStream(file);
            BitmapFactory.decodeStream(inputStream, null, o);
            inputStream.close();
            final int REQUIRED_SIZE = 75;
            int scale = 1;
            while (o.outWidth / scale / 2 >= REQUIRED_SIZE &&
                    o.outHeight / scale / 2 >= REQUIRED_SIZE) {
                scale *= 2;
            }

            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            inputStream = new FileInputStream(file);

            Bitmap selectedBitmap = BitmapFactory.decodeStream(inputStream, null, o2);
            inputStream.close();

            file.createNewFile();
            FileOutputStream outputStream = new FileOutputStream(file);
            selectedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            return file;
        } catch (Exception e) {
            return null;
        }
    }

    public void responseUpdateVisitor(ResponseModel body) {
        try {
            if (body.getStatus() == 1){
                JSONObject jsonObject = new JSONObject(gson.toJson(body.getResponse()));
                Intent intent = new Intent(this, AddVisitorActivity.class);
                intent.putExtra(INTENT_TAG, jsonObject.toString());
                startActivity(intent);
                finish();
            }else {
                customToast(body.getMessage());
            }
        }catch (Exception e){
            customToast(e.getMessage());
            Log.e(EXCEPTION_TAG, e.getMessage());
        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch (view.getId()){
            case R.id.scrollView:
                hideKeyboardFrom(AddVisitorActivity.this, view);
                break;
        }
        return false;
    }
}
