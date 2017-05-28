package eng.waterloo.what2eat;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.security.acl.Group;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import static android.R.attr.data;
import static android.R.attr.key;


/*
  sample database structure
  map: 1
  key: fake
  map: kenzo
  key: 4
   */
public class DBactivity extends AppCompatActivity {

    DatabaseReference root = FirebaseDatabase.getInstance().getReference();

    HashMap<String, String> map = new LinkedHashMap<>();

    //String largestVotingRestaurant;

    DatabaseReference a = root.child("restaurants");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dbactivity);
        //addToDB("abc","restaurant");
        //  getHighest();
        //DBActivityListener();
        addGroup("new2", "restaurant");
    }

    public void addGroup(final String groupID, final String restaurantName) {
        root.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean exists = false;
                for (DataSnapshot d : dataSnapshot.getChildren()) {
                    if (d.getKey().equals(groupID)) { //if the group ID exists
                        int votes = Integer.parseInt(d.child(restaurantName).getValue().toString());
                        root.child(groupID).child(restaurantName).setValue(votes+1);
                        break;
                    }else{
                        /* add to db */
                        root.child(groupID).child(restaurantName).setValue("1");
                    }
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


    public void DBActivityListener() {
        root.child("restaurants").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                /* everytime the data is changed, this will run */
                System.out.println("data is changed");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    /*public void getHighest() {

        root.child("restaurants").addListenerForSingleValueEvent(new ValueEventListener() {
                                                                     @Override
                                                                     public void onDataChange(DataSnapshot dataSnapshot) {

                                                                         for (DataSnapshot d : dataSnapshot.getChildren()) {
                                                                             map.put(d.getKey().toString(), d.getValue().toString());
                                                                         }

                                                                         int[] totalEle = new int[map.size()];
                                                                         int i = 0;
                                                                         for (String c : map.keySet()) {
                                                                             totalEle[i] = Integer.parseInt(map.get(c));
                                                                             i++;
                                                                         }
                                                                         Arrays.sort(totalEle);
                                                                         int biggest = totalEle[map.size() - 1];

                                                                         for (String c : map.keySet()) {
                                                                             if (Integer.parseInt(map.get(c)) == biggest) {
                                                                                 largestVotingRestaurant = c;
                                                                                 break;
                                                                             }

                                                                         }

                                                                         output();

                                                                     }

                                                                     @Override
                                                                     public void onCancelled(DatabaseError databaseError) {
                                                                         System.out.println(databaseError.getMessage());
                                                                     }
                                                                 }
        );


    }

    public void output() {
        for (String c : map.keySet()) {
            System.out.println("this is map: " + map.get(c));
            System.out.println("this is key: " + c);
            System.out.println("final string is: " + largestVotingRestaurant);
        }
    }

    public void addToDB(final String restaurant) {

        root.child("restaurants").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean exists = false;
                for (DataSnapshot d : dataSnapshot.getChildren()) {
                    if (restaurant.equals(d.getKey().toString())) {
                        int votes = Integer.parseInt(d.getValue().toString());
                        root.child("restaurants").child(d.getKey()).setValue(votes + 1);
                        exists = true;
                        break;
                    }
                    if (!exists) {
                        root.child("restaurants").child(restaurant).setValue("1");
                    }

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println(databaseError.getMessage());
            }
        });
    }*/


}