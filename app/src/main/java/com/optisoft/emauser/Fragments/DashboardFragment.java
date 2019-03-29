package com.optisoft.emauser.Fragments;


import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.optisoft.emauser.Activity.BillActivity;
import com.optisoft.emauser.Activity.NotificationActivity;
import com.optisoft.emauser.Activity.PaymentActivity;
import com.optisoft.emauser.HelperClasses.CommonPrefrence;
import com.optisoft.emauser.Model.BillModel;
import com.optisoft.emauser.Model.ResponseModel;
import com.optisoft.emauser.Model.UserModel;
import com.optisoft.emauser.R;
import com.optisoft.emauser.Webservices.CallApi;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.MPPointF;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;

import static com.optisoft.emauser.HelperClasses.ApiConstant.CURRENT_TAG;
import static com.optisoft.emauser.HelperClasses.ApiConstant.EXCEPTION_TAG;
import static com.optisoft.emauser.HelperClasses.ApiConstant.INTENT_TAG;
import static com.optisoft.emauser.HelperClasses.ApiConstant.NOTIFICATION_TAG;
import static com.optisoft.emauser.HelperClasses.ApiConstant.PAYMENT_TAG;
import static com.optisoft.emauser.HelperClasses.ApiConstant.PREVIOUS_TAG;

/**
 * A simple {@link Fragment} subclass.
 */
public class DashboardFragment extends Fragment implements View.OnClickListener, OnChartValueSelectedListener {

    private LineChart mChart;
    private PieChart mPieChart;
    private Typeface mTfLight;
    private TextView tv_current, tv_previous, tv_notifications, tv_payments;
    private CardView current_bill, prev_bill,payment,notification;
    private View view;
    protected String[] mParties = new String[] {
            "Electricity", "Security", "Water", "Garbage", "Other", ""
    };
    private CommonPrefrence commonPrefrence = new CommonPrefrence();
    private UserModel userModel = null;
    private CallApi callApi = new CallApi();
    private Gson gson = new Gson();
    private ArrayList<BillModel> list = new ArrayList<>();


    public DashboardFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_dashboard, container, false);

        userModel = commonPrefrence.getUserLoginSharedPref(getActivity());

        mTfLight = Typeface.createFromAsset(getActivity().getAssets(), "fonts/MyriadPro-Regular.otf");


        tv_current = (TextView) view.findViewById(R.id.tv_current);
        tv_previous = (TextView) view.findViewById(R.id.tv_previous);
        tv_notifications = (TextView) view.findViewById(R.id.tv_notifications);
        tv_payments = (TextView) view.findViewById(R.id.tv_payments);
        current_bill = (CardView) view.findViewById(R.id.current_bill);
        prev_bill = (CardView) view.findViewById(R.id.prev_bill);
        payment=(CardView)view.findViewById(R.id.payment);
        notification=(CardView)view.findViewById(R.id.notification);

        current_bill.setOnClickListener(this);
        prev_bill.setOnClickListener(this);
        payment.setOnClickListener(this);
        notification.setOnClickListener(this);

        setPieChart();

        setPieChartData();

        callApi.requestGraphData(DashboardFragment.this, userModel.getUserId());
        return view;
    }

    private void setPieChart() {
        mPieChart = (PieChart) view.findViewById(R.id.chart1);
        mPieChart.setUsePercentValues(true);
        mPieChart.getDescription().setEnabled(false);
        mPieChart.setExtraOffsets(5, 10, 5, 5);

        mPieChart.setDragDecelerationFrictionCoef(0.95f);

        mPieChart.setCenterTextTypeface(mTfLight);
        mPieChart.setCenterText(generateCenterSpannableText());

        mPieChart.setDrawHoleEnabled(true);
        mPieChart.setHoleColor(Color.WHITE);

        mPieChart.setTransparentCircleColor(Color.WHITE);
        mPieChart.setTransparentCircleAlpha(110);

        mPieChart.setHoleRadius(58f);
        mPieChart.setTransparentCircleRadius(61f);

        mPieChart.setDrawCenterText(true);

        mPieChart.setRotationAngle(0);
        // enable rotation of the chart by touch
        mPieChart.setRotationEnabled(true);
        mPieChart.setHighlightPerTapEnabled(true);

        // mPieChart.setUnit(" €");
        // mPieChart.setDrawUnitsInChart(true);

        // add a selection listener
        mPieChart.setOnChartValueSelectedListener(this);
       // setPieChartData(5, 100);

        mPieChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);
        // mPieChart.spin(2000, 0, 360);

        Legend l = mPieChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(0f);
        l.setYOffset(0f);

        // entry label styling
        mPieChart.setEntryLabelColor(Color.WHITE);
        mPieChart.setEntryLabelTypeface(mTfLight);
        mPieChart.setEntryLabelTextSize(12f);

    }

    private void setPieChartData() {

        int count   = list.size();

        ArrayList<PieEntry> entries = new ArrayList<PieEntry>();

        // NOTE: The order of the entries when being added to the entries array determines their position around the center of
        // the chart.
        /*for (int i = 0; i < count ; i++) {
            entries.add(new PieEntry((float) ((Math.random() * mult) + mult / 5), mParties[i % mParties.length], getResources().getDrawable(R.drawable.star)));
        } */

        for (int i = 0; i < count ; i++) {
            BillModel billModel = list.get(i);
            int value = Integer.parseInt(billModel.getBill_amount());
            entries.add(new PieEntry((float) value, billModel.getBill_title(), getResources().getDrawable(R.drawable.star)));
        }

        if (list.size()==0){
            entries.add(new PieEntry( 100, "No Bills", getResources().getDrawable(R.drawable.star)));
           // entries.add(new PieEntry(100));
        }

        PieDataSet dataSet = new PieDataSet(entries, "Bill Reports");

        dataSet.setDrawIcons(false);

        dataSet.setSliceSpace(3f);
        dataSet.setIconsOffset(new MPPointF(0, 40));
        dataSet.setSelectionShift(5f);

        // add a lot of colors

        ArrayList<Integer> colors = new ArrayList<Integer>();

        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);

        colors.add(ColorTemplate.getHoloBlue());

        dataSet.setColors(colors);
        //dataSet.setSelectionShift(0f);

        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        if (list.size()==0){
            data.setValueTextSize(0f);
        }else {
            data.setValueTextSize(11f);
        }
        data.setValueTextColor(Color.WHITE);
        data.setValueTypeface(mTfLight);
        mPieChart.setData(data);

        // undo all highlights
        mPieChart.highlightValues(null);

        mPieChart.invalidate();
    }

    private SpannableString generateCenterSpannableText() {
        SpannableString s = new SpannableString("Monthly Report\ngenerated by Estate Agent");
        s.setSpan(new RelativeSizeSpan(1.7f), 0, 14, 0);
        s.setSpan(new StyleSpan(Typeface.NORMAL), 14, s.length() - 12, 0);
        s.setSpan(new ForegroundColorSpan(Color.GRAY), 14, s.length() - 12, 0);
        s.setSpan(new RelativeSizeSpan(.8f), 14, s.length() - 12, 0);
        s.setSpan(new StyleSpan(Typeface.ITALIC), s.length() - 14, s.length(), 0);
        s.setSpan(new ForegroundColorSpan(ColorTemplate.getHoloBlue()), s.length() - 12, s.length(), 0);
        return s;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.current_bill:
                Intent intent = new Intent(getActivity(), BillActivity.class);
                intent.putExtra(INTENT_TAG, CURRENT_TAG);
                startActivity(intent);
                break;
            case R.id.prev_bill:
                Intent intent1 = new Intent(getActivity(), BillActivity.class);
                intent1.putExtra(INTENT_TAG, PREVIOUS_TAG);
                startActivity(intent1);
                break;

            case R.id.notification:
                Intent intent3 = new Intent(getActivity(), NotificationActivity.class);
                intent3.putExtra(INTENT_TAG, NOTIFICATION_TAG);
                startActivity(intent3);
                break;

            case R.id.payment:
                Intent intent2 = new Intent(getActivity(), PaymentActivity.class);
                intent2.putExtra(INTENT_TAG, PAYMENT_TAG);
                startActivity(intent2);
                break;
        }
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {
        if (e == null)
            return;
        Log.i("VAL SELECTED","Value: " + e.getY() + ", index: " + h.getX() + ", DataSet index: " + h.getDataSetIndex());
    }

    @Override
    public void onNothingSelected() {
        Log.i("PieChart", "nothing selected");
    }

    public void responseGraphData(ResponseModel model){
        try {
            if(model.getStatus()==1)
            {//{"id":"5","bill_month":"2018-05-01","bill_amount":"345","bill_title":"Electricity"}  "current":3,"previous":1,"notification":6,"payment":6
                JSONObject jsonObject = new JSONObject(gson.toJson(model.getResponse()));
                JSONArray jsonArray = jsonObject.getJSONArray("graph");
                Type type=new TypeToken<ArrayList<BillModel>>(){}.getType();
                list = gson.fromJson(jsonArray.toString(), type);

                tv_current.setText(jsonObject.getInt("current")+"");
                tv_previous.setText(jsonObject.getInt("previous")+"");
                tv_notifications.setText(jsonObject.getInt("notification")+"");
                tv_payments.setText(jsonObject.getInt("payment")+"");

            }else {
                customToast(model.getMessage());
            }
        }catch (Exception e){
            Log.e(EXCEPTION_TAG, e.getMessage());
            customToast(e.getMessage());
        }
        setPieChartData();
    }

    public void customToast(String s) {
        Toast.makeText(getActivity(), s, Toast.LENGTH_LONG).show();
    }

}