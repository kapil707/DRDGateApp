package com.drd.drdgateapp;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.zxing.WriterException;

import java.util.Calendar;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    //https://www.geeksforgeeks.org/how-to-generate-qr-code-in-android/
    // variables for imageview, edittext,
    // button, bitmap and qrencoder.

    Handler handler = new Handler();
    Runnable runnable;
    int delay = 5000;

    private ImageView qrCodeIV;
    //private EditText dataEdt;
    //private Button generateQrBtn;
    Bitmap bitmap;
    QRGEncoder qrgEncoder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // initializing all variables.
        qrCodeIV = findViewById(R.id.idIVQrcode);
        /*dataEdt = findViewById(R.id.idEdt);
        generateQrBtn = findViewById(R.id.idBtnGenerateQR);*/

        // initializing onclick listener for button.
        /*generateQrBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(dataEdt.getText().toString())) {

                    // if the edittext inputs are empty then execute
                    // this method showing a toast message.
                    Toast.makeText(MainActivity.this, "Enter some text to generate QR Code", Toast.LENGTH_SHORT).show();
                } else {
                    // below line is for getting
                    // the windowmanager service.

                }
            }
        });*/
    }

    private void load_qr()
    {
        //Toast.makeText(MainActivity.this, "Reload Qr Code",Toast.LENGTH_SHORT).show();

        java.util.Date noteTS = Calendar.getInstance().getTime();

        String time = "hh:mm"; // 12:00
        //tvTime.setText(DateFormat.format(time, noteTS));

        String date = "yyyy-MM-dd"; // 01 January 2013
        //tvDate.setText(DateFormat.format(date, noteTS));

        String gettime = DateFormat.format(time, noteTS) + "";
        String getdate = DateFormat.format(date, noteTS) + "";

        String mytext = "[{\"value_id\":\"drd-attendance\",\"return_id\":\"1\",\"time_id\":\""+gettime+"\",\"date_id\":\""+getdate+"\"}]";

        WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);

        // initializing a variable for default display.
        Display display = manager.getDefaultDisplay();

        // creating a variable for point which
        // is to be displayed in QR Code.
        Point point = new Point();
        display.getSize(point);

        // getting width and
        // height of a point
        int width = point.x;
        int height = point.y;

        // generating dimension from width and height.
        int dimen = width < height ? width : height;
        dimen = dimen * 3 / 4;

        // setting this dimensions inside our qr code
        // encoder to generate our qr code.
        qrgEncoder = new QRGEncoder(mytext, null,
                QRGContents.Type.TEXT, dimen);
        try {
            // getting our qrcode in the form of bitmap.
            bitmap = qrgEncoder.encodeAsBitmap();
            // the bitmap is set inside our image
            // view using .setimagebitmap method.
            qrCodeIV.setImageBitmap(bitmap);
        } catch (WriterException e) {
            // this method is called for
            // exception handling.
            Log.e("Tag", e.toString());
        }
    }

    @Override
    protected void onResume() {
        handler.postDelayed(runnable = new Runnable() {
            public void run() {
                handler.postDelayed(runnable, delay);
                load_qr();
            }
        }, delay);
        load_qr();
        test_api_call();
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        handler.removeCallbacks(runnable); //stop handler when activity not visible super.onPause();
    }

    private void test_api_call(){
        ApiService apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);

        Call<ResponseBody> call = apiService.testing("98c08565401579448aad7c64033dcb4081906dcb");
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    // Handle success response
                    // response.body() contains the response data

                    Toast.makeText(MainActivity.this,"onResponse",Toast.LENGTH_SHORT).show();

//                    try {
//                        writeTv(response.body().string());
//                    } catch (IOException e) {
//                        throw new RuntimeException(e);
//                    }
                } else {
                    // Handle error response
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                // Handle network failures or other errors
                Log.e("Bg-service-onFailure", " " + t.toString());
                Toast.makeText(MainActivity.this,"show_rider_chemist_photo_api onFailure",Toast.LENGTH_SHORT).show();
            }
        });
    }
}