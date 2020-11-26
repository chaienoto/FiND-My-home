package com.example.myhome.Interface;

public interface ILogin {
    void loginSuccess(String email);

    void loginFail(Exception e);
}
