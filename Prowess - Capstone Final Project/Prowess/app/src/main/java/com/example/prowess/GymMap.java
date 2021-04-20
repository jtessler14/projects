package com.example.prowess;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class GymMap extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener, AdapterView.OnItemSelectedListener, TaskLoadedCallback {

    ImageButton back;

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    Marker mCurrLocationMarker, marker1, marker2, marker3, marker4, marker5, marker6, marker7,
            marker8, marker9, marker10, marker11, marker12, marker13, marker14, marker15, marker16, marker17, marker18, marker19, marker20;
    LocationRequest mLocationRequest;
    private GoogleMap mMap;
    Spinner Gyms;
    ArrayList<String> GymList = new ArrayList<>();
    ArrayAdapter<String> GymAdapter;
    RequestQueue requestQueue;
    private Polyline currentPolyline;
    Button getDirection;
    private MarkerOptions place1, place2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gym_map);

        back = (ImageButton) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openHomePage();
            }
        });

        getDirection = findViewById(R.id.btnGetDirection);

        place1 = new MarkerOptions().position(new LatLng(27.658143, 85.3199503)).title("Location 1");
        place2 = new MarkerOptions().position(new LatLng(27.667491, 85.3208583)).title("Location 2");

        /*marker1 = new MarkerOptions()
                .position(new LatLng(37.41967651075259, -122.09731734025172))
                .title("KOA Fitness")
                .snippet("1050 Independence Ave, Mountain View, CA 94043");
        marker2 = new MarkerOptions()
                .position(new LatLng(37.41608245736202, -122.09386176716093))
                .title("Anarchy Barbell & Athletics")
                .snippet("14707076, Mountain View, CA 94043");
        marker3 = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(37.41543483465131, -122.09008521702249))
                .title("Fit4life")
                .snippet("1954 Old Middlefield Way, Mountain View, CA 94043"));
        marker4 = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(37.41506841404334, -122.09165162702313))
                .title("Kenyon Fitness")
                .snippet("2044 Old Middlefield Way, Mountain View, CA 94043"));
        marker5 = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(37.42683308283163, -122.09950717108244))
                .title("Tony Church Fitness")
                .snippet("4039 Transport St, Palo Alto, CA 94303"));
        marker6 = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(37.417221802822574, -122.10165293838918))
                .title("CrossFit Palo Alto")
                .snippet("2598 W Middlefield Rd, Mountain View, CA 94043"));
        marker7 = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(37.406245712398, -122.0977047268808))
                .title("NCFIT")
                .snippet("112 N Rengstorff Ave, Mountain View, CA 94043"));
        marker8 = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(37.418585179983765, -122.1017387693024))
                .title("Pennywell Fitness")
                .snippet("716 San Antonio Rd L, Palo Alto, CA 94303"));
        marker9 = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(37.40345443353956, -122.10968835286565))
                .title("24 Hour Fitness")
                .snippet("550 Showers Dr, Mountain View, CA 94040"));
        marker10 = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(37.40031799790857, -122.10855174740499))
                .title("Helios Fit")
                .snippet("2464 W El Camino Real, Mountain View, CA 94040"));
        marker11 = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(37.399498096729126, -122.07685641102852))
                .title("INTEGRATE Performance Fitness, LLC")
                .snippet("260 Moffett Blvd, Mountain View, CA 94043"));
        marker12 = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(37.393155635365815, -122.07951792451358))
                .title("Transform Fitness Studio")
                .snippet("319 Castro St, Mountain View, CA 94041"));
        marker13 = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(37.3879441462902, -122.08482786269))
                .title("AVENGERS GYM & FITNESS")
                .snippet("856 CA-82, Mountain View, CA 94040"));
        marker14 = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(37.39928256790453, -122.08344730160233))
                .title("Mountain View Fitness")
                .snippet("241 Polaris Ave, Mountain View, CA 94043"));
        marker15 = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(37.38981478253977, -122.0317213449166))
                .title("Ironwill Fitness")
                .snippet("477 N Mathilda Ave, Sunnyvale, CA 94085"));*/

        requestQueue = Volley.newRequestQueue(this);
        Gyms = (Spinner)findViewById(R.id.Gyms);


        //Takes the url provided which is connected to our team database
        String url = "http://cgi.sice.indiana.edu/~hhauf/Gym.php";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //This grabs the name of the array provided by the Gym.php file and takes all of the GymName's
                //These gyms are now taken and populates the stretches dropdown/spinner
                try {
                    JSONArray jsonArray = response.getJSONArray("Gym");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String stretchTarget = jsonObject.optString("GymName");
                        GymList.add(stretchTarget);
                        GymAdapter = new ArrayAdapter<>(GymMap.this,
                                R.layout.simple_spinner_item, GymList);
                        GymAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
                        Gyms.setAdapter(GymAdapter);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(jsonObjectRequest);
        Gyms.setOnItemSelectedListener(this);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkLocationPermission();
        }
        SupportMapFragment mapFragment = (SupportMapFragment)
                getSupportFragmentManager()
                        .findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);
    }
    //This takes the origin location and the destination and places them into a googleapis link to get the directions
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
        return url;
    }
    //This will add the Polyline to a destination
    @Override
    public void onTaskDone(Object... values) {
        if (currentPolyline != null)
            currentPolyline.remove();
        currentPolyline = mMap.addPolyline((PolylineOptions) values[0]);
    }

    //Setting up Google Map
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setZoomGesturesEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);
        mMap.addMarker(place1);
        mMap.addMarker(place2);



        //Initialize Google Play Services
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
                mMap.setMyLocationEnabled(true);
            }
        } else {
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
        }
    }
    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    //Gets the LocationRequest from the user
    @Override
    public void onConnected(Bundle bundle) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,
                    mLocationRequest, this);
        }
    }
    @Override
    public void onConnectionSuspended(int i) {
    }
    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }
//Showing Current Location Marker on Map
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        LocationManager locationManager = (LocationManager)
                getSystemService(Context.LOCATION_SERVICE);
        String provider = locationManager.getBestProvider(new Criteria(), true);
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Location locations = locationManager.getLastKnownLocation(provider);
        List<String> providerList = locationManager.getAllProviders();
        if (null != locations && null != providerList && providerList.size() > 0) {
            double longitude = locations.getLongitude();
            double latitude = locations.getLatitude();
            Geocoder geocoder = new Geocoder(getApplicationContext(),
                    Locale.getDefault());
            try {
                List<Address> listAddresses = geocoder.getFromLocation(latitude,
                        longitude, 1);
                if (null != listAddresses && listAddresses.size() > 0) {
                    String state = listAddresses.get(0).getAdminArea();
                    String country = listAddresses.get(0).getCountryName();
                    String subLocality = listAddresses.get(0).getSubLocality();
                    markerOptions.title("" + latLng + "," + subLocality + "," + state
                            + "," + country);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //Zooms view into the current location of the user
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
        mCurrLocationMarker = mMap.addMarker(markerOptions);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(11));
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient,
                    this);
        }

        //Populating map with different workout center locations

        marker1 = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(37.41967651075259, -122.09731734025172))
                .title("KOA Fitness")
                .snippet("1050 Independence Ave, Mountain View, CA 94043"));






        marker2 = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(37.41608245736202, -122.09386176716093))
                .title("Anarchy Barbell & Athletics")
                .snippet("14707076, Mountain View, CA 94043"));





        marker3 = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(37.41543483465131, -122.09008521702249))
                .title("Fit4life")
                .snippet("1954 Old Middlefield Way, Mountain View, CA 94043"));




        marker4 = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(37.41506841404334, -122.09165162702313))
                .title("Kenyon Fitness")
                .snippet("2044 Old Middlefield Way, Mountain View, CA 94043"));






        marker5 = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(37.42683308283163, -122.09950717108244))
                .title("Tony Church Fitness")
                .snippet("4039 Transport St, Palo Alto, CA 94303"));






        marker6 = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(37.417221802822574, -122.10165293838918))
                .title("CrossFit Palo Alto")
                .snippet("2598 W Middlefield Rd, Mountain View, CA 94043"));






        marker7 = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(37.406245712398, -122.0977047268808))
                .title("NCFIT")
                .snippet("112 N Rengstorff Ave, Mountain View, CA 94043"));






        marker8 = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(37.418585179983765, -122.1017387693024))
                .title("Pennywell Fitness")
                .snippet("716 San Antonio Rd L, Palo Alto, CA 94303"));






        marker9 = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(37.40345443353956, -122.10968835286565))
                .title("24 Hour Fitness")
                .snippet("550 Showers Dr, Mountain View, CA 94040"));






        marker10 = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(37.40031799790857, -122.10855174740499))
                .title("Helios Fit")
                .snippet("2464 W El Camino Real, Mountain View, CA 94040"));






        marker11 = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(37.399498096729126, -122.07685641102852))
                .title("INTEGRATE Performance Fitness, LLC")
                .snippet("260 Moffett Blvd, Mountain View, CA 94043"));






        marker12 = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(37.393155635365815, -122.07951792451358))
                .title("Transform Fitness Studio")
                .snippet("319 Castro St, Mountain View, CA 94041"));






        marker13 = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(37.3879441462902, -122.08482786269))
                .title("AVENGERS GYM & FITNESS")
                .snippet("856 CA-82, Mountain View, CA 94040"));






        marker14 = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(37.39928256790453, -122.08344730160233))
                .title("Mountain View Fitness")
                .snippet("241 Polaris Ave, Mountain View, CA 94043"));






        marker15 = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(37.38981478253977, -122.0317213449166))
                .title("Ironwill Fitness")
                .snippet("477 N Mathilda Ave, Sunnyvale, CA 94085"));






    }
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
    }
    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {
                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        mMap.setMyLocationEnabled(true);
                    }
                } else {
                    Toast.makeText(this, "permission denied",
                            Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }

    //This takes whatever is selected from gym dropdown and gets the diretions from the current location to whichever gym is selected by the user
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String text = Gyms.getSelectedItem().toString();
        if (text.equals("KOA Fitness")){
            marker1 = mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(37.41967651075259, -122.09731734025172))
                    .title("KOA Fitness")
                    .snippet("1050 Independence Ave, Mountain View, CA 94043")
                    .visible(true));

            getDirection.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new FetchURL(GymMap.this).execute(getUrl(mCurrLocationMarker.getPosition(), marker1.getPosition(), "driving"), "driving");
                }
            });




        }
        if (text.equals("Anarchy Barbell & Athletics")){
            marker2 = mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(37.41608245736202, -122.09386176716093))
                    .title("Anarchy Barbell & Athletics")
                    .snippet("14707076, Mountain View, CA 94043")
                    .visible(true));

            getDirection.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new FetchURL(GymMap.this).execute(getUrl(mCurrLocationMarker.getPosition(), marker2.getPosition(), "driving"), "driving");

                }
            });




        }
        if (text.equals("Fit4life")){
            marker3 = mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(37.41543483465131, -122.09008521702249))
                    .title("Fit4life")
                    .snippet("1954 Old Middlefield Way, Mountain View, CA 94043")
                    .visible(true));

            getDirection.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new FetchURL(GymMap.this).execute(getUrl(mCurrLocationMarker.getPosition(), marker3.getPosition(), "driving"), "driving");
                }
            });





        }
        if (text.equals("Kenyon Fitness")){
            marker4 = mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(37.41506841404334, -122.09165162702313))
                    .title("Kenyon Fitness")
                    .snippet("2044 Old Middlefield Way, Mountain View, CA 94043"));

            getDirection.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new FetchURL(GymMap.this).execute(getUrl(mCurrLocationMarker.getPosition(), marker4.getPosition(), "driving"), "driving");
                }
            });



        }

        if (text.equals("Tony Church Fitness")){
            marker5 = mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(37.42683308283163, -122.09950717108244))
                    .title("Tony Church Fitness")
                    .snippet("4039 Transport St, Palo Alto, CA 94303"));

            getDirection.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new FetchURL(GymMap.this).execute(getUrl(mCurrLocationMarker.getPosition(), marker5.getPosition(), "driving"), "driving");
                }
            });



        }

        if (text.equals("CrossFit Palo Alto")){
            marker6 = mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(37.417221802822574, -122.10165293838918))
                    .title("CrossFit Palo Alto")
                    .snippet("2598 W Middlefield Rd, Mountain View, CA 94043"));

            getDirection.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new FetchURL(GymMap.this).execute(getUrl(mCurrLocationMarker.getPosition(), marker6.getPosition(), "driving"), "driving");
                }
            });



        }

        if (text.equals("NCFIT")){
            marker7 = mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(37.406245712398, -122.0977047268808))
                    .title("NCFIT")
                    .snippet("112 N Rengstorff Ave, Mountain View, CA 94043"));

            getDirection.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new FetchURL(GymMap.this).execute(getUrl(mCurrLocationMarker.getPosition(), marker7.getPosition(), "driving"), "driving");
                }
            });



        }

        if (text.equals("Pennywell Fitness")){
            marker8 = mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(37.418585179983765, -122.1017387693024))
                    .title("Pennywell Fitness")
                    .snippet("716 San Antonio Rd L, Palo Alto, CA 94303"));

            getDirection.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new FetchURL(GymMap.this).execute(getUrl(mCurrLocationMarker.getPosition(), marker8.getPosition(), "driving"), "driving");
                }
            });



        }

        if (text.equals("24 Hour Fitness")){
            marker9 = mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(37.40345443353956, -122.10968835286565))
                    .title("24 Hour Fitness")
                    .snippet("550 Showers Dr, Mountain View, CA 94040"));

            getDirection.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new FetchURL(GymMap.this).execute(getUrl(mCurrLocationMarker.getPosition(), marker9.getPosition(), "driving"), "driving");
                }
            });



        }

        if (text.equals("Helios Fit")){
            marker10 = mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(37.40031799790857, -122.10855174740499))
                    .title("Helios Fit")
                    .snippet("2464 W El Camino Real, Mountain View, CA 94040"));

            getDirection.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new FetchURL(GymMap.this).execute(getUrl(mCurrLocationMarker.getPosition(), marker10.getPosition(), "driving"), "driving");
                }
            });



        }

        if (text.equals("INTEGRATE Performance Fitness, LLC")){
            marker11 = mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(37.399498096729126, -122.07685641102852))
                    .title("INTEGRATE Performance Fitness, LLC")
                    .snippet("260 Moffett Blvd, Mountain View, CA 94043"));

            getDirection.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new FetchURL(GymMap.this).execute(getUrl(mCurrLocationMarker.getPosition(), marker11.getPosition(), "driving"), "driving");
                }
            });



        }

        if (text.equals("Transform Fitness Studio")){
            marker12 = mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(37.393155635365815, -122.07951792451358))
                    .title("Transform Fitness Studio")
                    .snippet("319 Castro St, Mountain View, CA 94041"));

            getDirection.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new FetchURL(GymMap.this).execute(getUrl(mCurrLocationMarker.getPosition(), marker12.getPosition(), "driving"), "driving");
                }
            });



        }

        if (text.equals("AVENGERS GYM & FITNESS")){
            marker13 = mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(37.3879441462902, -122.08482786269))
                    .title("AVENGERS GYM & FITNESS")
                    .snippet("856 CA-82, Mountain View, CA 94040"));

            getDirection.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new FetchURL(GymMap.this).execute(getUrl(mCurrLocationMarker.getPosition(), marker13.getPosition(), "driving"), "driving");
                }
            });



        }

        if (text.equals("Mountain View Fitness")){
            marker14 = mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(37.39928256790453, -122.08344730160233))
                    .title("Mountain View Fitness")
                    .snippet("241 Polaris Ave, Mountain View, CA 94043"));

            getDirection.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new FetchURL(GymMap.this).execute(getUrl(mCurrLocationMarker.getPosition(), marker14.getPosition(), "driving"), "driving");
                }
            });



        }

        if (text.equals("Ironwill Fitness")){
            marker15 = mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(37.38981478253977, -122.0317213449166))
                    .title("Ironwill Fitness")
                    .snippet("477 N Mathilda Ave, Sunnyvale, CA 94085"));

            getDirection.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new FetchURL(GymMap.this).execute(getUrl(mCurrLocationMarker.getPosition(), marker15.getPosition(), "driving"), "driving");
                }
            });



        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void openHomePage() {
        Intent intent = new Intent(this, HomePage.class);
        startActivity(intent);
        finish();
    }
}