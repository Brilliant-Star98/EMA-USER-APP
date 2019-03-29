package com.optisoft.emauser.Activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.optisoft.emauser.Adapter.MyDuesAdapter;
import com.optisoft.emauser.Model.PaymentModel;
import com.optisoft.emauser.R;

import java.util.ArrayList;

public class MyDuesActivity extends AppCompatActivity implements View.OnClickListener {

    private ListView listView;
    private ImageView img_back;
    MyDuesAdapter myDuesAdapter;
    private ArrayList<PaymentModel> list = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_dues);

        listView = (ListView) findViewById(R.id.listview);
        img_back    = (ImageView) findViewById(R.id.img_back);

        list = new ArrayList<PaymentModel>();

        for(int i = 1; i< 15; i++){
            PaymentModel paymentModel = new PaymentModel();
           // paymentModel.setBill_number("BILL - "+i+" 434347");
            //paymentModel.setSeries(""+i+"/4 -1997");
            //paymentModel.setPrice("$ "+i+"9");
            list.add(paymentModel);
        }

        myDuesAdapter = new MyDuesAdapter(this,list);
        listView.setAdapter(myDuesAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                showBookingDialog();
            }
        });

        img_back.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
        }
    }

    public void showBookingDialog() {
        final Dialog dialog = new Dialog(MyDuesActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_pay_layout);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

        ImageView close = (ImageView) dialog.findViewById(R.id.close_img);
        TextView pay_now = (TextView) dialog.findViewById(R.id.pay_now);
        //LinearLayout ly_main_card = (LinearLayout)dialog. findViewById(R.id.ly_main_card);

       // CardView cardView = (CardView) dialog. findViewById(R.id.cardview);

       // ly_main_card.setBackgroundColor(getResources().getColor(R.color.transparent));
       // cardView .setBackgroundColor(getResources().getColor(R.color.transparent));

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        pay_now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyDuesActivity.this,ThankYouActivity.class);
                startActivity(intent);
            }
        });

        dialog.show();
    }


}
