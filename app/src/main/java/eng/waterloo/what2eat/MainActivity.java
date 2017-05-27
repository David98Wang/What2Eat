package eng.waterloo.what2eat;

import android.app.Activity;

import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent dbActivity = new Intent(getApplicationContext(), DBactivity.class);
        startActivity(dbActivity);

    }
}
