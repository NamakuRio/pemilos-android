package io.github.namakurio.pemilos.data;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import java.util.HashMap;

import io.github.namakurio.pemilos.activity.LoginActivity;

public class SessionManager {

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context context;
    int mode = 0;

    private static final String pref_name = "pesertapref";
    private static final String is_login = "islogin";
    public static final String kunci_id = "keyid";
    public static final String kunci_username = "keyusername";
    public static final String kunci_name = "keyname";
    public static final String kunci_email = "keyemail";
    public static final String kunci_apitoken = "keyapitoken";

    public SessionManager(Context context) {
        this.context = context;
        pref = context.getSharedPreferences(pref_name, mode);
        editor = pref.edit();
    }

    public void createSession(Integer id, String username, String name, String email, String apitoken) {
        editor.putBoolean(is_login, true);
        editor.putInt(kunci_id, id);
        editor.putString(kunci_username, username);
        editor.putString(kunci_name, name);
        editor.putString(kunci_email, email);
        editor.putString(kunci_apitoken, apitoken);
        editor.commit();
    }

    public void updateSession(String kunci, String value) {
        editor.putString(kunci, value);
        editor.commit();
    }

    public boolean checkLogin() {
//        if(!this.is_login()){
//            Intent i = new Intent(context, AuthActivity.class);
//            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            context.startActivity(i);
//        } else {
//            Intent i = new Intent(context, MainActivity.class);
//            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            context.startActivity(i);
//        }
        return pref.getBoolean(is_login, false);
    }

    private boolean is_login(){
        return pref.getBoolean(is_login, false);
    }

    public void logout(){
        editor.clear();
        editor.commit();
        Intent i = new Intent(context, LoginActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(i);
    }

    public HashMap<String, String> getUserDetails(){
        HashMap<String, String> user = new HashMap<String, String>();
        user.put(pref_name, pref.getString(pref_name, null));
        user.put(kunci_id, pref.getString(kunci_id, null));
        user.put(kunci_username, pref.getString(kunci_username, null));
        user.put(kunci_name, pref.getString(kunci_name, null));
        user.put(kunci_email, pref.getString(kunci_email, null));
        user.put(kunci_apitoken, pref.getString(kunci_apitoken, null));
        return user;
    }

}
