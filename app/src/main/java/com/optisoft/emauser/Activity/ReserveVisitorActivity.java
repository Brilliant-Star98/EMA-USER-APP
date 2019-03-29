package com.optisoft.emauser.Activity;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.optisoft.emauser.BroadcastReceiver.VisitorAlarm;
import com.optisoft.emauser.HelperClasses.CommonPrefrence;
import com.optisoft.emauser.HelperClasses.DatePickerFragment;
import com.optisoft.emauser.HelperClasses.SearchableSpinner;
import com.optisoft.emauser.HelperClasses.TimePickerFragment;
import com.optisoft.emauser.Model.ResponseModel;
import com.optisoft.emauser.Model.UserModel;
import com.optisoft.emauser.Model.VisitorModel;
import com.optisoft.emauser.R;
import com.optisoft.emauser.Webservices.CallApi;

import org.json.JSONArray;
import org.w3c.dom.Text;

import java.io.File;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.regex.Pattern;

import okhttp3.MediaType;
import okhttp3.RequestBody;

import static com.optisoft.emauser.HelperClasses.ApiConstant.ALARM_APPEAR;
import static com.optisoft.emauser.HelperClasses.ApiConstant.ALARM_TAG;
import static com.optisoft.emauser.HelperClasses.ApiConstant.EXCEPTION_TAG;
import static com.optisoft.emauser.HelperClasses.ApiConstant.INTENT_TAG;
import static com.optisoft.emauser.HelperClasses.ApiConstant.convert_time;
import static com.optisoft.emauser.HelperClasses.ApiConstant.hideKeyboardFrom;

public class ReserveVisitorActivity extends AppCompatActivity implements View.OnClickListener, View.OnTouchListener {

    private ImageView drawer;
    private static final String TAG = "VISITOR";
    private UserModel userModel = null;
    private CommonPrefrence commonPrefrence = new CommonPrefrence();
    private RadioButton male, female;
    private TextView submit, tv_title, status;
    private EditText et_name, et_mobile, et_date_in, et_date_out, et_time_in, et_time_out, et_vehicle_front, et_vehicle_back, et_num_person, time, date;
    private String nameStr, mobileStr, genderStr, dateInStr, dateOutStr, timeInStr, timeOutStr, vehicleStr, personStr, plate_front, plate_back;
    private CallApi callApi = new CallApi();
    private Gson gson = new Gson();
    private File destination = null;
    private boolean isAdd = true;
    private boolean isEdit = false;
    private VisitorModel visitorModel = null, updated_visitor_Model = null;
    private String visitorId = "";
    private ScrollView scrollView;
    private InputFilter filter, filter1;
    private Intent alertIntent = null;
    private PendingIntent pi = null;
    private Calendar calendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reserve_visitor);

        userModel = commonPrefrence.getUserLoginSharedPref(this);

        if (getIntent().hasExtra(INTENT_TAG)) {
            isAdd = false;
            String temp = getIntent().getStringExtra(INTENT_TAG);
            Type type = new TypeToken<VisitorModel>(){}.getType();
            visitorModel = gson.fromJson(temp, type);
        }

        if (visitorModel != null) {
            callApi.isReservedVisitor(this, visitorModel.getId());
        }
        filter = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                if (dend - start > 2)
                    return "";
                for (int i = start; i < end; ++i)
                {
                    if (!Pattern.compile("[ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz]*").matcher(String.valueOf(source.charAt(i))).matches())
                    {
                        return "";
                    }
                }

                return null;
            }
        };
        filter1 = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                if (dend - start > 3 )
                    return "";
                for (int i = start; i < end; ++i)
                {
                    if (!Pattern.compile("[ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890]*").matcher(String.valueOf(source.charAt(i))).matches())
                    {
                        return "";
                    }
                }

                return null;
            }
        };

        scrollView =    (ScrollView) findViewById(R.id.scrollView);
        male =          (RadioButton) findViewById(R.id.radioMale);
        female =        (RadioButton) findViewById(R.id.radioFemale);
        et_name =       (EditText) findViewById(R.id.et_name);
        et_mobile =     (EditText) findViewById(R.id.et_mobile);
        et_date_in =    (EditText) findViewById(R.id.et_date_in);
        et_date_out =   (EditText) findViewById(R.id.et_date_out);
        et_time_in =    (EditText) findViewById(R.id.et_time_in);
        et_time_out =   (EditText) findViewById(R.id.et_time_out);
        et_vehicle_front = (EditText) findViewById(R.id.et_vehicle_front);
        et_vehicle_front.setFilters(new InputFilter[]{filter});
        et_vehicle_back = (EditText) findViewById(R.id.et_vehicle_back);
        et_vehicle_back.setFilters(new InputFilter[]{filter1});
        et_num_person = (EditText) findViewById(R.id.et_num_person);


        tv_title = (TextView) findViewById(R.id.tv_title);
        status = (TextView) findViewById(R.id.status);
        submit = (TextView) findViewById(R.id.submit);
        drawer = (ImageView) findViewById(R.id.drawer);
        drawer.setOnClickListener(this);
        submit.setOnClickListener(this);
        et_date_in.setOnClickListener(this);
        et_date_out.setOnClickListener(this);
        et_time_in.setOnClickListener(this);
        et_time_out.setOnClickListener(this);

        if (!isAdd){
            setData();
        }

        scrollView.setOnTouchListener(this);
    }
    private void showExpiredAlert() {
        if(visitorModel == null)
            return;

        final Context context = this;
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

        alertDialogBuilder.setTitle("Your Visitor " + visitorModel.getVisitor_name() + "'s vehicle is expired");

        // set dialog message
        alertDialogBuilder
                .setMessage("Click YES to extend reservation of " + visitorModel.getVical_no())
                .setCancelable(false)
                .setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        et_time_out.setError("Enter Exiting Time for Extension");
                        et_time_out.requestFocus();
                    }
                })
                .setNegativeButton("No",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        dialog.cancel();
                        finish();
                        return;
                    }
                });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();
        // show it
        alertDialog.show();
    }
    private void setData() {
        if (visitorModel == null) {
            return;
        }
        status.setVisibility(View.VISIBLE);
        tv_title.setText("Visitor Detail");

        et_name.setText(visitorModel.getVisitor_name());
        et_num_person.setText(visitorModel.getNum_person());
        putVehicleNumber(visitorModel.getVical_no());
        //et_vehicle_num.setText(visitorModel.getVical_no());
        et_mobile.setText(visitorModel.getMobile());
        et_time_out.setText(visitorModel.getTime_out());
        et_time_in.setText(visitorModel.getTime_in());
        et_date_out.setText(visitorModel.getDate_out());
        et_date_in.setText(visitorModel.getDate_in());
        submit.setText("Update");
        if (visitorModel.getGender().equalsIgnoreCase("Male")){
            male.setChecked(true);
        }
        else{
            female.setChecked(true);
        }

        if (!visitorModel.getStatus().equals("0")) {
            et_num_person.setEnabled(false);
            et_vehicle_front.setEnabled(false);
            et_vehicle_back.setEnabled(false);
            et_mobile.setEnabled(false);
            et_name.setEnabled(false);
            et_time_in.setEnabled(false);
            et_date_in.setEnabled(false);
            male.setEnabled(false);
            female.setEnabled(false);
        }

//        if (!visitorModel.getStatus().equals("1")) {
//            et_time_out.setEnabled(false);
//            et_date_out.setEnabled(false);
//        }

        if (visitorModel.getStatus().equals("0") || visitorModel.getStatus().equals("1")) {
            isEdit = true;
            visitorId = visitorModel.getId();
            submit.setVisibility(View.VISIBLE);
        }
        else{
            submit.setVisibility(View.GONE);
        }

        switch (visitorModel.getStatus()){

            case "0":
                status.setBackgroundColor(Color.parseColor("#16b728"));
                status.setText("Scheduled");
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

        if (getIntent().hasExtra(ALARM_APPEAR)) {
            showExpiredAlert();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.drawer:
                Intent intent = new Intent(getApplication(), VisitorHistoryActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.submit:
                submitVisitorData();
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
                break;
        }
    }

    private void showTimePicker(EditText et) {
        time = et;
        TimePickerFragment time = new TimePickerFragment();
        time.setCallBack(onTime);
        time.show(getSupportFragmentManager(), "Time Picker");
    }

    TimePickerDialog.OnTimeSetListener onTime = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            time.setText(convert_time(hourOfDay) + ":" + convert_time(minute));
        }
    };

    private void putVehicleNumber(String str) {
        if (str.isEmpty())
            return;
        String[] splited = str.split("\\s+");
        et_vehicle_front.setText(splited[0]);
        et_vehicle_back.setText(splited[1]);
    }
    private void showDatePicker(EditText et) {
        date = et;
        DatePickerFragment date = new DatePickerFragment();
        Calendar calendar = Calendar.getInstance();
        Bundle args = new Bundle();
        args.putInt("year", calendar.get(Calendar.YEAR));
        args.putInt("month", calendar.get(Calendar.MONTH));
        args.putInt("day", calendar.get(Calendar.DAY_OF_MONTH));
        args.putBoolean("isDiableNext", true);
        date.setArguments(args);
        date.setCallBack(ondate);
        date.show(getSupportFragmentManager(), "Date Picker");
    }

    DatePickerDialog.OnDateSetListener ondate = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            String dateStr = convert_time(dayOfMonth) + "-" + convert_time(monthOfYear + 1) + "-" +  year;
            date.setText(dateStr);
        }
    };

    private Date convertFromString(String s, String type)
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat(type);
        Date convertedDate = new Date();

        try {
            convertedDate = dateFormat.parse(s);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return convertedDate;
    }

    private void submitVisitorData() {
        nameStr = et_name.getText().toString();
        mobileStr = et_mobile.getText().toString();
        dateInStr = et_date_in.getText().toString();
        dateOutStr = et_date_out.getText().toString();
        timeInStr = et_time_in.getText().toString();
        timeOutStr = et_time_out.getText().toString();
        plate_front = et_vehicle_front.getText().toString();
        plate_back = et_vehicle_back.getText().toString();
        personStr = et_num_person.getText().toString();

        if (male.isChecked()){
            genderStr = "Male";
        }else {
            genderStr = "Female";
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
        if (timeInStr.isEmpty()){
            et_time_in.setError("Select Entry Time");
            et_time_in.requestFocus();
            return;
        }
        if (dateOutStr.isEmpty()){
            et_date_in.setError("Select Exiting Date");
            et_date_in.requestFocus();
            return;
        }
        if (timeOutStr.isEmpty()){
            et_time_in.setError("Select Exiting Time");
            et_time_in.requestFocus();
            return;
        }
        if (personStr.isEmpty()){
            et_num_person.setError("Enter Number of persons");
            et_num_person.requestFocus();
            return;
        }
        Date t_in = convertFromString(dateInStr + ' ' + timeInStr + ":00", "dd-MM-yyyy hh:mm:ss");
        Date t_out = convertFromString(dateOutStr + ' ' + timeOutStr + ":00", "dd-MM-yyyy hh:mm:ss");
        if (convertFromString(dateInStr, "dd-MM-yyyy").getTime() > convertFromString(dateOutStr, "dd-MM-yyyy").getTime())
        {
            et_date_out.setError("Exiting Date has to be same or greater than Entry Date");
            et_date_out.requestFocus();
            return;
        }
        else if (t_in.getTime() >= t_out.getTime())
        {
            et_time_out.setError("Exiting Time has to be greater than Entry Time");
            et_time_out.requestFocus();
            return;
        }
        if (!plate_front.isEmpty() || !plate_back.isEmpty()) {
            if (!plate_front.isEmpty() && !plate_back.isEmpty()){
                if (plate_front.length() !=2 && plate_front.length() != 3){
                    et_vehicle_front.setError("Length has to be 2 or 3");
                    et_vehicle_front.requestFocus();
                    return;
                }
                else if (!isCarPlateCorrect()){
                    et_vehicle_back.setError("First 3 Letters have to be Number and last Letter has to be alphabet");
                    et_vehicle_back.requestFocus();
                    return;
                }
                else
                    vehicleStr = plate_front + ' ' + plate_back;
            }
            else if (!plate_front.isEmpty() && plate_back.isEmpty()){
                et_vehicle_back.setError("Enter 2ND REG No*");
                et_vehicle_back.requestFocus();
                return;
            }
            else if (plate_front.isEmpty() && !plate_back.isEmpty()){
                et_vehicle_front.setError("Enter 1ST REG No*");
                et_vehicle_front.requestFocus();
                return;
            }
        }
        else {
            vehicleStr = "";
            //customToast("This visitor will enter Without a Vehicle");
            et_vehicle_front.setError("Enter 1ST REG No*");
            et_vehicle_back.setError("Enter 2ND REG No*");
            et_vehicle_front.requestFocus();
            return;
        }
        if (Integer.parseInt(personStr) > 5){
            et_num_person.setError("5 person allowed in single entry");
            et_num_person.requestFocus();
            return;
        }



        String[] date_out = dateOutStr.split("-");
        String[] time_out = timeOutStr.split(":");
        calendar.set(Calendar.YEAR, Integer.parseInt(date_out[2]));
        calendar.set(Calendar.MONTH, Integer.parseInt(date_out[1]) - 1);
        calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(date_out[0]));
        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(time_out[0]));
        calendar.set(Calendar.MINUTE, Integer.parseInt(time_out[1]));
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        HashMap<String, RequestBody> map = new HashMap<>();
        map.put("agent_id", RequestBody.create(MediaType.parse("text/plain"), userModel.getAgent_id()));
        map.put("user_id", RequestBody.create(MediaType.parse("text/plain"), userModel.getUserId()));
        map.put("visitor_name", RequestBody.create(MediaType.parse("text/plain"), nameStr));
        map.put("mobile", RequestBody.create(MediaType.parse("text/plain"), mobileStr));
        map.put("date_in", RequestBody.create(MediaType.parse("text/plain"), dateInStr));
        map.put("date_out", RequestBody.create(MediaType.parse("text/plain"), dateOutStr));
        map.put("time_in", RequestBody.create(MediaType.parse("text/plain"), timeInStr));
        map.put("time_out", RequestBody.create(MediaType.parse("text/plain"), timeOutStr));
        map.put("vical_no", RequestBody.create(MediaType.parse("text/plain"), vehicleStr));
        map.put("person", RequestBody.create(MediaType.parse("text/plain"), personStr));
        map.put("gender", RequestBody.create(MediaType.parse("text/plain"), genderStr));

        if (isEdit) {
            updated_visitor_Model = visitorModel;
        }else{
            updated_visitor_Model = new VisitorModel();
        }

        updated_visitor_Model.setUser_id(userModel.getUserId());
        updated_visitor_Model.setVisitor_name(nameStr);
        updated_visitor_Model.setMobile(mobileStr);
        updated_visitor_Model.setGender(genderStr);
        updated_visitor_Model.setDate_in(dateInStr);
        updated_visitor_Model.setTime_in(timeInStr);
        updated_visitor_Model.setDate_out(dateOutStr);
        updated_visitor_Model.setTime_out(timeOutStr);
        updated_visitor_Model.setVical_no(vehicleStr);
        updated_visitor_Model.setNum_person(personStr);

        if (isEdit){
            map.put("visitor_id", RequestBody.create(MediaType.parse("text/plain"), visitorId));
            if (visitorModel.getStatus().equals("0")){
                map.put("status", RequestBody.create(MediaType.parse("text/plain"), "0"));
            }else {
                map.put("status", RequestBody.create(MediaType.parse("text/plain"), "3"));
            }
            callApi.requestUpdateReserveVisitor(this, map);

        }else {
            updated_visitor_Model.setStatus(Integer.toString(0));
            callApi.requestReserveVisitor(this, map);
        }
    }

    public void customToast(String message) {
        Toast.makeText(getApplication(), message, Toast.LENGTH_LONG).show();
    }

    public void setAlarmForVisitor(int vId) {
        //coded by Feng to make an alert at out time
        alertIntent = new Intent(this, VisitorAlarm.class);
        Bundle basket = new Bundle();
        basket.putString(ALARM_TAG, gson.toJson(updated_visitor_Model));
        alertIntent.putExtras(basket);
        pi = PendingIntent.getBroadcast(this.getApplicationContext(), vId, alertIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
        //am.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pi);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            am.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pi);
        }
        else{
            am.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pi);
        }
        customToast("ALERT will be set on " + updated_visitor_Model.getDate_out() + ' ' + updated_visitor_Model.getTime_out());
    }
    public void responseReserveVisitor(ResponseModel body) {
        try {
            if (body.getStatus() == 1) {
                customToast(body.getMessage());
                int vId = (int)Math.round(Double.parseDouble(gson.toJson(body.getResponse())));
                updated_visitor_Model.setId(Integer.toString(vId));
                setAlarmForVisitor(vId);
                Intent intent = new Intent(getApplication(), VisitorHistoryActivity.class);
                startActivity(intent);
                finish();
            } else {
                customToast(body.getMessage());
            }
        }catch (Exception e){
            customToast(e.getMessage());
            Log.e(EXCEPTION_TAG, e.getMessage());
        }
    }

    public void responseUpdateReserveVisitor(ResponseModel body) {
        try {
            if (body.getStatus() == 1) {
                customToast(body.getMessage());
                setAlarmForVisitor(Integer.parseInt(updated_visitor_Model.getId()));
                Intent intent = new Intent(getApplication(), VisitorHistoryActivity.class);
                startActivity(intent);
                finish();
            } else {
                customToast(body.getMessage());
            }
        }catch (Exception e){
            customToast(e.getMessage());
            Log.e(EXCEPTION_TAG, e.getMessage());
        }
    }

    public void responseIsReservedVisitor(ResponseModel body) {
        try {
            if (body.getStatus() != 1) {
                customToast(body.getMessage());
                finish();
            }
        }catch (Exception e){
            customToast(e.getMessage());
            Log.e(EXCEPTION_TAG, e.getMessage());
        }
    }
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (v.getId()) {
            case R.id.scrollView:
                hideKeyboardFrom(ReserveVisitorActivity.this, v);
                break;
        }
        return false;
    }

    private boolean isCarPlateCorrect() {
        String vehicle_back = et_vehicle_back.getText().toString();
        if (vehicle_back.length() != 4)
            return false;
        int i;
        for (i = 0 ; i < 4 ; i ++) {
            if ((i >= 0 && i <= 2) && !(vehicle_back.charAt(i) >= '0' && vehicle_back.charAt(i) <= '9'))
                return false;
            if (i == 3 && !((vehicle_back.charAt(i) >= 'A' && vehicle_back.charAt(i) <= 'Z') || (vehicle_back.charAt(i) >= 'a' && vehicle_back.charAt(i) <= 'z')))
                return false;
        }
        return true;
    }
}
