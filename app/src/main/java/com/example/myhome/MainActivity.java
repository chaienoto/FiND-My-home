package com.example.myhome;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.myhome.Fragment.Cities_Fragment;
import com.example.myhome.Fragment.Like_Fragment;
import com.example.myhome.Fragment.Rent_Fragment;
import com.example.myhome.Model.User;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {
    DrawerLayout drawer;
    Toolbar toolbar;
    NavigationView navigationView;
    ImageView profile_pic;
    TextView profile_name;
    Api api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bindingView();
        //gan toolbar + icon menu
        setSupportActionBar(toolbar);
        ActionBar ab=getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.ic_menu);
        ab.setDisplayHomeAsUpEnabled(true);
        api= new Api();
        api.getCurrentUserData(new Api.OnCompleteGetUserData() {
            @Override
            public void onCompleteGetData(User user) {
                if (user==null) {
                    resetStore();
                    return;
                }
                Store.login = true;
                Store.name = user.Name;
                Store.phoneNumber = user.phoneNumber;
                Store.imgUrl = user.imageUrl;
                Toast.makeText(MainActivity.this, "Well comeback " + user.Name, Toast.LENGTH_SHORT).show();
                profile_name.setText(user.Name);
                navigationView.getMenu().findItem(R.id.nav_login_activity).setTitle("Logout");
//                Uri uri= Uri.parse(user.imageUrl);
//                Picasso.with(MainActivity.this).load(uri).into(profile_pic);
            }
        });
        // gan Fragment mac định
        FragmentManager manager = getSupportFragmentManager();
        try {
            manager.beginTransaction().replace(R.id.flContent,Cities_Fragment.class.newInstance()).commit();
            setTitle("Tìm Phòng Trọ");
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        //chuyen frag tren nav_menu
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment;
                Class fragClass = null;
                switch (item.getItemId()){
                    case R.id.nav_like_fragment:
                        if (Store.login) fragClass=Like_Fragment.class;
                        else Toast.makeText(MainActivity.this, "You need to login for used this feature", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.nav_create_house_fragment:
                        if (Store.login) fragClass=Rent_Fragment.class;
                        else Toast.makeText(MainActivity.this, "You need to login for used this feature", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.nav_login_activity:
                        if (Store.login) {
                            logout();
                        } else {
                            Intent intent = new Intent(MainActivity.this,LoginActivity.class);
                            startActivity(intent);
                        }
                        break;
                    case R.id.nav_cities_fragment:
                    default: fragClass=Cities_Fragment.class;
                }
                try {
                    if (fragClass!=null) {
                        fragment = (Fragment) fragClass.newInstance();
                        FragmentManager manager = getSupportFragmentManager();
                        manager.beginTransaction().replace(R.id.flContent, fragment).commit();
                        item.setChecked(true);
                        setTitle(item.getTitle());
                    }
                    drawer.closeDrawers();

                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                return false;
            }
        });
    }

    private void logout() {
        new Api().logout();
        resetStore();
        Toast.makeText(MainActivity.this, "Logout Success", Toast.LENGTH_SHORT).show();
    }

    private void resetStore() {
        Store.login = false;
        Store.name = "";
        Store.id = "";
        Store.phoneNumber = "";
        Store.imgUrl = "";
        profile_name.setText("You not login yet");
        navigationView.getMenu().findItem(R.id.nav_login_activity).setTitle("Sign in");
    }

    private void bindingView() {
        drawer=(DrawerLayout)findViewById(R.id.drawer_layout);
        toolbar=(Toolbar)findViewById(R.id.toolbar);
        navigationView=findViewById(R.id.nav_view);
        profile_pic = (ImageView) navigationView.getHeaderView(0).findViewById(R.id.imageView);
        profile_name = (TextView) navigationView.getHeaderView(0).findViewById(R.id.nav_user_login);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==android.R.id.home) {
            drawer.openDrawer(GravityCompat.START);
        }
        return super.onOptionsItemSelected(item);
    }

}
