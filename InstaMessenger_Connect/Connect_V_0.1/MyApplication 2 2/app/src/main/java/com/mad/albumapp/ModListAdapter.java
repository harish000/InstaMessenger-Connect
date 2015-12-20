package com.mad.albumapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Trishaan on 12/4/15.
 */
public class ModListAdapter extends RecyclerView.Adapter<ModListAdapter.ViewHolderApp> {

    List<ParseFile> store = new ArrayList<>();

    public ModListAdapter(List<ParseFile> check_list) {
        this.store = check_list;
    }

    public class ViewHolderApp extends RecyclerView.ViewHolder
    {
        ImageView imgPhoto;
        ImageView imgAccept;
        ImageView imgDecline;
        public ViewHolderApp(View itemView) {
            super(itemView);
            imgPhoto = ((ImageView)itemView.findViewById(R.id.imgPhoto));
            imgAccept = ((ImageView)itemView.findViewById(R.id.imgAccept));
            imgDecline = ((ImageView)itemView.findViewById(R.id.imgDecline));
        }
    }

    @Override
    public ViewHolderApp onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_mod_list, parent, false);
        ViewHolderApp ViewHolderAppObj = new ViewHolderApp(v);
        return ViewHolderAppObj;
    }

    @Override
    public void onBindViewHolder(final ViewHolderApp holder, final int position) {
            final int location = position;
            holder.imgAccept.setImageResource(R.drawable.accept);
            holder.imgDecline.setImageResource(R.drawable.decline);

            ParseFile file = (ParseFile) store.get(position);
            file.getDataInBackground(new GetDataCallback() {
                @Override
                public void done(byte[] data, ParseException e) {
                    if (e == null) {
                        Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                        holder.imgPhoto.setImageBitmap(bitmap);
                    }
                }
            });

        holder.imgAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                ParseQuery<Album> query = new ParseQuery<Album>("Album");
                query.whereEqualTo("album_name", AlbumActivityIntergrated.album_name);
                query.findInBackground(new FindCallback<Album>() {
                    @Override
                    public void done(List<Album> objects, ParseException e) {
                        if (objects.size() > 0) {
                            Log.d("demo", String.valueOf(objects.get(0).getPhotos().size()));
                            Log.d("demo", String.valueOf(objects.get(0).getMod_list().size()));

                            List<ParseFile> fileList = objects.get(0).getPhotos();
                            Log.d("demo", String.valueOf(fileList.size()));
                            fileList.add(0, store.get(location));
                            objects.get(0).setPhotos(fileList);
                            List<ParseFile> modList = objects.get(0).getMod_list();
                            modList.remove(location);
                            objects.get(0).setMod_list(modList);

                            Log.d("demo", String.valueOf(objects.get(0).getPhotos().size()));
                            Log.d("demo", String.valueOf(objects.get(0).getMod_list().size()));
                            objects.get(0).saveInBackground(new SaveCallback() {
                                @Override
                                public void done(ParseException e) {
                                    if (e == null) {
                                        Log.d("demo", "photo accepted");
                                    } else {
                                        Log.d("demo", e.getLocalizedMessage());
                                    }
                                }
                            });
                        }
                    }
                });
                final int remove_index = holder.getAdapterPosition();
                store.remove(holder.getAdapterPosition());
                notifyDataSetChanged();
            }
        });

        holder.imgDecline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                ParseQuery<Album> query = new ParseQuery<Album>("Album");
                query.whereEqualTo("album_name", AlbumActivityIntergrated.album_name);
                query.findInBackground(new FindCallback<Album>() {
                    @Override
                    public void done(List<Album> objects, ParseException e) {
                        if (objects.size() > 0) {
                            Log.d("demo", String.valueOf(objects.get(0).getMod_list().size()));

                            List<ParseFile> modList = objects.get(0).getMod_list();
                            modList.remove(location);
                            objects.get(0).setMod_list(modList);

                            Log.d("demo", String.valueOf(objects.get(0).getMod_list().size()));
                            objects.get(0).saveInBackground(new SaveCallback() {
                                @Override
                                public void done(ParseException e) {
                                    if (e == null) {
                                        Log.d("demo", "photo declined");
                                    } else {
                                        Log.d("demo", e.getLocalizedMessage());
                                    }
                                }
                            });
                        }
                    }
                });
                final int remove_index = holder.getAdapterPosition();
                store.remove(holder.getAdapterPosition());
                notifyDataSetChanged();
            }
        });

        holder.imgPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    ParseFile file = (ParseFile)store.get(position);
                    file.getDataInBackground(new GetDataCallback() {
                        @Override
                        public void done(byte[] data, ParseException e) {
                            if (e == null) {
                                Intent intent_preview = new Intent(holder.itemView.getContext(), PhotoPreview.class);
                                intent_preview.putExtra("photoBytes", data);
                                holder.itemView.getContext().startActivity(intent_preview);
                            }
                        }
                    });

            }
        });
    }

    @Override
    public int getItemCount() {
        return store.size();
    }
}

