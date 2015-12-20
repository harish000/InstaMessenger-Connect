package com.mad.albumapp;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseInstallation;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SendCallback;
import com.parse.SignUpCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;

public class SignUpActivity extends AppCompatActivity {
    static String profile =null;
    String checked = null;
    String gender;
    private Toolbar toolbar;
    byte[] byteArray;
    String image = "@drawable/camera";
    String imgDecodableString;
    static String editCheck = null;
    static boolean imageCheck = false;
    public static final int pick_Photo = 101;
    ParseFile photoFile;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            // When an Image is picked
            if (requestCode == pick_Photo && resultCode == RESULT_OK && data != null) {
                // Get the Image from data
                Log.d("demo", "Inside if condition");
                Uri selectedImage = data.getData(); //Uri.parse("@drawable/avatar.png");

                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);
                // Log.d(TAG, String.valueOf(bitmap));

                ImageView imageView = (ImageView) findViewById(R.id.profileImage);
                imageView.setImageBitmap(bitmap);

                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byteArray = stream.toByteArray();
                imageCheck = true;
                /*ParseFile photoFile = new ParseFile("userProfile.png",byteArray);
                photoFile.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if(e==null)
                        {
                            Toast.makeText(SignUpActivity.this, "Image Stored", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(SignUpActivity.this, "Error", Toast.LENGTH_SHORT).show();
                        }
                    }
                });*/

            } else {

                image = "@drawable/camera";
                ImageView imageView = (ImageView) findViewById(R.id.profileImage);
                imageView.setImageResource(R.drawable.camera);

                Toast.makeText(this, "You haven't picked Image", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG)
                    .show();
        }


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        Intent intent= getIntent();
        Bundle bundle = intent.getExtras();
        if(bundle!=null)
        {
            editCheck = bundle.getString("edit");
        }
        else {
            editCheck = null;
        }
/*
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        toolbar.setTitle("Sign Up");// Attaching the layout to the toolbar object
        setSupportActionBar(toolbar);
*/
        ((ImageView)findViewById(R.id.profileImage)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_pickphoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent_pickphoto.setType("image/*");
                startActivityForResult(Intent.createChooser(intent_pickphoto, "Select Picture"), pick_Photo);
            }
        });

        profile = "public";
        checked = "n";

        CheckBox checkBox = (CheckBox) findViewById(R.id.checkBox);
        Switch aSwitch = (Switch) findViewById(R.id.switch1);

        checkBox.setChecked(false);
        aSwitch.setChecked(false);

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    checked = "y";
                } else {
                    checked = "n";
                }
            }
        });

        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    profile = "private";
                } else {
                    profile = "public";
                }
            }
        });

        gender = null;
        RadioGroup group = ((RadioGroup) findViewById(R.id.rg));
        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                gender = ((RadioButton) findViewById(checkedId)).getText().toString().trim();
            }
        });

        ((Button) findViewById(R.id.btnSignUp)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String userName = ((EditText) findViewById(R.id.etName)).getText().toString();
                final String emailId = ((EditText) findViewById(R.id.etEmail)).getText().toString();
                final String password = ((EditText) findViewById(R.id.etPassword)).getText().toString();
                final String confPassword = ((EditText) findViewById(R.id.etCnfPwd)).getText().toString();

                if (userName.equals("")) {
                    Toast.makeText(SignUpActivity.this, "Please Provide User Name!", Toast.LENGTH_SHORT).show();
                } else if (emailId.equals("")) {
                    Toast.makeText(SignUpActivity.this, "Please Provide EmailID!", Toast.LENGTH_SHORT).show();
                } else if (password.equals("")) {
                    Toast.makeText(SignUpActivity.this, "Please Provide Password!", Toast.LENGTH_SHORT).show();
                } else if (confPassword.equals("")) {
                    Toast.makeText(SignUpActivity.this, "Please Confirm Password!", Toast.LENGTH_SHORT).show();
                } else if (!imageCheck) {
                    Toast.makeText(SignUpActivity.this, "Please Provide Profile Picture!", Toast.LENGTH_SHORT).show();
                } else if (!password.equals(confPassword)) {
                    Toast.makeText(SignUpActivity.this, "Passwords does not match!", Toast.LENGTH_SHORT).show();
                } else if(gender==null)
                {
                    Toast.makeText(SignUpActivity.this, "Please Select Gender!", Toast.LENGTH_SHORT).show();
                }
                else {
                    Log.d("demo", "all fields verified");

                    if (byteArray != null) {
                        photoFile = new ParseFile("userProfile.png", byteArray);
                    }
                    photoFile.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null) {
                                ParseUser user = new ParseUser();
                                user.setUsername(emailId);
                                user.setPassword(password);
                                user.setEmail(emailId);
                                user.put("user_name", userName);
                                user.put("gender", gender);
                                user.put("profile", profile);
                                user.put("notifications", checked);
                                user.put("image", photoFile);
                                Log.d("demo", "buttonClicked going to sign up");
                                user.signUpInBackground(new SignUpCallback() {
                                    @Override
                                    public void done(ParseException e) {
                                        if (e == null) {
                                            //saveScaledPhoto();
                                            //ParseUser.getCurrentUser().saveInBackground();
                                            ParseInstallation installation = ParseInstallation.getCurrentInstallation();
                                            installation.put("userid", ParseUser.getCurrentUser().getObjectId());
                                            installation.put("notify",checked);
                                            installation.saveInBackground(new SaveCallback() {
                                                @Override
                                                public void done(ParseException e) {
                                                    if(e==null)
                                                    {
                                                        Log.d("demo","installed");
                                                    }
                                                    else
                                                    {
                                                        Log.d("demo",e.getMessage());
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
                                                        Log.d("demo","Push Notification Send!!");
                                                        //Toast.makeText(SignUpActivity.this, "Push Notification Send!!", Toast.LENGTH_SHORT).show();
                                                    } else {
                                                        Toast.makeText(SignUpActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });

                                            Log.d("demo", "sign up done");
                                            Toast.makeText(SignUpActivity.this, "Signed Up Successfully", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(SignUpActivity.this, AppPage.class);
                                            intent.putExtra("userName", ParseUser.getCurrentUser().getObjectId());
                                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            startActivity(intent);
                                            finish();
                                        } else {
                                            Toast.makeText(SignUpActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                            else
                            {

                            }
                        }
                    });
                }
            }
        });

        ((Button) findViewById(R.id.btnCancel)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });

    }

    /*private void saveScaledPhoto() {
        final ParseFile photoFile = new ParseFile("userProfile.png",byteArray);
        photoFile.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e==null) {
                    ParseUser.getCurrentUser().put("photo", photoFile);
                }
                else {
                    Toast.makeText(SignUpActivity.this,
                            "Error saving: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sign_up, menu);
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
