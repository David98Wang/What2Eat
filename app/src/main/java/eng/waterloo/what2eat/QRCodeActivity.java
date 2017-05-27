package eng.waterloo.what2eat;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import java.util.Random;

import static android.R.id.content;

/**
 * Created by whcda on 5/27/2017.
 */

public class QRCodeActivity extends Activity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode);

        addKeyListener();
    }

    protected void displayQRCode() {
        Random rand = new Random();
        ImageView imgQRCode = (ImageView) findViewById(R.id.imgQRCode);
        LinearLayout layoutMain = (LinearLayout) findViewById(R.id.layoutMain);
        int length = 600;
        Log.d("DEBUG", String.valueOf(length));
        Log.d("DEBUG", String.valueOf(length));
        int QRSize = 50;
        int blockPixel = length / QRSize - 1;
        Bitmap bmp = Bitmap.createBitmap(600, 600, Bitmap.Config.RGB_565);
        for (int x = 0; x < length - 100; x += blockPixel) {
            for (int y = 0; y < length - 100; y += blockPixel) {
                boolean res = (rand.nextBoolean());
                for (int a = 0; a < blockPixel; a++)
                    for (int b = 0; b < blockPixel; b++)
                        bmp.setPixel(x + a, y + b, res ? Color.BLACK : Color.WHITE);
            }
        }
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
                displayQRCode();
            }
        });
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
