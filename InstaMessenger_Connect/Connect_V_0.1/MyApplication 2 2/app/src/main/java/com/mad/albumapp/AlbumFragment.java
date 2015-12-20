package com.mad.albumapp;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;



/**
 * A simple {@link Fragment} subclass.
 */
public class AlbumFragment extends Fragment {


    public AlbumFragment() {
        // Required empty public constructor
    }

    Boolean alertReady;
    RecyclerView rcview;
    FragmentRVAdapter c_adapt;
    String privacy;
    View layout;
    private OnFragmentInteractionListener mListener;

      @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_album, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        /*try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }*/
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        privacy = "Public";
        layout = getActivity().getLayoutInflater().inflate(R.layout.alert_layout, null);
        ((Switch) layout.findViewById(R.id.schPrivacy)).setChecked(true);
        ((Switch) layout.findViewById(R.id.schPrivacy)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked) {
                    privacy = "Private";
                } else {
                    privacy = "Public";
                }
            }
        });

        if(ParseUser.getCurrentUser()!=null)
        {

        //MyAlbum
        ParseQuery<Album> myquery = new ParseQuery<Album>("Album");
        myquery.whereEqualTo("owner", ParseUser.getCurrentUser().getObjectId());
        myquery.findInBackground(new FindCallback<Album>() {
            @Override
            public void done(List<Album> objects, ParseException e) {
                RecyclerView rcview = ((RecyclerView) getActivity().findViewById(R.id.rvMyAlbum));
                LinearLayoutManager llmanager = new LinearLayoutManager(getActivity());
                rcview.setLayoutManager(llmanager);
                if (objects != null) {
                    if (objects.size() > 0) {
                        int height = objects.size() * 110;
                        ViewGroup.LayoutParams params = rcview.getLayoutParams();
                        //Changes the height and width to the specified *pixels*
                        params.height = height;
                        c_adapt = new FragmentRVAdapter(objects,"fragment");
                        rcview.setAdapter(c_adapt);
                    } else {
                        ViewGroup.LayoutParams params = rcview.getLayoutParams();
                        //Changes the height and width to the specified *pixels*
                        params.height = 0;
                        params.width = 0;
                        ((TextView) getActivity().findViewById(R.id.txtNoAlbum)).setVisibility(View.VISIBLE);
                    }
                }
            }
        });

        //invitedAlbum
        ParseQuery<Album> query_invi = new ParseQuery<Album>("Album");
            query_invi.whereNotEqualTo("owner", ParseUser.getCurrentUser().getObjectId());
        query_invi.findInBackground(new FindCallback<Album>() {
            @Override
            public void done(List<Album> objects, ParseException e) {
                List<Album> finallist = new ArrayList<Album>();
                RecyclerView rcview = ((RecyclerView) getActivity().findViewById(R.id.rvInvitedAlbum));
                LinearLayoutManager llmanager = new LinearLayoutManager(getActivity());
                rcview.setLayoutManager(llmanager);
                if(objects!=null) {
                    if (objects.size() > 0) {
                        for (Album ald_obj : objects) {
                            List<String> invi_list = ald_obj.getInvitees();
                            if (invi_list.size() > 0) {
                                for (String user : invi_list) {
                                    Log.d("demo",user);
                                    Log.d("demo",ParseUser.getCurrentUser().getObjectId());
                                    if (user.trim().equals(ParseUser.getCurrentUser().getObjectId().trim()) ){
                                        finallist.add(ald_obj);
                                        break;
                                    }
                                }
                            }
                        }
                        if (finallist.size() > 0) {
                            int height = finallist.size() * 110;
                            ViewGroup.LayoutParams params = rcview.getLayoutParams();
                            //Changes the height and width to the specified *pixels*
                            params.height = height;
                            c_adapt = new FragmentRVAdapter(finallist,"fragment");
                            rcview.setAdapter(c_adapt);
                        } else {
                            //rcview.setVisibility(View.INVISIBLE);
                            ViewGroup.LayoutParams params = rcview.getLayoutParams();
                            // Changes the height and width to the specified *pixels*
                            params.height = 0;
                            params.width = 0;
                            ((TextView) getActivity().findViewById(R.id.txtNoAlbumInvite)).setVisibility(View.VISIBLE);
                        }
                    } else {
                        ViewGroup.LayoutParams params = rcview.getLayoutParams();
                        // Changes the height and width to the specified *pixels*
                        params.height = 0;
                        params.width = 0;
                        ((TextView) getActivity().findViewById(R.id.txtNoAlbumInvite)).setVisibility(View.VISIBLE);
                    }
                }
            }
        });

        //Public ParseDetails
        ParseQuery<Album> query = new ParseQuery<Album>("Album");
        query.whereEqualTo("privacy", "Public");
        query.whereNotEqualTo("owner", ParseUser.getCurrentUser().getObjectId());
        query.findInBackground(new FindCallback<Album>() {
            @Override
            public void done(List<Album> objects, ParseException e) {
                RecyclerView rcview = ((RecyclerView) getActivity().findViewById(R.id.rvPublicAlbum));
                LinearLayoutManager llmanager = new LinearLayoutManager(getActivity());
                rcview.setLayoutManager(llmanager);
                if (objects != null) {
                    if (objects.size() > 0) {
                        int height = objects.size() * 110;
                        ViewGroup.LayoutParams params = rcview.getLayoutParams();
                        //Changes the height and width to the specified *pixels*
                        params.height = height;
                        c_adapt = new FragmentRVAdapter(objects,"fragment");
                        rcview.setAdapter(c_adapt);
                    } else {
                        ViewGroup.LayoutParams params = rcview.getLayoutParams();
                        // Changes the height and width to the specified *pixels*
                        params.height = 0;
                        params.width = 0;
                        ((TextView) getActivity().findViewById(R.id.txtNoPubAlbum)).setVisibility(View.VISIBLE);
                    }
                }
            }
        });

        ((ImageView) getActivity().findViewById(R.id.imgAdd)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder alert_getAlbumName = new AlertDialog.Builder(getActivity());
                alert_getAlbumName.setTitle("Settings");
                ImageView vi = new ImageView(getActivity());
                //vi.setImageResource(R.drawable.addalbum);
                //alert_getAlbumName.setView(vi);
                alert_getAlbumName.setView(layout);

                alert_getAlbumName.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    }
                });
                alert_getAlbumName.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // Canceled.
                        dialog.cancel();
                    }
                });
                final AlertDialog alert = alert_getAlbumName.create();

                alertReady = false;
                alert.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialog) {
                        if (alertReady == false) {
                            Button button = alert.getButton(DialogInterface.BUTTON_POSITIVE);
                            button.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Log.d("demo", privacy);
                                    final String name = ((EditText) layout.findViewById(R.id.etAlbumName)).getText().toString().trim();//inputET.getText().toString().trim();
                                    if (name.equals("")) {
                                        Toast.makeText(getActivity(), "Please Enter Album Name!", Toast.LENGTH_SHORT).show();
                                    } else {
                                        ParseQuery<Album> query = new ParseQuery<Album>("Album");
                                        //query.whereEqualTo("owner", ParseUser.getCurrentUser().getObjectId());
                                        query.whereEqualTo("album_name", name);
                                        query.findInBackground(new FindCallback<Album>() {
                                            @Override
                                            public void done(List<Album> objects, ParseException e) {
                                                if (objects.size() > 0) {
                                                    Toast.makeText(getActivity(), "An Album with this name already exists. Please provide different name!", Toast.LENGTH_SHORT).show();
                                                } else {
                                                    ArrayList<String> inv = new ArrayList<String>();
                                                    //inv.add("S91Zl5avf9");
                                                    Album album_obj = new Album();
                                                    album_obj.setOwner(ParseUser.getCurrentUser().getObjectId());
                                                    album_obj.setPrivacy(privacy);
                                                    album_obj.setAlbum_name(name);
                                                    album_obj.setInvitees(inv);
                                                    album_obj.setMod_list(new ArrayList<ParseFile>());
                                                    List<ParseFile> file_list = new ArrayList<>();
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

                                                    Intent intentCreate = new Intent(getActivity(), AlbumActivityIntergrated.class);
                                                    intentCreate.putExtra("from","fragment");
                                                    intentCreate.putExtra("album_name", name);
                                                    //intentCreate.putExtra("privacy", privacy);
                                                    startActivity(intentCreate);
                                                }
                                            }
                                        });
                                    }
                                }
                            });
                            alertReady = true;
                        }
                    }
                });
                alert.show();
                //Intent i = new Intent(getActivity(),Main2Activity.class);
                //getActivity().startActivity(i);
            }
        });
    }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }
}
