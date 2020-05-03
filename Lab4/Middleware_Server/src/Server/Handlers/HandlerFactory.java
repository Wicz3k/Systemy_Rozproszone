package Server.Handlers;

import Server.DeviceType;

public class HandlerFactory {
    public static DeviceHandler CreateHandler(String name, DeviceType type){
        switch(type){
            case Camera:
                return new CameraHandler(name);
            case CentralHeating:
                return new CentralHeatingHandler(name);
            case SlowWeatherStation:
                return new SlowWeatherStationHandler(name);
            case WeatherStation:
                return new WeatherStationHandler(name);
            default:
                throw new IllegalArgumentException(name);
        }
    }
}
