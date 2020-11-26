package com.example.myhome;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

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
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    DrawerLayout drawer;
    Toolbar toolbar;
    NavigationView navigationView;
    ImageView profile_pic;
    TextView profile_name;
    Api api = new Api();
    Boolean isLoggedIn = false;



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

        //check login
        isLoggedIn = api.isUserLogin();

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
                        fragClass=Like_Fragment.class;
                        break;
                    case R.id.nav_create_house_fragment:
                        fragClass=Rent_Fragment.class;
                        break;
                    case R.id.nav_login_activity:
                        Intent intent = new Intent(MainActivity.this,LoginActivity.class);
                        startActivity(intent);
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

    private void bindingView() {
        drawer=(DrawerLayout)findViewById(R.id.drawer_layout);
        toolbar=(Toolbar)findViewById(R.id.toolbar);
        //anh xa nav
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
    private  void getData(){
        // lấy thông tin từ fb
        GraphRequest request = GraphRequest.newMeRequest(
                AccessToken.getCurrentAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(final JSONObject object, GraphResponse response) {
                        try {
                            //kiểm tra xem đã tồn tại cái nào trong db của mình chưa
                            final FirebaseFirestore db = FirebaseFirestore.getInstance();
                            CollectionReference userRef = db.collection("user");
                            // bắt đầu tìm
                            userRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
                                @Override
                                public void onEvent(@javax.annotation.Nullable QuerySnapshot queryDocumentSnapshots, @javax.annotation.Nullable FirebaseFirestoreException e) {
                                    int check = 0; // biến kiểm tra
                                     for (QueryDocumentSnapshot doc: queryDocumentSnapshots){
                                        try {
                                            if (doc.getId()== object.getString("id")) check = 1; // check = 1 là có rồi thì thôi
                                        } catch (JSONException e1) {
                                            e1.printStackTrace();
                                        }
                                    }
                                }
                            });
                            profile_name.setText(object.getString("name"));
                            Uri uri= Uri.parse((String) object.getJSONObject("picture").getJSONObject("data").get("url"));
                            Picasso.with(MainActivity.this).load(uri).into(profile_pic);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,email,gender,birthday,picture{url}");
        request.setParameters(parameters);
        request.executeAsync();
    }
}
