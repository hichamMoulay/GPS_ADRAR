package com.dev_app.gps_adrar.testing;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.dev_app.gps_adrar.GPSTracker;
import com.dev_app.gps_adrar.MainActivity;
import com.dev_app.gps_adrar.R;
import com.dev_app.gps_adrar.api.FetchURL;
import com.dev_app.gps_adrar.api.TaskLoadedCallback;
import com.dev_app.gps_adrar.databinding.ActivityMapsTestBinding;
import com.dev_app.gps_adrar.drawer_menu.adapter_drawr;
import com.dev_app.gps_adrar.fragment.setting;
import com.dev_app.gps_adrar.variable;
import com.directions.route.AbstractRouting;
import com.directions.route.Route;
import com.directions.route.RouteException;
import com.directions.route.Routing;
import com.directions.route.RoutingListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.snackbar.Snackbar;
import com.yarolegovich.slidingrootnav.SlidingRootNav;
import com.yarolegovich.slidingrootnav.SlidingRootNavBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

//public class MapsActivityTEST extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.OnConnectionFailedListener, RoutingListener {
public class MapsActivityTEST extends FragmentActivity implements OnMapReadyCallback, TaskLoadedCallback, LocationListener {

    private static final String TAG = "MapsActivityTEST";
    private GoogleMap mMap;
    private ActivityMapsTestBinding binding;
    SearchView searchView;
    GPSTracker gpsTracker;
    boolean isStart = false;
    int second = 0;
    Button btnStart;
    private Marker marker;
    private Polyline currentPolyline;
    View view;
    BottomSheetDialog dialog;

    GPSTracker tracker;
    private Handler handler = new Handler();

    // 14 min
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsTestBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        gpsTracker = new GPSTracker(MapsActivityTEST.this);
        searchView = findViewById(R.id.search_view);

        view = LayoutInflater.from(this).inflate(R.layout.dialog_bottom, null, false);
        dialog = new BottomSheetDialog(MapsActivityTEST.this);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                String location = searchView.getQuery().toString();
                List<Address> addressList = null;

                if (location != null || !location.equals("")) {
                    Geocoder geocoder = new Geocoder(MapsActivityTEST.this);

                    try {
                        addressList = geocoder.getFromLocationName(location, 1);
                        System.out.println("addressList= " + addressList);
                        if (addressList.size() >= 1) {
                            gpsTracker = new GPSTracker(MapsActivityTEST.this);
                            Address address = addressList.get(0);

                            LatLng latLngNow = new LatLng(gpsTracker.getLatitude(), gpsTracker.getLongitude());
                            LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());

                            //PolylineOptions polylineOptions = new PolylineOptions().color(Color.GREEN).width(10).geodesic(true);
                            //polylineOptions.add(latLngNow, latLng);

                            mMap.addMarker(new MarkerOptions().position(latLng).title(location));
                            //mMap.addPolyline(polylineOptions);

                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 50));

                            //new FetchURL(MapsActivityTEST.this).execute(getUrl(latLngNow, latLng, "driving"), "driving");

                            String txt = "يرجى الانتظار يتم الحساب ..";
                            //bottomDialog(txt, txt);

                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        setDrawer(savedInstanceState);

        //setData();
/*
        double latA = 27.8901519,
                lngA = -0.2822334,

                latB = 27.899096,
                lngB = -0.2778335;

        LatLng a = new LatLng(latA, lngA);
        LatLng b = new LatLng(latB, lngB);

        Log.e("test distance 1 = ", gpsTracker.CalculationByDistance(a, b) + "");
        Log.e("test distance 2 = ", gpsTracker.getDistance(latA, lngA, latB, lngB) + "");
*/

        mapFragment.getMapAsync(this);

    }

    private MarkerOptions marker_start, marker_end;

    void onClickMap() {

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(@NonNull LatLng latLng) {
                mMap.clear();

                gpsTracker = new GPSTracker(MapsActivityTEST.this);
                LatLng locationSet = new LatLng(gpsTracker.getLatitude(), gpsTracker.getLongitude());

                marker_start = new MarkerOptions().position(locationSet).title("position start");
                marker_end = new MarkerOptions().position(latLng).title("position end");

                new FetchURL(MapsActivityTEST.this).execute(getUrl(marker_start.getPosition(), marker_end.getPosition(), "driving"), "driving");
                //Toast.makeText(MapsActivityTEST.this, "wait Map click ", Toast.LENGTH_LONG).show();
                String txt = "يرجى الانتظار يتم الحساب ..";
                //bottomDialog(txt, txt);
                //getUrl(marker_start.getPosition(), marker_end.getPosition(), "driving");
                //Findroutes(marker_start.getPosition(), marker_end.getPosition());
            }
        });

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(@NonNull Marker marker1) {

                mMap.clear();

                gpsTracker = new GPSTracker(MapsActivityTEST.this);
                marker = marker1;
                LatLng locationSet = new LatLng(gpsTracker.getLatitude(), gpsTracker.getLongitude());

                marker_start = new MarkerOptions().position(locationSet).title("position start");

                //getUrl(marker_start.getPosition(), marker1.getPosition(), "driving");
                new FetchURL(MapsActivityTEST.this).execute(getUrl(marker_start.getPosition(), marker1.getPosition(), "driving"), "driving");

                String txt = "يرجى الانتظار يتم الحساب ..";
                //bottomDialog(txt, txt);
                return true;
            }
        });


    }

    void multiClick(View view) {

        switch (view.getId()) {
            case R.id.mosqi:
                ClickMosqi();
                break;
            case R.id.atm:
                ClickAtm();
                break;
            case R.id.station:
                ClickStation();
                break;
            case R.id.bus:
                ClickBus();
                break;
            case R.id.hotel:
                ClickHotel();
                break;
            case R.id.hospital:
                ClickHospital();
                break;
            case R.id.airplane:
                ClickAirplane();
                break;
        }

    }

    /*
        public void Findroutes(LatLng Start, LatLng End) {
            if (Start == null || End == null) {
                Toast.makeText(MapsActivityTEST.this, "Unable to get location", Toast.LENGTH_LONG).show();
            } else {

                Routing routing = new Routing.Builder()
                        .travelMode(AbstractRouting.TravelMode.DRIVING)
                        .withListener(this)
                        .alternativeRoutes(true)
                        .waypoints(Start, End)
                        .key(String.valueOf(R.string.google_maps_key))  //also define your api key here.
                        .build();
                routing.execute();
            }
        }
    */

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        onClickMap();

        gpsTracker = new GPSTracker(MapsActivityTEST.this);
        LatLng locationSet = new LatLng(gpsTracker.getLatitude(), gpsTracker.getLongitude());
        mMap.addMarker(new MarkerOptions().position(locationSet).title("position start"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(locationSet));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(locationSet, 50));

    }

    void getDataGoogle(String url) {

        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Log.e(TAG, "response volley " + response);

                try {

                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("status"); // == "OK";
                    JSONArray jsonArray = jsonObject.getJSONArray("routes");

                    double distanceTotal = 0, durationTotal = 0;

                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONArray jsonArrayLegs = jsonArray.getJSONObject(i).getJSONArray("legs");

                        for (int l = 0; l < jsonArrayLegs.length(); l++) {

                            JSONArray jsonArraySteps = jsonArrayLegs.getJSONObject(l).getJSONArray("steps");

                            for (int s = 0; s < jsonArraySteps.length(); s++) {

                                JSONObject object = jsonArraySteps.getJSONObject(s);

                                JSONObject distance = object.getJSONObject("distance");
                                JSONObject duration = object.getJSONObject("duration");

                                String distanceText = distance.getString("text");
                                String durationText = duration.getString("text")
                                        .replace("min", "")
                                        .replace("mins", "")
                                        .replace("s", "")
                                        .trim();


                                if (durationText.contains("mins")) {
                                    durationText = durationText.replace("mins", "").trim();

                                } else if (durationText.contains("s")) {
                                    Log.e(TAG, " ok ok " + durationText);
                                    durationText = durationText.replace("s", "").trim();
                                    durationTotal += Double.parseDouble(durationText) * 60;
                                }
                                durationTotal += Double.parseDouble(durationText);

                                if (distanceText.contains("km")) {

                                    distanceText = distanceText.replace("km", "").trim();
                                    distanceTotal += Double.parseDouble(distanceText) * 1000;

                                } else {
                                    distanceText = distanceText.replace("m", "").trim();
                                    distanceTotal += Double.parseDouble(distanceText);
                                }

                                Log.e(TAG, "response volley: " + object);
                            }


                        }

                    }

                    Log.e(TAG, "total  distance Total" + distanceTotal);
                    Log.e(TAG, "total  duration Total" + durationTotal);

                    bottomDialog(durationTotal + "", distanceTotal + "");


                } catch (JSONException e) {
                    Log.e(TAG, "error json " + e.getMessage());
                    bottomDialog("0", "0");
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "error volley " + error);
            }
        });
        Volley.newRequestQueue(this).add(request);

    }

    boolean isEnabled = true;

    void setData() {

        TextView txtSpeed = findViewById(R.id.txtSpeed);
        TextView txtTime = findViewById(R.id.txtTime);
        btnStart = findViewById(R.id.btnStart);

        Handler handler = new Handler();

        new Runnable() {
            @Override
            public void run() {
                gpsTracker = new GPSTracker(MapsActivityTEST.this);
                Log.e("isGPSEnabled ", gpsTracker.isGPSEnabled + "");
                //Log.e("hasSpeed ", gpsTracker.getLocation().hasSpeed()+ "");

                double speed = 0;

                if (!gpsTracker.isGPSEnabled && isEnabled) {
                    gpsTracker.showSettingsAlert();
                    gpsTracker = new GPSTracker(MapsActivityTEST.this);
                    isEnabled = false;
                } else if (gpsTracker.isGPSEnabled) {
                    //speed = gpsTracker.getLocation().getSpeed() * 6.3;
                }

                if (speed >= 100)
                    txtSpeed.setTextColor(Color.RED);
                else
                    txtSpeed.setTextColor(Color.BLACK);

                txtSpeed.setText("" + speed + "\nkm/h");

                if (isStart)
                    second++;

                txtTime.setText(second + "\nsecond");

                handler.postDelayed(this, 1000);
            }
        }.run();

    }

    public void btnClick(View view) {

        isStart = !isStart;

        if (!isStart) {
            second = 0;
            btnStart.setText("start");
        } else {
            btnStart.setText("reset");
        }
    }

    private String getUrl(LatLng origin, LatLng dest, String directionMode) {
        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
        // Mode
        String mode = "mode=" + directionMode;
        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + mode;
        // Output format
        String output = "json";
        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters + "&key=" + getString(R.string.google_maps_key);
        Log.d(TAG, "getUrl: " + url);
        getDataGoogle(url);
        return url;
    }

    /*
        //Routing call back functions.
        @Override
        public void onRoutingFailure(RouteException e) {
            View parentLayout = findViewById(android.R.id.content);
            Snackbar snackbar = Snackbar.make(parentLayout, e.toString(), Snackbar.LENGTH_LONG);
            snackbar.show();
            Findroutes(marker_start.getPosition(), marker_end.getPosition());

            Log.e("RouteException", e.getMessage());
        }

        @Override
        public void onRoutingStart() {
            //Toast.makeText(MapsActivityTEST.this, "Finding Route...", Toast.LENGTH_LONG).show();
        }

        //If Route finding success..
        @Override
        public void onRoutingSuccess(ArrayList<Route> route, int shortestRouteIndex) {

            CameraUpdate center = CameraUpdateFactory.newLatLng(marker_start.getPosition());
            CameraUpdate zoom = CameraUpdateFactory.zoomTo(16);
            if (polylines != null) {
                polylines.clear();
            }
            PolylineOptions polyOptions = new PolylineOptions();
            LatLng polylineStartLatLng = null;
            LatLng polylineEndLatLng = null;


            polylines = new ArrayList<>();
            //add route(s) to the map using polyline
            for (int i = 0; i < route.size(); i++) {

                if (i == shortestRouteIndex) {
                    polyOptions.color(Color.GREEN);
                    polyOptions.width(7);
                    polyOptions.addAll(route.get(shortestRouteIndex).getPoints());
                    Polyline polyline = mMap.addPolyline(polyOptions);
                    polylineStartLatLng = polyline.getPoints().get(0);
                    int k = polyline.getPoints().size();
                    polylineEndLatLng = polyline.getPoints().get(k - 1);
                    polylines.add(polyline);

                } else {

                }

            }

            //Add Marker on route starting position
            MarkerOptions startMarker = new MarkerOptions();
            startMarker.position(polylineStartLatLng);
            startMarker.title("My Location");
            mMap.addMarker(startMarker);

            //Add Marker on route ending position
            MarkerOptions endMarker = new MarkerOptions();
            endMarker.position(polylineEndLatLng);
            endMarker.title("Destination");
            mMap.addMarker(endMarker);
        }

        @Override
        public void onRoutingCancelled() {
            Findroutes(marker_start.getPosition(), marker_end.getPosition());
        }

        @Override
        public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
            Findroutes(marker_start.getPosition(), marker_end.getPosition());

        }
    */

    @Override
    public void onTaskDone(Object... values) {
        PolylineOptions[] polylineOptions = {new PolylineOptions()};

        if (currentPolyline != null)
            currentPolyline.remove();

        currentPolyline = mMap.addPolyline((PolylineOptions) values[0]);
        //currentPolyline.setColor(Color.GREEN);

        Log.e(TAG, "onTaskDone: " + currentPolyline.getPoints() + "");

        Calendar cal = Calendar.getInstance();

        int numberDay = cal.get(Calendar.DAY_OF_WEEK) - 1, second = 0;
        String nameDay = days(numberDay);
        int periodTime = typeTime();
        double countTime = 0, countDistance = 0, speed = 0;

        Log.e(TAG, "count  " + currentPolyline.getPoints().size() + "");
        Log.e(TAG, "onTaskDone: number day " + numberDay + "");
        Log.e(TAG, "onTaskDone: nameDay day " + nameDay + "");
        Log.e(TAG, "onTaskDone: period Time " + periodTime + "");

        for (int i = 1; i < currentPolyline.getPoints().size(); i++) {

            double lat_a = currentPolyline.getPoints().get(i - 1).latitude,
                    lon_a = currentPolyline.getPoints().get(i - 1).longitude,
                    lat_b = currentPolyline.getPoints().get(i).latitude,
                    lon_b = currentPolyline.getPoints().get(i).longitude;

            double distance = gpsTracker.getDistance(lat_a, lon_a, lat_b, lon_b);

/*
            Log.e(TAG, "onTaskDone: lat_a " + lat_a + "");
            Log.e(TAG, "onTaskDone: lon_a " + lon_a + "");
            Log.e(TAG, "onTaskDone: lat_b " + lat_b + "");
            Log.e(TAG, "onTaskDone: lon_b " + lon_b + "");
            Log.e(TAG, "onTaskDone: distance " + distance + "");
*/

            JSONObject r1 = variable.predictDistance(lat_a, lon_a, lat_b, lon_b,
                    numberDay, periodTime, distance, MapsActivityTEST.this);

            JSONObject r2 = variable.predictTime(lat_a, lon_a, lat_b, lon_b,
                    numberDay, periodTime, distance, MapsActivityTEST.this);

            double result_Distance = 0, result_time = 0;

            try {

                result_Distance = Integer.parseInt(r1.getString("result_Distance"));
                //result_time = Integer.parseInt(r2.getString("result_time"));


            } catch (JSONException e) {
                e.printStackTrace();
            } finally {


                if (result_Distance == 1) {
                    // 10 km/h
                    speed = 10;
                    second = 20;
                    polylineOptions[0].add(currentPolyline.getPoints().get(i)).color(Color.RED);
                } else if (result_Distance == 2) {
                    // 30 km/h
                    speed = 30;
                    second = 15;
                    polylineOptions[0].add(currentPolyline.getPoints().get(i)).color(Color.YELLOW);
                } else if (result_Distance == 3) {
                    // 50 km/h
                    speed = 50;
                    second = 10;
                    polylineOptions[0].add(currentPolyline.getPoints().get(i)).color(Color.GREEN);
                } else if (result_Distance == 4) {
                    // 60 km/h
                    speed = 60;
                    second = 5;
                    polylineOptions[0].add(currentPolyline.getPoints().get(i)).color(Color.BLUE);
                }

                mMap.addPolyline(polylineOptions[0]);

                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLng(currentPolyline.getPoints().get(i - 1));
                mMap.animateCamera(cameraUpdate);

                double timeR = 0.0014;  // 5 / 3600; // 0.0014
                double distance2 = timeR * speed;

                double tt = distance / distance2;

                result_time = tt * second;

            }

            countTime += result_time;
            countDistance += result_Distance;

        }

        Log.e(TAG, "model total time " + countTime + "");
        Log.e(TAG, "model total distance " + countDistance + "");

        //bottomDialog(countDistance + "", countTime + "");

    }

    void bottomDialog(String txtDestonce, String txtTime) {

        dialog.cancel();

        TextView txtTotalTime = view.findViewById(R.id.txtTotalTime);
        TextView txtTotalDestonce = view.findViewById(R.id.txtTotalDestonce);
        Button btnGo = view.findViewById(R.id.btnGo);
        Button btnCancel = view.findViewById(R.id.btnCancell);

        txtTotalTime.setText(txtTime);
        txtTotalDestonce.setText(txtDestonce);

        btnGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDataTxt.run();
                gpsTracker = new GPSTracker(MapsActivityTEST.this);

                if (gpsTracker.canGetLocation())
                    onLocationChanged(gpsTracker.getLocation());
                else
                    gpsTracker.showSettingsAlert();

            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    setDataTxt.wait();

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                dialog.cancel();
                mMap.clear();
            }
        });

        dialog.setContentView(view);


        dialog.show();

    }

    private static void animateMarker(final GoogleMap map, final Marker marker, final List<LatLng> directionPoint,
                                      final boolean hideMarker, final List<Float> degree, final List<Integer> colors) {
        final Handler handler = new Handler();
        final long start = SystemClock.uptimeMillis();
        final long duration = 300000;
        final PolylineOptions[] polylineOptions = {new PolylineOptions()};
        final Interpolator interpolator = new LinearInterpolator();
        if (map != null) {
            handler.post(new Runnable() {
                int i = 0;

                @Override
                public void run() {
                    long elapsed = SystemClock.uptimeMillis() - start;
                    float t = interpolator.getInterpolation((float) elapsed / duration);

                    if (i < directionPoint.size() - 1) {

                        final LatLng currentPosition = new LatLng(
                                directionPoint.get(i).latitude * (1 - t) + directionPoint.get(i + 1).latitude * t,
                                directionPoint.get(i).longitude * (1 - t) + directionPoint.get(i + 1).longitude * t);

                        marker.setRotation(degree.get(i));
                        marker.setPosition(currentPosition);
                        polylineOptions[0].add(directionPoint.get(i)).color(colors.get(i));
                        map.addPolyline(polylineOptions[0]);
                        if (i % 5 != 0) {
                            polylineOptions[0] = new PolylineOptions();
                            polylineOptions[0].add(directionPoint.get(i)).color(colors.get(i));
                            map.addPolyline(polylineOptions[0]);
                        }
                        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLng(currentPosition);
                        map.animateCamera(cameraUpdate);
                        i++;

                    }
                    if (t < 1.0) {
                        // Post again 100ms later.
                        handler.postDelayed(this, 100);
                    } else {
                        if (hideMarker) {
                            marker.setVisible(false);
                        } else {
                            marker.setVisible(true);
                        }
                    }
                }
            });

        }
    }

    int typeTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH");
        int number = Integer.parseInt(dateFormat.format(new Date()));

        if (number >= 7 && number <= 16)
            return 1;
        else if (number > 16 && number <= 21)
            return 2;
        else
            return 3;

    }

    String dateNOW() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");
        return dateFormat.format(new Date());
    }

    String days(int day) {

        String[] days = new String[]{"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};

        return days[day];
    }

    public void imgClick(View view) {

        multiClick(view);
    }

    void setDrawer(Bundle savedInstanceState) {


        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("GPS ADRAR");
        //setSupportActionBar(toolbar);

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

    @Override
    public void onLocationChanged(@NonNull Location location) {

        double latitude = location.getLatitude(),
                longitude = location.getLongitude();

        LatLng position = new LatLng(latitude, longitude);

        mMap.addMarker(new MarkerOptions().position(position));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(position, 50));

        Toast.makeText(this, "test chang " + location.getSpeed() * 6.3, Toast.LENGTH_LONG).show();

    }


    public void ClickMosqi() {
        gpsTracker = new GPSTracker(MapsActivityTEST.this);

        if (!gpsTracker.canGetLocation()) {
            gpsTracker.showSettingsAlert();
            return;
        }

        ArrayList<LatLng> listMosqi = new ArrayList<>();

        listMosqi.add(new LatLng(12, 13));
        listMosqi.add(new LatLng(12, 13));
        listMosqi.add(new LatLng(12, 13));
        listMosqi.add(new LatLng(12, 13));
        listMosqi.add(new LatLng(12, 13));
        listMosqi.add(new LatLng(12, 13));
        listMosqi.add(new LatLng(12, 13));
        listMosqi.add(new LatLng(12, 13));
        listMosqi.add(new LatLng(12, 13));

        LatLng local = new LatLng(gpsTracker.getLatitude(), gpsTracker.getLongitude());
        LatLng mainMosqi = new LatLng(0, 0);
        double mainDistance = 999999999999999999999999999.0;

        for (LatLng list : listMosqi) {

            double distance = gpsTracker.getDistance(gpsTracker.getLatitude(), gpsTracker.getLongitude(),
                    list.latitude, list.longitude);

            if (distance <= mainDistance) {
                mainMosqi = list;
            }
            mainDistance = distance;

        }

        new FetchURL(MapsActivityTEST.this).execute(getUrl(local, mainMosqi, "driving"), "driving");

    }

    public void ClickAtm() {
        gpsTracker = new GPSTracker(MapsActivityTEST.this);

        if (!gpsTracker.canGetLocation()) {
            gpsTracker.showSettingsAlert();
            return;
        }
        LatLng mainAtm = new LatLng(0, 0);
        ArrayList<LatLng> listAtm = new ArrayList<>();

        listAtm.add(new LatLng(12, 13));
        listAtm.add(new LatLng(12, 13));
        listAtm.add(new LatLng(12, 13));
        listAtm.add(new LatLng(12, 13));
        listAtm.add(new LatLng(12, 13));
        listAtm.add(new LatLng(12, 13));
        listAtm.add(new LatLng(12, 13));
        listAtm.add(new LatLng(12, 13));
        listAtm.add(new LatLng(12, 13));

        LatLng local = new LatLng(gpsTracker.getLatitude(), gpsTracker.getLongitude());

        double mainDistance = 999999999999999999999999999.0;

        for (LatLng list : listAtm) {

            double distance = gpsTracker.getDistance(gpsTracker.getLatitude(), gpsTracker.getLongitude(),
                    list.latitude, list.longitude);

            if (distance <= mainDistance) {
                mainAtm = list;
            }
            mainDistance = distance;

        }

        new FetchURL(MapsActivityTEST.this).execute(getUrl(local, mainAtm, "driving"), "driving");

    }

    public void ClickStation() {
        gpsTracker = new GPSTracker(MapsActivityTEST.this);

        if (!gpsTracker.canGetLocation()) {
            gpsTracker.showSettingsAlert();
            return;
        }
        LatLng mainStation = new LatLng(0, 0);
        ArrayList<LatLng> listStation = new ArrayList<>();

        listStation.add(new LatLng(12, 13));
        listStation.add(new LatLng(12, 13));
        listStation.add(new LatLng(12, 13));
        listStation.add(new LatLng(12, 13));
        listStation.add(new LatLng(12, 13));
        listStation.add(new LatLng(12, 13));
        listStation.add(new LatLng(12, 13));
        listStation.add(new LatLng(12, 13));
        listStation.add(new LatLng(12, 13));

        LatLng local = new LatLng(gpsTracker.getLatitude(), gpsTracker.getLongitude());

        double mainDistance = 999999999999999999999999999.0;

        for (LatLng list : listStation) {

            double distance = gpsTracker.getDistance(gpsTracker.getLatitude(), gpsTracker.getLongitude(),
                    list.latitude, list.longitude);

            if (distance <= mainDistance) {
                mainStation = list;
            }
            mainDistance = distance;

        }

        new FetchURL(MapsActivityTEST.this).execute(getUrl(local, mainStation, "driving"), "driving");

    }

    public void ClickBus() {
        gpsTracker = new GPSTracker(MapsActivityTEST.this);

        if (!gpsTracker.canGetLocation()) {
            gpsTracker.showSettingsAlert();
            return;
        }
        LatLng mainBus = new LatLng(0, 0);
        ArrayList<LatLng> listBus = new ArrayList<>();

        listBus.add(new LatLng(12, 13));
        listBus.add(new LatLng(12, 13));
        listBus.add(new LatLng(12, 13));
        listBus.add(new LatLng(12, 13));
        listBus.add(new LatLng(12, 13));
        listBus.add(new LatLng(12, 13));
        listBus.add(new LatLng(12, 13));
        listBus.add(new LatLng(12, 13));
        listBus.add(new LatLng(12, 13));

        LatLng local = new LatLng(gpsTracker.getLatitude(), gpsTracker.getLongitude());

        double mainDistance = 999999999999999999999999999.0;

        for (LatLng list : listBus) {

            double distance = gpsTracker.getDistance(gpsTracker.getLatitude(), gpsTracker.getLongitude(),
                    list.latitude, list.longitude);

            if (distance <= mainDistance) {
                mainBus = list;
            }
            mainDistance = distance;

        }

        new FetchURL(MapsActivityTEST.this).execute(getUrl(local, mainBus, "driving"), "driving");

    }

    public void ClickHotel() {
        gpsTracker = new GPSTracker(MapsActivityTEST.this);

        if (!gpsTracker.canGetLocation()) {
            gpsTracker.showSettingsAlert();
            return;
        }
        LatLng mainHotel = new LatLng(0, 0);
        ArrayList<LatLng> listHotel = new ArrayList<>();

        listHotel.add(new LatLng(12, 13));
        listHotel.add(new LatLng(12, 13));
        listHotel.add(new LatLng(12, 13));
        listHotel.add(new LatLng(12, 13));
        listHotel.add(new LatLng(12, 13));
        listHotel.add(new LatLng(12, 13));
        listHotel.add(new LatLng(12, 13));
        listHotel.add(new LatLng(12, 13));
        listHotel.add(new LatLng(12, 13));

        LatLng local = new LatLng(gpsTracker.getLatitude(), gpsTracker.getLongitude());

        double mainDistance = 999999999999999999999999999.0;

        for (LatLng list : listHotel) {

            double distance = gpsTracker.getDistance(gpsTracker.getLatitude(), gpsTracker.getLongitude(),
                    list.latitude, list.longitude);

            if (distance <= mainDistance) {
                mainHotel = list;
            }
            mainDistance = distance;

        }

        new FetchURL(MapsActivityTEST.this).execute(getUrl(local, mainHotel, "driving"), "driving");

    }

    public void ClickHospital() {
        gpsTracker = new GPSTracker(MapsActivityTEST.this);

        if (!gpsTracker.canGetLocation()) {
            gpsTracker.showSettingsAlert();
            return;
        }
        LatLng mainHospital = new LatLng(0, 0);
        ArrayList<LatLng> listHospital = new ArrayList<>();

        listHospital.add(new LatLng(12, 13));
        listHospital.add(new LatLng(12, 13));
        listHospital.add(new LatLng(12, 13));
        listHospital.add(new LatLng(12, 13));
        listHospital.add(new LatLng(12, 13));
        listHospital.add(new LatLng(12, 13));
        listHospital.add(new LatLng(12, 13));
        listHospital.add(new LatLng(12, 13));
        listHospital.add(new LatLng(12, 13));

        LatLng local = new LatLng(gpsTracker.getLatitude(), gpsTracker.getLongitude());

        double mainDistance = 999999999999999999999999999.0;

        for (LatLng list : listHospital) {

            double distance = gpsTracker.getDistance(gpsTracker.getLatitude(), gpsTracker.getLongitude(),
                    list.latitude, list.longitude);

            if (distance <= mainDistance) {
                mainHospital = list;
            }
            mainDistance = distance;

        }

        new FetchURL(MapsActivityTEST.this).execute(getUrl(local, mainHospital, "driving"), "driving");

    }

    public void ClickAirplane() {
        gpsTracker = new GPSTracker(MapsActivityTEST.this);

        if (!gpsTracker.canGetLocation()) {
            gpsTracker.showSettingsAlert();
            return;
        }
        LatLng mainAirplane = new LatLng(0, 0);
        ArrayList<LatLng> listAirplane = new ArrayList<>();

        listAirplane.add(new LatLng(12, 13));
        listAirplane.add(new LatLng(12, 13));
        listAirplane.add(new LatLng(12, 13));
        listAirplane.add(new LatLng(12, 13));
        listAirplane.add(new LatLng(12, 13));
        listAirplane.add(new LatLng(12, 13));
        listAirplane.add(new LatLng(12, 13));
        listAirplane.add(new LatLng(12, 13));
        listAirplane.add(new LatLng(12, 13));

        LatLng local = new LatLng(gpsTracker.getLatitude(), gpsTracker.getLongitude());

        double mainDistance = 999999999999999999999999999.0;

        for (LatLng list : listAirplane) {

            double distance = gpsTracker.getDistance(gpsTracker.getLatitude(), gpsTracker.getLongitude(),
                    list.latitude, list.longitude);

            if (distance <= mainDistance) {
                mainAirplane = list;
            }
            mainDistance = distance;

        }

        new FetchURL(MapsActivityTEST.this).execute(getUrl(local, mainAirplane, "driving"), "driving");

    }

    boolean isSend = false;
    Runnable setDataTxt = new Runnable() {
        @Override
        public void run() {

            tracker = new GPSTracker(MapsActivityTEST.this);
            mamhl mamhl = new mamhl();

            for (LatLng position : mamhl.listMamhl()) {

                if (tracker.getDistance(tracker.getLatitude(), tracker.getLongitude(), position.latitude, position.longitude) <= 1 &&
                        !isSend) {

                    isSend = true;
                    /// her send ..
                }

            }

            handler.postDelayed(this, 1000);
        }
    };


}