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
 * Created by Trishaan on 11/19/15.
 */
public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolderApp> {
    ArrayList<Integer> check_list = new ArrayList<>();
    ArrayList<PhotoStore> store = new ArrayList<>();
    public interface getGalleryImage
    {
        public void getImage();
    }

    getGalleryImage int_obj;
    public CustomAdapter(ArrayList<PhotoStore> check_list, getGalleryImage int_obj) {
        this.store = check_list;
        this.int_obj = int_obj;
    }

    public class ViewHolderApp extends RecyclerView.ViewHolder
    {
        ImageView imgPhoto;
        TextView txtAddPhoto;
        ImageView imgDelete;
        public ViewHolderApp(View itemView) {
            super(itemView);
            imgPhoto = ((ImageView)itemView.findViewById(R.id.imgPhoto));
            txtAddPhoto = ((TextView)itemView.findViewById(R.id.txtAddPhoto));
            imgDelete = ((ImageView)itemView.findViewById(R.id.imgDelete));
        }
    }

    @Override
    public ViewHolderApp onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewalbum, parent, false);
        ViewHolderApp ViewHolderAppObj = new ViewHolderApp(v);
        return ViewHolderAppObj;
    }

    @Override
    public void onBindViewHolder(final ViewHolderApp holder, final int position) {
        final PhotoStore result = store.get(position);
        if(result.getFlag()==0)
        {
            holder.imgPhoto.setImageResource(R.drawable.add);
            holder.txtAddPhoto.setText("Add Photo");
            holder.imgDelete.setVisibility(View.INVISIBLE);
        }

        if(result.getFlag()==1)
        {
            holder.txtAddPhoto.setText("");
            if(AlbumActivityIntergrated.public_flag == false) {
                holder.imgDelete.setVisibility(View.VISIBLE);
                holder.imgDelete.setImageResource(R.drawable.decline);
            }

            ParseFile file = (ParseFile) result.getFile();
            file.getDataInBackground(new GetDataCallback() {
                @Override
                public void done(byte[] data, ParseException e) {
                    if (e == null) {
                        Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                        //Drawable d = new BitmapDrawable(holder.itemView.getResources(),bitmap);
                        holder.imgPhoto.setImageBitmap(bitmap);
                    }
                }
            });
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(((TextView)v.findViewById(R.id.txtAddPhoto)).getText().equals("Add Photo")) {
                    AlbumActivityIntergrated.holder = holder;
                    Intent intent_pickphoto = new Intent(Intent.ACTION_GET_CONTENT);
                    intent_pickphoto.setType("image/*");
                    ((AlbumActivityIntergrated)holder.itemView.getContext()).startActivityForResult(Intent.createChooser(intent_pickphoto, "Select photo"), 101);
                }
                else
                {
                    ParseFile file = (ParseFile) result.getFile();
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
            }
        });

        holder.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("demo", "image at" + String.valueOf(holder.getAdapterPosition()));
                final int remove_index = holder.getAdapterPosition();
                store.remove(holder.getAdapterPosition());
                notifyDataSetChanged();

                ParseQuery<Album> query = new ParseQuery<Album>("Album");
                query.whereEqualTo("album_name",AlbumActivityIntergrated.album_name);
                query.findInBackground(new FindCallback<Album>() {
                    @Override
                    public void done(List<Album> objects, ParseException e) {
                        if (objects.size() > 0) {
                                List<ParseFile> fileList = objects.get(0).getPhotos();
                                fileList.remove(remove_index-1);
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

