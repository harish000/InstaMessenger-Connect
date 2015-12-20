package com.mad.albumapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;



import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import android.widget.Toast;

import com.parse.GetDataCallback;
import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseFile;
import com.parse.ParseUser;




public class AppPage extends AppCompatActivity  {

    Bitmap bitmap;
    private DrawerLayout mDrawerLayout;
    ViewPager viewPager;
    FragmentAdapter fragmentAdapter;
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_page);
        int page_no = -1;
        ViewPager viewPager = (ViewPager) findViewById(R.id.pager);

        FragmentAdapter adap = new FragmentAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adap);

        if(getIntent()!=null)
        {
            if(getIntent().getExtras()!=null) {
                if (getIntent().getExtras().getString("page_no") != null) {
                    page_no = Integer.parseInt(getIntent().getExtras().getString("page_no").trim());
                }
            }
        }

        if(page_no>-1)
        {
            viewPager.setCurrentItem(page_no);
        }
        //viewPager.setAdapter(new FragmentAdapter(getSupportFragmentManager()));


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarapp);
        toolbar.setTitle("Connect:)");
        setSupportActionBar(toolbar);



        final ActionBar ab = getSupportActionBar();
/*
        ParseFile imageFile= (ParseFile) ParseUser.getCurrentUser().getParseFile("image");
        imageFile.getDataInBackground(new GetDataCallback() {
            @Override
            public void done(byte[] data, ParseException e) {
                if (e == null) {
                    bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                } else {
                    Toast.makeText(AppPage.this, e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });*/



      /* mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        new SetNavView().execute();
*/

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_app_page, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.logout) {
            //ParseFacebookUtils.
            Toast.makeText(AppPage.this,"Logout Successful", Toast.LENGTH_LONG).show();
            ParseUser.logOutInBackground(new LogOutCallback() {
                @Override
                public void done(ParseException e) {
                    if(e == null)
                    {
                        Toast.makeText(AppPage.this, "Logout Successful", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(AppPage.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                    }
                }
            });
            return true;
        }
        else if(id == R.id.search)
        {
            Intent intent = new Intent(AppPage.this, OtherUserProfile.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



}
