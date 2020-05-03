package Server;

import com.zeroc.Ice.*;

import java.lang.Exception;
import java.util.Hashtable;

public class SmartServer {
    public void t1(String[] args)
    {
        int status = 0;
        Communicator communicator = null;
        ServantLocator sl = null;

        try
        {
            // 1. Inicjalizacja ICE - utworzenie communicatora
            communicator = Util.initialize(args);

            // 2. Konfiguracja adaptera
            // METODA 1 (polecana produkcyjnie): Konfiguracja adaptera Adapter1 jest w pliku konfiguracyjnym podanym jako parametr uruchomienia serwera
            ObjectAdapter adapter = communicator.createObjectAdapter("SmartAdapter");

            Hashtable<String, DeviceType> availableDevices = getAvailableDevices();
            sl = new SmartServantLocator(adapter, availableDevices);
            adapter.addServantLocator(sl, "weather");
            adapter.addServantLocator(sl, "camera");
            adapter.addServantLocator(sl, "heating");


            // 5. Aktywacja adaptera i przejście w pętlę przetwarzania żądań
            adapter.activate();

            System.out.println("Entering event processing loop...");

            communicator.waitForShutdown();

        }
        catch (Exception e)
        {
            System.err.println(e);
            status = 1;
        }
        if (communicator != null)
        {
            // Clean up
            //
            try
            {
                communicator.destroy();
            }
            catch (Exception e)
            {
                System.err.println(e);
                status = 1;
            }
        }
        System.exit(status);
    }

    private Hashtable<String, DeviceType> getAvailableDevices() {
        Hashtable<String, DeviceType> availableDevices = new Hashtable<>();
        availableDevices.put("weather1", DeviceType.WeatherStation);
        availableDevices.put("weather2", DeviceType.WeatherStation);
        availableDevices.put("weatherSlow", DeviceType.SlowWeatherStation);
        availableDevices.put("cam1", DeviceType.Camera);
        availableDevices.put("cam2", DeviceType.Camera);
        availableDevices.put("heating1", DeviceType.CentralHeating);
        return availableDevices;
    }

    public static void main(String[] args)
    {
        SmartServer app = new SmartServer();
        app.t1(args);
    }
}
