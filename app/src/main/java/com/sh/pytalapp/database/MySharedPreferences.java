package com.sh.pytalapp.database;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;
import com.sh.pytalapp.model.ToBe;
import com.sh.pytalapp.model.ChanLe;
import com.sh.pytalapp.model.User;

import java.lang.reflect.Type;
import java.util.List;

public class MySharedPreferences {
    private static final String MY_SHARE_PREFERENCES = "MySharedPreferences";
    private Context mContext;

    public MySharedPreferences(Context mContext) {
        this.mContext = mContext;
    }

    public void putInt(String key, int value) {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(MY_SHARE_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    public void putLong(String key, Long value) {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(MY_SHARE_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong(key, value);
        editor.apply();
    }

    public void putString(String key, String value) {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(MY_SHARE_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public void putBoolean(String key, boolean value) {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(MY_SHARE_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public int getInt(String key) {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(MY_SHARE_PREFERENCES, Context.MODE_PRIVATE);
        return sharedPreferences.getInt(key, -1);
    }

    public Long getLong(String key) {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(MY_SHARE_PREFERENCES, Context.MODE_PRIVATE);
        return sharedPreferences.getLong(key, 0L);
    }

    public String getString(String key) {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(MY_SHARE_PREFERENCES, Context.MODE_PRIVATE);
        return sharedPreferences.getString(key, "");
    }

    public boolean getBoolean(String key) {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(MY_SHARE_PREFERENCES, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(key, false);
    }

    public void clearAllData() {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(MY_SHARE_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }

    public void clearDataByKey(String key) {
        SharedPreferences preferences = mContext.getSharedPreferences(MY_SHARE_PREFERENCES, Context.MODE_PRIVATE);
        preferences.edit().remove(key).apply();
    }

    public void putListTaiXiu(String key, List<ChanLe> listItems) {
        SharedPreferences pref = mContext.getSharedPreferences(MY_SHARE_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        Gson gson = new GsonBuilder().create();
        JsonArray array = gson.toJsonTree(listItems).getAsJsonArray();
        editor.putString(key, array.toString());
        editor.apply();
    }

    public List<ChanLe> getListTaiXiu(String key) {
        Gson gson = new Gson();
        List<ChanLe> listItems;
        SharedPreferences sharedPref = mContext.getSharedPreferences(MY_SHARE_PREFERENCES, Context.MODE_PRIVATE);
        String jsonPreferences = sharedPref.getString(key, "");

        Type type = new TypeToken<List<ChanLe>>() {
        }.getType();
        listItems = gson.fromJson(jsonPreferences, type);

        return listItems;
    }

    public void putChanLe(String key, ToBe customer) {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(MY_SHARE_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String value = gson.toJson(customer);
        editor.putString(key, value);
        editor.apply();
    }


    public ToBe getChanLe(String key) {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(MY_SHARE_PREFERENCES, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString(key, "");
        return gson.fromJson(json, ToBe.class);
    }

    public void putListChanLe(String key, List<ToBe> listItems) {
        SharedPreferences pref = mContext.getSharedPreferences(MY_SHARE_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        Gson gson = new GsonBuilder().create();
        JsonArray array = gson.toJsonTree(listItems).getAsJsonArray();
        editor.putString(key, array.toString());
        editor.apply();
    }

    public List<ToBe> getListChanLe(String key) {
        Gson gson = new Gson();
        List<ToBe> listItems;
        SharedPreferences sharedPref = mContext.getSharedPreferences(MY_SHARE_PREFERENCES, Context.MODE_PRIVATE);
        String jsonPreferences = sharedPref.getString(key, "");

        Type type = new TypeToken<List<ToBe>>() {
        }.getType();
        listItems = gson.fromJson(jsonPreferences, type);

        return listItems;
    }

    public void putUser(String key, User customer) {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(MY_SHARE_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String value = gson.toJson(customer);
        editor.putString(key, value);
        editor.apply();
    }

    public User getUser(String key) {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(MY_SHARE_PREFERENCES, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString(key, "");
        return gson.fromJson(json, User.class);
    }

}
