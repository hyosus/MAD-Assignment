package sg.edu.np.mad.assignment.weather.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import sg.edu.np.mad.assignment.R;
import sg.edu.np.mad.assignment.weather.model.WeatherResponse;
import sg.edu.np.mad.assignment.weather.network.WeatherService;

public class WeatherDetailActivity extends AppCompatActivity {
    public static String BaseUrl = "https://api.openweathermap.org/";
    public static String AppId = "8807203da8313b0107bfd5ec66ced07c";

    TextView tvTemperature, tvWeather, tvCity, tvCountry;
    ImageView imageView;
    Button btnBack;
    String latitude, longitude, country, city, temp, date, main;
    Activity context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_detail);
        context = WeatherDetailActivity.this;

        latitude = getIntent().getStringExtra("latitude");
        longitude = getIntent().getStringExtra("longitude");
        city = getIntent().getStringExtra("city");
        tvTemperature = findViewById(R.id.tempDisplay);
        tvWeather = findViewById(R.id.weather);
        tvCity = findViewById(R.id.location);
        tvCountry = findViewById(R.id.country);
        imageView = findViewById(R.id.button1);
        btnBack = findViewById(R.id.btnBack);

        tvTemperature.setText(".......");
        tvWeather.setText(".....");
        tvCity.setText("...........");
        tvCountry.setText(".....");

        getCurrentData();


        btnBack.setOnClickListener(v -> onBackPressed());
        imageView.setOnClickListener(v -> {
            getCurrentData();
        });
    }

    public void getCurrentData() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BaseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        WeatherService weatherService = retrofit.create(WeatherService.class);
        Call<WeatherResponse> call = weatherService.getCurrentWeatherData(latitude, longitude, AppId);
        call.enqueue(new Callback() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) {
                if (response.code() == 200) {
                    WeatherResponse weatherResponse = (WeatherResponse) response.body();
                    assert weatherResponse != null;
                    Calendar calendar = Calendar.getInstance();
                    @SuppressLint("SimpleDateFormat")
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE-dd-MM");
                    date = simpleDateFormat.format(calendar.getTime());

                    country = weatherResponse.sys.country;
                    if (TextUtils.isEmpty(city)) {
                        city = weatherResponse.name;
                    }
                    temp = String.valueOf(weatherResponse.main.temp);
                    main = String.valueOf(weatherResponse.weather.get(0).getMain());

                    setDataInViews();
                }
            }

            @Override
            public void onFailure(@NonNull Call call, @NonNull Throwable t) {
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void setDataInViews() {
        tvTemperature.setText(changeTemp(temp) + " \u2103");
        tvWeather.setText(main);
        tvCountry.setText(country);
        tvCity.setText(city);
    }

    private String changeTemp(String x) {
        double celsius = Double.parseDouble(x) - 273.0;
        Integer i = (int) celsius;
        return String.valueOf(i);
    }
}