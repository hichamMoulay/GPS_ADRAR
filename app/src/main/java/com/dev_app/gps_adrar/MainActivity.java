package com.dev_app.gps_adrar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.pm.PackageManager;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.chaquo.python.Python;
import com.chaquo.python.android.AndroidPlatform;
import com.dev_app.gps_adrar.drawer_menu.adapter_drawr;
import com.dev_app.gps_adrar.fragment.setting;
import com.yarolegovich.slidingrootnav.SlidingRootNav;
import com.yarolegovich.slidingrootnav.SlidingRootNavBuilder;

import org.json.JSONObject;

import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private static final int KEY_GPS_PERMISSION = 1;
    private static final int KEY_GPS_ACCESS_FINE_LOCATION = 2;
    public static String url;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setDrawer(savedInstanceState);

        setDataTest();

        setFrameLayout(new gps());
    }


    void setDataTest(){
        if (!Python.isStarted()) {
            Python.start(new AndroidPlatform(MainActivity.this));
        }

        //PyObject obj1 = pyObject.callAttr("predictDistance", 27.8829680268151, -0.28564667934856, 27.8829668014029, -0.285645876144924, 7, 3, 0.92987615457897);

        JSONObject r1 = variable.predictDistance(27.8829680268151, -0.28564667934856,
                27.8829668014029, -0.285645876144924, 7, 3, 0.92987615457897,
                MainActivity.this);

        JSONObject r2 = variable.predictTime(27.8829680268151, -0.28564667934856,
                27.8829668014029, -0.285645876144924, 7, 3, 0.92987615457897,
                MainActivity.this);

        Toast.makeText(MainActivity.this, "" + r1, Toast.LENGTH_SHORT).show();
        Toast.makeText(MainActivity.this, "" + r2, Toast.LENGTH_SHORT).show();

        Log.e("result 1",r1+"");
        Log.e("result 1",r2+"");

        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) ==
                        PackageManager.PERMISSION_GRANTED) {

        } else {
            checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION, KEY_GPS_PERMISSION);
            checkPermission(Manifest.permission.ACCESS_FINE_LOCATION, KEY_GPS_ACCESS_FINE_LOCATION);
        }

    }

    void PermissionGPS() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) ==
                        PackageManager.PERMISSION_GRANTED) {

        } else {
            checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION, KEY_GPS_PERMISSION);
            checkPermission(Manifest.permission.ACCESS_FINE_LOCATION, KEY_GPS_ACCESS_FINE_LOCATION);
        }
    }

    void checkPermission(String permission, int code) {

        if (ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_DENIED) {

            ActivityCompat.requestPermissions(this, new String[]{permission}, code);

        } else {
            Toast.makeText(this, "permission already GRANTED", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == KEY_GPS_PERMISSION) {

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                Toast.makeText(this, "permissions GPS is GRANTED", Toast.LENGTH_SHORT).show();

            } else {
                Toast.makeText(this, "permissions GPS is DENIED", Toast.LENGTH_SHORT).show();
            }

        } else if (requestCode == KEY_GPS_ACCESS_FINE_LOCATION) {

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                Toast.makeText(this, "permissions GPS is GRANTED", Toast.LENGTH_SHORT).show();

            } else {
                Toast.makeText(this, "permissions GPS is DENIED", Toast.LENGTH_SHORT).show();
            }

        }
    }


    void setDrawer(Bundle savedInstanceState) {


        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Waslni");
        setSupportActionBar(toolbar);

        Object var;

        SlidingRootNav slidingRootNav = new SlidingRootNavBuilder(this)
                .withDragDistance(180) //Horizontal translation of a view. Default == 180dp
                .withRootViewScale(0.75f) //Content view's scale will be interpolated between 1f and 0.7f. Default == 0.65f;
                .withRootViewElevation(25) //Content view's elevation will be interpolated between 0 and 10dp. Default == 8.
                .withRootViewYTranslation(4) //Content view's translationY will be interpolated between 0 and 4. Default == 0
                .withToolbarMenuToggle(toolbar)
                .withSavedState(savedInstanceState)
                .withMenuLayout(R.layout.drawer_menu)
                .inject();

        RecyclerView list = findViewById(R.id.drawer_list);
        adapter_drawr adapter_drawr = new adapter_drawr(this);
        //GridLayoutManager manager=new GridLayoutManager(this,1,GridLayoutManager.VERTICAL,false);
        list.setLayoutManager(new LinearLayoutManager(this));
        //list.setLayoutManager(manager);
        list.setAdapter(adapter_drawr);

        ImageView imgSetting = findViewById(R.id.imgSettting);
        TextView txtSetting = findViewById(R.id.txtSetting);

        imgSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                slidingRootNav.closeMenu();
                setFrameLayout(new setting());
            }
        });

        txtSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                slidingRootNav.closeMenu();
                setFrameLayout(new setting());
            }
        });

    }

    public void setFrameLayout(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, fragment).commit();
    }

    private String[] loadScreenTitle() {
        return getResources().getStringArray(R.array.titles);
    }

}