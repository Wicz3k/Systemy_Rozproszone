package Server.Handlers;

import Smart.weatherData;
import com.zeroc.Ice.Current;

import java.util.Map;

public class SlowWeatherStationHandler extends WeatherStationHandler {
    public SlowWeatherStationHandler(String name) {
        super(name);
    }

    @Override
    public float getTemperature(Current current) {
        sleep(1000);
        return super.getTemperature(current);
    }

    @Override
    public float getHumidity(Current current) {
        sleep(2000);
        return super.getHumidity(current);
    }

    @Override
    public int getPressure(Current current) {
        sleep(1000);
        return super.getPressure(current);
    }

    @Override
    public weatherData getWeatherData(Current current) {
        sleep(1000);
        return super.getWeatherData(current);
    }

    @Override
    public Map<String, weatherData> getDailyWeather(Current current) {
        sleep(1000);
        return super.getDailyWeather(current);
    }

    private void sleep(int time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            println("interupted");
        }
    }
}
