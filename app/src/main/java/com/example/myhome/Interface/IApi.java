package com.example.myhome.Interface;

import com.example.myhome.Api;
import com.example.myhome.Model.User;


public interface IApi {
    void createUser(String email, String pass, User user, ISignUp callback);

    void login(String id, String pass, ILogin callback);

    void getCurrentUserData(Api.OnCompleteGetUserData callback);

    void getLikedHouse(Api.OnCompleteGetLikedHouse callback);

    void updateLikedHouse(String s, Api.OnCompleteAddLikedHouse callback);

    void unlikeThisRoom(String path, Api.OnUnlikeComplete callback);
}
