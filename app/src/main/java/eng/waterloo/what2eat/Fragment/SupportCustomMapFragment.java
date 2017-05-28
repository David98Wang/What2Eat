package eng.waterloo.what2eat.Fragment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

/**
 * Created by David Wang on 10/24/2017.
 */

public class SupportCustomMapFragment extends SupportMapFragment implements OnMapReadyCallback,
        ActivityCompat.OnRequestPermissionsResultCallback{
    private final static String LOG_TAG = "CustomMapFragment";
    private final static int ACCESS_FINE_LOCATION = 123;
    GoogleMap mGoogleMap;

    public void getMapAsync() {
        getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mGoogleMap = googleMap;
        if (ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            Log.d(LOG_TAG, "Fine location access granted");
            mGoogleMap.setMyLocationEnabled(true);
            mGoogleMap.getUiSettings().setMyLocationButtonEnabled(true);

        } else {
            Log.d(LOG_TAG, "Fine location access not granted");
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    ACCESS_FINE_LOCATION);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case ACCESS_FINE_LOCATION: {
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    mGoogleMap.setMyLocationEnabled(true);
                }
            }
            return;
        }
    }
}
