package com.optisoft.emauser.Activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
import android.widget.Toast;

import com.optisoft.emauser.Adapter.ContactsAdapter;
import com.optisoft.emauser.HelperClasses.CommonPrefrence;
import com.optisoft.emauser.HelperClasses.DatePickerFragment;
import com.optisoft.emauser.Model.ResponseModel;
import com.optisoft.emauser.Model.UserModel;
import com.optisoft.emauser.Model.VisitorModel;
import com.optisoft.emauser.R;
import com.optisoft.emauser.Webservices.CallApi;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;

import static com.optisoft.emauser.HelperClasses.ApiConstant.INTENT_TAG;
import static com.optisoft.emauser.HelperClasses.ApiConstant.convert_time;
import static com.optisoft.emauser.HelperClasses.ApiConstant.hideKeyboardFrom;

public class ListVisitorActivity extends AppCompatActivity implements View.OnClickListener, ContactsAdapter.ContactsAdapterListener, View.OnTouchListener {

    private UserModel userModel = null;
    private CommonPrefrence commonPrefrence = new CommonPrefrence();
    private ImageView drawer;
    private EditText search, date;
    private RecyclerView recyclerView;
    private Gson gson = new Gson();
    private CallApi callApi = new CallApi();
    private ArrayList<VisitorModel> mList;
    private LinearLayout nodata;
    private ContactsAdapter adapter;
    private String currentDate = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_visitor);

        userModel = commonPrefrence.getUserLoginSharedPref(this);

        search       = (EditText) findViewById(R.id.search);
        date         = (EditText) findViewById(R.id.date);
        nodata       = (LinearLayout)findViewById(R.id.nodata);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        mList        = new ArrayList<>();


        drawer     = (ImageView) findViewById(R.id.drawer);
        drawer.setOnClickListener(this);
        date.setOnClickListener(this);

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {}
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() != 0){
                    adapter.getFilter().filter(search.getText().toString());
                }
            }
        });



        Calendar calender = Calendar.getInstance();
        currentDate = convert_time(calender.get(Calendar.DAY_OF_MONTH))+"-"+convert_time(calender.get(Calendar.MONTH) + 1)+"-"+calender.get(Calendar.YEAR);
        date.setText(currentDate);
        loadData();

        recyclerView.setOnTouchListener(this);
        nodata.setOnTouchListener(this);
    }

    private void loadData() {
        callApi.requestLoadVisitor(this, userModel.getUserId(),currentDate);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.drawer:
                finish();
                break;
            case R.id.date:
                showDatePicker();
                break;

            default:
                return;
        }
    }

    private void showDatePicker() {
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
            currentDate = dateStr;
            loadData();
        }
    };


    public void customToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    public void responseLoadVisitor(ResponseModel body) {
        try {
            if (mList != null){
                mList.clear();
            }
            if (body.getStatus() == 1){
                JSONArray jsonArray = new JSONArray(gson.toJson(body.getResponse()));
                Type type=new TypeToken<ArrayList<VisitorModel>>(){}.getType();
                mList = gson.fromJson(jsonArray.toString(), type);
                adapter = new ContactsAdapter(this, mList, this);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();

                if (mList.size() == 0){
                    nodata.setVisibility(View.VISIBLE);
                }else {
                    nodata.setVisibility(View.GONE);
                }

            }else {
            nodata.setVisibility(View.VISIBLE);
                customToast(body.getMessage());
            }
        }catch (Exception e){
            e.getStackTrace();
            nodata.setVisibility(View.VISIBLE);
            customToast(e.getMessage());
        }
    }

    @Override
    public void onContactSelected(VisitorModel contact) {
        Intent intent = new Intent(getApplication(), AddVisitorActivity.class);
        intent.putExtra(INTENT_TAG, gson.toJson(contact));
        startActivity(intent);
        //Toast.makeText(getApplicationContext(), "Selected: " + contact.getVisitor_name() + ", " + contact.getMobile(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onCancelSelected(VisitorModel contact) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch (view.getId()){
            case R.id.recyclerView:
                hideKeyboardFrom(ListVisitorActivity.this, view);
                break;
            case R.id.nodata:
                hideKeyboardFrom(ListVisitorActivity.this, view);
                break;
        }
        return false;
    }
}
