package Server.Handlers;

import Smart.CentralHeating;
import Smart.heatingMode;
import com.zeroc.Ice.Current;

public class CentralHeatingHandler extends DeviceHandler implements CentralHeating {
    private float temperature = 21;
    private heatingMode mode;

    public CentralHeatingHandler(String name) {
        super(name);
    }

    @Override
    public void setTemperature(float temperature, Current current) {
        println("Set temperature: " + temperature);
        this.temperature = temperature;
    }

    @Override
    public float getDesiredTemperature(Current current) {
        println("Get desired temperature: " + temperature);
        return temperature;
    }

    @Override
    public void setHeatingMode(heatingMode mode, Current current) {
        println("set heating mode: " + mode);
        this.mode = mode;
    }

    @Override
    public heatingMode getHeatingMode(Current current) {
        println("Get heating mode: " + mode);
        return mode;
    }
}
