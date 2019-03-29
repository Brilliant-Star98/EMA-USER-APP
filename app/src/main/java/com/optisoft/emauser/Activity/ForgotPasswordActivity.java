package com.optisoft.emauser.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.optisoft.emauser.Model.ResponseModel;
import com.optisoft.emauser.Model.UserModel;
import com.optisoft.emauser.R;
import com.optisoft.emauser.Webservices.CallApi;
import com.google.gson.Gson;
import com.hbb20.CountryCodePicker;

import java.util.HashMap;

import static com.optisoft.emauser.HelperClasses.ApiConstant.EXCEPTION_TAG;

public class  ForgotPasswordActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView img_back;
    EditText mobileeditText;
    CountryCodePicker countryCodePicker;
    TextView forgotpasswordsubmit,btn;
    private Gson gson = new Gson();

    String mobSt,countryCodePickerSt;
    private UserModel userModel = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        mobileeditText=(EditText)findViewById(R.id.phone_et);
        countryCodePicker=(CountryCodePicker)findViewById(R.id.country);
        forgotpasswordsubmit=(TextView)findViewById(R.id.login_tv);
        img_back = (ImageView) findViewById(R.id.img_back);
        btn=(TextView)findViewById(R.id.login_tv);
        img_back.setOnClickListener(this);


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                load(0);
            }
        });

    }
    private void load(int index){

        mobSt=mobileeditText.getText().toString();
        countryCodePickerSt=countryCodePicker.getSelectedCountryCode();
        if (mobSt.isEmpty()) {
            mobileeditText.setError("Enter Your Number");
            mobileeditText.requestFocus();
            return;
        }
        HashMap<String, String> map = new HashMap<>();
        map.put("country code",countryCodePickerSt);
        map.put("phone",mobSt);


        CallApi callApi=new CallApi();
        callApi.requestForgotPassword(ForgotPasswordActivity.this,map);
    }

    public void customToast(String s) {
        Toast.makeText(getApplication(), s, Toast.LENGTH_LONG).show();
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
                case R.id.img_back:
                    finish();
                    break;


        }
    }


    public void requestForgotPassword(ResponseModel model){
        try {
            customToast(model.getMessage());
            if(model.getStatus()==1)
            {
                finish();
            }

        }catch (Exception e){
            Log.e(EXCEPTION_TAG, e.getMessage());
            customToast(e.getMessage());
        }

    }



}
