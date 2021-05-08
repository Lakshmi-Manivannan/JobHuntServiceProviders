package com.lakshmimanivannan.jobhuntappserviceproviders;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;


public class SessionManager {

    SharedPreferences userSession;
    SharedPreferences.Editor editor;
    Context context;

    private static final String IS_LOGIN = "IsLoggedIn";

    public static final String Key_FULLNAME = "fullname";
    public static final String Key_USERNAME = "username";
    public static final String Key_PASSWORD = "password";
    public static final String Key_EMAIL = "email";
    public static final String Key_IMAGE = "image";
    public static final String Key_PNO = "pno";
    public static final String Key_LOCATION = "location";
    public static final String Key_CATEGORY = "category";
    public static final String Key_DESCRIPTION = "description";


    public SessionManager(Context _context){
        context = _context;
        userSession= context.getSharedPreferences("userLoginSession",Context.MODE_PRIVATE);
        editor = userSession.edit();
    }

    public  void createLoginSession(String fullname,String username,String password,String pno,String image,String email,String location,String category,String description){

        editor.putBoolean(IS_LOGIN,true);

        editor.putString(Key_FULLNAME,fullname);
        editor.putString(Key_USERNAME,username);
        editor.putString(Key_EMAIL,email);
        editor.putString(Key_IMAGE,image);
        editor.putString(Key_PNO,pno);
        editor.putString(Key_PASSWORD,password);
        editor.putString(Key_LOCATION,location);
        editor.putString(Key_CATEGORY,category);
        editor.putString(Key_DESCRIPTION,description);

        editor.commit();
    }

    public HashMap<String , String> getUserDetailFromSession(){

        HashMap<String,String> userdata = new HashMap<String,String>();

        userdata.put(Key_FULLNAME,userSession.getString(Key_FULLNAME,null));
        userdata.put(Key_USERNAME,userSession.getString(Key_USERNAME,null));
        userdata.put(Key_EMAIL,userSession.getString(Key_EMAIL,null));
        userdata.put(Key_IMAGE,userSession.getString(Key_IMAGE,null));
        userdata.put(Key_PNO,userSession.getString(Key_PNO,null));
        userdata.put(Key_PASSWORD,userSession.getString(Key_PASSWORD,null));
        userdata.put(Key_CATEGORY,userSession.getString(Key_CATEGORY,null));
        userdata.put(Key_LOCATION,userSession.getString(Key_LOCATION,null));
        userdata.put(Key_DESCRIPTION,userSession.getString(Key_DESCRIPTION,null));

        return userdata;
    }

    public boolean checkLogin(){
        if((userSession.getBoolean(IS_LOGIN,false))){
            return true;
        }
        else
            return false;
    }

    public void logout(){
        editor.clear();
        editor.commit();
    }
}
