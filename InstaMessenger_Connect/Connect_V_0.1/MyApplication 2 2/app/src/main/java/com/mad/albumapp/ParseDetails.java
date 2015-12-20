package com.mad.albumapp;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;

import java.util.List;

/**
 * Created by Sriharish on 11/21/2015.
 */
@ParseClassName("ParseDetails")
public class ParseDetails extends ParseObject
{
    String userId;
    String name;
    ParseFile photoFile;


    String fromUser;
    String toUser;
    String messageDescription;


    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getAlbum_name() {
        return album_name;
    }

    public void setAlbum_name(String album_name) {
        this.album_name = album_name;
    }

    public List<ParseFile> getPhotos() {
        return photos;
    }

    public void setPhotos(List<ParseFile> photos) {
        this.photos = photos;
    }

    public List<String> getInvitees() {
        return invitees;
    }

    public void setInvitees(List<String> invitees) {
        this.invitees = invitees;
    }

    public List<ParseFile> getMod_list() {
        return mod_list;
    }

    public void setMod_list(List<ParseFile> mod_list) {
        this.mod_list = mod_list;
    }

    public String getPrivacy() {
        return privacy;
    }

    public void setPrivacy(String privacy) {
        this.privacy = privacy;
    }

    String owner;
    String album_name;
    List<ParseFile> photos;
    List<String> invitees;
    List<ParseFile> mod_list;
    String privacy;


    public String getFromUser() {
        return fromUser;
    }

    public void setFromUser(String fromUser) {
        this.fromUser = fromUser;
    }

    public String getToUser() {
        return toUser;
    }

    public void setToUser(String toUser) {
        this.toUser = toUser;
    }

    public String getMessageDescription() {
        return messageDescription;
    }

    public void setMessageDescription(String messageDescription) {
        this.messageDescription = messageDescription;
    }



    public ParseFile getPhotoFile() {
        return getParseFile("photo");
    }

    public void setPhotoFile(ParseFile photoFile) {
        put("photo",photoFile);
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
