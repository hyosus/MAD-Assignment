package sg.edu.np.mad.assignment.weather.network;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import sg.edu.np.mad.assignment.weather.model.WeatherResponse;

public interface WeatherService {
    @GET("data/2.5/weather?")
    Call<WeatherResponse> getCurrentWeatherData(@Query("lat") String lat, @Query("lon") String lon, @Query("appid") String app_id);
}