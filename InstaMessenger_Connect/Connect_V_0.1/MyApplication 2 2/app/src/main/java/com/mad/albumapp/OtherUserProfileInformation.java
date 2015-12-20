package com.mad.albumapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class OtherUserProfileInformation extends AppCompatActivity {

    Bitmap bitmap = null;
    ArrayList<ParseUser> yourClassList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_user_profile_information);


        int position = getIntent().getIntExtra("index", 0);
        yourClassList = OtherUserProfile.parseUsers;
        Log.d("demo", yourClassList.get(0).getString("user_name"));

        ((TextView)findViewById(R.id.othername)).setText(yourClassList.get(position).getString("user_name"));
        ((TextView)findViewById(R.id.otheremail)).setText(yourClassList.get(position).getUsername());


        if(yourClassList.get(position).getString("gender").toLowerCase().equals("male"))
        {
            ((RadioButton)findViewById(R.id.othermale)).setChecked(true);
            ((RadioButton)findViewById(R.id.othermale)).setClickable(false);
            ((RadioButton)findViewById(R.id.othermale)).setFocusable(false);
        }
        else
        {
            ((RadioButton)findViewById(R.id.otherfemale)).setChecked(true);
            ((RadioButton)findViewById(R.id.otherfemale)).setClickable(false);
            ((RadioButton)findViewById(R.id.otherfemale)).setFocusable(false);
        }


        if(yourClassList.get(position).getString("profile").toLowerCase().equals("private"))
        {
            ((Switch)findViewById(R.id.otherswitch)).setChecked(true);
        }
        else
        {
            ((Switch)findViewById(R.id.otherswitch)).setChecked(false);
        }
        ((Switch)findViewById(R.id.otherswitch)).setClickable(false);
        ((Switch)findViewById(R.id.otherswitch)).setFocusable(false);

        if(yourClassList.get(position).getString("notifications").toLowerCase().equals("y"))
        {
            ((CheckBox)findViewById(R.id.othercheckbox)).setChecked(true);
        }
        else
        {
            ((CheckBox)findViewById(R.id.othercheckbox)).setChecked(true);
        }
        ((CheckBox)findViewById(R.id.othercheckbox)).setClickable(false);
        ((CheckBox)findViewById(R.id.othercheckbox)).setFocusable(false);





        ParseFile imageFile= (ParseFile) yourClassList.get(position).getParseFile("image");
                //getParseUser.getCurrentUser().getParseFile("image");
        imageFile.getDataInBackground(new GetDataCallback() {
            @Override
            public void done(byte[] data, ParseException e) {
                if (e == null) {
                    bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                    ((ImageView) findViewById(R.id.otherimage)).setImageBitmap(bitmap);
                } else {
                    Toast.makeText(OtherUserProfileInformation.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });





    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_other_user_profile_information, menu);
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
