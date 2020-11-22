package com.example.goeat;

public class Position {
       boolean is_verified;
        double latitude;
        double longitude;
        public Position(){

        }
        public String getString(){
            return is_verified+" "+latitude+" "+longitude;
        }
        public boolean getIs_verified(){
            return is_verified;
        }
        public double getLatitude(){
            return latitude;
        }
        public double getLongitude(){
            return longitude;
        }
}
