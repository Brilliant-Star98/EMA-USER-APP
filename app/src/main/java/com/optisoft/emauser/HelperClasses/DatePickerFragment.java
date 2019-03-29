package com.optisoft.emauser.HelperClasses;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

/**
 * Created by OptiSoft_A on 8/31/2017.
 */

public class DatePickerFragment extends DialogFragment {
    DatePickerDialog.OnDateSetListener ondateSet;

    public DatePickerFragment() {
    }

    public void setCallBack(DatePickerDialog.OnDateSetListener ondate) {
        ondateSet = ondate;
    }

    private int year, month, day;
    private boolean isDisableNext = false;

    @Override
    public void setArguments(Bundle args) {
        super.setArguments(args);
        year = args.getInt("year");
        month = args.getInt("month");
        day = args.getInt("day");
        isDisableNext = args.getBoolean("isDisableNext", false);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        DatePickerDialog dp = new DatePickerDialog(getActivity(), ondateSet, year, month, day);
        if (isDisableNext){
            dp.getDatePicker().setMinDate(System.currentTimeMillis());
        }
        return dp;
    }
}