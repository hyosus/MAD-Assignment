package sg.edu.np.mad.assignment.weather.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import sg.edu.np.mad.assignment.R;
import sg.edu.np.mad.assignment.weather.adapter.WeatherAdapter;
import sg.edu.np.mad.assignment.weather.model.WeatherDataModel;

public class WeatherActivity extends AppCompatActivity {
    Button btnBack;
    EditText etSearch;
    int REQUEST_CODE = 100, REQUEST_CODE_CHECK_SETTINGS = 321;
    FusedLocationProviderClient client;
    Double latitude, longitude;
    String city;


    RecyclerView recyclerview;
    WeatherAdapter adapter;
    List<WeatherDataModel> modelList;
    Activity context;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        context = WeatherActivity.this;
        btnBack = findViewById(R.id.btnBack);
        recyclerview = findViewById(R.id.recyclerview);
        etSearch = findViewById(R.id.etSearch);
        client = LocationServices.getFusedLocationProviderClient(context);

        modelList = new ArrayList<>();

        if (!checkLocationPermission()) {
            requestForPermission();
        }
        modelList.add(new WeatherDataModel("New York", "40.7127281", "-74.0060152"));
        modelList.add(new WeatherDataModel("Chicago", "41.8755616", "-87.6244212"));
        modelList.add(new WeatherDataModel("Phoenix", "33.4484367", "-112.074141"));
        modelList.add(new WeatherDataModel("San Diego", "32.7174202", "-117.1627728"));
        modelList.add(new WeatherDataModel("Austin", "30.2711286", "-97.7436995"));
        modelList.add(new WeatherDataModel("Yishun", "1.4293839", "103.8350282"));
        modelList.add(new WeatherDataModel("Kampong Ubi", "1.3299354", "103.8970444"));
        modelList.add(new WeatherDataModel("Seletar", "1.4098488", "103.8773789"));
        modelList.add(new WeatherDataModel("Jurong East", "1.333108", "103.7422939"));
        modelList.add(new WeatherDataModel("Paya Lebar", "1.3174795000000001", "103.89235252467483"));
        modelList.add(new WeatherDataModel("Shanghai", "31.2322758", "121.4692071"));
        modelList.add(new WeatherDataModel("Beijing", "40.190632", "116.412144"));
        modelList.add(new WeatherDataModel("Guangzhou", "23.1301964", "113.2592945"));
        modelList.add(new WeatherDataModel("Chongqing", "29.5647398", "106.5478767"));
        modelList.add(new WeatherDataModel("Shenzhen", "22.5445741", "114.0545429"));
        modelList.add(new WeatherDataModel("Kuala Lumpur", "3.1516964", "101.6942371"));
        modelList.add(new WeatherDataModel("Malacca", "2.3293744", "102.2880962"));
        modelList.add(new WeatherDataModel("George Town", "5.4141619", "100.3287352"));
        modelList.add(new WeatherDataModel("Kuching", "1.5574127", "110.3439862"));
        modelList.add(new WeatherDataModel("Kota Kinabalu", "1.5574127", "116.0728988"));
        modelList.add(new WeatherDataModel("Berlin", "52.5170365", "13.3888599"));
        modelList.add(new WeatherDataModel("Rome", "41.8933203", "12.4829321"));
        modelList.add(new WeatherDataModel("Helsinki", "60.1717794", "24.9413548"));
        modelList.add(new WeatherDataModel("Athens", "39.3289242", "-82.1012479"));
        modelList.add(new WeatherDataModel("Prague", "35.485837000000004", "-96.69326517032614"));
        modelList.add(new WeatherDataModel("Tokyo", "35.6828387", "139.7594549"));
        modelList.add(new WeatherDataModel("Kyoto", "35.021041", "135.7556075"));
        modelList.add(new WeatherDataModel("Osaka", "34.7021912", "135.4955866"));
        modelList.add(new WeatherDataModel("Sapporo", "43.061936", "141.3542924"));
        modelList.add(new WeatherDataModel("Yokohama", "35.444991", "139.636768"));
        modelList.add(new WeatherDataModel("London", "51.5073219", "-0.1276474"));
        modelList.add(new WeatherDataModel("Bristol", "51.4538022", "-2.5972985"));
        modelList.add(new WeatherDataModel("City of London", "51.5156177", "-0.0919983"));
        modelList.add(new WeatherDataModel("Leicester", "52.6361398", "-1.1330789"));
        modelList.add(new WeatherDataModel("Edinburgh", "55.9533456", "-3.1883749"));
        modelList.add(new WeatherDataModel("Sydney", "-33.8698439", "151.2082848"));
        modelList.add(new WeatherDataModel("Melbourne", "-37.8142176", "144.9631608"));
        modelList.add(new WeatherDataModel("Canberra", "-35.2975906", "149.1012676"));
        modelList.add(new WeatherDataModel("Brisbane", "-27.4689682", "153.0234991"));
        modelList.add(new WeatherDataModel("Adelaide", "-34.9281805", "138.5999312"));

        setDataInRecyclerview();

        btnBack.setOnClickListener(v -> onBackPressed());
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filterList(s.toString());
            }
        });
    }

    private void filterList(String s) {
        List<WeatherDataModel> filteredList = new ArrayList<>();
        for (WeatherDataModel item : modelList) {
            String cityName = item.getCityName();
            if (cityName.toLowerCase().contains(s.toLowerCase())) {
                filteredList.add(item);
            }
        }
        if (filteredList.size() != 0) {
            adapter.filterList(filteredList);
        }
    }

    //Map Permission
    private boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            getCurrentLocation();
            return true;
        } else {
            return false;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void requestForPermission() {
        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE && grantResults.length > 0 && grantResults[0] + grantResults[1] == PackageManager.PERMISSION_GRANTED) {
            getCurrentLocation();
        } else {
            Toast.makeText(context, "Permission denied", Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressLint("MissingPermission")
    private void getCurrentLocation() {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            client.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    Location location = task.getResult();
                    if (location != null) {
                        latitude = location.getLatitude();
                        longitude = location.getLongitude();
                    } else {
                        LocationRequest locationRequest = new LocationRequest()
                                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                                .setInterval(10000)
                                .setFastestInterval(1000)
                                .setNumUpdates(1);

                        LocationCallback locationCallback = new LocationCallback() {
                            @Override
                            public void onLocationResult(LocationResult locationResult) {
                                Location location1 = locationResult.getLastLocation();
                                latitude = location1.getLatitude();
                                longitude = location1.getLongitude();
                            }
                        };
                        client.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
                        if (latitude == null) {
                            getCurrentLocation();
                            client.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
                        }
                    }
                    //Get Address
                    Geocoder geocoder;
                    List<Address> addresses;
                    geocoder = new Geocoder(context, Locale.getDefault());
                    try {
                        if (latitude != null) {
                            addresses = geocoder.getFromLocation(latitude, longitude, 1);
                            city = addresses.get(0).getLocality();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    String lat = String.valueOf(latitude);
                    String lng = String.valueOf(longitude);
                    modelList.add(0, new WeatherDataModel("Your Location", lat, lng));
                    recyclerview.setAdapter(null);
                    setDataInRecyclerview();
                }
            });
        } else {
            LocationRequest locationRequest = LocationRequest.create().setInterval(10000).setFastestInterval(1000)
                    .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);

            LocationServices.getSettingsClient(context)
                    .checkLocationSettings(builder.build())
                    .addOnFailureListener(e -> {
                        try {
                            ResolvableApiException resolvable = (ResolvableApiException) e;
                            resolvable.startResolutionForResult(context, REQUEST_CODE_CHECK_SETTINGS);
                        } catch (IntentSender.SendIntentException sendEx) {
                            // Ignore the error.
                        }
                    });
        }
    }

    private void setDataInRecyclerview() {
        recyclerview.setLayoutManager(new LinearLayoutManager(context, RecyclerView.VERTICAL, false));
        adapter = new WeatherAdapter(modelList, context, city);
        recyclerview.setAdapter(adapter);
    }
}