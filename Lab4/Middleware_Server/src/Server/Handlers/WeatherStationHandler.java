package Server.Handlers;

import Smart.WeatherStation;
import Smart.weatherData;
import com.zeroc.Ice.Current;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;

public class WeatherStationHandler extends DeviceHandler implements WeatherStation {
    private Random rand = new Random();

    public WeatherStationHandler(String name) {
        super(name);
    }

    @Override
    public float getTemperature(Current current) {
        println("Get temperature");
        return getTemperature();
    }

    @Override
    public float getHumidity(Current current) {
        println("Get humidity");
        return getHumidity();
    }

    @Override
    public int getPressure(Current current) {
        println("Get pressure");
        return getPressure();
    }

    @Override
    public weatherData getWeatherData(Current current) {
        println("Get weather data");
        return getWeatherData();
    }

    @Override
    public Map<String, weatherData> getDailyWeather(Current current) {
        println("Get daily weather");
        Map<String, weatherData> data = new LinkedHashMap<>();
        for(int i=0; i<24; i++){
            data.put(i+":00", getWeatherData());
        }
        return data;
    }

    private float getTemperature() {
        return rand.nextFloat()*40-10;
    }

    private float getHumidity() {
        return rand.nextFloat()*80+10;
    }

    private int getPressure() {
        return rand.nextInt(100)+950;
    }

    private weatherData getWeatherData(){
        return new weatherData(getTemperature(), getHumidity(), getPressure());
    }
}
