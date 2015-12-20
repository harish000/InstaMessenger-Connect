package com.mad.albumapp;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseObject;
import com.parse.ParseTwitterUtils;
import com.parse.ParseUser;

import java.util.Arrays;
import java.util.List;
import  com.facebook.FacebookSdk;

/**
 * Created by Sriharish on 11/21/2015.
 */
public class ParseClass extends Application
{
    public static final String YOUR_APPLICATION_KEY_ID = "3e5zZgyMaCgutqNhaFIR1RtpgoyENngA4dG8rQ5l";
    public static final String YOUR_CLIENT_KEY = "9U4zBPQy8VDeFw5VsFYS9WVcJslxtbc9ijEJIrmV";
    public static final String YOUR_TWITTER_CONSUMER_KEY = "NpBuaYctZUqgeGOSMv3iSGFHF";
    public static final String YOUR_TWITTER_CONSUMER_SECRET = "YyuYiWBhbQ7iC3Y7N9lNmZW1YijmJoSOnN9xGRpQnxHkiWXL87";
    public static final String YOUR_FACEBOOK_APP_ID ="406580882874675";

    @Override
    public void onCreate() {
        super.onCreate();
        ParseObject.registerSubclass(Album.class);
        ParseObject.registerSubclass(ParseDetails.class);
        Parse.initialize(this, YOUR_APPLICATION_KEY_ID, YOUR_CLIENT_KEY);
        ParseFacebookUtils.initialize(this);
        ParseTwitterUtils.initialize(YOUR_TWITTER_CONSUMER_KEY, YOUR_TWITTER_CONSUMER_SECRET);
    }
/*public static void facebookLogin(Context context)
    {
        ParseFacebookUtils.initialize(context);
    }*/
}
