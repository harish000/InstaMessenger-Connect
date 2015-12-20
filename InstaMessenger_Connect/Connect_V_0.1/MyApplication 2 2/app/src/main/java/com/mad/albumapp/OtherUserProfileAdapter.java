package com.mad.albumapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sriharish on 12/12/2015.
 */
public class OtherUserProfileAdapter extends ArrayAdapter<ParseUser>
{
    Context mContext;
    int mResource;
    ArrayList<ParseUser> mObject;
    public OtherUserProfileAdapter(Context context, int resource, List<ParseUser> objects) {
        super(context, resource, objects);

        this.mContext = context;
        this.mResource = resource;
        this.mObject = new ArrayList<>(objects);
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {


        if (convertView == null)
        {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.other_user_profile_details, parent, false);
        }

        ((TextView)convertView.findViewById(R.id.otherusername)).setText(mObject.get(position).getString("user_name"));
        ((Button)convertView.findViewById(R.id.otheruserprofile)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(mContext, OtherUserProfileInformation.class);
                intent.putExtra("index", position);
                v.getContext().startActivity(intent);
            }
        });

        AlertDialog.Builder alert_getAlbumName = new AlertDialog.Builder(convertView.getContext());
        alert_getAlbumName.setTitle("Alert").setMessage("No Albums Exist").setCancelable(true);
        final AlertDialog alert = alert_getAlbumName.create();

        ((Button)convertView.findViewById(R.id.otheruseralbum)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                ParseQuery<Album> query = new ParseQuery<Album>("Album");
                query.whereEqualTo("owner",mObject.get(position).getObjectId());
                query.whereEqualTo("privacy", "Public");
                query.findInBackground(new FindCallback<Album>() {
                    @Override
                    public void done(List<Album> objects, ParseException e) {
                        if (objects.size() > 0) {
                            Intent intent = new Intent(mContext, ViewUserAlbum.class);
                            intent.putExtra("user_id", mObject.get(position).getObjectId());
                            v.getContext().startActivity(intent);
                        } else {
                            alert.show();
                        }
                    }
                });
            }
        });

        return convertView;

    }
}
