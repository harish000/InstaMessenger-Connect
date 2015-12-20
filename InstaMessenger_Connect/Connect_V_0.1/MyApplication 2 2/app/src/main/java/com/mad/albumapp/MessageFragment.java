package com.mad.albumapp;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SendCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class MessageFragment extends Fragment
{
    static ArrayList<ParseObject> messageList;
    static ArrayList<String> users;
    List<ParseUser> userList;
    List<ParseUser> userList_reply;
    ListViewAdapter adapter;
    ArrayList<String> user_reply = new ArrayList<>();
    ParseObject object;
    Boolean reply;

    public MessageFragment() {
        // Required empty public constructor
    }

    static String toUser;
    static String description;
    final Context context = getActivity();
    ListView listView;

    View view;
    static AlertDialog.Builder builder;
    static AlertDialog alertDialog;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {




        final ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setMessage("Loading Messages...");

        View view = inflater.inflate(R.layout.fragment_message, container, false);
        Log.d("demo", "inside Oncreateview Created");

        ParseQuery<ParseUser> parseUserParseQuery = ParseUser.getQuery();
        parseUserParseQuery.whereEqualTo("objectId", ParseUser.getCurrentUser().getObjectId());
        parseUserParseQuery.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> objects, ParseException e) {
                if(e== null)
                {

                    //dialog.show();
                    ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("messagesObject");
                    query.whereEqualTo("toUser",objects.get(0));
                    query.findInBackground(new FindCallback<ParseObject>() {
                        @Override
                        public void done(List<ParseObject> objects, ParseException e) {
                            if (e == null) {
                                messageList = new ArrayList<ParseObject>(objects);
                                Log.d("demo","messageList Reterivec");
                                Log.d("demo", messageList.toString());
                                settingListView();
                                //dialog.dismiss();

                            }
                            else
                                Toast.makeText(getActivity(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                else {
                    Toast.makeText(getActivity(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });



        Log.d("demo", "thread executed");

        return view;

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

        users = new ArrayList<>();

        reply = false;
        userList = new ArrayList<>();

        final FloatingActionButton actionButton = (FloatingActionButton) getActivity().findViewById(R.id.fab);

        //Getting list of all users
        final ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(final List<ParseUser> objects, ParseException e) {
                if (e == null) {
                    Log.d("demo", "user list reterived");

                    for (int i = 0; i < objects.size(); i++) {
                        if (!objects.get(i).getString("user_name").equals(ParseUser.getCurrentUser().getString("user_name")) && !objects.get(i).getString("profile").equals("private")) {
                            userList.add(objects.get(i));
                            users.add(objects.get(i).getString("user_name"));
                        }
                        else
                            continue;
                    }

                    actionButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            reply = false;
                            composeMessageMethod();

                        }
                    });

                }
            }
        });
        //Functionality for floating action button - new message
    }

    private void composeMessageMethod() {
        builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.alert_dialog, null);
        builder.setView(view);
        builder.setTitle("New Message");

        final Spinner spinner = (Spinner) view.findViewById(R.id.spinner);

        ArrayAdapter<String> adapter = null;
        if(!reply) {
            adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, users);
        }
        else
        {
            adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item,user_reply);
        }
        spinner.setAdapter(adapter);

        EditText editText = (EditText) view.findViewById(R.id.etMsg);
        alertDialog = builder.create();
        alertDialog.show();



        view.findViewById(R.id.btnSend).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                description = ((EditText) view.findViewById(R.id.etMsg)).getText().toString();
                ParseUser newToUser = new ParseUser();

                if(!reply) {
                    if ((spinner.getSelectedItemPosition()) != AdapterView.INVALID_POSITION) {
                        toUser = users.get(spinner.getSelectedItemPosition());
                        newToUser = userList.get(spinner.getSelectedItemPosition());
                    }
                }
                else
                {
                    if ((spinner.getSelectedItemPosition()) != AdapterView.INVALID_POSITION) {
                        toUser = user_reply.get(spinner.getSelectedItemPosition());
                        newToUser = userList_reply.get(spinner.getSelectedItemPosition());
                    }
                }

                final ParseUser touserobj = newToUser;
                            //new saveMessagesinBg().execute();
                            object = new ParseObject("messagesObject");
                            object.put("toUser", newToUser);
                            object.put("fromUser", ParseUser.getCurrentUser());
                            object.put("description", description);
                            object.put("read", "no");
                            object.saveInBackground(new SaveCallback() {
                                @Override
                                public void done(ParseException e) {
                                    if (e == null) {
                                        Toast.makeText(getActivity(), "Message Sent", Toast.LENGTH_LONG).show();
                                        Log.d("message", "toUser: " + toUser + " desc: " + description);
                                        alertDialog.dismiss();

                                        //Push Notification
                                        ParseQuery<ParseInstallation> pushQuery = ParseInstallation.getQuery();
                                        pushQuery.whereEqualTo("userid", touserobj.getObjectId());
                                        pushQuery.whereEqualTo("notify","y");
                                        String data = "{\n" +
                                                "    \"data\": {\n" +
                                                "        \"message\": \"Hello! You have a new message!!\",\n" +
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
                                                    //Toast.makeText(getActivity(), "Push Notification Send!!", Toast.LENGTH_SHORT).show();
                                                } else {
                                                    Toast.makeText(getActivity(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                    } else {
                                        Toast.makeText(getActivity(), e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                                        alertDialog.dismiss();
                                    }
                                }
                            });

            }
        });
        view.findViewById(R.id.btnCancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
    }


    private void settingListView()
    {
        user_reply = new ArrayList<>();
        userList_reply = new ArrayList<>();
        listView = (ListView) getActivity().findViewById(R.id.listView);
        adapter = new ListViewAdapter(getActivity(),messageList);
        Log.d("demo", "passing adapter");
        listView.setAdapter(adapter);
        adapter.setNotifyOnChange(true);

        Log.d("demo", "adapter passed");
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, final int position, long id) {

                ParseObject msg_obj = messageList.get(position);
                msg_obj.put("read", "yes");
                msg_obj.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            //Toast.makeText(getActivity(), "Clicked ", Toast.LENGTH_SHORT).show();
                            final AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
                            LayoutInflater inflater = getActivity().getLayoutInflater();
                            final View view1 = inflater.inflate(R.layout.alert_dialog_view_message, null);
                            builder1.setView(view1);
                            builder1.setTitle("New Message");
                            final AlertDialog alertDialog1 = builder1.create();
                            try {
                                ((TextView) view1.findViewById(R.id.fromUserName)).setText(messageList.get(position).getParseUser("fromUser").fetchIfNeeded().getString("user_name"));
                            } catch (ParseException ex) {
                                ex.printStackTrace();
                            }

                            ((TextView)view1.findViewById(R.id.messageBody)).setText(messageList.get(position).getString("description"));
                            alertDialog1.show();
                            view1.findViewById(R.id.reply).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    reply=true;
                                    try {
                                        user_reply.add(messageList.get(position).getParseUser("fromUser").fetchIfNeeded().getString("user_name"));
                                    } catch (ParseException e1) {
                                        e1.printStackTrace();
                                    }
                                    try {
                                        userList_reply.add(messageList.get(position).getParseUser("fromUser").fetchIfNeeded());
                                    } catch (ParseException e1) {
                                        e1.printStackTrace();
                                    }
                                    alertDialog1.dismiss();
                                    composeMessageMethod();
                                }
                            });
                            view1.findViewById(R.id.cancelView).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    alertDialog1.dismiss();
                                }
                            });


                            Log.d("demo", "read");
                            messageList.get(position).put("read", "yes");
                            adapter = new ListViewAdapter(getActivity(),messageList);
                            listView.setAdapter(adapter);

                        } else {
                            Log.d("demo",e.getLocalizedMessage());
                        }
                    }
                });
            }
        });
    }

}
