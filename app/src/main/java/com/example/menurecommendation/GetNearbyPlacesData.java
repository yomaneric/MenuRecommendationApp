package com.example.menurecommendation;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

class GetNearbyPlacesData extends AsyncTask<Object, String, String> {

    private String googlePlacesData;
    private GoogleMap mMap;
    private Activity activity;
    String searchType;
    String url;


    GetNearbyPlacesData(Activity activity, String searchType){
        this.activity = activity;
        switch (searchType) {
            case MapFragment.supermarket:
                this.searchType = "Supermarket";
                break;
            case MapFragment.convenience_store:
                this.searchType = "Convenience Store";
                break;
            default:
                break;
        }
    }

    @Override
    protected String doInBackground(Object... params){
        mMap = (GoogleMap)params[0];
        url = (String)params[1];

        DownloadURL downloadURL = new DownloadURL();
        try {
            googlePlacesData = downloadURL.readUrl(url);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return googlePlacesData;
    }

    @Override
    protected void onPostExecute(String s){

        List<HashMap<String, String>> nearbyPlaceList;
        DataParser parser = new DataParser();
        nearbyPlaceList = parser.parse(s);
        Log.d("nearbyplacesdata","called parse method");
        showNearbyPlaces(nearbyPlaceList);
    }

    private void showNearbyPlaces(List<HashMap<String, String>> nearbyPlaceList)
    {
        if (nearbyPlaceList.size() == 0)
            Toast.makeText(activity,"No " + searchType + " Nearby", Toast.LENGTH_LONG).show();
        else {
            Toast.makeText(activity,"Nearby " + searchType + "s", Toast.LENGTH_LONG).show();
            for(int i = 0; i < nearbyPlaceList.size(); i++)
            {
                MarkerOptions markerOptions = new MarkerOptions();
                HashMap<String, String> googlePlace = nearbyPlaceList.get(i);

                String placeName = googlePlace.get("place_name");
                String vicinity = googlePlace.get("vicinity");
                double lat = Double.parseDouble( googlePlace.get("lat"));
                double lng = Double.parseDouble( googlePlace.get("lng"));

                LatLng latLng = new LatLng( lat, lng);
                markerOptions.position(latLng);
                markerOptions.title(placeName + " : "+ vicinity);
                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));

                mMap.addMarker(markerOptions);
                //mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
            }
        }
    }
}