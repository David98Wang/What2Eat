package eng.waterloo.what2eat;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.encoder.Encoder;

import java.util.Random;

import static android.R.id.content;

/**
 * Created by whcda on 5/27/2017.
 */

public class QRCodeActivity extends Activity {
    public static int NumberOfPeople = -1;
    public static String groupName;
    ImageView imgQRCode;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode);
        imgQRCode = (ImageView)findViewById(R.id.imgQRCode);
        addKeyListener();
    }

    protected void displayQRCode() {
        Bitmap bmp = encodeToQrCode(randomString(),1000,1000);

        ((ImageView) findViewById(R.id.imgQRCode)).setImageBitmap(bmp);

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
            public void onClick(View v){
                popUp();
                new Thread(new Runnable() {
                    public void run() {
                        // a potentially  time consuming task
                        QRCodeActivity.groupName=randomString();
                        final Bitmap temp = encodeToQrCode(groupName,1000,1000);
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
                Intent next = new Intent(QRCodeActivity.this,ResultActivity.class);//Change to real activity
                startActivity(next);
            }
        });
    }
    public void popUp(){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setTitle("Number of People");
        alert.setMessage("Please enter number of people in your Group");

        // Set an EditText view to get user input
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        alert.setView(input);
        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                if(input.getText().toString()=="")input.setText("1");
                QRCodeActivity.NumberOfPeople =  Math.max(Integer.valueOf(input.getText().toString()),1);
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Canceled.
            }
        });

        alert.show();
    }
    public static Bitmap encodeToQrCode(String text, int width, int height){
        QRCodeWriter writer = new QRCodeWriter();
        BitMatrix matrix = null;

        try {
            matrix = writer.encode(text, BarcodeFormat.QR_CODE, 1000, 1000);
        } catch (WriterException ex) {
            ex.printStackTrace();
        }
        Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        Log.d("ERROR123",String.valueOf(bmp.getWidth()));
        for (int x = 0; x < 1000; x++){
            for (int y = 0; y < 1000; y++){
                        bmp.setPixel(x , y , matrix.get(x, y) ? Color.BLACK : Color.WHITE);
            }
        }
        return bmp;
    }
    public static String randomString() {
        Random generator = new Random();
        StringBuilder randomStringBuilder = new StringBuilder();
        int randomLength = 10;
        char tempChar;
        for (int i = 0; i < randomLength; i++){
            tempChar = (char) (generator.nextInt(96) + 32);
            randomStringBuilder.append(tempChar);
        }
        return randomStringBuilder.toString();
    }
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                String contents = intent.getStringExtra("SCAN_RESULT");
                String format = intent.getStringExtra("SCAN_RESULT_FORMAT");
                Log.d("DEBUG", contents);
            } else if (resultCode == RESULT_CANCELED) {
                // Handle cancel
            }
        }
    }

}
