package com.parked.parked;

import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * Created by kevin on 5/23/17.
 */

public class ParkingSpotClient {
    private String myJSONOBJECT;


    private static final String TAG_RESULTS="result";
    private static final String TAG_PARKED= "parked";
    private static final String TAG_LAT = "lat";
    private static final String TAG_LONG ="long";

    JSONArray spots = null;

    ArrayList<HashMap<String, String>> spotList;

    private static ParkingSpotClient parkingSpotClient;
    private Context mContext;
    private List<ParkingSpot> parkingSpots;

    public static ParkingSpotClient get(Context context){
        if (parkingSpotClient == null){
            parkingSpotClient = new ParkingSpotClient(context);
        }

        return parkingSpotClient;
    }

    private ParkingSpotClient(Context context){
        //mContext = context.getApplicationContext();
        spotList = new ArrayList<HashMap<String,String>>();
    }

    public void addParkingSpot(ParkingSpot spot){
        //ContentValues values = getContentValues(spot);
        parkingSpots.add(spot);
    }

    public List<ParkingSpot> getParkingSpots(){



        return parkingSpots;
    }


    private static ContentValues getContentValues(ParkingSpot spot) {
        ContentValues values = new ContentValues();
        //values.put(UUID, crime.getId().toString());
        //values.put(TITLE, crime.getTitle());
        //values.put(DATE, crime.getDate().getTime());
        //values.put(SOLVED, crime.isSolved() ? 1 : 0);
        return values;
    }



}
