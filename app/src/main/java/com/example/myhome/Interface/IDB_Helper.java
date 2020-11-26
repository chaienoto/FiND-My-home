package com.example.myhome.Interface;

import com.example.myhome.DB_help;
import com.example.myhome.Model.User;

import java.util.Map;

public interface IDB_Helper {
    void initialNewUser(User user, Map<String, Object> like_init, Map<String, Object> rent_init);

    void getUserData(String uid, DB_help.OnGetUserData callback);

    void getLikedHouse(DB_help.OnGetLikedData callback);

    boolean updateLikedHouse(String s);
}
