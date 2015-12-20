package com.mad.albumapp;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sriharish on 12/4/2015.
 */
public class ListViewAdapter extends ArrayAdapter<ParseObject> {

    Context con;
    List<ParseObject> userList;

    public ListViewAdapter(Context context, ArrayList<ParseObject> objects) {
        super(context,R.layout.message_layout,objects);

        this.con = context;
        this.userList = objects;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        if (convertView == null)
        {
            LayoutInflater inflater = (LayoutInflater) con.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.message_layout, parent, false);
        }

        if(userList.get(position).getString("read").equals("no"))
        {
            convertView.setBackgroundColor(convertView.getResources().getColor(android.R.color.holo_red_light));
        }

        TextView textView = (TextView) convertView.findViewById(R.id.textViewfromUser);
        try {
            textView.setText(userList.get(position).getParseUser("fromUser").fetchIfNeeded().getString("user_name"));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        ((ImageView)convertView.findViewById(R.id.imgdelete)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                ParseQuery<ParseObject> objectParseQuery = new ParseQuery<ParseObject>("messagesObject");
                objectParseQuery.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> objects, ParseException e)
                    {
                        if(e==null)
                        {
                            objects.get(position).deleteInBackground(new DeleteCallback() {
                                @Override
                                public void done(ParseException e) {
                                    if(e==null) {
                                        Log.d("demo", "deleted");
                                        MessageFragment.messageList.remove(position);
                                        notifyDataSetChanged();


                                    }

                                    else
                                        Toast.makeText(v.getContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                        else
                        {
                            Log.d("demo","couldn't find obj to delete");
                        }

                    }
                });
            }
        });
        Log.d("demo", "convert view set");

        return convertView;
    }
}
