package com.optisoft.emauser.HelperClasses;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.optisoft.emauser.Model.UserModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by OptiSoft_A on 4/10/2018.
 */

public class CommonPrefrence {

    private static String PREF_KEY = "preference" ;
    static SharedPreferences.Editor editor;

    public UserModel getUserLoginSharedPref(Context context) {
        SharedPreferences sharedPref= PreferenceManager.getDefaultSharedPreferences(context);
        String json=sharedPref.getString("USER_LOGIN",null);
        Gson gson=new Gson();
        Type type=new TypeToken<UserModel>(){}.getType();

        UserModel beans = null ;
        try {
            beans = gson.fromJson(json, type);
        } catch(Exception e) {
            return null ;
        }
        return beans;
    }

    public void setUserLoginSharedPref(Context context, UserModel beans) {
        SharedPreferences sharedPref= PreferenceManager.getDefaultSharedPreferences(context);
        editor = sharedPref.edit();
        Gson gson=new Gson();
        String json=gson.toJson(beans);
        editor.putString("USER_LOGIN",json);
        editor.commit();

    }

    public String getFbTockenPref(Context context) {
        SharedPreferences sharedPref= PreferenceManager.getDefaultSharedPreferences(context);
        String json=sharedPref.getString("FIREBASE_TOKEN",null);
        Gson gson=new Gson();
        Type type=new TypeToken<String>(){}.getType();

        String beans = null ;
        try {
            beans = gson.fromJson(json, type);
        } catch(Exception e) {
            return null ;
        }
        return beans;
    }

    public void setFbTockenPref(Context context, String beans) {
        SharedPreferences sharedPref= PreferenceManager.getDefaultSharedPreferences(context);
        editor = sharedPref.edit();
        Gson gson=new Gson();
        String json=gson.toJson(beans);
        editor.putString("FIREBASE_TOKEN",json);
        editor.commit();

    }


    public ArrayList<String> getDistressSharedPref(Context context) {
        SharedPreferences sharedPref= PreferenceManager.getDefaultSharedPreferences(context);
        String json=sharedPref.getString("DISTRESS",null);
        Gson gson=new Gson();
        Type type=new TypeToken<ArrayList<String>>(){}.getType();

        ArrayList<String> beans = null ;
        try {
            beans = gson.fromJson(json, type);
        } catch(Exception e) {
            return null ;
        }
        return beans;
    }

    public void setDistressSharedPref(Context context, ArrayList<String> beans) {
        SharedPreferences sharedPref= PreferenceManager.getDefaultSharedPreferences(context);
        editor = sharedPref.edit();
        Gson gson=new Gson();
        String json=gson.toJson(beans);
        editor.putString("DISTRESS",json);
        editor.commit();

    }

}
