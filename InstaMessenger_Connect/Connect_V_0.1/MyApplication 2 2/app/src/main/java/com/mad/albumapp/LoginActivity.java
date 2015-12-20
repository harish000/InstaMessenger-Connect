package com.mad.albumapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class LoginActivity extends AppCompatActivity
{

    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        toolbar.setTitle("Login");// Attaching the layout to the toolbar object
        setSupportActionBar(toolbar);


        ((Button)findViewById(R.id.btnLogin)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailId = ((EditText) findViewById(R.id.etEmail)).getText().toString();
                String password = ((EditText) findViewById(R.id.etPassword)).getText().toString();
                if (emailId.equals("") || emailId.equals(password.equals(""))) {
                    Toast.makeText(LoginActivity.this, "Fields Empty", Toast.LENGTH_LONG).show();
                } else {
                    ParseUser.logInInBackground(emailId, password, new LogInCallback() {
                        @Override
                        public void done(ParseUser user, ParseException e) {
                            if (e == null) {
                                ParseInstallation installation = ParseInstallation.getCurrentInstallation();
                                installation.put("userid", ParseUser.getCurrentUser().getObjectId());
                                installation.put("notify",user.getString("notifications"));
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

                                Intent intent = new Intent(LoginActivity.this, AppPage.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(LoginActivity.this, e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            }
        });

        ((Button)findViewById(R.id.btnCancelLogin)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
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
