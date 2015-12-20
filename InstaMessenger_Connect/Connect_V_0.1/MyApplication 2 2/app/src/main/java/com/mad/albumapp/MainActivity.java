package com.mad.albumapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseFile;
import com.parse.ParseInstallation;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseTwitterUtils;
import com.parse.ParseUser;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Arrays;
import java.util.List;

import com.parse.SaveCallback;
import com.parse.SendCallback;
import com.parse.internal.signpost.OAuthConsumer;
import com.parse.internal.signpost.basic.DefaultOAuthConsumer;
import com.parse.twitter.Twitter;

import org.apache.commons.io.IOUtils;
import org.json.JSONException;
import org.json.JSONObject;

import javax.net.ssl.HttpsURLConnection;


public class MainActivity extends AppCompatActivity {

    public static String loginInFrom;
    SharedPreferences.Editor loginfrom;

    static String email;
    static String gender = new String();
    static String name;

    static Bitmap bitmap;

    static Profile mFbProfile;

    static JSONObject object=null;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ParseFacebookUtils.onActivityResult(requestCode, resultCode, data);
    }

    /* public static final String YOUR_TWITTER_CONSUMER_KEY = "NpBuaYctZUqgeGOSMv3iSGFHF";
        public static final String YOUR_TWITTER_CONSUMER_SECRET = "YyuYiWBhbQ7iC3Y7N9lNmZW1YijmJoSOnN9xGRpQnxHkiWXL87";
    */
    //public static Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loginInFrom = null;
        Log.d("demo", "Inside OnCreate");
        loginfrom = getSharedPreferences("loginfrom", MODE_PRIVATE).edit();


/*

        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.suleiman.parseapp",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }

*/


        //context =  MainActivity.this;
//        ParseTwitterUtils.initialize(YOUR_TWITTER_CONSUMER_KEY, YOUR_TWITTER_CONSUMER_SECRET);
        if (ParseUser.getCurrentUser() != null) {
            Intent intent = new Intent(MainActivity.this, AppPage.class);
            intent.putExtra("user_id", ParseUser.getCurrentUser().getObjectId());
            startActivity(intent);
        }
        ((Button) findViewById(R.id.buttonLogin)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                loginInFrom = "parse";
                loginfrom.putString("loginfrom", loginInFrom);
                loginfrom.commit();
                intent.putExtra("loginfrom", loginInFrom);
                startActivity(intent);
                finish();

            }
        });

        ((Button) findViewById(R.id.buttonSignUp)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SignUpActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                loginInFrom = "parse";
                loginfrom.putString("loginfrom", loginInFrom);
                loginfrom.commit();
                intent.putExtra("loginfrom", loginInFrom);
                startActivity(intent);
                finish();

            }
        });

        ((Button) findViewById(R.id.buttonFacebook)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //ParseClass.facebookLogin(MainActivity.this);
                Log.d("demo", "Button Clicked");
                //ParseFacebookUtils.initialize(MainActivity.this);
                List<String> permissions = Arrays.asList("email", "public_profile", "user_photos");
                Log.d("demo", "Permissions provided");
                ParseFacebookUtils.logInWithReadPermissionsInBackground(MainActivity.this, permissions, new LogInCallback() {
                    @Override
                    public void done(ParseUser user, ParseException err) {
                        if(user!=null && err==null)
                        {

                        }
                        if (user == null) {
                            Log.d("demo", "Uh oh. The user cancelled the Facebook login.");
                        } else if (user.isNew()) {
                            Log.d("demo", "User signed up and logged in through Facebook!");

                            mFbProfile = Profile.getCurrentProfile();


                            // Intent intent = new Intent(MainActivity.this, AppPage.class);
                            // intent.putExtra("user_id", ParseUser.getCurrentUser().getObjectId());
                            loginInFrom = "facebook";
                            loginfrom.putString("loginfrom", loginInFrom);
                            loginfrom.commit();

                            Log.d("demo", "getUserDetailsMethod");
                            getUserDetailsFromFB();
                            Log.d("demo", "getUserDetailsDone");

                            ParseInstallation installation = ParseInstallation.getCurrentInstallation();
                            installation.put("userid", user.getObjectId());
                            if(ParseUser.getCurrentUser().getString("notifications")!=null) {
                                installation.put("notify", ParseUser.getCurrentUser().getString("notifications"));
                            }
                            else
                            {
                                installation.put("notify", "n");
                            }
                            installation.saveInBackground(new SaveCallback() {
                                @Override
                                public void done(ParseException e) {
                                    if (e == null) {
                                        Log.d("demo", "installed");
                                    } else {
                                        Log.d("demo", e.getLocalizedMessage());
                                    }
                                }
                            });

//Push Notification
                            ParseQuery<ParseInstallation> pushQuery = ParseInstallation.getQuery();
                            pushQuery.whereNotEqualTo("userid", ParseUser.getCurrentUser().getObjectId());
                            pushQuery.whereEqualTo("notify","y");
                            String data = "{\n" +
                                    "    \"data\": {\n" +
                                    "        \"message\": \"Hello! We have a new user!!\",\n" +
                                    "        \"title\": \"Connect:) \"\n" +
                                    "    }\n" +
                                    "}";
                            JSONObject jsondata = null;
                            try {
                                jsondata = new JSONObject(data);
                            } catch (JSONException e1) {
                                e1.printStackTrace();
                            }
                            ParsePush push = new ParsePush();
                            push.setQuery(pushQuery);
                            push.setData(jsondata);
                            push.sendInBackground(new SendCallback() {
                                @Override
                                public void done(ParseException e) {
                                    if (e == null) {
                                        Log.d("demo", "Push Notification Send!!");
                                        //Toast.makeText(MainActivity.this, "Push Notification Send!!", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(MainActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                            // startActivity(intent);
                        } else {

                            ParseInstallation installation = ParseInstallation.getCurrentInstallation();
                            installation.put("userid", user.getObjectId());
                            if(ParseUser.getCurrentUser().getString("notifications")!=null) {
                                installation.put("notify", ParseUser.getCurrentUser().getString("notifications"));
                            }
                            else
                            {
                                installation.put("notify", "n");
                            }
                            installation.saveInBackground(new SaveCallback() {
                                @Override
                                public void done(ParseException e) {
                                    if(e==null)
                                    {
                                        Log.d("demo", "installed");
                                    }else
                                    {
                                        Log.d("demo", e.getLocalizedMessage());
                                    }
                                }
                            });

                            Log.d("demo", "User logged in through Facebook!");
                            Intent intent = new Intent(MainActivity.this, AppPage.class);
                            intent.putExtra("user_id", ParseUser.getCurrentUser().getObjectId());
                            loginInFrom = "facebook";
                            loginfrom.putString("loginfrom", loginInFrom);
                            loginfrom.commit();
                            startActivity(intent);
                        }
                    }
                });

            }
        });

        ((Button) findViewById(R.id.buttonTwitter)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ParseTwitterUtils.logIn(MainActivity.this, new LogInCallback() {
                    @Override
                    public void done(ParseUser user, ParseException err) {
                        if(user!=null && err==null)
                        {

                        }
                        if (user == null) {
                            Log.d("MyApp", "Uh oh. The user cancelled the Twitter login.");
                        } else if (user.isNew()) {
                            Log.d("MyApp", "User signed up and logged in through Twitter!");
                            // Intent intent = new Intent(MainActivity.this, AppPage.class);
                            loginInFrom = "twitter";
                            loginfrom.putString("loginfrom", loginInFrom);
                            loginfrom.commit();
                            Twitter twitter = ParseTwitterUtils.getTwitter();
                            new TwitterLogin(twitter).execute("https://api.twitter.com/1.1/users/show.json?screen_name=" + twitter.getScreenName());

                            /*intent.putExtra("user_id", ParseUser.getCurrentUser().getObjectId());
                            intent.putExtra("loginfrom", loginInFrom);
                            startActivity(intent);*/

                            ParseInstallation installation = ParseInstallation.getCurrentInstallation();
                            installation.put("userid", user.getObjectId());
                            if(ParseUser.getCurrentUser().getString("notifications")!=null) {
                                installation.put("notify", ParseUser.getCurrentUser().getString("notifications"));
                            }
                            else
                            {
                                installation.put("notify", "n");
                            }
                            installation.saveInBackground(new SaveCallback() {
                                @Override
                                public void done(ParseException e) {
                                    if (e == null) {
                                        Log.d("demo", "installed");
                                    } else {
                                        Log.d("demo", e.getLocalizedMessage());
                                    }
                                }
                            });

                            //Push Notification
                            ParseQuery<ParseInstallation> pushQuery = ParseInstallation.getQuery();
                            pushQuery.whereNotEqualTo("userid", ParseUser.getCurrentUser().getObjectId());
                            pushQuery.whereEqualTo("notify", "y");
                            String data = "{\n" +
                                    "    \"data\": {\n" +
                                    "        \"message\": \"Hello! We have a new user!!\",\n" +
                                    "        \"title\": \"Connect:) \"\n" +
                                    "    }\n" +
                                    "}";
                            JSONObject jsondata = null;
                            try {
                                jsondata = new JSONObject(data);
                            } catch (JSONException e1) {
                                e1.printStackTrace();
                            }
                            ParsePush push = new ParsePush();
                            push.setQuery(pushQuery);
                            push.setData(jsondata);
                            push.sendInBackground(new SendCallback() {
                                @Override
                                public void done(ParseException e) {
                                    if (e == null) {
                                        Log.d("demo","Push Notification Send!!");
                                        //Toast.makeText(MainActivity.this, "Push Notification Send!!", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(MainActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                        } else {
                            Log.d("MyApp", "User logged in through Twitter!");
                            //  Intent intent = new Intent(MainActivity.this, AppPage.class);
                            ParseInstallation installation = ParseInstallation.getCurrentInstallation();
                            installation.put("userid", user.getObjectId());
                            if(ParseUser.getCurrentUser().getString("notifications")!=null) {
                                installation.put("notify", ParseUser.getCurrentUser().getString("notifications"));
                            }
                            else
                            {
                                installation.put("notify", "n");
                            }
                            installation.saveInBackground(new SaveCallback() {
                                @Override
                                public void done(ParseException e) {
                                    if (e == null) {
                                        Log.d("demo", "installed");
                                    } else {
                                        Log.d("demo", e.getLocalizedMessage());
                                    }
                                }
                            });

                            loginInFrom = "twitter";
                            loginfrom.putString("loginfrom", loginInFrom);
                            loginfrom.commit();
                            /*intent.putExtra("user_id", ParseUser.getCurrentUser().getObjectId());
                            intent.putExtra("loginfrom", loginInFrom);
                            startActivity(intent);*/


                        }
                    }
                });

            }
        });

    }

    private void getUserDetailsFromTwitter(JSONObject jsonObject)
    {
        try {
            name= jsonObject.getString("name");
            email = jsonObject.getString("name")+"@twitter.com";

            new TwitterImage().execute(jsonObject);


        } catch (JSONException e) {
            e.printStackTrace();
        }


    }



    private void getUserDetailsFromFB() {
        Log.d("demo", "Inside GetUserDetailsFromFb");
        GraphRequest graphRequest = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject jsonObject, GraphResponse graphResponse) {
                        Log.d("demo", jsonObject.toString());
                        try {
                            email = jsonObject.getString("email");
                            name = jsonObject.getString("name");
                            gender = jsonObject.getString("gender");

                            saveNewUser();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

        Bundle bundle = new Bundle();
        bundle.putString("fields", "id, email, name, gender, link");
        graphRequest.setParameters(bundle);
        graphRequest.executeAsync();


        //.executeAsync();
        Log.d("demo", "Gonna start custom AsyncTask");
        ProfilePhotoAsyncTask profilePhotoAsyncTask = new ProfilePhotoAsyncTask(mFbProfile);
        profilePhotoAsyncTask.execute();
        Log.d("demo", "custom AsyncTask done");

    }

    private void saveNewUser() {
        Log.d("demo", "Inside saveNewUser");
        final ParseUser parseUser = ParseUser.getCurrentUser();
        parseUser.setUsername(email);
        parseUser.setEmail(email);
        parseUser.put("user_name", name);

        if(gender.equals(""))
            gender="male";
        parseUser.put("gender", gender);
        parseUser.put("notifications", "y");
        parseUser.put("profile", "public");


        /*ByteArrayOutputStream stream = new ByteArrayOutputStream();
        byte[] data = null;
        if(stream!=null) {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            data = stream.toByteArray();
        }*/

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.camera);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] bitMapData = stream.toByteArray();

        final ParseFile parseFile = new ParseFile("userProfile.png", bitMapData);
        parseFile.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    parseUser.put("image", parseFile);
                    parseUser.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null) {
                                Toast.makeText(MainActivity.this, "New user logged in via fb", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(MainActivity.this, AppPage.class);
                                startActivity(intent);
                            } else {
                                Toast.makeText(MainActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(MainActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    class ProfilePhotoAsyncTask extends AsyncTask<String, String, String> {

        Profile profile;
        public Bitmap bitmap;

        public ProfilePhotoAsyncTask(Profile profile) {
            this.profile = profile;
        }

        @Override
        protected String doInBackground(String... params) {
            // Fetching data from URI and storing in bitmap
            Log.d("demo", "Inside doInBackground");
            bitmap = DownloadImageBitmap(profile.getProfilePictureUri(200, 200).toString());
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

        }
    }

    public static Bitmap DownloadImageBitmap(String url) {
        Bitmap bm = null;
        try {
            URL aURL = new URL(url);
            URLConnection conn = aURL.openConnection();
            conn.connect();
            InputStream is = conn.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(is);
            bitmap = BitmapFactory.decodeStream(bis);
            bis.close();
            is.close();
        } catch (IOException e) {
            Log.d("demo", "Error getting bitmap");
        }
        return bm;
    }

    public class TwitterLogin extends AsyncTask<String, Void, JSONObject> {

        Twitter twitter;

        TwitterLogin (Twitter twitter)
        {
            this.twitter = twitter;
        }

        @Override
        protected void onPostExecute(JSONObject aVoid) {
            super.onPostExecute(aVoid);
            getUserDetailsFromTwitter(aVoid);
        }

        @Override
        protected JSONObject doInBackground(String... params) {
            try
            {
                URL url = new URL(params[0]);
                HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
                con.setRequestMethod("GET");
                signRequest(twitter, con);
                InputStream in = new BufferedInputStream(con.getInputStream());
                object = new JSONObject(IOUtils.toString(in));
                Log.d("object:", object.toString());
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

            return object;
        }
    }

    public void signRequest(Twitter twitter, HttpsURLConnection request)
    {
        OAuthConsumer consumer = new DefaultOAuthConsumer(twitter.getConsumerKey(), twitter.getConsumerSecret());
        consumer.setTokenWithSecret(twitter.getAuthToken(), twitter.getAuthTokenSecret());
        try
        {
            consumer.sign(request);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public class TwitterImage extends AsyncTask<JSONObject, Void, Void>
    {


        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            saveNewUser();

        }

        @Override
        protected Void doInBackground(JSONObject... params) {
            JSONObject object1 = params[0];
            URL url = null;
            try {
                String s = object1.getString("profile_image_url");
                s = s.replace("_normal","");
                url = new URL(s);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                bitmap = BitmapFactory.decodeStream(input);
            } catch (JSONException | IOException e) {
                e.printStackTrace();
            }

            return null;
        }
    }

}