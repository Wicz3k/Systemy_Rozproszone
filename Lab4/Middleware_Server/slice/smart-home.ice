
#ifndef HOME_ICE
#define HOME_ICE

module Smart{
    enum heatingMode{
        OFF, ON, HOLIDAY
    }

    struct cameraPosition{
        float pan;
        float tilt;
        float zoom;
    }

    struct weatherData{
        float temperature;
        float humidity;
        int pressure;
    }

    dictionary<string, weatherData> dailyWeather;

    struct errorCode{
        int code;
        int count;
    }

    sequence<errorCode> errorCodes;

    exception ArgumentOutOfRangeException{
        string argumentName;
        float expectedMinimum;
        float expectedMaximum;
    }

    interface Device{
        errorCodes getErrorCodes();
        void resetErrorCodes();
    }

    interface WeatherStation extends Device{
        float getTemperature();
        float getHumidity();
        int getPressure();
        weatherData getWeatherData();
        dailyWeather getDailyWeather();
    }

    interface CentralHeating extends Device{
        void setTemperature(float temperature);
        float getDesiredTemperature();
        void setHeatingMode(heatingMode mode);
        heatingMode getHeatingMode();
    }

    interface Camera extends Device{
        bool setPosition(cameraPosition position)
            throws ArgumentOutOfRangeException;
        cameraPosition getPosition();
        bool getLocked();
        void setLocked(bool locked);
    }
}

#endif