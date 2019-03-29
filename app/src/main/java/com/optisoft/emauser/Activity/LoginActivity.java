package com.optisoft.emauser.Activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.optisoft.emauser.HelperClasses.CommonPrefrence;
import com.optisoft.emauser.HelperClasses.PasswordValidator;
import com.optisoft.emauser.Model.ResponseModel;
import com.optisoft.emauser.Model.UserModel;
import com.optisoft.emauser.R;
import com.optisoft.emauser.Webservices.CallApi;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hbb20.CountryCodePicker;

import org.json.JSONArray;

import java.lang.reflect.Type;
import java.util.HashMap;

import static com.optisoft.emauser.HelperClasses.ApiConstant.EMAIL_PATTERN;
import static com.optisoft.emauser.HelperClasses.ApiConstant.EXCEPTION_TAG;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText password,phone;
    private CountryCodePicker country;
    private TextView login_tv, forgot_password, signup, agent_login, guard_login;
    private String countryCode, countryName, phoneSt, passwordSt;
    private PasswordValidator passwordValidator;
    private CallApi callApi = new CallApi();
    private Gson gson = new Gson();
    private UserModel userModel;
    private CommonPrefrence commonPrefrence;
    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        commonPrefrence = new CommonPrefrence();
        if (commonPrefrence.getUserLoginSharedPref(this) != null){
            gotoMain();
        }

        passwordValidator = new PasswordValidator();

        country = (CountryCodePicker) findViewById(R.id.country);
        phone = (EditText)findViewById(R.id.phone_et);
        password = (EditText)findViewById(R.id.password_et);
        login_tv = (TextView) findViewById(R.id.login_tv);
        agent_login = (TextView) findViewById(R.id.agent_login);
        guard_login = (TextView) findViewById(R.id.guard_login);
        forgot_password = (TextView) findViewById(R.id.forgot_password);
        signup = (TextView) findViewById(R.id.signup);

        login_tv.setOnClickListener(this);
        forgot_password.setOnClickListener(this);
        signup.setOnClickListener(this);
        agent_login.setOnClickListener(this);
        guard_login.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
             case R.id.login_tv:
                callLogin();
                break;
             case R.id.forgot_password:
                gotoForgot();
                break;
            case R.id.signup:
                callSignUp();
                break;
            case R.id.agent_login:
                callAgentLogin("agent");
                break;
            case R.id.guard_login:
                callAgentLogin("guard");
                break;
        }

    }

    private void callAgentLogin(final String type) {
        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.agent_login_layout);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        TextView title  = (TextView) dialog.findViewById(R.id.title);
        TextView btn  = (TextView) dialog.findViewById(R.id.btn);
        TextView close  = (TextView) dialog.findViewById(R.id.close);
        final EditText et_email  = (EditText) dialog.findViewById(R.id.et_email);
        final EditText et_password  = (EditText) dialog.findViewById(R.id.et_password);

        if (type.equals("guard")){
            title.setText("GUARD LOGIN");
        }

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = et_email.getText().toString();
                String pass  = et_password.getText().toString();

                if (email.isEmpty()){
                    et_email.setError("Enter your registered email id");
                    et_email.requestFocus();
                    return;
                }
                if (!email.matches(EMAIL_PATTERN)){
                    et_email.setError("Invalid email id");
                    et_email.requestFocus();
                }
                if (pass.isEmpty()){
                    et_password.setError("Enter password");
                    et_password.requestFocus();
                    return;
                }
                if (pass.length()< 6){
                    et_password.setError("Invalid password");
                    et_password.requestFocus();
                    return;
                }

                HashMap<String, String> map = new HashMap<>();
                map.put("email", email);
                map.put("password", pass);
                map.put("fb_token", commonPrefrence.getFbTockenPref(LoginActivity.this));
                if (type.equals("guard")){
                    callApi.requestGuardLogin(LoginActivity.this, map);
                }else {
                    callApi.requestAgentLogin(LoginActivity.this, map);
                }
            }
        });
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void callLogin() {
        if (commonPrefrence.getFbTockenPref(this) == null){
            customToast("There is some error....!\nPlease restart app");
            return;
        }
        countryName = country.getSelectedCountryName();
        countryCode = country.getSelectedCountryCode();
        phoneSt = phone.getText().toString();
        passwordSt = password.getText().toString();

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

        HashMap<String, String> map = new HashMap<>();
        map.put("country", countryName);
        map.put("country_code", countryCode);
        map.put("phone", phoneSt);
        map.put("password", passwordSt);
        map.put("fb_token", commonPrefrence.getFbTockenPref(this));

        callApi.requestLogin(LoginActivity.this,map);
    }

    private void callSignUp() {
        Intent intent = new Intent(getApplication(), SignUpActivity.class);
        startActivity(intent);
    }

    private void gotoForgot() {
        Intent intent = new Intent(LoginActivity.this,ForgotPasswordActivity.class);
        startActivity(intent);
    }

    private void gotoMain() {
        Intent intent = new Intent(LoginActivity.this,MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void customToast(String message) {
        Toast.makeText(getApplication(), message, Toast.LENGTH_LONG).show();
    }

    public void responseLogin(ResponseModel body) {
        try {
            customToast(body.getMessage());
            if (body.getStatus() == 1){
                JSONArray jsonArray = new JSONArray(gson.toJson(body.getResponse()));
                Type type=new TypeToken<UserModel>(){}.getType();
                userModel = gson.fromJson(jsonArray.get(0).toString(), type);
                if (userModel != null){
                    CommonPrefrence commonPrefrence = new CommonPrefrence();
                    commonPrefrence.setUserLoginSharedPref(LoginActivity.this, userModel);
                    gotoMain();
                }
            }
        }catch (Exception e){
            customToast(e.getMessage());
            Log.e(EXCEPTION_TAG, e.getMessage());
        }
    }

    public void responseAgentLogin(ResponseModel body) {
        try {
            customToast(body.getMessage());
            if (body.getStatus() == 1){
                dialog.dismiss();
                JSONArray jsonArray = new JSONArray(gson.toJson(body.getResponse()));
                Type type=new TypeToken<UserModel>(){}.getType();
                userModel = gson.fromJson(jsonArray.get(0).toString(), type);
                if (userModel != null){
                    CommonPrefrence commonPrefrence = new CommonPrefrence();
                    commonPrefrence.setUserLoginSharedPref(LoginActivity.this, userModel);
                    gotoMain();
                }
            }
        }catch (Exception e){
            customToast(e.getMessage());
            Log.e(EXCEPTION_TAG, e.getMessage());
        }
    }
}
