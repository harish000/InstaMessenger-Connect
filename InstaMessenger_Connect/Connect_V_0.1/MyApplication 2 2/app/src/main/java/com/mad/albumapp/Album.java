package com.mad.albumapp;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;

import java.util.List;

/**
 * Created by Trishaan on 12/3/15.
 */
@ParseClassName("Album")
public class Album extends ParseObject {
    String owner;
    String album_name;
    List<ParseFile> photos;
    List<String> invitees;
    List<ParseFile> mod_list;
    String privacy;

    public String getPrivacy() {
        return getString("privacy");
    }

    public void setPrivacy(String privacy) {
       put("privacy",privacy);
    }

    public List<ParseFile> getMod_list() {
        return getList("mod_list");
    }

    public void setMod_list(List<ParseFile> mod_list) {
        put("mod_list",mod_list);
    }

    public String getAlbum_name() {
        return getString("album_name");
    }

    public void setAlbum_name(String album_name) {
        put("album_name",album_name);
    }

    public List<String> getInvitees() {
        return getList("invitees");
    }

    public void setInvitees(List<String> invitees) {
        put("invitees",invitees);
    }

    public String getOwner() {
        return getString("owner");
    }

    public void setOwner(String owner) {
        put("owner",owner);
    }

    public List<ParseFile> getPhotos() {
        return getList("photos");
    }

    public void setPhotos(List<ParseFile> photos) {
        put("photos",photos);
    }
}
