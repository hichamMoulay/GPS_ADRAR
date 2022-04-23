package com.dev_app.gps_adrar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.chaquo.python.Python;
import com.chaquo.python.android.AndroidPlatform;
import com.dev_app.gps_adrar.databinding.ActivityMapsTestBinding;
import com.dev_app.gps_adrar.testing.MainActivityTEST;
import com.dev_app.gps_adrar.testing.MapsActivityTEST;
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
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class gps extends Fragment implements
        GoogleApiClient.OnConnectionFailedListener, RoutingListener {

    private GoogleMap mMap;
    SearchView searchView;
    GPSTracker gpsTracker;
    boolean isStart = false;
    int second = 0;
    Button btnStart;
    View view;
    Location destinationLocation = null;
    boolean locationPermission = false;
    private List<Polyline> polylines = null;
    private Context context;

    private OnMapReadyCallback callback = new OnMapReadyCallback() {

        @Override
        public void onMapReady(GoogleMap googleMap) {
            mMap = googleMap;
            onClickMap();

            gpsTracker = new GPSTracker(context);
            LatLng locationSet = new LatLng(gpsTracker.getLatitude(), gpsTracker.getLongitude());
            mMap.addMarker(new MarkerOptions().position(locationSet).title("position start"));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(locationSet));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(locationSet, 10));

//            LatLng azzoua = new LatLng(27.2018742235377, 0.160042718052864);
//            LatLng adrar = new LatLng(27.87534605, -0.28564769);
//
//            LatLng anyPlase = new LatLng(27.8811466666666, -0.289636666666666);
//            LatLng anyPlase1 = new LatLng(27.2181191900745, -0.18024880439043);
//
//            googleMap.addMarker(new MarkerOptions().position(azzoua).title("Marker in azzoua"));
//            googleMap.addMarker(new MarkerOptions().position(adrar).title("Marker in adrar"));
//            googleMap.addMarker(new MarkerOptions().position(anyPlase).title("Marker in any"));
//            googleMap.addMarker(new MarkerOptions().position(anyPlase1).title("Marker in any 1"));
//
//            googleMap.moveCamera(CameraUpdateFactory.newLatLng(adrar));
//
//            PolylineOptions optionsLine=new PolylineOptions().color(Color.GREEN).width(10).geodesic(true);
//            optionsLine.add(azzoua,adrar);
//
//            PolylineOptions optionsLine1=new PolylineOptions().color(Color.RED).width(10).geodesic(true);
//            optionsLine1.add(anyPlase1,anyPlase);
//
//
//            googleMap.addPolyline(optionsLine);
//            googleMap.addPolyline(optionsLine1);

        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view1 = inflater.inflate(R.layout.fragment_gps, container, false);
        view = view;
        context = container.getContext();
        gpsTracker = new GPSTracker(container.getContext());
/*
        searchView = view.findViewById(R.id.search_view);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                String location = searchView.getQuery().toString();
                List<Address> addressList = null;

                System.out.println("query= " + query);
                System.out.println("searchView= " + searchView.getQuery());

                if (location != null || !location.equals("")) {
                    Geocoder geocoder = new Geocoder(container.getContext());

                    try {
                        addressList = geocoder.getFromLocationName(location, 1);
                        System.out.println("addressList= " + addressList);
                        if (addressList.size() >= 1) {
                            gpsTracker = new GPSTracker(container.getContext());
                            Address address = addressList.get(0);

                            LatLng latLngNow = new LatLng(gpsTracker.getLatitude(), gpsTracker.getLongitude());
                            LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());

                            PolylineOptions polylineOptions = new PolylineOptions().color(Color.GREEN).width(10).geodesic(true);
                            polylineOptions.add(latLngNow, latLng);


                            mMap.addMarker(new MarkerOptions().position(latLng).title(location));
                            mMap.addPolyline(polylineOptions);

                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));

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
*/
        setData(view1);

        return view1;
    }

    boolean isEnabled = true;

    void setData(View view) {

        TextView txtSpeed = view.findViewById(R.id.txtSpeed);
        TextView txtTime = view.findViewById(R.id.txtTime);
        btnStart = view.findViewById(R.id.btnStart);

        Handler handler = new Handler();
/*
        new Runnable() {
            @Override
            public void run() {
                gpsTracker = new GPSTracker(context);
                Log.e("isGPSEnabled ", gpsTracker.isGPSEnabled + "");
                //Log.e("hasSpeed ", gpsTracker.getLocation().hasSpeed()+ "");

                double speed = 0;

                if (!gpsTracker.isGPSEnabled && isEnabled) {
                    gpsTracker.showSettingsAlert();
                    gpsTracker = new GPSTracker(context);
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
*/
    }

    private MarkerOptions marker_start, marker_end;

    void onClickMap() {

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(@NonNull LatLng latLng) {

                gpsTracker = new GPSTracker(context);
                LatLng locationSet = new LatLng(gpsTracker.getLatitude(), gpsTracker.getLongitude());

                marker_start = new MarkerOptions().position(locationSet).title("position start");
                marker_end = new MarkerOptions().position(latLng).title("position end");

                Findroutes(marker_start.getPosition(), marker_end.getPosition());
            }
        });

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(@NonNull Marker marker) {

                Toast.makeText(context, "onMarkerClick = " + marker.getPosition(), Toast.LENGTH_LONG).show();

                return true;
            }
        });


    }

    public void Findroutes(LatLng Start, LatLng End) {
        if (Start == null || End == null) {
            Toast.makeText(context, "Unable to get location", Toast.LENGTH_LONG).show();
        } else {

            Routing routing = new Routing.Builder()
                    .travelMode(AbstractRouting.TravelMode.DRIVING)
                    .withListener((RoutingListener) this)
                    .alternativeRoutes(true)
                    .waypoints(Start, End)
                    .key(String.valueOf(R.string.google_maps_key))  //also define your api key here.
                    .build();
            routing.execute();
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
    }


    //Routing call back functions.
    @Override
    public void onRoutingFailure(RouteException e) {
        View parentLayout = view.findViewById(android.R.id.content);
        Snackbar snackbar = Snackbar.make(parentLayout, e.toString(), Snackbar.LENGTH_LONG);
        snackbar.show();
        Findroutes(marker_start.getPosition(), marker_end.getPosition());
    }

    @Override
    public void onRoutingStart() {
        Toast.makeText(context, "Finding Route...", Toast.LENGTH_LONG).show();
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

}