package com.mad.albumapp;

import android.content.Intent;
import android.graphics.Bitmap;
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

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseInstallation;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

import java.io.ByteArrayOutputStream;
import java.util.List;

public class UserProfileEditActivity extends AppCompatActivity {
    static String profile =null;
    String checked =null;
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

                ImageView imageView = (ImageView) findViewById(R.id.profileImageedit);
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
                ImageView imageView = (ImageView) findViewById(R.id.profileImageedit);
                imageView.setImageResource(R.drawable.camera);

                Toast.makeText(this, "You haven't picked Image", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, e.getLocalizedMessage(), Toast.LENGTH_LONG)
                    .show();
        }


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile_edit);
      //  Toast.makeText(UserProfileEditActivity.this, "Inside UserProfile EDit", Toast.LENGTH_SHORT).show();
        Log.d("demo", "Inside User PRofile Edit");
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
        ((ImageView)findViewById(R.id.profileImageedit)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_pickphoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent_pickphoto.setType("image/*");
                startActivityForResult(Intent.createChooser(intent_pickphoto, "Select Picture"), pick_Photo);
            }
        });


        profile = "public";
        checked = "n";

        final CheckBox checkBox = (CheckBox) findViewById(R.id.checkBoxnew);
        Switch aSwitch = (Switch) findViewById(R.id.switch1new);

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

        gender=null;
        RadioGroup group = ((RadioGroup) findViewById(R.id.rgnew));
        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                gender = ((RadioButton) findViewById(checkedId)).getText().toString().trim();
            }
        });

        ((Button)findViewById(R.id.btnupdate)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String userName = ((EditText) findViewById(R.id.etNamenew)).getText().toString();
                final String emailId = ((EditText) findViewById(R.id.etEmailnew)).getText().toString();
                final String password = ((EditText) findViewById(R.id.etPasswordnew)).getText().toString();
                final String confPassword = ((EditText) findViewById(R.id.etCnfPwdnew)).getText().toString();


                if (userName.equals("")) {
                    Toast.makeText(UserProfileEditActivity.this, "Please Provide User Name!", Toast.LENGTH_SHORT).show();
                } else if (emailId.equals("")) {
                    Toast.makeText(UserProfileEditActivity.this, "Please Provide EmailID!", Toast.LENGTH_SHORT).show();
                } else if (password.equals("")) {
                     .makeText(UserProfileEditActivity.this, "Please Provide Password!", Toast.LENGTH_SHORT).show();
                } else if (confPassword.equals("")) {
                    Toast.makeText(UserProfileEditActivity.this, "Please Confirm Password!", Toast.LENGTH_SHORT).show();
                } else if (!imageCheck) {
                    Toast.makeText(UserProfileEditActivity.this, "Please Provide Profile Picture!", Toast.LENGTH_SHORT).show();
                } else if (!password.equals(confPassword)) {
                    Toast.makeText(UserProfileEditActivity.this, "Password does not match", Toast.LENGTH_SHORT).show();
                } else if (gender == null) {
                    Toast.makeText(UserProfileEditActivity.this, "Please Select Gender!", Toast.LENGTH_SHORT).show();
                } else {
                    Log.d("demo", "all fields verified");

                    if (byteArray != null) {
                        photoFile = new ParseFile("userProfile.png", byteArray);
                    }
                    photoFile.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null) {
                                ParseUser user = ParseUser.getCurrentUser();
                                user.setUsername(emailId);
                                user.setPassword(password);
                                user.setEmail(emailId);
                                user.put("user_name", userName);
                                user.put("gender", gender);
                                user.put("profile", profile);
                                user.put("notifications", checked);
                                user.put("image", photoFile);
                                Log.d("demo", "buttonClicked going to sign up");
                                user.saveInBackground(new SaveCallback() {
                                    @Override
                                    public void done(ParseException e) {
                                        if (e == null) {
                                            ParseQuery<ParseInstallation> insQuery = ParseInstallation.getQuery();
                                            insQuery.whereEqualTo("userid", ParseUser.getCurrentUser().getObjectId());
                                            insQuery.findInBackground(new FindCallback<ParseInstallation>() {
                                                @Override
                                                public void done(List<ParseInstallation> objects, ParseException e) {
                                                    if (objects != null) {
                                                        if (objects.size() > 0) {
                                                            ParseInstallation obj = objects.get(0);
                                                            obj.put("notify", checked);
                                                            obj.saveInBackground(new SaveCallback() {
                                                                @Override
                                                                public void done(ParseException e) {
                                                                    if (e == null) {
                                                                        Log.d("demo", "installation object updated");
                                                                    } else {
                                                                        Log.d("demo", e.getLocalizedMessage());
                                                                    }
                                                                }
                                                            });
                                                        }
                                                    }
                                                }
                                            });

                                            //saveScaledPhoto();
                                            //ParseUser.getCurrentUser().saveInBackground();
                                            Log.d("demo", "update done");
                                            Toast.makeText(UserProfileEditActivity.this, "Updated Successfully", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(UserProfileEditActivity.this, AppPage.class);
                                            intent.putExtra("userName", ParseUser.getCurrentUser().getObjectId());
                                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            startActivity(intent);
                                            finish();
                                        } else {
                                            Toast.makeText(UserProfileEditActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            } else {

                            }
                        }
                    });
                }
            }
        });

        ((Button) findViewById(R.id.btnCancelnew)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserProfileEditActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });

    }

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
