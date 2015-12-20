package com.mad.albumapp;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class UserProfileFragment extends Fragment {


    public UserProfileFragment() {
        // Required empty public constructor
    }
    Bitmap bitmap;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_user_profile, container, false);



        return view;
    }



    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        ((EditText) getActivity().findViewById(R.id.etNameedit)).setText(ParseUser.getCurrentUser().getString("user_name"));
        ((EditText)getActivity().findViewById(R.id.etNameedit)).setFocusable(false);
        ((EditText)getActivity().findViewById(R.id.etNameedit)).setClickable(false);
        ((EditText) getActivity().findViewById(R.id.etEmailedit)).setText(ParseUser.getCurrentUser().getUsername());
        ((EditText)getActivity().findViewById(R.id.etEmailedit)).setFocusable(false);
        ((EditText)getActivity().findViewById(R.id.etEmailedit)).setClickable(false);

        if(ParseUser.getCurrentUser().getString("gender")!=null) {

            if (ParseUser.getCurrentUser().getString("gender").toLowerCase().equals("male")) {
                ((RadioButton) getActivity().findViewById(R.id.rbMaleedit)).setChecked(true);
                ((RadioButton) getActivity().findViewById(R.id.rbMaleedit)).setClickable(false);
                ((RadioButton) getActivity().findViewById(R.id.rbMaleedit)).setFocusable(false);
            } else {
                ((RadioButton) getActivity().findViewById(R.id.rbFemaleedit)).setChecked(true);
                ((RadioButton) getActivity().findViewById(R.id.rbFemaleedit)).setClickable(false);
                ((RadioButton) getActivity().findViewById(R.id.rbFemaleedit)).setFocusable(false);
            }
        }
        ((RadioGroup)getActivity().findViewById(R.id.rgedit)).setClickable(false);
        ((RadioGroup)getActivity().findViewById(R.id.rgedit)).setFocusable(false);

        if(ParseUser.getCurrentUser().getString("profile")!=null) {
            if (ParseUser.getCurrentUser().getString("profile").toLowerCase().equals("private")) {
                ((Switch) getActivity().findViewById(R.id.switch1edit)).setChecked(true);
            } else {
                ((Switch) getActivity().findViewById(R.id.switch1edit)).setChecked(false);
            }
        }
        ((Switch)getActivity().findViewById(R.id.switch1edit)).setClickable(false);
        ((Switch)getActivity().findViewById(R.id.switch1edit)).setFocusable(false);

        if(ParseUser.getCurrentUser().getString("notifications")!=null) {
            if (ParseUser.getCurrentUser().getString("notifications").toLowerCase().equals("y")) {
                ((CheckBox) getActivity().findViewById(R.id.checkBoxedit)).setChecked(true);
            } else {
                ((CheckBox) getActivity().findViewById(R.id.checkBoxedit)).setChecked(false);
            }
        }
        ((CheckBox)getActivity().findViewById(R.id.checkBoxedit)).setClickable(false);
        ((CheckBox)getActivity().findViewById(R.id.checkBoxedit)).setFocusable(false);


        if (ParseUser.getCurrentUser().getParseFile("image")!=null) {
            ParseFile imageFile = (ParseFile) ParseUser.getCurrentUser().getParseFile("image");
            imageFile.getDataInBackground(new GetDataCallback() {
                @Override
                public void done(byte[] data, ParseException e) {
                    if (e == null) {
                        bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                        ((ImageView) getActivity().findViewById(R.id.imageedit)).setImageBitmap(bitmap);
                    } else {

                    }
                }
            });
        }

        ((FloatingActionButton)getActivity().findViewById(R.id.floatingActionButtonedit)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), UserProfileEditActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.putExtra("edit", "edit");
                startActivity(intent);
                getActivity().finish();
            }
        });



    }

}
