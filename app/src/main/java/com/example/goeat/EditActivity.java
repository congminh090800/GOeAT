    package com.example.goeat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroupOverlay;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.goeat.database.PlaceDAO;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class EditActivity extends AppCompatActivity {
    private static int RESULT_LOAD_IMAGE = 1;
    private ImageView food;
    private Button submit;

    private EditText name, address, tags, phone, opcl, pricerange, imageLINK;
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
        if(mIndex==-1){AddFood();}else {loadFood();}

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
    void AddFood() {
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditActivity.this,FoodlistActivity.class);
                intent.putExtra("district",mDistrict);
                finish();
                startActivity(intent);
            }
        });
        uploadImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               onButtonShowPopupWindow(v);
            }
        });


        delete.setVisibility(View.INVISIBLE);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String holdRange = pricerange.getText().toString();
                holdRange = holdRange.replaceAll("[^a-zA-Z0-9]", " ");
                int i = holdRange.length();
                holdRange = holdRange.substring(13,i);
                i = holdRange.length();
                holdRange =holdRange.substring(0,i-5);

                List<String> tempCateory = new ArrayList<String>(Arrays.asList(tags.getText().toString().split(",")));
                List<String> tempPhone = new ArrayList<String>(Arrays.asList(phone.getText().toString().split(",")));

                Log.d("Nameeee",instance.getName());
                placeDA0.getInstance();
                instance.setName(name.getText().toString());
                Log.d("INSTANCEEEE",instance.getName());
                instance.setAddress(address.getText().toString());
                instance.setPhones(tempPhone);
                instance.setCategories(tempCateory);
                List<String> tempRangeOneTwo = new ArrayList<String>(Arrays.asList(holdRange.split("-")));
                PriceRange tempRange = new PriceRange();
                tempRange.setMin_price(Integer.parseInt(tempRangeOneTwo.get(0)));
                tempRange.setMax_price(Integer.parseInt(tempRangeOneTwo.get(1)));
                instance.setPrice_range(tempRange);
                List<String> tempTime=new ArrayList<String>(Arrays.asList(opcl.getText().toString().split("-")));
                instance.setBegin(tempTime.get(0));
                instance.setEnd((tempTime.get(1)));
                Log.d("Addressss",instance.getAddress());


                BitmapDrawable drawable = (BitmapDrawable) food.getDrawable();
                Bitmap bitmap = drawable.getBitmap();
                //////////////// GỌI HÀM ADD LÊN DATABASE!!!!!!!!!!

            }
        });

}
    void loadFood(){
        delete.setVisibility(View.VISIBLE);
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
        phone.setText("Phone:"+(instance.getPhones().get(0)==null?"":instance.getPhones().get(0)));
        opcl.setText("OPEN/CLOSED: " + instance.getBegin() + " - " + instance.getEnd());
        pricerange.setText("PRICE RANGE: " + instance.getPrice_range().min_price + "-" + instance.getPrice_range().max_price + "(VND)");

        uploadImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onButtonShowPopupWindow(v);
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
                        Intent back = new Intent(EditActivity.this,FoodlistActivity.class);
                        back.putExtra("district",mDistrict);
                        finish();
                        startActivity(back);
                    }
                });

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

                List<String> tempCateory = new ArrayList<String>(Arrays.asList(tags.getText().toString().substring(6).split(", ")));
                List<String> tempPhone = new ArrayList<String>(Arrays.asList(phone.getText().toString().substring(6).split(", ")));

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
                List<String> tempTime=new ArrayList<String>(Arrays.asList(opcl.getText().toString().substring(13).split(" - ")));
                instance.setBegin(tempTime.get(0));
                instance.setEnd((tempTime.get(1)));
                Log.d("Addressss",instance.getAddress());

                BitmapDrawable drawable = (BitmapDrawable) food.getDrawable();
                Bitmap bitmap = drawable.getBitmap();
                placeDA0.getInstance().uploadFoodImg(bitmap,instance).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        Toast.makeText(getApplicationContext(),"Cập nhật thành công",Toast.LENGTH_LONG);
                    }
                });
                placeDA0.getInstance().update(mDistrict,instance).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Log.d("Updateee",instance.getName());
                            Intent back = new Intent(EditActivity.this,FoodlistActivity.class);
                            back.putExtra("district",mDistrict);
                            finish();
                            Toast.makeText(getApplicationContext(),"Cập nhật thành công",Toast.LENGTH_LONG);
                            startActivity(back);

                        }
                    }
                });


            }
        });
    }
    public static void applyDim(@NonNull ViewGroup parent, float dimAmount) {
        Drawable dim = new ColorDrawable(Color.BLACK);
        dim.setBounds(0, 0, parent.getWidth(), parent.getHeight());
        dim.setAlpha((int) (255 * dimAmount));

        ViewGroupOverlay overlay = parent.getOverlay();
        overlay.add(dim);
    }

    public static void clearDim(@NonNull ViewGroup parent) {
        ViewGroupOverlay overlay = parent.getOverlay();
        overlay.clear();
    }

    public void onButtonShowPopupWindow(View v) {

        LayoutInflater inflater = (LayoutInflater)
                getSystemService(LAYOUT_INFLATER_SERVICE);

        View popupView = inflater.inflate(R.layout.popup_imglink, null);
        submit = popupView.findViewById(R.id.OK);
        imageLINK = popupView.findViewById(R.id.imageLINK);


        final ViewGroup root = (ViewGroup) getWindow().getDecorView().getRootView();
        // create the popup window
        applyDim(root, 0.5f);
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true; // lets taps outside the popup also dismiss it
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        // show the popup window
        // which view you pass in doesn't matter, it is only used for the window tolken
        popupWindow.showAtLocation(v, Gravity.CENTER, 0, 0);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        instance.setPhoto(imageLINK.toString());
                        clearDim(root);

                    }
                });
                popupWindow.dismiss();
                clearDim(root);
            }
        });
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                clearDim(root);
            }
        });
};
}
