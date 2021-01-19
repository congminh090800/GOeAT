    package com.example.goeat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.goeat.database.PlaceDAO;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class EditActivity extends AppCompatActivity {
    private static int RESULT_LOAD_IMAGE = 1;
    private ImageView food;
    private EditText name, address, tags, phone, opcl, pricerange, pricerangedublicate;
    private String mDistrict;
    private Place instance;
    private PlaceDAO placeDA0;
    private Button save, cancel,delete;
    private ImageButton uploadImg;
    //    //sử dụng SHARED PREFERENCES để lấy địa chỉ hiện tại ở bất cứ class nào, ví dụ bên dưới
//    SharedPreferences sharedPref = getSharedPreferences("GOeAT", Context.MODE_PRIVATE);
//    curAddress=sharedPref.getString("curAddress","Vietnam|Thành phố Hồ Chí Minh|Bình Thạnh");
    private String mTag;
    private int mIndex;
    int foodIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
//        getSupportActionBar().setTitle("");
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setElevation(0);
        //UI INIT
        mIndex = getIntent().getIntExtra("index", -1);
        mDistrict = getIntent().getStringExtra("district");
        InitializeUI();
        instance = FoodlistActivity.listPlaceDistrict.get(mIndex);
        address.setText(instance.getAddress());
        Picasso.get().load(instance.getPhoto()).into(food);
        name.setText(instance.getName());
        tags.setText("TAGS: ");
        for (String tag : instance.getCategories()) {
            tags.append(tag);
            if (tag != instance.getCategories().get(instance.getCategories().size() - 1)) {
                tags.append(", ");
            }
        }
        phone.setText(instance.getPhones().get(0));
        opcl.setText("OPEN/CLOSED: " + instance.getBegin() + " - " + instance.getEnd());
        pricerange.setText("PRICE RANGE: " + instance.getPrice_range().min_price + "-" + instance.getPrice_range().max_price + "(VND)");

        uploadImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                startActivityForResult(i, RESULT_LOAD_IMAGE);
            }
        });


        save.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String holdRange = pricerange.getText().toString();
            holdRange = holdRange.replaceAll("[^a-zA-Z0-9]", " ");
            int i = holdRange.length();
            holdRange = holdRange.substring(13,i);
            i = holdRange.length();
            holdRange =holdRange.substring(0,i-5);

            List<String> tempCateory = new ArrayList<String>(Arrays.asList(tags.toString().split(",")));
            List<String> tempPhone = new ArrayList<String>(Arrays.asList(phone.toString().split(",")));

            Log.d("Nameeee",instance.getName());
            placeDA0.getInstance();
            instance.setName(name.getText().toString());
            Log.d("INSTANCEEEE",instance.getName());
            instance.setAddress(address.getText().toString());
            instance.setPhones(tempPhone);
            instance.setCategories(tempCateory);
            List<String> tempRangeOneTwo = new ArrayList<String>(Arrays.asList(holdRange.split(" ")));
            PriceRange tempRange = new PriceRange();
            tempRange.setMin_price(Integer.parseInt(tempRangeOneTwo.get(0)));
            tempRange.setMax_price(Integer.parseInt(tempRangeOneTwo.get(1)));
            instance.setPrice_range(tempRange);
            Log.d("Addressss",instance.getAddress());

            placeDA0.getInstance().update(mDistrict,instance).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Log.d("Updateee",instance.getName());

                        Toast.makeText(getApplicationContext(),"Cập nhật thành công",Toast.LENGTH_LONG);
                    }
                }
            });
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    placeDA0.getInstance();
                    placeDA0.getInstance().delete(mDistrict,instance.getId()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(getApplicationContext(),"Xóa thành công",Toast.LENGTH_LONG);
                        }
                    });
                    Intent back = new Intent(EditActivity.this,FoodlistActivity.class);
                    startActivity(back);
                }
            });

        }
    });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditActivity.this,FoodlistActivity.class);
                intent.putExtra("district",mDistrict);
                finish();
                startActivity(intent);

            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode,int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            food.setImageBitmap(BitmapFactory.decodeFile(picturePath));
        }}
    void InitializeUI() {
        uploadImg = findViewById(R.id.upload_edit_img);
        save = findViewById(R.id.save_Edit);
        cancel = findViewById(R.id.cancel_Edit);
        food = findViewById(R.id.edit_img);
        name = findViewById(R.id.edit_name);
        address = findViewById(R.id.edit_address);
        tags = findViewById(R.id.edit_tags);
        phone = findViewById(R.id.edit_phone);
        delete = findViewById(R.id.delete_edit);
        opcl = findViewById(R.id.edit_opcl);
        pricerange = findViewById(R.id.edit_pricerange);
    }

}