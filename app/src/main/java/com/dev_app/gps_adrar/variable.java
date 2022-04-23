package com.dev_app.gps_adrar;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.chaquo.python.android.AndroidPlatform;
import com.dev_app.gps_adrar.login.login;

import org.json.JSONException;
import org.json.JSONObject;

public class variable extends Application {



    public static JSONObject predictDistance(double latitude_a, double longitude_a, double latitude_b, double longitude_b, int number_day,
                                             int period_time, double distance, Context context) {


        if (!Python.isStarted()) {
            Python.start(new AndroidPlatform(context));
        }

        Python python = Python.getInstance();
        PyObject pyObject = python.getModule("app");
        PyObject obj1 = pyObject.callAttr("predictDistance", latitude_a, longitude_a, latitude_b,
                longitude_b, number_day, period_time, distance);

        String result_1 = obj1.toString();
        Log.e("result Distance = ", result_1);


        /// object always return name key equals result_Distance

        try {

            return new JSONObject(result_1);

        } catch (JSONException e) {

            return null;
        }
    }


    public static JSONObject predictTime(double latitude_a, double longitude_a, double latitude_b, double longitude_b, int number_day,
                                         int period_time, double distance, Context context) {


        if (!Python.isStarted()) {
            Python.start(new AndroidPlatform(context));
        }

        Python python = Python.getInstance();
        PyObject pyObject = python.getModule("app");
        PyObject obj1 = pyObject.callAttr("predictTime", latitude_a, longitude_a, latitude_b,
                longitude_b, number_day, period_time, distance);

        String result_1 = obj1.toString();
        Log.e("result time = ", result_1);

        try {
            /// object always return name key equals result_time
            return new JSONObject(result_1);

        } catch (JSONException e) {

            return null;
        }
    }

    public static void showDialog(String s, login login) {

    }

    public void setIdUser(String idUser) {

    }
}
