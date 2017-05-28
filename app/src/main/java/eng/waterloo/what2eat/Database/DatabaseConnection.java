package eng.waterloo.what2eat.Database;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by whcda on 5/30/2017.
 */

public class DatabaseConnection {
    private DatabaseReference root = FirebaseDatabase.getInstance().getReference();

    private Group group;

    public DatabaseConnection(String groupID) {
        initializeGroup(groupID);
    }

    private void initializeGroup(final String gropuID) {
        root.child(gropuID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    //TODO add group exists case
                    System.out.println(gropuID+"Group Found");
                } else{
                    createGroup(group);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    private void createGroup(Group group){
        Map<String, Object> userData = new HashMap<String, Object>();

        userData.put("Size",Integer.toString(1));
        userData.put("Location", group.location);
        userData.put("Votes",group.votes);


        root.setValue(group.ID);

        DatabaseReference groupRef = root.child("groupID");

        groupRef.setValue(userData);

    }
}
