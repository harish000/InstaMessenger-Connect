package com.mad.albumapp;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class OtherUserProfile extends AppCompatActivity {

    static ArrayList<ParseUser> parseUsers;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_user_profile);


        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Loading Users...");
        dialog.show();
        ParseQuery<ParseUser> parseQuery = ParseUser.getQuery();
        parseQuery.whereEqualTo("profile","public");
        parseQuery.whereNotEqualTo("objectId",ParseUser.getCurrentUser().getObjectId());
        parseQuery.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> objects, ParseException e) {
                if(e==null)
                {
                    dialog.dismiss();
                }
                parseUsers = new ArrayList<ParseUser>(objects);
                ListView listView = ((ListView) findViewById(R.id.listviewusers));
                OtherUserProfileAdapter  otherUserProfileAdapter = new OtherUserProfileAdapter(OtherUserProfile.this, R.layout.other_user_profile_details, objects);
                listView.setAdapter(otherUserProfileAdapter);


            }
        });



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_other_user_profile, menu);
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
}
