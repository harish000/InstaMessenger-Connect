package com.mad.albumapp;

import android.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.List;

public class ViewUserAlbum extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_user_album);

        final RecyclerView rcview = (RecyclerView)findViewById(R.id.rvMyAlbum);
        LinearLayoutManager llmgr = new LinearLayoutManager(ViewUserAlbum.this);
        rcview.setLayoutManager(llmgr);

        if(getIntent()!=null)
        {
            if(getIntent().getExtras().getString("user_id")!=null)
            {
                ParseQuery<Album> query = new ParseQuery<Album>("Album");
                query.whereEqualTo("owner",getIntent().getExtras().getString("user_id").trim());
                query.whereEqualTo("privacy","Public");
                query.findInBackground(new FindCallback<Album>() {
                    @Override
                    public void done(List<Album> objects, ParseException e) {
                        if(objects.size()>0)
                        {
                            FragmentRVAdapter adapter = new FragmentRVAdapter(objects,"search");
                            rcview.setAdapter(adapter);
                        }
                    }
                });
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_view_user_album, menu);
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
