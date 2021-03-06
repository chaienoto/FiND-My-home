package com.example.myhome.Fragment;


import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myhome.Adapter.picture_adapter;
import com.example.myhome.LoginActivity;
import com.example.myhome.R;
import com.example.myhome.Store;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import javax.annotation.Nullable;
import static android.app.Activity.RESULT_OK;


public class Create_house_Fragment extends Fragment {
    EditText house_owner, house_phone, house_address, house_price, house_water, house_electric, house_P_lot, house_net, house_service, house_detail;
    Spinner house_city, house_district, house_type;
    Button house_add_img, house_add_house;
    RecyclerView picture_house_recycleview;
    ArrayList<String> hType = new ArrayList<String>();
    ArrayList<Uri> house_picture = new ArrayList<Uri>();
    ArrayList<String> house_picture_id = new ArrayList<String>();
    String house_path = "";
    ArrayList<String> pName = new ArrayList<String>();
    final ArrayList<String> pID = new ArrayList<String>();
    ArrayList<String> dName = new ArrayList<String>();
    ArrayList<String> dID = new ArrayList<String>();
    String cpID, cdID, ID;

    public Create_house_Fragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_house, container, false);
        house_owner = view.findViewById(R.id.house_owner);
        house_phone = view.findViewById(R.id.house_phone);
        house_address = view.findViewById(R.id.house_address);
        house_price = view.findViewById(R.id.house_price);
        house_water = view.findViewById(R.id.house_water);
        house_electric = view.findViewById(R.id.house_electric);
        house_P_lot = view.findViewById(R.id.house_P_lot);
        house_net = view.findViewById(R.id.house_net);
        house_service = view.findViewById(R.id.house_service);
        house_city = view.findViewById(R.id.house_city);
        house_district = view.findViewById(R.id.house_district);
        house_type = view.findViewById(R.id.house_type);
        house_add_img = view.findViewById(R.id.house_add_img);
        house_add_house = view.findViewById(R.id.house_add_house);
        house_detail = view.findViewById(R.id.house_detail);
        picture_house_recycleview = view.findViewById(R.id.picture_house_recycleview);



            house_owner.setText(Store.name);
            house_phone.setText(Store.phoneNumber);

            hType.add("KTX");
            hType.add("Phòng đơn không chung chủ");
            hType.add("Dãy phòng trọ đơn");
            hType.add("Phòng share");
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(Create_house_Fragment.this.getContext(), android.R.layout.simple_list_item_1, hType);
            house_type.setAdapter(adapter);

            pName.add("Hà Nội");
            pName.add("Cần Thơ");
            pName.add("TP. Buôn Mê Thuật");
            pName.add("TP. Hồ Chí Minh");
            pName.add("Đà Nẵng");
            pID.add("hanoi");
            pID.add("cantho");
            pID.add("tpbmt");
            pID.add("hcm");
            pID.add("danang");
            ArrayAdapter<String> adapter0 = new ArrayAdapter<String>(Create_house_Fragment.this.getContext(), android.R.layout.simple_list_item_1, pName);
            house_city.setAdapter(adapter0);
            house_city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    cpID = pID.get(position);
                    dID.clear();
                    dName.clear();
                    final FirebaseStorage storage = FirebaseStorage.getInstance();
                    final FirebaseFirestore db = FirebaseFirestore.getInstance();
                    CollectionReference collectionReference = db.collection(cpID);
                    collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                            for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                                if (doc.getId() != null) {
                                    dID.add(doc.getId());
                                    dName.add(String.valueOf(doc.get("name")));
                                }
                            }
                            ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(Create_house_Fragment.this.getContext(), android.R.layout.simple_list_item_1, dName);
                            house_district.setAdapter(adapter1);
                            // lấy id của cái quận tại đây, những câu lệnh tiếp theo viết trong phần này mới chạy dc.
                            house_district.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    cdID = dID.get(house_district.getSelectedItemPosition());
                                    //lấy mấy cái ngoài Spinner
                                    house_add_img.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent gallary = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                                            startActivityForResult(gallary, 999);
                                        }
                                    });
                                    house_add_house.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            house_picture_id.clear();


                                            for (Uri uri:house_picture) {

                                                final ProgressDialog progressDialog = new ProgressDialog(getContext());
                                                progressDialog.setTitle("Uploading");
                                                progressDialog.show();

                                                StorageReference storageRef = storage.getReference();
                                                final Uri file = Uri.fromFile(new File(String.valueOf(uri)));
                                                UUID img= UUID.randomUUID();
                                                house_picture_id.add(img.toString());
                                                StorageReference riversRef = storageRef.child("images/"+img);
                                                riversRef.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                                    @Override
                                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                        progressDialog.dismiss();
                                                    }
                                                }).addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        progressDialog.dismiss();
                                                    }
                                                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                                    @Override
                                                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                                        //calculating progress percentage
                                                        double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                                                        //displaying percentage in progress dialog
                                                        progressDialog.setMessage("Uploaded Picture: "+file.getLastPathSegment()+" :" + ((int) progress) + "%");
                                                    }
                                                });
                                            }
                                            Map<String, Object> house = new HashMap<>();
                                            house.put("house_owner", house_owner.getText().toString());
                                            house.put("house_phone", house_phone.getText().toString());
                                            house.put("house_address", house_address.getText().toString());
                                            house.put("house_city", house_city.getSelectedItem().toString());
                                            house.put("house_district", house_district.getSelectedItem().toString());
                                            house.put("house_type", house_type.getSelectedItem().toString());
                                            house.put("house_price", house_price.getText().toString());
                                            house.put("house_water", house_water.getText().toString());
                                            house.put("house_electric", house_electric.getText().toString());
                                            house.put("house_P_lot", house_P_lot.getText().toString());
                                            house.put("house_net", house_net.getText().toString());
                                            house.put("house_service", house_service.getText().toString());
                                            house.put("house_detail", house_detail.getText().toString());
                                            house.put("house_picture_id", house_picture_id);
                                            UUID hID= UUID.randomUUID();
                                            house_path = pID.get(house_city.getSelectedItemPosition())+"/"+dID.get(house_district.getSelectedItemPosition())+"/house/"+hID;
                                            db.collection(cpID).document(cdID).collection("house").document(hID.toString()).set(house);
                                            DocumentReference rentHelper = db.collection("user").document(Store.id).collection("store").document("rent");
                                            rentHelper.update("rent_house_id", FieldValue.arrayUnion(house_path)).isSuccessful();
                                            Toast.makeText(getContext(), "Post Successfully", Toast.LENGTH_SHORT).show();
                                        }
                                    });

                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {

                                }

                            });
                        }
                    });
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 999) {
            house_picture.add(data.getData());
            picture_adapter adapter = new picture_adapter(Create_house_Fragment.this.getContext(), house_picture);
            LinearLayoutManager manager = new LinearLayoutManager(Create_house_Fragment.this.getContext(), LinearLayoutManager.HORIZONTAL, false);
            picture_house_recycleview.setLayoutManager(manager);
            picture_house_recycleview.setAdapter(adapter);

        }
    }
}
