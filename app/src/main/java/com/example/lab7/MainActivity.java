package com.example.lab7;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private static final String API_KEY = "А я вам его не скажу)";
    private static final String BASE_URL = "https://api.openweathermap.org/data/2.5/";

    private TextView weatherCity1;
    private TextView weatherCity2;
    private TextView weatherCity3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        weatherCity1 = findViewById(R.id.weather_city1);
        weatherCity2 = findViewById(R.id.weather_city2);
        weatherCity3 = findViewById(R.id.weather_city3);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        WeatherService service = retrofit.create(WeatherService.class);

        loadWeatherData(service, "Moscow", weatherCity1);
        loadWeatherData(service, "London", weatherCity2);
        loadWeatherData(service, "Tokyo", weatherCity3);
    }

    private void loadWeatherData(WeatherService service, String city, TextView textView) {
        Call<WeatherResponse> call = service.getWeather(city, API_KEY, "metric");
        call.enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                Log.d("isSuccessful", response.isSuccessful() + "");
                Log.d("body", response.body() + "");
                Log.d("message", response.message() + "");
                Log.d("requesturl", call.request().url() + "");
                if (response.isSuccessful() && response.body() != null) {
                    WeatherResponse weatherResponse = response.body();
                    String weatherInfo = "Температура: " + weatherResponse.getMain().getTemp() + "°C\n" +
                            "Состояние: " + weatherResponse.getWeather()[0].getDescription();
                    textView.setText(weatherInfo);
                } else {
                    textView.setText("Ошибка при загрузке");
                }
            }

            @Override
            public void onFailure(Call<WeatherResponse> call, Throwable t) {
                textView.setText("Error: " + t.getMessage());
            }
        });
    }
}