package com.example.myhome;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myhome.Interface.ISignUp;
import com.example.myhome.Model.User;

public class SignUpActivity extends AppCompatActivity implements ISignUp {
    EditText edt_username,edt_password,edt_passwordagain,
            edt_fullname,edt_phone;
    Button btn_signup,btn_signin;
    Api api = new Api();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        bindingView();

        btn_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               startActivity(LoginActivity.class);
            }
        });
        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = edt_username.getText().toString();
                String password = edt_password.getText().toString();
                String name = edt_fullname.getText().toString();
                String phone = edt_phone.getText().toString();
                String imgUrl = "";
                User user =  new User(name, phone, imgUrl);
                if (TextUtils.isEmpty(email))
                ToastMessage("Please enter  email...");
                if (TextUtils.isEmpty(password))
                    ToastMessage("Please enter  password...");
                 else api.createUser(email, password,user,SignUpActivity.this);

            }

        });

    }

    private void bindingView() {
        edt_username = findViewById(R.id.edt_username);
        edt_password = findViewById(R.id.edt_password);
        edt_passwordagain = findViewById(R.id.edt_passwordagain);
        edt_fullname = findViewById(R.id.edt_fullname);
        edt_phone = findViewById(R.id.edt_phone);
        btn_signup = findViewById(R.id.btn_signup);
        btn_signin = findViewById(R.id.btn_signin);
        edt_fullname = findViewById(R.id.edt_fullname);
        edt_phone = findViewById(R.id.edt_phone);
    }

    private void ToastMessage(String m){
        Toast.makeText(SignUpActivity.this, m, Toast.LENGTH_SHORT).show();
    }
    private void startActivity(Class c){
        Intent intent = new Intent(SignUpActivity.this,c);
        startActivity(intent);
        finish();
    }
    @Override
    public void onCreateUserSuccessful() {
        ToastMessage("Account Created Successful");
        startActivity(MainActivity.class);
    }

    @Override
    public void onCreateUserFail(Exception e) {
        ToastMessage("Error" + e.toString());
    }
}
