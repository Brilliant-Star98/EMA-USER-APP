package com.optisoft.emauser.HelperClasses;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;

import java.util.Calendar;

/**
 * Created by OptiSoft_A on 8/31/2017.
 */

public class TimePickerFragment extends DialogFragment{
    TimePickerDialog.OnTimeSetListener onTimeSet;

    public TimePickerFragment() {
    }

    public void setCallBack(TimePickerDialog.OnTimeSetListener onTime) {
        onTimeSet = onTime;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        TimePickerDialog tp = new TimePickerDialog(getActivity(),onTimeSet, hour, minute, DateFormat.is24HourFormat(getActivity()));
        return tp;
    }
}