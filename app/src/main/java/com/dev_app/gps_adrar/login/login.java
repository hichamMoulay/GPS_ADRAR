package com.dev_app.gps_adrar.login;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.dev_app.gps_adrar.GPSTracker;
import com.dev_app.gps_adrar.MainActivity;
import com.dev_app.gps_adrar.R;
import com.dev_app.gps_adrar.variable;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class login extends AppCompatActivity {


    private EditText edtPhone, edtName;
    private variable global;
    private GPSTracker tracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edtPhone = findViewById(R.id.edtPhone);
        edtName = findViewById(R.id.edtName);

        global = (variable) getApplicationContext();
        tracker = new GPSTracker(login.this);

        if (testIsLogin()) {

            startActivity(new Intent(login.this, MainActivity.class));
            finish();
        }

    }

    public void btnOnClick(View view) {
        tracker = new GPSTracker(login.this);

        if (edtName.getText().toString().isEmpty()) {
            edtName.setError("الرجاء ادخال الاسم الشخصي");
            edtName.setFocusable(true);

        } else if (edtPhone.getText().toString().isEmpty()) {
            edtPhone.setError("الرجاء ادخال رقم الهاتف");
            edtPhone.setFocusable(true);

        } else if (!tracker.canGetLocation()) {
            variable.showDialog("يرجى تشغيل خدمة الموقع اولا GPS", login.this);
        } else
            loginUser();


    }

    boolean testIsLogin() {

        SharedPreferences preferences = getSharedPreferences("app", MODE_PRIVATE);
        String idUser = preferences.getString("idUser", "-1");
        global.setIdUser(idUser);

        return !idUser.equals("-1");
    }

    void loginUser() {
        tracker = new GPSTracker(login.this);

        ProgressDialog dialog = new ProgressDialog(login.this);
        dialog.setMessage("يرجى الانتظار يتم تسجيل الدخول ..");
        dialog.show();

        StringRequest request = new StringRequest(Request.Method.POST, MainActivity.url + "login.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.cancel();

                try {

                    JSONObject object = new JSONObject(response);

                    String resp = object.getString("response");


                    if (resp.equals("ok")) {
                        String idUser = object.getString("idUser");

                        SharedPreferences.Editor editor = getSharedPreferences("app", MODE_PRIVATE).edit();
                        editor.putString("idUser", idUser);
                        editor.apply();

                        startActivity(new Intent(login.this, MainActivity.class));
                        finish();

                    } else {
                        variable.showDialog("يرجى اعادة تسجيل الدخول ", login.this);
                    }


                } catch (JSONException e) {
                    variable.showDialog(e + " حدث خطا في تسجيل الدخول \n", login.this);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.cancel();
                variable.showDialog("" + error, login.this);

            }
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();

                map.put("phone", edtPhone.getText().toString());
                map.put("name", edtName.getText().toString());

                map.put("latitude", tracker.getLatitude() + "".trim());
                map.put("longitude", tracker.getLongitude() + "".trim());

                return map;
            }
        };

        Volley.newRequestQueue(login.this).add(request);

    }

}