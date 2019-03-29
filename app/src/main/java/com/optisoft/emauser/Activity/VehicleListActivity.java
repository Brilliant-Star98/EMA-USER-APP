package com.optisoft.emauser.Activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.optisoft.emauser.HelperClasses.CommonPrefrence;
import com.optisoft.emauser.HelperClasses.DatePickerFragment;
import com.optisoft.emauser.Model.ResponseModel;
import com.optisoft.emauser.Model.UserModel;
import com.optisoft.emauser.Model.VehicleModel;
import com.optisoft.emauser.R;
import com.optisoft.emauser.Adapter.VehicleListAdapter;
import com.optisoft.emauser.Webservices.CallApi;

import org.json.JSONArray;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static com.optisoft.emauser.HelperClasses.ApiConstant.EXCEPTION_TAG;

public class VehicleListActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView img_back;
    private ListView listView;
    private EditText date_from, date_to, date;
    VehicleListAdapter vehicleListAdapter;
    private CommonPrefrence commonPrefrence = new CommonPrefrence();
    private UserModel userModel = null;
    private CallApi callApi = new CallApi();
    private Gson gson = new Gson();
    public ArrayList<String> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_list);

        userModel = commonPrefrence.getUserLoginSharedPref(this);
        callApi.getCarListForUser(VehicleListActivity.this, userModel.getUserId());

        listView = (ListView)findViewById(R.id.listview_vehicle);
        list = new ArrayList<String>();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
                final String item = (String) parent.getItemAtPosition(position);
                view.animate().setDuration(800).alpha(0)
                        .withEndAction(new Runnable() {
                            @Override
                            public void run() {
                                //list.remove(item);
                                //vehicleListAdapter.notifyDataSetChanged();
                                view.setAlpha(1);
                            }
                        });
                showDateSelectionDialog(item);
            }
        });



        img_back = (ImageView)findViewById(R.id.img_back_1);
        img_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_back_1:
                finish();
                break;
        }
    }

    public void customToast(String s) {
        Toast.makeText(getApplication(), s, Toast.LENGTH_LONG).show();
    }

    public void showVehicleList(ResponseModel model) {
        try {
            customToast(model.getMessage());
            if (model.getStatus()==1)
            {
                JSONArray jsonArray = new JSONArray(gson.toJson(model.getResponse()));
                Type type=new TypeToken<ArrayList<VehicleModel>>(){}.getType();
                ArrayList<VehicleModel> list_model = gson.fromJson(jsonArray.toString(), type);
                for (int i = 0 ; i < list_model.size() ; i ++){
                    VehicleModel vModel = list_model.get(i);
                    list.add(vModel.getCarPlate());
                }

                vehicleListAdapter = new VehicleListAdapter(this, list);
                listView.setAdapter(vehicleListAdapter);
            }

        }catch (Exception e){
            Log.e(EXCEPTION_TAG, e.getMessage());
            customToast(e.getMessage());
        }
    }

    public void showDateSelectionDialog(final String car_plate) {
        final Dialog dialog = new Dialog(VehicleListActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_datetime_select_layout);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

        ImageView close = (ImageView) dialog.findViewById(R.id.date_close_img);
        TextView search_date = (TextView) dialog.findViewById(R.id.search_car_history);
        date_from = (EditText) dialog.findViewById(R.id.from_date);
        date_to = (EditText) dialog.findViewById(R.id.to_date);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        search_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            Intent intent = new Intent(VehicleListActivity.this,VehicleHistoryActivity.class);
            intent.putExtra("car_plate", car_plate);
            intent.putExtra("date_from", date_from.getText().toString());
            String date_to_str = null;
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            try {
                Date d = sdf.parse(date_to.getText().toString());
                Calendar c = Calendar.getInstance();
                c.setTime(d);
                c.add(Calendar.DATE, 1);

                Date d_plus = c.getTime();
                date_to_str = sdf.format(d_plus);

            } catch (ParseException e) {
                e.printStackTrace();
            }

            intent.putExtra("date_to", date_to_str);

            startActivity(intent);
            }
        });

        date_from.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePicker(date_from);
            }
        });

        date_to.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePicker(date_to);
            }
        });

        dialog.show();
    }

    private void showDatePicker(EditText et) {
        date = et;
        DatePickerFragment date = new DatePickerFragment();
        Calendar calender = Calendar.getInstance();
        Bundle args = new Bundle();
        args.putInt("year", calender.get(Calendar.YEAR));
        args.putInt("month", calender.get(Calendar.MONTH));
        args.putInt("day", calender.get(Calendar.DAY_OF_MONTH));
        args.putBoolean("isDisableNext", false);
        date.setArguments(args);
        date.setCallBack(ondate);
        date.show(getSupportFragmentManager(), "Date Picker");
    }

    DatePickerDialog.OnDateSetListener ondate = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            String dateStr =  year + "-" + convert_time(monthOfYear+1) + "-"+convert_time(dayOfMonth);
            date.setText(dateStr);
        }
    };

    public static String convert_time(int n){
        return n < 10 ? "0" + n : "" + n;
    }
}
