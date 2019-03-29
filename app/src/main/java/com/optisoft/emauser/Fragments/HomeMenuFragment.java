package com.optisoft.emauser.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.optisoft.emauser.Activity.AddVisitorActivity;
import com.optisoft.emauser.Activity.BillActivity;
import com.optisoft.emauser.Activity.ContactUsActivity;
import com.optisoft.emauser.Activity.DistressMessageActivity;
import com.optisoft.emauser.Activity.ListVisitorActivity;
import com.optisoft.emauser.Activity.LoginActivity;
import com.optisoft.emauser.Activity.NotificationActivity;
import com.optisoft.emauser.Activity.PaymentActivity;
import com.optisoft.emauser.Activity.ProfileActivity;
import com.optisoft.emauser.Activity.ReserveVisitorActivity;
import com.optisoft.emauser.Activity.VehicleListActivity;
import com.optisoft.emauser.Activity.VisitorHistoryActivity;
import com.optisoft.emauser.HelperClasses.CommonPrefrence;
import com.optisoft.emauser.Model.ResponseModel;
import com.optisoft.emauser.Model.UserModel;
import com.optisoft.emauser.R;
import com.optisoft.emauser.Webservices.CallApi;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;

import java.util.ArrayList;

import static com.optisoft.emauser.HelperClasses.ApiConstant.CURRENT_TAG;
import static com.optisoft.emauser.HelperClasses.ApiConstant.EXCEPTION_TAG;
import static com.optisoft.emauser.HelperClasses.ApiConstant.IMAGE_URL;
import static com.optisoft.emauser.HelperClasses.ApiConstant.INTENT_TAG;


public class HomeMenuFragment extends Fragment implements View.OnClickListener {

    private TextView notification_tv, my_dues_tv, payment_history_tv, contact_us_tv, logout_tv, tv_distress_message, contact_us_tv_agent, tv_distress_message_agent, vehicle_history_tv;
    private TextView full_name, phone, edit_profile, tv_add_visitor, tv_list_visitor, tv_reserve_visitor;
    private LinearLayout ly_agent, ly_user, ly_guard;
    private CommonPrefrence commonPrefrence;
    private UserModel userModel;
    private ImageView profile_image;
    private int roleId = 3;
    private CallApi callApi = new CallApi();
    private Gson gson = new Gson();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_home_menu, container, false);
        commonPrefrence = new CommonPrefrence();
        userModel = commonPrefrence.getUserLoginSharedPref(getActivity());
        roleId = Integer.parseInt(userModel.getRoleId());

        full_name =(TextView)view.findViewById(R.id.full_name);
        phone =(TextView)view.findViewById(R.id.phone);
        profile_image =(ImageView)view.findViewById(R.id.profile_image);
        ly_agent =(LinearLayout) view.findViewById(R.id.ly_agent);
        ly_user  =(LinearLayout) view.findViewById(R.id.ly_user);
        ly_guard  =(LinearLayout) view.findViewById(R.id.ly_guard);


        notification_tv =(TextView)view.findViewById(R.id.notification_tv);
        my_dues_tv =(TextView)view.findViewById(R.id.my_dues_tv);
        payment_history_tv =(TextView)view.findViewById(R.id.payment_history_tv);
        vehicle_history_tv = (TextView)view.findViewById(R.id.veicle_history_tv);
        contact_us_tv =(TextView)view.findViewById(R.id.contact_us_tv);
        logout_tv =(TextView)view.findViewById(R.id.logout_tv);
        edit_profile =(TextView)view.findViewById(R.id.edit_profile);
        tv_add_visitor =(TextView)view.findViewById(R.id.tv_add_visitor);
        tv_list_visitor =(TextView)view.findViewById(R.id.tv_list_visitor);
        tv_distress_message =(TextView)view.findViewById(R.id.tv_distress_message);
        contact_us_tv_agent =(TextView)view.findViewById(R.id.contact_us_tv_agent);
        tv_distress_message_agent =(TextView)view.findViewById(R.id.tv_distress_message_agent);
        tv_reserve_visitor = (TextView)view.findViewById(R.id.reserve_visitor);

        tv_distress_message_agent.setOnClickListener(this);
        contact_us_tv_agent.setOnClickListener(this);
        tv_distress_message.setOnClickListener(this);
        notification_tv.setOnClickListener(this);
        my_dues_tv.setOnClickListener(this);
        payment_history_tv.setOnClickListener(this);
        contact_us_tv.setOnClickListener(this);
        logout_tv.setOnClickListener(this);
        edit_profile.setOnClickListener(this);
        tv_add_visitor.setOnClickListener(this);
        tv_list_visitor.setOnClickListener(this);
        vehicle_history_tv.setOnClickListener(this);
        tv_reserve_visitor.setOnClickListener(this);

        if (userModel != null){
            setNavView();
        }
        return view;
    }

    private void setNavView() {
        if (!userModel.getFname().isEmpty())
        full_name.setText(userModel.getFname()+" "+ userModel.getLname());
        phone.setText("+"+userModel.getCountry_code()+"-"+userModel.getMobile());
        Picasso.with(getActivity()).load(IMAGE_URL+userModel.getProfile_picture()).placeholder(R.drawable.user).error(R.drawable.user).into(profile_image);

        if (roleId == 2){
            edit_profile.setVisibility(View.GONE);
            ly_user.setVisibility(View.GONE);
            ly_guard.setVisibility(View.GONE);
            ly_agent.setVisibility(View.VISIBLE);
        }else if (roleId == 4){
            edit_profile.setVisibility(View.GONE);
            ly_user.setVisibility(View.GONE);
            ly_agent.setVisibility(View.GONE);
            ly_guard.setVisibility(View.VISIBLE);
        }else {
            edit_profile.setVisibility(View.VISIBLE);
            ly_user.setVisibility(View.VISIBLE);
            ly_agent.setVisibility(View.GONE);
            ly_guard.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.edit_profile:
                Intent intent0 = new Intent(getActivity(), ProfileActivity.class);
                startActivity(intent0);
                break;
            case R.id.notification_tv:
                Intent intent1 = new Intent(getActivity(), NotificationActivity.class);
                startActivity(intent1);
                break;
            case R.id.contact_us_tv_agent:
                Intent intent5 = new Intent(getActivity(), NotificationActivity.class);
                startActivity(intent5);
                break;
            case R.id.tv_distress_message_agent:
                callApi.requestDistressMessages(HomeMenuFragment.this, userModel.getUserId());
                break;
              case R.id.my_dues_tv:
                  Intent intent = new Intent(getActivity(), BillActivity.class);
                  intent.putExtra(INTENT_TAG, CURRENT_TAG);
                  startActivity(intent);
                break;
              case R.id.payment_history_tv:
                Intent intent3 = new Intent(getActivity(), PaymentActivity.class);
                startActivity(intent3);
                break;
              case R.id.contact_us_tv:
                Intent intent4 = new Intent(getActivity(), ContactUsActivity.class);
                intent4.putExtra(INTENT_TAG, "contact");
                startActivity(intent4);
                break;
            case R.id.logout_tv:
                callLogout();
                break;
            case R.id.tv_distress_message:
                callDistressMessage();
                break;
            case R.id.tv_add_visitor:
                callAddVisitor();
                break;
            case R.id.tv_list_visitor:
                callListVisitor();
                break;
            case R.id.veicle_history_tv:
                Intent intent6 = new Intent(getActivity(), VehicleListActivity.class);
                startActivity(intent6);
                break;
            case R.id.reserve_visitor:
                Intent intent7 = new Intent(getActivity(), VisitorHistoryActivity.class);
                startActivity(intent7);
                break;
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

    private void callDistressMessage() {
        Intent intent4 = new Intent(getActivity(), ContactUsActivity.class);
        intent4.putExtra(INTENT_TAG, "distress_message");
        startActivity(intent4);
    }

    private void callLogout() {
        commonPrefrence.setUserLoginSharedPref(getActivity(), null);
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivity(intent);
        getActivity().finishAffinity();
    }

    public void responseDistressMessages(ResponseModel body) {
        try {
            if (body.getStatus() == 1){
                JSONArray jsonArray = new JSONArray(gson.toJson(body.getResponse()));
                ArrayList<String> list = new ArrayList<>();
                for (int i = 0; i < jsonArray.length(); i++){
                    list.add(jsonArray.getJSONObject(i).toString());
                }
                if (list.size() > 0){
                    commonPrefrence.setDistressSharedPref(getActivity(), list);
                    Intent intent = new Intent(getActivity(), DistressMessageActivity.class);
                    startActivity(intent);
                }
            }
        }catch (Exception e){
            Log.e(EXCEPTION_TAG, e.getMessage());
            customToast(e.getMessage());
        }
    }

    public void customToast(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
    }
}
