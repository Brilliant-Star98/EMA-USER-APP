package com.optisoft.emauser.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.optisoft.emauser.Fragments.AgentDashboardFragment;
import com.optisoft.emauser.Fragments.DashboardFragment;
import com.optisoft.emauser.Fragments.GuardDashboardFragment;
import com.optisoft.emauser.Fragments.HomeMenuFragment;
import com.optisoft.emauser.HelperClasses.CommonPrefrence;
import com.optisoft.emauser.Model.UserModel;
import com.optisoft.emauser.R;

import static com.optisoft.emauser.HelperClasses.ApiConstant.DRAWER_HOME;
import static com.optisoft.emauser.HelperClasses.ApiConstant.FRAG_HOME;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private DrawerLayout drawer;
    private ImageView drawer_img;
    private FloatingActionButton floatBtn;
    private CommonPrefrence commonPrefrence = new CommonPrefrence();
    private UserModel userModel = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (commonPrefrence.getDistressSharedPref(this) != null){
            Intent intent = new Intent(this, DistressMessageActivity.class);
            startActivity(intent);
        }

        userModel = commonPrefrence.getUserLoginSharedPref(this);

        drawer     = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer_img = (ImageView) findViewById(R.id.drawer);
        floatBtn   = (FloatingActionButton) findViewById(R.id.floatBtn);

        getSupportFragmentManager().beginTransaction().add(R.id.content_menu, new HomeMenuFragment(), DRAWER_HOME).addToBackStack(null).commit();
        if (userModel.getRoleId().equals("2")){
            floatBtn.setVisibility(View.GONE);
            getSupportFragmentManager().beginTransaction().add(R.id.content, new AgentDashboardFragment(), FRAG_HOME).addToBackStack(null).commit();
        }else if (userModel.getRoleId().equals("4")){
            floatBtn.setVisibility(View.GONE);
            getSupportFragmentManager().beginTransaction().add(R.id.content, new GuardDashboardFragment(), FRAG_HOME).addToBackStack(null).commit();
        }else {
            floatBtn.setVisibility(View.VISIBLE);
            getSupportFragmentManager().beginTransaction().add(R.id.content, new DashboardFragment(), FRAG_HOME).addToBackStack(null).commit();
        }

        drawer.setOnClickListener(this);
        drawer_img.setOnClickListener(this);
        floatBtn.setOnClickListener(this);

    }

    private void showDrawer() {
        final android.support.v4.app.FragmentTransaction ft1 = getSupportFragmentManager().beginTransaction();
        ft1.replace(R.id.content_menu, new HomeMenuFragment(), DRAWER_HOME);
        ft1.commit();
        setDrawer();
    }

    public void setDrawer() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            drawer.openDrawer(GravityCompat.START);
        }
    }

    @Override
    public void onBackPressed() {

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }else{
            finish();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.drawer:
                showDrawer();
                break;

            case R.id.floatBtn:
                Intent intent = new Intent(getApplication(), NotificationActivity.class);
                startActivity(intent);
                break;

        }
    }
}
