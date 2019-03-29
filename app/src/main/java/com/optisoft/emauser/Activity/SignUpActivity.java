package com.optisoft.emauser.Activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.optisoft.emauser.HelperClasses.PasswordValidator;
import com.optisoft.emauser.Model.ResponseModel;
import com.optisoft.emauser.Model.UserModel;
import com.optisoft.emauser.R;
import com.optisoft.emauser.Webservices.CallApi;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hbb20.CountryCodePicker;

import org.json.JSONArray;

import java.lang.reflect.Type;
import java.util.HashMap;

import static com.optisoft.emauser.HelperClasses.ApiConstant.EMAIL_PATTERN;
import static com.optisoft.emauser.HelperClasses.ApiConstant.EXCEPTION_TAG;
import static com.optisoft.emauser.HelperClasses.ApiConstant.PLACE_PICKER_REQUEST;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView login, signup, verify, agent_name, select_location, selected_location;
    private LinearLayout agent_ly;
    private EditText fname, lname, email, phone, password, cnf_password, agent_code;
    private String fnameSt, lnameSt, emailSt, phoneSt, passwordSt, cnf_passwordSt, agent_codeSt, countryName, countryCode, short_code;
    private CountryCodePicker country;
    private boolean isAgentVerified = false;
    private UserModel userModel = null;
    private Gson gson =new Gson();
    private PasswordValidator passwordValidator;
    private CallApi callApi = new CallApi();
    private Dialog dialog;
    private String lat, lng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        passwordValidator = new PasswordValidator();

        country = (CountryCodePicker) findViewById(R.id.country);
        fname = (EditText) findViewById(R.id.fname);
        lname = (EditText) findViewById(R.id.lname);
        email = (EditText) findViewById(R.id.email);
        phone = (EditText) findViewById(R.id.phone);
        password =(EditText) findViewById(R.id.password);
        cnf_password = (EditText) findViewById(R.id.cnf_password);
        agent_code = (EditText) findViewById(R.id.agent_code);
        agent_name = (TextView) findViewById(R.id.agent_name);
        select_location   = (TextView) findViewById(R.id.select_location);
        selected_location = (TextView) findViewById(R.id.selected_location);
        agent_ly = (LinearLayout) findViewById(R.id.agent_ly);


        login = (TextView) findViewById(R.id.login);
        signup = (TextView) findViewById(R.id.signup);
        verify = (TextView) findViewById(R.id.verify);
        login.setOnClickListener(this);
        signup.setOnClickListener(this);
        verify.setOnClickListener(this);
        select_location.setOnClickListener(this);

        setAgentCodeKeyChangeListner();
    }

    private void setAgentCodeKeyChangeListner() {
        agent_code.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                isAgentVerified = false;
                resetAgentCodeActive();
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                isAgentVerified = false;
                resetAgentCodeActive();
            }

            @Override
            public void afterTextChanged(Editable editable) {
                isAgentVerified = false;
                resetAgentCodeActive();
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.login:
                callLogin();
                break;

            case R.id.signup:
                callSignup();
                break;
            case R.id.verify:
                callVerify();
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

    private void callVerify() {
        phoneSt = phone.getText().toString();
        passwordSt = password.getText().toString();
        cnf_passwordSt = cnf_password.getText().toString();
        agent_codeSt = agent_code.getText().toString();
        fnameSt = fname.getText().toString();
        lnameSt = lname.getText().toString();
        emailSt = email.getText().toString();

        if (fnameSt.isEmpty()){
            fname.setError("Enter First Name");
            fname.requestFocus();
            return;
        }
        if (lnameSt.isEmpty()){
            lname.setError("Enter Last Name");
            lname.requestFocus();
            return;
        }
        if (emailSt.isEmpty()){
            email.setError("Enter Email Id");
            email.requestFocus();
            return;
        }
        if (!emailSt.matches(EMAIL_PATTERN)){
            email.setError("Invalid Email Id");
            email.requestFocus();
            return;
        }
        if (phoneSt.isEmpty()){
            phone.setError("Enter Phone Number");
            phone.requestFocus();
            return;
        }
        if (passwordSt.isEmpty()){
            password.setError("Enter Password");
            password.requestFocus();
            return;
        }
        if (passwordSt.length() < 6){
            password.setError("Invalid Password");
            password.requestFocus();
            return;
        }
        if (!cnf_passwordSt.equals(passwordSt)){
            cnf_password.setError("Enter Confirm Password");
            cnf_password.requestFocus();
            return;
        }

        if (lat.isEmpty()){
            customToast("Select your location");
            return;
        }
        if (agent_codeSt.isEmpty()){
            agent_code.setError("Enter Agent Code");
            agent_code.requestFocus();
            return;
        }

        if (!agent_codeSt.startsWith("EMA") || agent_codeSt.length() < 4){
            agent_code.setError("Invalid Agent Code");
            agent_code.requestFocus();
            return;
        }

        callApi.requestVerifyAgentCode(SignUpActivity.this, agent_codeSt.substring(3));
    }

    private void callSignup() {
        countryName = country.getSelectedCountryName();
        countryCode = country.getSelectedCountryCode();
        short_code  = country.getSelectedCountryNameCode();
        phoneSt = phone.getText().toString();
        passwordSt = password.getText().toString();
        cnf_passwordSt = cnf_password.getText().toString();
        agent_codeSt = agent_code.getText().toString();
        fnameSt = fname.getText().toString();
        lnameSt = lname.getText().toString();
        emailSt = email.getText().toString();

        if (fnameSt.isEmpty()){
            fname.setError("Enter First Name");
            fname.requestFocus();
            return;
        }
        if (lnameSt.isEmpty()){
            lname.setError("Enter Last Name");
            lname.requestFocus();
            return;
        }
        if (emailSt.isEmpty()){
            email.setError("Enter Email Id");
            email.requestFocus();
            return;
        }
        if (!emailSt.matches(EMAIL_PATTERN)){
            email.setError("Invalid Email Id");
            email.requestFocus();
            return;
        }
        if (phoneSt.isEmpty()){
            phone.setError("Enter Phone Number");
            phone.requestFocus();
            return;
        }
        if (passwordSt.isEmpty()){
            password.setError("Enter Password");
            password.requestFocus();
            return;
        }
        if (passwordSt.length() < 6){
            password.setError("Invalid Password");
            password.requestFocus();
            return;
        }
        if (!cnf_passwordSt.equals(passwordSt)){
            cnf_password.setError("Enter Confirm Password");
            cnf_password.requestFocus();
            return;
        }
        if (lat.isEmpty()){
            customToast("Select your location");
            return;
        }

        if (isAgentVerified && userModel != null){

            HashMap<String, String> map = new HashMap<>();
            map.put("country", countryName);
            map.put("country_code", countryCode);
            map.put("short_code", short_code);
            map.put("fname", fnameSt);
            map.put("lname", lnameSt);
            map.put("email", emailSt);
            map.put("phone", phoneSt);
            map.put("password", passwordSt);
            map.put("latitude", lat);
            map.put("longitude", lng);
            map.put("agent_code", agent_codeSt.substring(3));

            callApi.requestSignUpUser(SignUpActivity.this,map);

        }else {
            agent_code.setError("Verify Agent Code");
            agent_code.requestFocus();
            return;
        }
    }

    private void callLogin() {
        Intent intent = new Intent(getApplication(), LoginActivity.class);
        startActivity(intent);
        finishAffinity();
    }

    public void responseSignupUser(ResponseModel body) {
        try {
            if (body.getStatus() == 1){
                customToast(body.getMessage());
                showLoginAlertDialog(body.getMessage());
            }else {
                customToast(body.getMessage());
            }
        }catch (Exception e){
            customToast(e.getMessage());
            Log.e(EXCEPTION_TAG, e.getMessage());
        }
    }

    private void showLoginAlertDialog(String msg) {
        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.common_dialog_layout);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        TextView btn  = (TextView) dialog.findViewById(R.id.btn);
        TextView title  = (TextView) dialog.findViewById(R.id.title);
        TextView text  = (TextView) dialog.findViewById(R.id.msg);
        ImageView img  = (ImageView) dialog.findViewById(R.id.img);
        title.setText("Success");
        btn.setText("Close");
        text.setText(msg);
        //  img.setImageDrawable(getResources().getDrawable(R.drawable.success));
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent intent = new Intent(getApplication(), LoginActivity.class);
                startActivity(intent);
                finishAffinity();
            }
        });
        dialog.show();
    }


    public void customToast(String message) {
        Toast.makeText(getApplication(), message, Toast.LENGTH_LONG).show();
    }

    public void responseVerifyAgentCode(ResponseModel body) {
        try {
            if (body.getStatus() == 1){
                JSONArray jsonArray = new JSONArray(gson.toJson(body.getResponse()));
                Type type=new TypeToken<UserModel>(){}.getType();
                userModel = gson.fromJson(jsonArray.get(0).toString(), type);
                if (userModel != null){
                    isAgentVerified = true;
                    resetAgentCodeActive();
                }
            }else {
                customToast(body.getMessage());
                isAgentVerified = false;
                resetAgentCodeActive();
            }
        }catch (Exception e){
            customToast(e.getMessage());
            Log.e(EXCEPTION_TAG, e.getMessage());
        }
    }

    public void resetAgentCodeActive(){
        if (isAgentVerified){
            verify.setBackgroundColor(getResources().getColor(R.color.green));
            verify.setEnabled(false);
            verify.setText("verified");
            agent_ly.setVisibility(View.VISIBLE);
            agent_name.setText(userModel.getFname() + " " + userModel.getLname());
        }else {
            userModel = null;
            agent_ly.setVisibility(View.GONE);
            verify.setBackgroundColor(getResources().getColor(R.color.colorAccent));
            verify.setEnabled(true);
            verify.setText("VERIFY");
        }

    }
}
