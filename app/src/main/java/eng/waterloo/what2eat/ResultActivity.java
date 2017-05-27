package eng.waterloo.what2eat;

import android.app.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class ResultActivity extends Activity {

   // DBactivity testActivity = new DBactivity();
    String resultString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.resultactivity);


//        Intent dbActivity = new Intent(getApplicationContext(), DBactivity.class);
//        startActivity(dbActivity);

        TextView displayTV = (TextView) findViewById(R.id.resultTV);

        //resultString = testActivity.getHighest();
        resultString = testing();

        displayTV.setText(resultString);

    }

    public void sendMessage(View view) {
        // Do something in response to button
        Intent QRintent = new Intent(this,QRCodeActivity.class);
        startActivity(QRintent);
    }

    public String testing(){

        return "Waterloo Star";
    }
}
