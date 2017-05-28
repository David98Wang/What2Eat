package eng.waterloo.what2eat.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import eng.waterloo.what2eat.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent current = new Intent(this,QRCodeActivity.class);
        startActivity(current);
    }
}
