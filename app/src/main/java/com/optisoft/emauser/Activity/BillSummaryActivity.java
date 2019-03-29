package com.optisoft.emauser.Activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.optisoft.emauser.HelperClasses.CommonPrefrence;
import com.optisoft.emauser.Model.BillModel;
import com.optisoft.emauser.Model.UserModel;
import com.optisoft.emauser.R;
import com.optisoft.emauser.Webservices.CallApi;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

import static com.optisoft.emauser.HelperClasses.ApiConstant.INTENT_TAG;

public class BillSummaryActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String TAG = "TAG";
    private ImageView drawer;
    private TextView title;
    TextView billtype,name,address,email,mobile,generateddate,duedate,billamount,billunits,billrate,latefee, total_amount;
    TextView paynow,paid,pdf;
    TextView billtypeSt,nameSt,addressSt,emailSt,mobileSt,generateddateSt,duedateSt,billamountSt,billunitsSt,billrateSt,latefeeSt,paynowSt,paidSt;
    private CommonPrefrence commonPrefrence = new CommonPrefrence();
    private BillModel billModel = null;
    ImageView ema_image;
    private Gson gson = new Gson();
    private UserModel userModel = null;
    boolean boolean_permission;
    public static int REQUEST_PERMISSIONS = 1;
    Bitmap bitmap;
    private boolean boolean_save;
    LinearLayout pdflayout;
    CallApi callApi=new CallApi();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_summary);

        userModel=commonPrefrence.getUserLoginSharedPref(this);
        drawer = (ImageView)findViewById(R.id.drawer);
        title = (TextView)findViewById(R.id.title);
        title.setText("Bill Summary");


        Intent intent = getIntent();

        if (intent.hasExtra(INTENT_TAG))
        {
            String temStr = intent.getStringExtra(INTENT_TAG);
            Type type=new TypeToken<BillModel>(){}.getType();
            billModel = gson.fromJson(temStr, type);
        }



        ema_image=(ImageView)findViewById(R.id.dollar);
        billtype=(TextView)findViewById(R.id.bill_type);
        name=(TextView)findViewById(R.id.name);
        address=(TextView)findViewById(R.id.address);
        email=(TextView)findViewById(R.id.mail);
        mobile=(TextView)findViewById(R.id.phone);
        generateddate=(TextView)findViewById(R.id.generate_date);
        duedate=(TextView)findViewById(R.id.due_date);
        billamount=(TextView)findViewById(R.id.bill_amount);
        billrate=(TextView)findViewById(R.id.bill_rate);
         billunits=(TextView)findViewById(R.id.bill_unit);
        latefee=(TextView)findViewById(R.id.late_fee);
        total_amount=(TextView)findViewById(R.id.total_amount);
        paynow=(TextView)findViewById(R.id.paynowbtn);
        paid=(TextView)findViewById(R.id.paidbtn);
        pdf=(TextView)findViewById(R.id.pdf_btn);
       pdflayout=(LinearLayout)findViewById(R.id.pdf_layout);


        fn_permission();
        drawer.setOnClickListener(this);
        paid.setOnClickListener(this);
        pdf.setOnClickListener(this);

           paynow.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                   Intent intent=new Intent(BillSummaryActivity.this, MpesaActivity.class);
                   intent.putExtra(INTENT_TAG, gson.toJson(billModel));
                   startActivity(intent);
               }
           });

        setBillView();
    }


    private void setBillView() {
        if (billModel == null) {
            return;
        }
        try {
            if (billModel.getStatus().equals("0")){
                paynow.setVisibility(View.VISIBLE);
                paid.setVisibility(View.GONE);
            }else {
                paid.setVisibility(View.VISIBLE);
                paynow.setVisibility(View.GONE);
            }
            billtype.setText(billModel.getBill_title() + " Bill");
            name.setText(userModel.getFname() + " " + userModel.getLname());
            address.setText(userModel.getFull_address());
            email.setText(userModel.getEmail());
            mobile.setText("+" + userModel.getCountry_code() + "-" + userModel.getMobile());
            generateddate.setText("Generated Date : " +billModel.getBill_generated_date());
            duedate.setText("Due Date : " + billModel.getDue_date());
            billamount.setText("$" +billModel.getBill_amount());
            billrate.setText(billModel.getRate());
            latefee.setText("$" +billModel.getLate_fee());

            total_amount.setText("$"+billModel.getTotal_amount());
            billunits.setText(billModel.getUnit());

                  } catch (Exception e) {
            Log.e("EXCEPTION", e.getMessage());
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.drawer:
                finish();
                break;

            case R.id.pdf_btn:
                if (boolean_permission) {
                    callApi.requestDownloadBill(BillSummaryActivity.this, billModel.getId(), userModel.getUserId());
                }else {
                    fn_permission();
                }
                break;
            case R.id.paidbtn:
                if (boolean_permission) {
                    callApi.requestDownloadInvoice(BillSummaryActivity.this,billModel.getTxn_id(), billModel.getId(), userModel.getUserId());
                }else {
                    fn_permission();
                }
                break;
        }
    }

    private void fn_permission() {
        if ((ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)||
                (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)) {

            if ((ActivityCompat.shouldShowRequestPermissionRationale(BillSummaryActivity.this, android.Manifest.permission.READ_EXTERNAL_STORAGE))) {
            } else {
                ActivityCompat.requestPermissions(BillSummaryActivity.this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_PERMISSIONS);

            }

            if ((ActivityCompat.shouldShowRequestPermissionRationale(BillSummaryActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE))) {
            } else {
                ActivityCompat.requestPermissions(BillSummaryActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        REQUEST_PERMISSIONS);

            }
        } else {
            boolean_permission = true;


        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSIONS) {

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                boolean_permission = true;
            } else {
                Toast.makeText(getApplicationContext(), "Please allow the permission", Toast.LENGTH_LONG).show();

            }
        }
    }


    public void customToast(String message) {
        Toast.makeText(getApplication(), message, Toast.LENGTH_LONG).show();
    }
}
