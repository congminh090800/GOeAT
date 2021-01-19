package com.example.goeat.database;

import android.app.Activity;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.goeat.Place;
import com.example.goeat.auth.Auth;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Executor;

public class PlaceDAO {
    private final StorageReference storageReference = FirebaseStorage.getInstance().getReference();
    private static class Loader {
        static final PlaceDAO INSTANCE = new PlaceDAO();
    }

    public static PlaceDAO getInstance() {
        return PlaceDAO.Loader.INSTANCE;
    }

    private final DatabaseReference placesRef;

    public PlaceDAO(){
        placesRef = FirebaseDatabase.getInstance().getReference().child("Places").child("HoChiMinh");
    }

    public Task<Void> delete(String district, long id){
        return placesRef.child(district).child(String.valueOf(id)).removeValue();
    }

    public Task<Void> save(String district, Place place){
        return placesRef.child(district).child(String.valueOf(place.getId())).setValue(place.toMap());
    }

    public Task<Void> update(String district, Place place){
        return save(district, place);
    }

    public Task<List<Place>> getPlacesByDistrict(String district) {
        final TaskCompletionSource<List<Place>> task = new TaskCompletionSource<>();
        placesRef.child(district)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        List<Place> result = new ArrayList<>();
                        for (DataSnapshot data: snapshot.getChildren()) {
                            result.add(data.getValue(Place.class));
                        }
                        task.setResult(result);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        task.setException(error.toException());
                    }
                });

        return task.getTask();
    }
    private static <T> OnFailureListener taskcsFailureListener(final TaskCompletionSource<T> taskCompletionSource) {
        return new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                taskCompletionSource.setException(e);
            }
        };
    }

    public Task<Uri> uploadFoodImg(Bitmap bitmap,Place place) {
        final TaskCompletionSource<Uri> taskCompletionSource = new TaskCompletionSource<>();
        final String filename = String.valueOf(place.getId());
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 20, baos);
        byte[] data = baos.toByteArray();
        storageReference.child("images").child(filename).putBytes(data)
                .addOnFailureListener(taskcsFailureListener(taskCompletionSource))
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        taskSnapshot.getStorage().getDownloadUrl()
                                .addOnFailureListener(taskcsFailureListener(taskCompletionSource))
                                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        taskCompletionSource.setResult(uri);
                                    }
                                });
                    }
                });
        return taskCompletionSource.getTask();
    }
    public void test(){
        Task<List<Place>> taskDistrictDetail = PlaceDAO.getInstance().getPlacesByDistrict("BinhChanh");


        taskDistrictDetail
                .addOnSuccessListener(new OnSuccessListener<List<Place>>() {
                    @Override
                    public void onSuccess(List<Place> places) {
                        Place first = places.get(0);

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //log err
                    }
                });

        taskDistrictDetail.addOnCompleteListener(new OnCompleteListener<List<Place>>() {
            @Override
            public void onComplete(@NonNull Task<List<Place>> task) {
                if (task.isSuccessful()) {
                    List<Place> placeList = task.getResult();
                } else  {
                    //err
                }
            }
        });
    }
}
