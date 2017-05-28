package eng.waterloo.what2eat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.junit.Test;

import eng.waterloo.what2eat.Database.Group;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void test_addition_isCorrect() throws Exception {
        DatabaseReference root = FirebaseDatabase.getInstance().getReference();
        root.child("ID").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                System.out.println("Group Found");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}