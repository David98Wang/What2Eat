package eng.waterloo.what2eat;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DBactivity extends AppCompatActivity {

    DatabaseReference root = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dbactivity);
        //addToDB("kenzo");
        getHighest();
    }

    public void getHighest() {
        DatabaseReference root = FirebaseDatabase.getInstance().getReference().child("restaurants");

    }


    public void addToDB(final String restaurant){

        root.child("restaurants").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean exists = false;
                for(DataSnapshot d: dataSnapshot.getChildren()){
                    if(restaurant.equals(d.getKey().toString())){
                        int votes = Integer.parseInt(d.getValue().toString());
                        root.child("restaurants").child(d.getKey()).setValue(votes + 1);
                        exists = true;
                        break;
                    }
                if(!exists){
                    root.child("restaurants").child(restaurant).setValue("1");
                }

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


}
