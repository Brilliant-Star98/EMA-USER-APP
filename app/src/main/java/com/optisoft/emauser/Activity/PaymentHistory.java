package com.optisoft.emauser.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.optisoft.emauser.Adapter.PaymentHistoryAdapter;
import com.optisoft.emauser.Model.PaymentModel;
import com.optisoft.emauser.R;

import java.util.ArrayList;

public class PaymentHistory extends AppCompatActivity implements View.OnClickListener {

    private ListView listView;
    private ImageView img_back;
    PaymentHistoryAdapter paymentHistoryAdapter;
    private ArrayList<PaymentModel> list = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_history);

        listView = (ListView) findViewById(R.id.listview);
        img_back    = (ImageView) findViewById(R.id.img_back);

        list = new ArrayList<PaymentModel>();

        for(int i = 1; i< 15; i++){
            PaymentModel paymentModel = new PaymentModel();
           /* paymentModel.setBill_number("BILL - "+i+" 434347");
            paymentModel.setSeries(""+i+"/4 -1997");
            paymentModel.setPrice("$ "+i+"9");*/
            list.add(paymentModel);
        }

        paymentHistoryAdapter = new PaymentHistoryAdapter(this,list);
        listView.setAdapter(paymentHistoryAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(PaymentHistory.this,"payment histiory",Toast.LENGTH_SHORT).show();
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
}
