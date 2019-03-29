package com.optisoft.emauser.Activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.optisoft.emauser.Adapter.ContactsAdapter;
import com.optisoft.emauser.HelperClasses.CommonPrefrence;
import com.optisoft.emauser.HelperClasses.DatePickerFragment;
import com.optisoft.emauser.Model.ResponseModel;
import com.optisoft.emauser.Model.UserModel;
import com.optisoft.emauser.Model.VisitorModel;
import com.optisoft.emauser.R;
import com.optisoft.emauser.Webservices.CallApi;

import org.json.JSONArray;
import org.json.JSONException;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;

import static com.optisoft.emauser.HelperClasses.ApiConstant.INTENT_TAG;
import static com.optisoft.emauser.HelperClasses.ApiConstant.convert_time;
import static com.optisoft.emauser.HelperClasses.ApiConstant.hideKeyboardFrom;

public class VisitorHistoryActivity extends AppCompatActivity implements View.OnClickListener, View.OnTouchListener, ContactsAdapter.ContactsAdapterListener {

    private UserModel userModel = null;
    private CommonPrefrence commonPrefrence = new CommonPrefrence();
    private EditText search, date;
    private RecyclerView recyclerView;
    private Gson gson = new Gson();
    private CallApi callApi = new CallApi();
    private ArrayList<VisitorModel> mList;
    private LinearLayout nodata;
    private ContactsAdapter adapter;
    private String currentDate = "";
    private ImageView drawerImage;
    private TextView submit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visitor_history);

        userModel = commonPrefrence.getUserLoginSharedPref(this);

        drawerImage = (ImageView) findViewById(R.id.img_back_2);
        submit = (TextView) findViewById(R.id.submit_reserve_new_visitor);
        search = (EditText) findViewById(R.id.search_visitor_name);
        date = (EditText) findViewById(R.id.search_visitor_date);
        nodata = (LinearLayout) findViewById(R.id.nodata);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView_visitor_list);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        mList = new ArrayList<>();

        drawerImage.setOnClickListener(this);
        date.setOnClickListener(this);
        submit.setOnClickListener(this);

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() != 0) {
                    adapter.getFilter().filter(search.getText().toString());
                }
                else
                    adapter.getFilter().filter("");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        Calendar calendar = Calendar.getInstance();
        currentDate = convert_time(calendar.get(Calendar.DAY_OF_MONTH)) + "-" + convert_time(calendar.get(Calendar.MONTH) + 1) + "-" + calendar.get(Calendar.YEAR);
        date.setText(currentDate);
        loadData();

        recyclerView.setOnClickListener(this);
        nodata.setOnTouchListener(this);
    }

    public void loadData() {
        callApi.requestLoadReservedVisitor(this, userModel.getUserId(), currentDate);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.img_back_2:
                finish();
                break;
            case R.id.search_visitor_date:
                showDatePicker();
                break;
            case R.id.submit_reserve_new_visitor:
                Intent intent = new Intent(this, ReserveVisitorActivity.class);
                startActivity(intent);
                finish();
                break;
            default:
                return;
        }
    }

    private void showDatePicker() {
        DatePickerFragment dpf = new DatePickerFragment();
        Calendar calender = Calendar.getInstance();
        Bundle args = new Bundle();
        args.putInt("year", calender.get(Calendar.YEAR));
        args.putInt("month", calender.get(Calendar.MONTH));
        args.putInt("day", calender.get(Calendar.DAY_OF_MONTH));
        args.putBoolean("isDisableNext", false);
        dpf.setArguments(args);
        dpf.setCallBack(ondate);
        dpf.show(getSupportFragmentManager(), "Date Picker");
    }

    DatePickerDialog.OnDateSetListener ondate = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            String dateStr =  convert_time(dayOfMonth)+"-"+convert_time(monthOfYear+1)+"-"+year;
            date.setText(dateStr);
            currentDate = dateStr;
            loadData();
        }
    };

    public void responseLoadReservedVisitor(ResponseModel body) {
        try {
            if (mList != null){
                mList.clear();
            }
            if (body.getStatus() == 1) {
                JSONArray jsonArray = new JSONArray(gson.toJson(body.getResponse()));
                Type type = new TypeToken<ArrayList<VisitorModel>>(){}.getType();
                mList = gson.fromJson(jsonArray.toString(), type);
                adapter = new ContactsAdapter(this, mList, this);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();

                if (mList.size() == 0){
                    nodata.setVisibility(View.VISIBLE);
                }else{
                    nodata.setVisibility(View.GONE);
                }
            }
            else{
                nodata.setVisibility(View.VISIBLE);
                customToast(body.getMessage());
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void responseCancelReservation(ResponseModel body) {
            if (mList != null){
                mList.clear();
            }
            if (body.getStatus() == 1) {
                customToast(body.getMessage());
                loadData();
            }
            else{
                customToast(body.getMessage());
            }
    }

    public void customToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (v.getId()){
            case R.id.recyclerView_visitor_list:
                hideKeyboardFrom(VisitorHistoryActivity.this, v);
                break;
            case R.id.nodata:
                hideKeyboardFrom(VisitorHistoryActivity.this, v);
                break;
        }
        return false;
    }

    @Override
    public void onContactSelected(VisitorModel contact) {
        Intent intent = new Intent(getApplication(), ReserveVisitorActivity.class);
        intent.putExtra(INTENT_TAG, gson.toJson(contact));
        startActivity(intent);
        finish();
    }

    @Override
    public void onCancelSelected(VisitorModel contact) {
        callApi.requestCancelReservation(this, contact.getId());
    }
}
