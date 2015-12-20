package com.mad.albumapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.List;

public class ModList extends AppCompatActivity {

    RecyclerView rcview;
    ModListAdapter c_adapt;
    String album_name_intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mod_list);

        rcview = ((RecyclerView)findViewById(R.id.recyclerview));
        GridLayoutManager grid_layout = new GridLayoutManager(ModList.this,2);
        rcview.setLayoutManager(grid_layout);

        album_name_intent = null;
        if (getIntent() != null) {
            if (getIntent().getExtras().getString("album_name") != null) {
                album_name_intent = getIntent().getExtras().getString("album_name");
            }
        }

        ParseQuery<Album> query = new ParseQuery<Album>("Album");
        query.whereEqualTo("album_name", album_name_intent);
        query.findInBackground(new FindCallback<Album>() {
            @Override
            public void done(List<Album> objects, ParseException e) {
                if(objects.size()>0)
                {
                    Album album_object = objects.get(0);
                    c_adapt = new ModListAdapter(objects.get(0).getMod_list());
                    rcview.setAdapter(c_adapt);
                    /*if(album_object.getMod_list().size()>0)
                    {

                    }
                    else
                    {

                    }*/
                }
            }

        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            Log.d("demo",album_name_intent);
            Intent backIntent = new Intent(ModList.this, AlbumActivityIntergrated.class);
            backIntent.putExtra("album_name", album_name_intent.trim());
            backIntent.putExtra("from","fragment");
            backIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(backIntent);
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_mod_list, menu);
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
