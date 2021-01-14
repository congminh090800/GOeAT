package com.example.goeat.database;

import android.app.Activity;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.goeat.Place;
import com.example.goeat.auth.Auth;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

public class PlaceDAO {

    private static class Loader {
        static final PlaceDAO INSTANCE = new PlaceDAO();
    }

    public static PlaceDAO getInstance() {
        return PlaceDAO.Loader.INSTANCE;
    }

    private final DatabaseReference placesRef;

    private PlaceDAO(){
        placesRef = FirebaseDatabase.getInstance().getReference().child("Places");
    }

    public Task<Void> delete(String district, long id){
        return placesRef.child("HoChiMinh").child(district).child(String.valueOf(id)).removeValue();
    }

    public Task<Void> save(String district, Place place){
        return placesRef.child("HoChiMinh").child(district).child(String.valueOf(place.getId())).setValue(place.toMap());
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
}
