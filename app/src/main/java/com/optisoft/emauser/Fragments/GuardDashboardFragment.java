package com.optisoft.emauser.Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.optisoft.emauser.Activity.AddVisitorActivity;
import com.optisoft.emauser.Activity.ListVisitorActivity;
import com.optisoft.emauser.HelperClasses.CommonPrefrence;
import com.optisoft.emauser.Model.ResponseModel;
import com.optisoft.emauser.Model.UserModel;
import com.optisoft.emauser.R;
import com.optisoft.emauser.Webservices.CallApi;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.Calendar;

import static com.optisoft.emauser.HelperClasses.ApiConstant.EXCEPTION_TAG;
import static com.optisoft.emauser.HelperClasses.ApiConstant.convert_time;

/**
 * A simple {@link Fragment} subclass.
 */
public class GuardDashboardFragment extends Fragment implements View.OnClickListener {

    private CardView card_add_visitor ,card_list_visitor;
    private Gson gson = new Gson();
    private CallApi callApi = new CallApi();
    private CommonPrefrence commonPrefrence = new CommonPrefrence();
    private UserModel userModel = null;
    private String currentDate = "";
    private TextView tv_total;

    public GuardDashboardFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_guard_dashboard, container, false);

        userModel = commonPrefrence.getUserLoginSharedPref(getActivity());

        card_add_visitor  = (CardView) view.findViewById(R.id.card_add_visitor);
        card_list_visitor = (CardView) view.findViewById(R.id.card_list_visitor);
        tv_total    = (TextView) view.findViewById(R.id.tv_total);

        card_add_visitor.setOnClickListener(this);
        card_list_visitor.setOnClickListener(this);

        Calendar calender = Calendar.getInstance();
        currentDate = convert_time(calender.get(Calendar.DAY_OF_MONTH))+"-"+convert_time(calender.get(Calendar.MONTH) + 1)+"-"+calender.get(Calendar.YEAR);
        loadData();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadData();
    }

    private void loadData() {
        callApi.requestLoadGuardData(this, userModel.getUserId(), currentDate);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.card_add_visitor:
                callAddVisitor();
                break;
            case R.id.card_list_visitor:
                callListVisitor();
                break;

            default:
                return;
        }
    }

    private void callListVisitor() {
        Intent intent = new Intent(getActivity(), ListVisitorActivity.class);
        startActivity(intent);
    }

    private void callAddVisitor() {
        Intent intent = new Intent(getActivity(), AddVisitorActivity.class);
        startActivity(intent);
    }

    public void responseLoadVisitor(ResponseModel body) {
        try {
            if (body.getStatus() == 1){
                JSONObject object = new JSONObject(gson.toJson(body.getResponse()));
                if (object.has("total_visitor")){
                    tv_total.setText(object.getInt("total_visitor")+"");
                }
            }
        }catch (Exception e){
            customToast(e.getMessage());
            Log.e(EXCEPTION_TAG, e.getMessage());
        }
    }

    public void customToast(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
    }
}
