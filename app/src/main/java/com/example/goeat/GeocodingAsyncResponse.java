package com.example.goeat;

import android.location.Address;

public interface GeocodingAsyncResponse {
    void processFinish(Address address);
}
