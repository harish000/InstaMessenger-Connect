package com.mad.albumapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sriharish on 12/9/2015.
 */
public class FragmentRVAdapter extends RecyclerView.Adapter<FragmentRVAdapter.ViewHolderApp> {

    List<Album> store = new ArrayList<>();
    String from;

    public FragmentRVAdapter(List<Album> check_list, String from ) {
        this.store = check_list;
        this.from= from;
    }

    public class ViewHolderApp extends RecyclerView.ViewHolder
    {
        ImageView imgAlbumPhotos;
        TextView txtAlbumTitle;
        TextView photoCount;
        public ViewHolderApp(View itemView) {
            super(itemView);
            imgAlbumPhotos = ((ImageView)itemView.findViewById(R.id.imgAlbumPics));
            txtAlbumTitle = ((TextView)itemView.findViewById(R.id.txtAlbumName));
            photoCount = ((TextView)itemView.findViewById(R.id.txtPhotoCount));
        }
    }

    @Override
    public ViewHolderApp onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_album_details, parent, false);
        ViewHolderApp ViewHolderAppObj = new ViewHolderApp(v);
        return ViewHolderAppObj;
    }

    @Override
    public void onBindViewHolder(final ViewHolderApp holder, final int position) {
        final int location = position;
        final Album album_object = store.get(position);
        holder.txtAlbumTitle.setText(album_object.getAlbum_name().trim());
        int count = album_object.getPhotos().size();
        if(count>0)
        {
            holder.photoCount.setText(String.valueOf(count) + " Photos");
        }
        else
        {
            holder.photoCount.setText("Empty Album");
        }
        final ArrayList<Drawable> drawableList = new ArrayList<>();
        if(count>0)
        {
            ParseFile file = (ParseFile) album_object.getPhotos().get(0);

            file.getDataInBackground(new GetDataCallback() {
                @Override
                public void done(byte[] data, ParseException e) {
                    if (e == null) {
                        Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                        holder.imgAlbumPhotos.setImageBitmap(bitmap);
                    }
                }
            });
        }
        else
        {
            holder.imgAlbumPhotos.setImageResource(R.drawable.empty_album);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(album_object.getOwner().equals(ParseUser.getCurrentUser().getObjectId())) {
                    Intent intentCreate = new Intent(holder.itemView.getContext(), AlbumActivityIntergrated.class);
                    intentCreate.putExtra("album_name", album_object.getAlbum_name().trim());
                    intentCreate.putExtra("from", from);
                    holder.itemView.getContext().startActivity(intentCreate);
                }
                else
                {
                    if (album_object.getPhotos().size() > 0) {
                        Intent intentCreate = new Intent(holder.itemView.getContext(), AlbumActivityIntergrated.class);
                        intentCreate.putExtra("album_name", album_object.getAlbum_name().trim());
                        intentCreate.putExtra("from", from);
                        holder.itemView.getContext().startActivity(intentCreate);
                    } else {
                        Toast.makeText(holder.itemView.getContext(), "Album is Empty!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }


        /*AnimationDrawable animation = new AnimationDrawable();
        animation.addFrame(holder.itemView.getResources().getDrawable(R.drawable.block),1);
        animation.addFrame(holder.itemView.getResources().getDrawable(R.drawable.accept), 1);
        *//*for(Drawable d : drawableList)
        {
            animation.addFrame(d,1);
        }*//*
        animation.setOneShot(false);
        if(drawableList.size()>0) {
            holder.imgAlbumPhotos.setImageDrawable(drawableList.get(0));//.setBackgroundDrawable(animation);
        }

        // start the animation!
        animation.start();*/

    @Override
    public int getItemCount() {
        return store.size();
    }
}
