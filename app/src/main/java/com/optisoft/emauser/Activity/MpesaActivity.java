package com.optisoft.emauser.Activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.GravityEnum;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bdhobare.mpesa.Mode;
import com.bdhobare.mpesa.Mpesa;
import com.bdhobare.mpesa.interfaces.AuthListener;
import com.bdhobare.mpesa.interfaces.MpesaListener;
import com.bdhobare.mpesa.models.STKPush;
import com.bdhobare.mpesa.utils.Pair;
import com.optisoft.emauser.Adapter.CurrentBillAdapter;
import com.optisoft.emauser.HelperClasses.CommonPrefrence;
import com.optisoft.emauser.Model.BillModel;
import com.optisoft.emauser.Model.ResponseModel;
import com.optisoft.emauser.Model.UserModel;
import com.optisoft.emauser.R;
import com.optisoft.emauser.Webservices.CallApi;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

import static com.optisoft.emauser.HelperClasses.ApiConstant.CURRENT_TAG;
import static com.optisoft.emauser.HelperClasses.ApiConstant.EXCEPTION_TAG;
import static com.optisoft.emauser.HelperClasses.ApiConstant.INTENT_TAG;

public class MpesaActivity extends AppCompatActivity implements AuthListener,MpesaListener, View.OnClickListener {
    private static final String CONSUMER_KEY    = "jeE9KRNLSUeE8V1JnqmgEWpRCbrT0oAR";
    private static final String CONSUMER_SECRET = "9Y1Fs33SYIQjAHvq";

    public static final String BUSINESS_SHORT_CODE = "174379";
    public static final String PASSKEY = "bfb279f9aa9bdbcf158e97dd71a467cd2e0c893059b10f78e6b72ada1ed2c919";
    public static final String CALLBACK_URL = "YOUR_CALLBACK_URL";


    public static final String  NOTIFICATION = "PushNotification";
    public static final String SHARED_PREFERENCES = "com.bdhobare.mpesa_android_sdk";

    private CommonPrefrence commonPrefrence = new CommonPrefrence();
    private UserModel userModel = null;
    TextView pay;
    private RecyclerView recyclerView;
    ProgressDialog dialog;
    EditText phone;
    EditText amount;
    private CurrentBillAdapter adapter;

    private ArrayList<BillModel> arraList;
    private ImageView drawer;
    private String tag = CURRENT_TAG;

    private BillModel billModel = null;

    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private Gson gson = new Gson();
    private Dialog dialog1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mpesa);
        Mpesa.with(this, CONSUMER_KEY, CONSUMER_SECRET);
        userModel=commonPrefrence.getUserLoginSharedPref(this);
        Intent intent = getIntent();

        if (intent.hasExtra(INTENT_TAG))
        {
            String temStr = intent.getStringExtra(INTENT_TAG);
            Type type=new TypeToken<BillModel>(){}.getType();
            billModel = gson.fromJson(temStr, type);
        }




        arraList = new ArrayList<>();
        pay = (TextView)findViewById(R.id.pay);
        phone = (EditText)findViewById(R.id.phone);
        amount = (EditText)findViewById(R.id.amount);
        drawer=(ImageView)findViewById(R.id.drawer) ;
        drawer.setOnClickListener(this);
        if (billModel !=null){
            amount.setText(billModel.getBill_amount());
            amount.setEnabled(false);
        }


        Mpesa.with(this, CONSUMER_KEY, CONSUMER_SECRET, Mode.SANDBOX);
        dialog = new ProgressDialog(this);
        dialog.setMessage("Processing");
        dialog.setIndeterminate(true);

        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String p = phone.getText().toString();
                int a = Integer.valueOf(amount.getText().toString());
                if (p.isEmpty()){
                    phone.setError("Enter phone.");
                    return;
                }
                pay(p, a);
            }
        });

        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(NOTIFICATION)) {
                    String title = intent.getStringExtra("title");
                    String message = intent.getStringExtra("message");
                    int code = intent.getIntExtra("code", 0);
                    showDialog(title, message, code);

                }
            }
        };

    }

    @Override
    public void onAuthError(Pair<Integer, String> result) {
        Log.e("Error", result.message);
    }

    @Override
    public void onAuthSuccess() {

        //TODO make payment
        pay.setEnabled(true);
    }
    private void pay(String phone, int amount){
        dialog.show();
        STKPush.Builder builder = new STKPush.Builder(BUSINESS_SHORT_CODE, PASSKEY, amount,BUSINESS_SHORT_CODE, phone);

        String token = commonPrefrence.getFbTockenPref(this);

        builder.setFirebaseRegID(token);
        STKPush push = builder.build();



        Mpesa.getInstance().pay(this, push);

    }
    private void showDialog(String title, String message,int code){
        MaterialDialog dialog = new MaterialDialog.Builder(this)
                .title(title)
                .titleGravity(GravityEnum.CENTER)
                .customView(R.layout.success_dialog, true)
                .positiveText("OK")
                .cancelable(false)
                .widgetColorRes(R.color.colorPrimary)
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        super.onPositive(dialog);
                        dialog.dismiss();
                        finish();
                    }
                })
                .build();
        View view=dialog.getCustomView();
        TextView messageText = (TextView)view.findViewById(R.id.message);
        ImageView imageView = (ImageView)view.findViewById(R.id.success);
        if (code != 0){
            imageView.setVisibility(View.GONE);
        }
        messageText.setText(message);
        dialog.show();
    }

    @Override
    public void onMpesaError(Pair<Integer, String> result) {
        dialog.hide();
        Toast.makeText(this, result.message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onMpesaSuccess(String MerchantRequestID, String CheckoutRequestID, String CustomerMessage) {
        dialog.hide();
        Toast.makeText(this, CustomerMessage, Toast.LENGTH_SHORT).show();

        HashMap<String, String> map = new HashMap<>();

        map.put("user_id",userModel.getUserId());
        map.put("bill_id",billModel.getId());
        map.put("amount",billModel.getBill_amount());
        map.put("merchant_id",MerchantRequestID);
        map.put("transaction_id",CheckoutRequestID);


        CallApi callApi=new CallApi();
        callApi.mPesaTransaction(MpesaActivity.this,map);

    }
    @Override
    protected void onResume() {
        super.onResume();

        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(NOTIFICATION));

    }
    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.drawer:
                finish();
                break;
        }
    }

    public void mPesaTransaction(ResponseModel model){
        try {
            customToast(model.getMessage());
            if(model.getStatus()==1)
            {
                showLoginAlertDialog(model.getMessage());
            }



        }catch (Exception e){
            Log.e(EXCEPTION_TAG, e.getMessage());
            customToast(e.getMessage());
        }

    }

    private void showLoginAlertDialog(String msg) {
        dialog1 = new Dialog(this);
        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog1.setCancelable(false);
        dialog1.setContentView(R.layout.common_dialog_layout);
        dialog1.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dialog1.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        TextView btn  = (TextView) dialog1.findViewById(R.id.btn);
        TextView title  = (TextView) dialog1.findViewById(R.id.title);
        TextView text  = (TextView) dialog1.findViewById(R.id.msg);
        ImageView img  = (ImageView) dialog1.findViewById(R.id.img);
        title.setText("Success");
        btn.setText("Close");
        img.setImageDrawable(getResources().getDrawable(R.drawable.checked));
        text.setText(msg);
        //  img.setImageDrawable(getResources().getDrawable(R.drawable.success));
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog1.dismiss();
                Intent intent=new Intent(MpesaActivity.this,MainActivity.class);
                startActivity(intent);
                finishAffinity();
            }
        });
        dialog1.show();
    }

    public void customToast(String s) {
        Toast.makeText(getApplication(), s, Toast.LENGTH_LONG).show();
    }
}
