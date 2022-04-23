package com.dev_app.gps_adrar.testing;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.chaquo.python.android.AndroidPlatform;
import com.dev_app.gps_adrar.R;
import com.dev_app.gps_adrar.variable;

import org.json.JSONObject;

public class MainActivityTEST extends AppCompatActivity {


    private static final int KEY_GPS_PERMISSION = 1;
    private static final int KEY_GPS_ACCESS_FINE_LOCATION = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_test);

        if (!Python.isStarted()) {
            Python.start(new AndroidPlatform(this));
        }

        //PyObject obj1 = pyObject.callAttr("predictDistance", 27.8829680268151, -0.28564667934856, 27.8829668014029, -0.285645876144924, 7, 3, 0.92987615457897);

        JSONObject r1 = variable.predictDistance(27.8829680268151, -0.28564667934856,
                27.8829668014029, -0.285645876144924, 7, 3, 0.92987615457897,
                MainActivityTEST.this);

        JSONObject r2 = variable.predictTime(27.8829680268151, -0.28564667934856,
                27.8829668014029, -0.285645876144924, 7, 3, 0.92987615457897,
                MainActivityTEST.this);

//        Toast.makeText(this, "" + r1, Toast.LENGTH_SHORT).show();
//        Toast.makeText(this, "" + r2, Toast.LENGTH_SHORT).show();

        Log.e("result 1",r1+"");
        Log.e("result 1",r2+"");

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) ==
                        PackageManager.PERMISSION_GRANTED) {

        } else {
            checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION, KEY_GPS_PERMISSION);
            checkPermission(Manifest.permission.ACCESS_FINE_LOCATION, KEY_GPS_ACCESS_FINE_LOCATION);
        }


        startActivity(new Intent(this,MapsActivityTEST.class));
        finish();

    }

    void checkPermission(String permission, int code) {

        if (ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_DENIED) {

            ActivityCompat.requestPermissions(this, new String[]{permission}, code);

        } else {
            //Toast.makeText(this, "permission already GRANTED", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == KEY_GPS_PERMISSION) {

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                //Toast.makeText(this, "permissions GPS is GRANTED", Toast.LENGTH_SHORT).show();

            } else {
                //Toast.makeText(this, "permissions GPS is DENIED", Toast.LENGTH_SHORT).show();
            }

        } else if (requestCode == KEY_GPS_ACCESS_FINE_LOCATION) {

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                //Toast.makeText(this, "permissions GPS is GRANTED", Toast.LENGTH_SHORT).show();

            } else {
//                Toast.makeText(this, "permissions GPS is DENIED", Toast.LENGTH_SHORT).show();
            }

        }

    }
}
