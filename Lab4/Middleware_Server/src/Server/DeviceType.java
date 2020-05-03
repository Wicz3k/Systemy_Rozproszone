package Server;

public enum DeviceType {
    Camera, CentralHeating, SlowWeatherStation, WeatherStation;

    public String getCategory(){
        switch(this){
            case Camera:
                return "camera";
            case CentralHeating:
                return "heating";
            case SlowWeatherStation:
            case WeatherStation:
                return "weather";
            default:
                throw new IllegalArgumentException();
        }
    }
}
