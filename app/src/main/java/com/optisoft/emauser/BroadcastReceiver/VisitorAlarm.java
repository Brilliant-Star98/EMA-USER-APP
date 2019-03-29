package com.optisoft.emauser.BroadcastReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.optisoft.emauser.Activity.ReserveVisitorActivity;
import com.optisoft.emauser.Model.VisitorModel;
import com.optisoft.emauser.Webservices.CallApi;

import java.lang.reflect.Type;

import static com.optisoft.emauser.HelperClasses.ApiConstant.ALARM_APPEAR;
import static com.optisoft.emauser.HelperClasses.ApiConstant.ALARM_TAG;
import static com.optisoft.emauser.HelperClasses.ApiConstant.INTENT_TAG;


public class VisitorAlarm extends BroadcastReceiver {

    private VisitorModel visitorModel = null;
    private Gson gson = new Gson();
    private CallApi callApi = new CallApi();

    @Override
    public void onReceive(Context context, Intent intent) {

        //Toast.makeText(context, "This visitor is at out of time.", Toast.LENGTH_LONG).show();
        Bundle gotBasket = intent.getExtras();
        String temp = gotBasket.getString(ALARM_TAG);
        Type type = new TypeToken<VisitorModel>(){}.getType();
        visitorModel = gson.fromJson(temp, type);

        Intent launchIntent = new Intent(context, ReserveVisitorActivity.class);
        launchIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        launchIntent.putExtra(INTENT_TAG, gson.toJson(visitorModel));
        launchIntent.putExtra(ALARM_APPEAR, "notify");
        context.startActivity(launchIntent);
    }
}
