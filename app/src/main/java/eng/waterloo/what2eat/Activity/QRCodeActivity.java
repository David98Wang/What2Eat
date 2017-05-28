package eng.waterloo.what2eat.Activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.util.Random;

import com.google.android.gms.location.places.Places;

import eng.waterloo.what2eat.Fragment.SupportCustomMapFragment;
import eng.waterloo.what2eat.R;


/**
 * Created by whcda on 5/27/2017.
 */

public class QRCodeActivity extends FragmentActivity implements GoogleApiClient.OnConnectionFailedListener {
    public static int NumberOfPeople = 1;
    public static String groupName;
    private GoogleMap map;
    ImageView imgQRCode;
    SupportCustomMapFragment mCustomMapFragment;
    private GoogleApiClient mGoogleApiClient;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(eng.waterloo.what2eat.R.layout.activity_qrcode);

        mCustomMapFragment = ((SupportCustomMapFragment) getSupportFragmentManager().findFragmentById(R.id.map));
        mCustomMapFragment.getMapAsync();

        addKeyListener();

        initializeSeekBarListener();
        initializePlacesApi();
        //initializePlacePicker();
        final PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.sbLocation);

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {

            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.
                CameraUpdate toLocation = CameraUpdateFactory.newLatLng(place.getLatLng());
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(place.getLatLng(), 15));

                Log.i("ERROR", "Place: " + place.getName());
            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.i("ERROR", "An error occurred: " + status);
            }
        });
        Log.d("LOCATIONASDF", String.valueOf(ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED));

        //groupName = randomString();
        //groupName = QRCodeActivity.groupName;
        //imgQRCode = (ImageView)findViewById(R.id.imgQRCode);

    }

    public void initializePlacesApi() {
        mGoogleApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(this, this)
                .build();
    }

    public void initializePlacePicker() {
        int PLACE_PICKER_REQUEST = 1;
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
        try {
            startActivityForResult(builder.build(this), PLACE_PICKER_REQUEST);
        } catch (Exception e){
            Log.e("PLAY_SERVICE_ERROR","Can you please not");
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }

    protected void displayQRCode() {
        Bitmap bmp = encodeToQrCode(randomString(), 1000, 1000);

        //((ImageView) findViewById(R.id.imgQRCode)).setImageBitmap(bmp);

    }

    protected void addKeyListener() {
        findViewById(R.id.btnJoinGroup).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    Intent intent = new Intent("com.google.zxing.client.android.SCAN");
                    intent.putExtra("SCAN_MODE", "QR_CODE_MODE"); // "PRODUCT_MODE for bar codes
                    startActivityForResult(intent, 0);
                } catch (Exception e) {

                    Uri marketUri = Uri.parse("market://details?id=com.google.zxing.client.android");
                    Intent marketIntent = new Intent(Intent.ACTION_VIEW, marketUri);
                    startActivity(marketIntent);

                }
            }
        });
        findViewById(R.id.btnNewGroup).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                popUp();
                new Thread(new Runnable() {
                    public void run() {
                        // a potentially  time consuming task
                        QRCodeActivity.groupName = randomString();
                        final Bitmap temp = encodeToQrCode(groupName, 1000, 1000);
                        imgQRCode.post(new Runnable() {
                            public void run() {
                                imgQRCode.setImageBitmap(temp);
                            }
                        });
                    }
                }).start();
                //displayQRCode();
            }
        });
        findViewById(R.id.btnFinish).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent next = new Intent(QRCodeActivity.this, VoteRestaurants_Activity.class);//TODO:Change to real activity
                startActivity(next);
            }
        });
//        map.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
//            @Override
//            public boolean onMyLocationButtonClick() {
//                if (mGoogleApiClient != null) {
//                    LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, , this);
//                }
//                return false;
//            }
//        });
    }

//    public void getNearbyRestaurant() {
//        RequestQueue queue = Volley.newRequestQueue(this);
//        String baseUrl = "http://www.google.com";
//
//
//        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        // Display the first 500 characters of the response string.
//                        mTextView.setText("Response is: " + response.substring(0, 500));
//                    }
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                mTextView.setText("That didn't work!");
//            }
//        });
//    }

    public void popUp() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setTitle("Number of People");
        alert.setMessage("Please enter number of people in your Group");

        // Set an EditText view to get user input
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        alert.setView(input);
        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                if (input.getText().toString() == "") input.setText("1");
                QRCodeActivity.NumberOfPeople = Math.max(Integer.valueOf(input.getText().toString()), 1);
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Canceled.
            }
        });

        alert.show();
    }

    public static Bitmap encodeToQrCode(String text, int width, int height) {
        QRCodeWriter writer = new QRCodeWriter();
        BitMatrix matrix = null;

        try {
            matrix = writer.encode(text, BarcodeFormat.QR_CODE, 1000, 1000);
        } catch (WriterException ex) {
            ex.printStackTrace();
        }
        Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        Log.d("ERROR123", String.valueOf(bmp.getWidth()));
        for (int x = 0; x < 1000; x++) {
            for (int y = 0; y < 1000; y++) {
                bmp.setPixel(x, y, matrix.get(x, y) ? Color.BLACK : Color.WHITE);
            }
        }
        return bmp;
    }

    public static String randomString() {
        Random generator = new Random();
        StringBuilder randomStringBuilder = new StringBuilder();
        int randomLength = 100;
        char tempChar;
        for (int i = 0; i < randomLength; i++) {
            tempChar = (char) (generator.nextInt(26) + 'a');
            randomStringBuilder.append(tempChar);
        }
        String ret = randomStringBuilder.toString();
        ret = ret + String.valueOf(NumberOfPeople);
        return ret;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                QRCodeActivity.groupName = intent.getStringExtra("SCAN_RESULT");
                NumberOfPeople = Integer.valueOf(groupName.charAt(groupName.length() - 1));
                System.out.println("jqpowjfpqow" + NumberOfPeople);
                String format = intent.getStringExtra("SCAN_RESULT_FORMAT");
                Log.d("DEBUG", groupName);
                popUp();
            } else if (resultCode == RESULT_CANCELED) {
                // Handle cancel
            }
        }
    }
    private void initializeSeekBarListener(){
        final TextView tvDistance = (TextView)findViewById(R.id.tvDistance);
        ((SeekBar)findViewById(R.id.barDistance)).setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tvDistance.setText(String.format("%d km",progress/5));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

}
