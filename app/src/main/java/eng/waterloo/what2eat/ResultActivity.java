package eng.waterloo.what2eat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import android.app.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;

import static eng.waterloo.what2eat.DBactivity.*;

public class ResultActivity extends Activity {

    DatabaseReference root = FirebaseDatabase.getInstance().getReference();
    HashMap<String, String> map = new LinkedHashMap<>();

    public static String largestVotingRestaurant;
//    public static int peopleVoted = 0;
//    public static int currentVoted = 0;
    public static TextView displayTV; // text view to display final result
    public static Button backToMenuButton; // text view to display final result

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.resultactivity);

//        new Thread(new Runnable(){
//            public void run(){
//                while(true)System.out.println(largestVotingRestaurant);
//            }
//        }).start();

        displayTV = (TextView) findViewById(R.id.resultTV);
        displayTV.setText("Getting Results...");

        backToMenuButton = (Button) findViewById(R.id.returnMenuButton);
        backToMenuButton.setClickable(true);
        DBEventListener();
        getHighest();

//        while (peopleVoted != 6){
//            peopleVoted = 0;
//            for (String c : map.keySet()) {
//                peopleVoted += Integer.parseInt(map.get(c));
//            }
//

//        Intent dbActivity = new Intent(getApplicationContext(), DBactivity.class);
//        startActivity(dbActivity);

    }


    //switching to main menu
    public void returnMainMenu(View view) {
        // Do something in response to button
        Intent QRintent = new Intent(this, QRCodeActivity.class);
        startActivity(QRintent);
    }


    //******************DATA BASE OPERATIONS*********************//

    public void DBEventListener() {
        root.child("restaurants").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
            /*perform w/e action everytime the database is modified*/
                for (DataSnapshot d : dataSnapshot.getChildren()) {
                    map.put(d.getKey().toString(), d.getValue().toString());

                }
                for (String s : map.keySet()) {
                    System.out.println("this is key: " + s);
                    System.out.println("this is value: " + map.get(s));
                }

                if (checkVotes(map)) {
                    System.out.println("largest voting restaurant: " + largestVotingRestaurant);
                    displayTV.setText(largestVotingRestaurant);
                    backToMenuButton.setClickable(true);
                }
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void getHighest() {

        root.child("restaurants").addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        //get map of users in datasnapshot

                        for (DataSnapshot d : dataSnapshot.getChildren()) {
                            map.put(d.getKey().toString(), d.getValue().toString());

                        }
                        //output();
                        /* put ratings in an array and find out the biggest rating */
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

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        System.out.println("failed to read " + databaseError.getMessage());
                    }
                }

        );

    }

    private boolean checkVotes(HashMap<String, String> map) {
        int totalVotes = 0;

        for (String c : map.keySet()) {
//                peopleVoted += Integer.parseInt(map.get(c));
            // System.out.println("restaurant votes: "+map.get(c));
            totalVotes += Integer.parseInt(map.get(c));

            System.out.println("this is map: " + map.get(c));
            System.out.println("this is key: " + c);

        }
        System.out.println("total votes: " + totalVotes);

        if (totalVotes == 10) {
            return true;
        }
        return false;
    }


//    public void output() {
//        for (String c : map.keySet()) {
//            System.out.println("this is map: " + map.get(c));
//            System.out.println("this is key: " + c);
//        }
//    }

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
                System.out.println("failed to read " + databaseError.getMessage());
            }
        });
    }

//    public String get_result() {
//        System.out.println("getter out " + largestVotingRestaurant);
//        return largestVotingRestaurant;
//    }


}