package eng.waterloo.what2eat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.hardware.SensorListener;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static android.media.CamcorderProfile.get;
import static android.provider.ContactsContract.CommonDataKinds.Website.URL;

public class VoteRestaurants_Activity extends AppCompatActivity {

    ArrayList<RestaurantObject> RestaurantList;
    DatabaseReference root = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.restaurant_item);

        RestaurantList = new ArrayList<RestaurantObject>();

        final RadioGroup radioGroup = (RadioGroup) findViewById(R.id.myRadioGroup);

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=43.469757,-80.540952&radius=5000&type=restaurant&key=AIzaSyD1CZrCl8DLwxMmmp80qygV5O6HytKvj-A";

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // Display the first 500 characters of the response string.
                Log.d("Response is: ", response);
                try{
                    JSONObject reader = new JSONObject(response);
                    JSONArray sys  = reader.getJSONArray("results");

                    for (int i = 0; i < sys.length() || i<50; i++) {

                        JSONObject c = sys.getJSONObject(i);
                        String name = c.getString("name");
                        RestaurantList.add(new RestaurantObject(name));
                        Log.d("name", name);

                        //Add to radio group
                        RadioButton tempRadio = new RadioButton(getApplicationContext());
                        tempRadio.setText(name);
                        tempRadio.setId(i);
                        radioGroup.addView(tempRadio);


                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("error", "failed JSON Parse");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("messege", "That didn't work!");
            }
        });
        // Add the request to the RequestQueue.
        queue.add(stringRequest);


        //ListItemAdapter itemsAdapter = new ListItemAdapter(this, RestaurantList);
        //ListView listView = (ListView) findViewById(R.id.listView);
        //listView.setAdapter(itemsAdapter);

        Button voteButton = (Button) findViewById(R.id.vote);
        voteButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                int radioButtonID = radioGroup.getCheckedRadioButtonId();
                String selectedName = RestaurantList.get(radioButtonID).getName();
                addGroup(QRCodeActivity.groupName, selectedName);
                Log.d("selected radio", Integer.toString(radioButtonID));
                Intent t = new Intent(VoteRestaurants_Activity.this,ResultActivity.class);
                startActivity(t);
            }
        });
        Button backButton = (Button) findViewById(R.id.back);
        backButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent t = new Intent(VoteRestaurants_Activity.this,QRCodeActivity.class);
                startActivity(t);
            }
        });
    }

    public void addGroup(final String groupID, final String restaurantName) {
        root.addListenerForSingleValueEvent(new ValueEventListener() {


            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean exists = false;
                boolean res = false;
                for (DataSnapshot d : dataSnapshot.getChildren()) {
                    if (d.getKey().equals(groupID)) { //if the group ID exists
                        for (DataSnapshot m : dataSnapshot.child(groupID).getChildren()) { //loop through the items in group ID. search all keys
//                            System.out.println(m.getKey().toString());
                            if (m.getKey().equals(restaurantName)) { //if keys match

                                int votes = Integer.parseInt(d.child(restaurantName).getValue().toString());
                                root.child(groupID).child(restaurantName).setValue(votes + 1);
                                exists = true; //both group ID and restaurants exist
                                break;
                            } else {
                                continue;
                            }
                        }
                        res = true; //only group ID exist, restaurant doesn't exist
                    }
                }
                if (!res) {
                        /* add to db */
                    System.out.println("this group doesn't exist. Added a new group and a new restaurant");
                    root.child(groupID).child(restaurantName).setValue("1");

                }
                if (!exists) {
                    System.out.println("this restaurant doesn't exist. Added to associated group ID ");
                    root.child(groupID).child(restaurantName).setValue("1");
                }


//                for (DataSnapshot d : dataSnapshot.getChildren()) {
//                    if (d.hasChild(groupID)) {
//                        for (DataSnapshot m : d.getChildren()) {
//                            String key = m.getKey().toString();
//                            String value = m.getValue().toString();
//                            break;
//                        }
//
//                    }
//                     if(groupID.equals(d.getKey().toString())){
//
//                    int votes = Integer.parseInt(d.getValue().toString());
//
//                      root.child(groupID).child(restaurantName).setValue(votes+1);
//                    exists = true;
//                    break;
//                     }
//                          if (!exists) {
//                            root.child(groupID).child(restaurantName).setValue("1");
//                      }
//                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println(databaseError.getMessage());
            }
        });


    }
}