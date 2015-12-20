package com.mad.albumapp;

import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.widget.Toast;

import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseInstallation;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SendCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class AlbumActivityIntergrated extends AppCompatActivity implements CustomAdapter.getGalleryImage {
        public static Uri passUri;
        public static CustomAdapter.ViewHolderApp holder;
        ArrayList<Integer> check_list;
        ArrayList<PhotoStore> photoStore;
        RecyclerView rcview;
        CustomAdapter c_adapt;
        static String album_name;
        Boolean invitee_flag;
        static Boolean public_flag;

        CharSequence[] userList;
        List<ParseUser> user_main;
        List<ParseUser> user_list;
        CharSequence[] inviteeList;
        List<String> invitees;
        private Toolbar toolbar;
        String intent_from;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_album_activity_intergrated);

                toolbar = (Toolbar) findViewById(R.id.tool_bar);
                setSupportActionBar(toolbar);
                final ActionBar ab = getSupportActionBar();

                //invalidateOptionsMenu();

                invitee_flag = false;
                public_flag = false;

                photoStore = new ArrayList<>();

                rcview = (RecyclerView)findViewById(R.id.recyclerview);
                GridLayoutManager glManager = new GridLayoutManager(AlbumActivityIntergrated.this,2);
                rcview.setLayoutManager(glManager);

                String album_name_intent = null;
                if(getIntent()!=null)
                {
                        if(getIntent().getExtras().getString("album_name")!=null)
                        {
                                album_name_intent = getIntent().getExtras().getString("album_name");
                                album_name = album_name_intent;
                        }
                        if(getIntent().getExtras().getString("from")!=null)
                        {
                                intent_from =  getIntent().getExtras().getString("from").trim();
                        }
                }

                ParseQuery<ParseUser> user_query = ParseUser.getQuery();
                user_query.whereNotEqualTo("objectId",ParseUser.getCurrentUser().getObjectId());
                user_query.findInBackground(new FindCallback<ParseUser>() {
                        @Override
                        public void done(List<ParseUser> objects, ParseException e) {
                                if(objects.size()>0) {
                                        user_list = objects;
                                }
                        }
                });

       /* //should be placed in Album Fragment
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy_MMM_dd_HH:mm:ss");
        Date date = new Date();
        //Log.d("demo", ParseUser.getCurrentUser().getUsername() + ":" + dateFormat.format(date));
        //album_name = ParseUser.getCurrentUser().getUsername()+"-"+dateFormat.format(date);

        album_name = "Arthi-2015_Dec_04_20:38:09";*/

                setTitle(album_name);



                if(!album_name_intent.equals(null) && !album_name_intent.equals(""))
                {
                        final String current_user = ParseUser.getCurrentUser().getObjectId();
                        ParseQuery<Album> query = new ParseQuery<Album>("Album");
                        query.whereEqualTo("album_name", album_name_intent);
                        query.findInBackground(new FindCallback<Album>() {
                                @Override
                                public void done(List<Album> objects, ParseException e) {
                                        if (objects.size() == 0) {
                                                //new album
                                                PhotoStore photo_obj = new PhotoStore();
                                                photo_obj.setFlag(0);
                                                photo_obj.setFile(null);
                                                photoStore.add(photo_obj);
                                                //GridLayoutManager glManager = new GridLayoutManager(AlbumActivityIntergrated.this,2);
                                                c_adapt = new CustomAdapter(photoStore, AlbumActivityIntergrated.this);
                                                rcview.setAdapter(c_adapt);
                                        } else {
                                                //existing album
                                                Album object = objects.get(0);
                                                if (object.getOwner().trim().equals(current_user.trim())) {
                                                        Log.d("demo","owner");
                                                        PhotoStore photo_obj = new PhotoStore();
                                                        photo_obj.setFlag(0);
                                                        photo_obj.setFile(null);
                                                        photoStore.add(photo_obj);
                                                        if (object.getPhotos().size() > 0) {
                                                                for (ParseFile file : object.getPhotos()) {
                                                                        PhotoStore photo_obj1 = new PhotoStore();
                                                                        photo_obj1.setFlag(1);
                                                                        photo_obj1.setFile(file);
                                                                        photoStore.add(photo_obj1);
                                                                }
                                                        }
                                                        c_adapt = new CustomAdapter(photoStore, AlbumActivityIntergrated.this);
                                                        rcview.setAdapter(c_adapt);
                                                } else {
                                                        if (object.getInvitees().size() > 0) {
                                                                Log.d("demo","invitee entry");
                                                                for (String invitee : object.getInvitees()) {
                                                                        if (invitee.trim().equals(current_user.trim())) {
                                                                                Log.d("demo","invitee");
                                                                                invitee_flag = true;
                                                                                public_flag = true;
                                                                                invalidateOptionsMenu();
                                                                                PhotoStore photo_obj = new PhotoStore();
                                                                                photo_obj.setFlag(0);
                                                                                photo_obj.setFile(null);
                                                                                photoStore.add(photo_obj);
                                                                                if (object.getPhotos().size() > 0) {
                                                                                        for (ParseFile file : object.getPhotos()) {
                                                                                                PhotoStore photo_obj1 = new PhotoStore();
                                                                                                photo_obj1.setFlag(1);
                                                                                                photo_obj1.setFile(file);
                                                                                                photoStore.add(photo_obj1);
                                                                                        }
                                                                                }
                                                                                c_adapt = new CustomAdapter(photoStore, AlbumActivityIntergrated.this);
                                                                                rcview.setAdapter(c_adapt);
                                                                                break;
                                                                        }
                                                                }
                                                        }
                                                        if (invitee_flag == false) {
                                                                Log.d("demo","public");
                                                                public_flag = true;
                                                                invalidateOptionsMenu();
                                                                if (object.getPhotos().size() > 0) {
                                                                        for (ParseFile file : object.getPhotos()) {
                                                                                PhotoStore photo_obj1 = new PhotoStore();
                                                                                photo_obj1.setFlag(1);
                                                                                photo_obj1.setFile(file);
                                                                                photoStore.add(photo_obj1);
                                                                        }
                                                                }
                                                                c_adapt = new CustomAdapter(photoStore, AlbumActivityIntergrated.this);
                                                                rcview.setAdapter(c_adapt);
                                                        }
                                                }
                                        }
                                }
                        });
                }

        /*ParseUser user = new ParseUser();
        user.setEmail("bava@1.c");
        user.setUsername("Bava");
        user.setPassword("pwd");
        user.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
                if(e!=null)
                {
                    Log.d("demo",e.getLocalizedMessage());
                }
            }
        });*/


            /*ParseUser.logInInBackground("Bava", "pwd", new LogInCallback() {
                @Override
                public void done(ParseUser user, ParseException e) {
                    if(e==null)
                    {
                        Log.d("demo","Logged in");
                    }
                }
            });*/

        }

        @Override
        public void getImage() {
                Log.d("demo", "in intent call");
                Intent intent_pickphoto = new Intent(Intent.ACTION_GET_CONTENT);
                intent_pickphoto.setType("image/*");
                startActivityForResult(Intent.createChooser(intent_pickphoto,"Select photo"), 101);
        }

        protected void onActivityResult(int requestCode, int resultCode, Intent data) {
                super.onActivityResult(requestCode, resultCode, data);
                if (requestCode == 101 && resultCode == RESULT_OK && data != null && data.getData() != null) {

                        Uri selectedImage = data.getData();
                        InputStream iStream = null;
                        byte[] inputData =null;
                        try {
                                iStream = getContentResolver().openInputStream(selectedImage);
                        } catch (FileNotFoundException e) {
                                e.printStackTrace();
                        }
                        try {
                                inputData = getBytes(iStream);
                        } catch (IOException e) {
                                e.printStackTrace();
                        }

                        ParseFile file = new ParseFile("image.png", inputData);
                        final ParseFile file_final = file;
                        try {
                                file.save();
                        } catch (ParseException e) {
                                e.printStackTrace();
                        }

                        if(invitee_flag==false) {
                                PhotoStore photo_obj = new PhotoStore();
                                photo_obj.setFlag(0);
                                photo_obj.setFile(null);
                                photoStore.set(0, photo_obj);

                                final PhotoStore photo_obj1 = new PhotoStore();
                                photo_obj1.setFlag(1);
                                photo_obj1.setFile(file_final);
                                photoStore.add(1, photo_obj1);

                                c_adapt.notifyDataSetChanged();
                                Log.d("demo", String.valueOf(photoStore.size()));
                        }
                        else
                        {
                                Toast.makeText(AlbumActivityIntergrated.this, "Photo will be added upon Owner Verification", Toast.LENGTH_SHORT).show();
                        }


                        ParseQuery<Album> query = new ParseQuery<Album>("Album");
                        //query.whereEqualTo("owner",ParseUser.getCurrentUser().getObjectId());
                        query.whereEqualTo("album_name",album_name);
                        query.findInBackground(new FindCallback<Album>() {
                                @Override
                                public void done(final List<Album> objects, ParseException e) {
                                        if (objects.size() > 0) {
                                                if (invitee_flag == false) {
                                                        List<ParseFile> fileList = objects.get(0).getPhotos();
                                                        fileList.add(0,file_final);
                                                        objects.get(0).setPhotos(fileList);
                                                        Log.d("demo", String.valueOf(objects.get(0).getPhotos().size()));
                                                        objects.get(0).saveInBackground(new SaveCallback() {
                                                                @Override
                                                                public void done(ParseException e) {
                                                                        if (e == null) {
                                                                                Log.d("demo", "album updated");
                                                                        } else {
                                                                                Log.d("demo", e.getLocalizedMessage());
                                                                        }
                                                                }
                                                        });
                                                } else {
                                                        List<ParseFile> fileList = objects.get(0).getMod_list();
                                                        fileList.add(0,file_final);
                                                        objects.get(0).setMod_list(fileList);
                                                        Log.d("demo", String.valueOf(objects.get(0).getMod_list().size()));
                                                        objects.get(0).saveInBackground(new SaveCallback() {
                                                                @Override
                                                                public void done(ParseException e) {
                                                                        if (e == null) {
                                                                                Log.d("demo", "modlist updated");

                                                                                //Push Notification
                                                                                ParseQuery<ParseInstallation> pushQuery = ParseInstallation.getQuery();
                                                                                pushQuery.whereEqualTo("notify","y");
                                                                                pushQuery.whereEqualTo("userid", objects.get(0).getOwner().toString().trim());
                                                                                String data = "{\n" +
                                                                                        "    \"data\": {\n" +
                                                                                        "        \"message\": \"Hello! Please review photo add request!!\",\n" +
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
                                                                                                        //Toast.makeText(AlbumActivityIntergrated.this, "Push Notification Send!!", Toast.LENGTH_SHORT).show();
                                                                                                } else {
                                                                                                        Toast.makeText(AlbumActivityIntergrated.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                                                                                }
                                                                                        }
                                                                                });

                                                                        } else {
                                                                                Log.d("demo", e.getLocalizedMessage());
                                                                        }
                                                                }
                                                        });
                                                }
                                        } else {
                                                ArrayList<String> inv = new ArrayList<String>();
                                                //inv.add("S91Zl5avf9");
                                                Album album_obj = new Album();
                                                album_obj.setOwner(ParseUser.getCurrentUser().getObjectId());
                                                //album_obj.setPrivacy("Public");
                                                album_obj.setAlbum_name(album_name);
                                                album_obj.setInvitees(inv);
                                                album_obj.setMod_list(new ArrayList<ParseFile>());
                                                List<ParseFile> file_list = new ArrayList<>();
                                                file_list.add(0,file_final);
                                                album_obj.setPhotos(file_list);
                                                album_obj.saveInBackground(new SaveCallback() {
                                                        @Override
                                                        public void done(ParseException e) {
                                                                if (e == null) {
                                                                        Log.d("demo", "album created");
                                                                } else {
                                                                        Log.d("demo", e.getLocalizedMessage());
                                                                }
                                                        }
                                                });
                                        }
                                }
                        });
                }
        }

        public byte[] getBytes(InputStream inputStream) throws IOException {
                ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
                int bufferSize = 1024;
                byte[] buffer = new byte[bufferSize];

                int len = 0;
                while ((len = inputStream.read(buffer)) != -1) {
                        byteBuffer.write(buffer, 0, len);
                }
                return byteBuffer.toByteArray();
        }

        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
                // Inflate the menu; this adds items to the action bar if it is present.
                if(invitee_flag==false && public_flag==false)
                {
                        getMenuInflater().inflate(R.menu.menu_album_activity_intergrated, menu);
                }
                return true;
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
                // Handle action bar item clicks here. The action bar will
                // automatically handle clicks on the Home/Up button, so long
                // as you specify a parent activity in AndroidManifest.xml.
                int id = item.getItemId();

                //noinspection SimplifiableIfStatement
                if (id == R.id.action_chg_privacy) {
                        final AlertDialog.Builder  privacyDialog = new AlertDialog.Builder(AlbumActivityIntergrated.this);
                        final CharSequence[] privacyList = {"Public","Private"};

                        privacyDialog.setTitle("Make Album").setItems(privacyList, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, final int which) {
                                        ParseQuery<Album> query = new ParseQuery<Album>("Album");
                                        query.whereEqualTo("album_name",AlbumActivityIntergrated.album_name);
                                        query.findInBackground(new FindCallback<Album>() {
                                                @Override
                                                public void done(List<Album> objects, ParseException e) {
                                                        if (objects.size() > 0) {
                                                                objects.get(0).setPrivacy(privacyList[which].toString());
                                                                Log.d("demo", String.valueOf(privacyList[which].toString()));
                                                                objects.get(0).saveInBackground(new SaveCallback() {
                                                                        @Override
                                                                        public void done(ParseException e) {
                                                                                if (e == null) {
                                                                                        Log.d("demo", "privacy updated");
                                                                                } else {
                                                                                        Log.d("demo", e.getLocalizedMessage());
                                                                                }
                                                                        }
                                                                });
                                                        }
                                                }
                                        });
                                }
                        });

                        privacyDialog.show();

                        return true;
                }

                if(id==R.id.action_invite)
                {
                        final AlertDialog.Builder  userDialog = new AlertDialog.Builder(AlbumActivityIntergrated.this);
                        ParseQuery<ParseUser> query = ParseUser.getQuery();
                        query.whereNotEqualTo("objectId", ParseUser.getCurrentUser().getObjectId());
                        query.findInBackground(new FindCallback<ParseUser>() {
                                @Override
                                public void done(final List<ParseUser> objects, com.parse.ParseException e) {
                                        if(objects.size()>0) {
                                                user_main = objects;
                                                Collections.sort(user_main, new Comparator<ParseUser>() {
                                                        @Override
                                                        public int compare(ParseUser lhs, ParseUser rhs) {
                                                                return lhs.getString("username").trim().compareTo(rhs.getString("username").trim());
                                                        }
                                                });
                                                int array_size = objects.size();
                                                userList = new String[array_size];
                                                int i = 0;
                                                for (ParseUser user_object : user_main) {
                                                        userList[i] = user_object.getString("user_name").trim();
                                                        i++;
                                                }

                                                        userDialog.setTitle("Users").setCancelable(true).setItems(userList, new DialogInterface.OnClickListener() {
                                                                @Override
                                                                public void onClick(DialogInterface dialog, final int which) {
                                                                        ParseQuery<Album> query = new ParseQuery<Album>("Album");
                                                                        query.whereEqualTo("album_name", AlbumActivityIntergrated.album_name);
                                                                        query.findInBackground(new FindCallback<Album>() {
                                                                                @Override
                                                                                public void done(List<Album> objects, ParseException e) {
                                                                                        if (objects.size() > 0) {
                                                                                                List<String> inviteeList = objects.get(0).getInvitees();
                                                                                                inviteeList.add(user_main.get(which).getObjectId());
                                                                                                objects.get(0).setInvitees(inviteeList);
                                                                                                Log.d("demo", String.valueOf(objects.get(0).getInvitees().size()));
                                                                                                objects.get(0).saveInBackground(new SaveCallback() {
                                                                                                        @Override
                                                                                                        public void done(ParseException e) {
                                                                                                                if (e == null) {
                                                                                                                        Log.d("demo", "invitee updated");

                                                                                                                        //Push Notification
                                                                                                                        ParseQuery<ParseInstallation> pushQuery = ParseInstallation.getQuery();
                                                                                                                        pushQuery.whereEqualTo("userid", user_main.get(which).getObjectId());
                                                                                                                        pushQuery.whereEqualTo("notify","y");
                                                                                                                        String data = "{\n" +
                                                                                                                                "    \"data\": {\n" +
                                                                                                                                "        \"message\": \"Hello! You are invited to view an album.!!\",\n" +
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
                                                                                                                                                //Toast.makeText(AlbumActivityIntergrated.this, "Push Notification Send!!", Toast.LENGTH_SHORT).show();
                                                                                                                                        } else {
                                                                                                                                                Toast.makeText(AlbumActivityIntergrated.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                                                                                                                        }
                                                                                                                                }
                                                                                                                        });
                                                                                                                } else {
                                                                                                                        Log.d("demo", e.getLocalizedMessage());
                                                                                                                }
                                                                                                        }
                                                                                                });
                                                                                        }
                                                                                }
                                                                        });
                                                                }
                                                        });

                                        }
                                        else
                                        {
                                                userDialog.setTitle("Users").setCancelable(true).setMessage("No Users Found").show();
                                        }
                                        userDialog.show();
                                }
                        });
                        return true;
                }

                if(id==R.id.action_invite_stats)
                {
                        final AlertDialog.Builder  userDialog = new AlertDialog.Builder(AlbumActivityIntergrated.this);
                        ParseQuery<Album> query = new ParseQuery<Album>("Album");
                        query.whereEqualTo("album_name", AlbumActivityIntergrated.album_name);
                        query.whereEqualTo("owner",ParseUser.getCurrentUser().getObjectId());
                        query.findInBackground(new FindCallback<Album>() {
                                @Override
                                public void done(List<Album> objects, ParseException e) {
                                        if(objects.size()>0)
                                        {
                                                Album album_obj = objects.get(0);
                                                invitees = album_obj.getInvitees();
                                                if(invitees.size()>0) {
                                                        int array_size = invitees.size();
                                                        inviteeList = new String[array_size];
                                                        int i = 0;
                                                        for (String invitee : invitees) {
                                                                /*ParseQuery<ParseUser> query = ParseUser.getQuery();
                                                                query.whereEqualTo("objectId",invitee.trim());
                                                                query.findInBackground(new FindCallback<ParseUser>() {
                                                                        @Override
                                                                        public void done(List<ParseUser> objects, ParseException e) {
                                                                                if(objects.size()>0)
                                                                                {
                                                                                        inviteeList[j] = objects.get(0).getString("user_name");
                                                                                }
                                                                        }
                                                                });*/
                                                                ParseUser req_user = new ParseUser();
                                                                for(ParseUser user: user_list)
                                                                {
                                                                        if(user.getObjectId().equals(invitee))
                                                                        {
                                                                                req_user = user;
                                                                                break;
                                                                        }
                                                                }
                                                                inviteeList[i] = req_user.getString("user_name");
                                                                i++;
                                                        }
                                                        userDialog.setTitle("Invitees").setCancelable(true).setItems(inviteeList, new DialogInterface.OnClickListener() {
                                                                @Override
                                                                public void onClick(DialogInterface dialog, int which) {

                                                                }
                                                        });

                                                }
                                                else
                                                {
                                                        userDialog.setTitle("Invitees").setCancelable(true).setMessage("No Invitees Found").show();
                                                }
                                                userDialog.show();
                                        }
                                }
                        });

                        return true;
                }

                if(id==R.id.action_mod_list)
                {
                        ParseQuery<Album> query = new ParseQuery<Album>("Album");
                        query.whereEqualTo("album_name", album_name);
                        query.findInBackground(new FindCallback<Album>() {
                                @Override
                                public void done(List<Album> objects, ParseException e) {
                                        if(objects.size()>0)
                                        {
                                                if(objects.get(0).getMod_list().size()>0)
                                                {
                                                        Intent viewModList = new Intent(AlbumActivityIntergrated.this,ModList.class);
                                                        viewModList.putExtra("album_name",album_name);
                                                        startActivity(viewModList);
                                                }
                                                else
                                                {
                                                        Toast.makeText(AlbumActivityIntergrated.this,"No photos to view!",Toast.LENGTH_SHORT).show();
                                                }
                                        }
                                }

                        });
                        return true;
                }

                if(id==R.id.action_delete)
                {
                        AlertDialog.Builder builder = new AlertDialog.Builder(AlbumActivityIntergrated.this);
                        builder.setMessage("Are you sure you want to delete?").setCancelable(true);

                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogAlert, int which) {
                                        ParseQuery<Album> query = new ParseQuery<Album>("Album");
                                        query.whereEqualTo("album_name", album_name);
                                        query.findInBackground(new FindCallback<Album>() {
                                                @Override
                                                public void done(List<Album> objects, ParseException e) {
                                                        if (objects.size() > 0) {
                                                                Album album_object = objects.get(0);
                                                                album_object.deleteInBackground(new DeleteCallback() {
                                                                        @Override
                                                                        public void done(ParseException e) {
                                                                                if (e == null) {
                                                                                        Log.d("demo", "Deleted Successfully!");
                                                                                        Toast.makeText(AlbumActivityIntergrated.this,"Deleted Successfully!",Toast.LENGTH_SHORT).show();
                                                                                        Intent backIntent = new Intent(AlbumActivityIntergrated.this, AppPage.class);
                                                                                        backIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                                                        backIntent.putExtra("page_no", "1");
                                                                                        startActivity(backIntent);
                                                                                        finish();
                                                                                } else {
                                                                                        Log.d("demo", e.getLocalizedMessage());
                                                                                }
                                                                        }
                                                                });
                                                        }
                                                }
                                        });
                                }
                        });
                        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogAlert, int which) {
                                        dialogAlert.cancel();
                                }
                        });

                        AlertDialog alert = builder.create();
                        alert.show();
                        return true;
                }
                return super.onOptionsItemSelected(item);
        }

        @Override
        public boolean onKeyDown(int keyCode, KeyEvent event) {
                if ((keyCode == KeyEvent.KEYCODE_BACK)) {
                        if(intent_from!=null)
                        {
                                if(intent_from.equals("fragment"))
                                {
                                        Intent backIntent = new Intent(AlbumActivityIntergrated.this, AppPage.class);
                                        backIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        backIntent.putExtra("page_no","1");
                                        startActivity(backIntent);
                                }
                                else
                                {
                                        /*Intent backIntent = new Intent(AlbumActivityIntergrated.this, OtherUserProfile.class);
                                        backIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(backIntent);*/
                                }
                        }

                        finish();
                }
                return super.onKeyDown(keyCode, event);
        }
}
