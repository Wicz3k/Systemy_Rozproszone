package Server;

import Server.Handlers.DeviceHandler;
import Server.Handlers.HandlerFactory;
import com.zeroc.Ice.*;
import com.zeroc.Ice.Object;

import java.util.Hashtable;

public class SmartServantLocator implements ServantLocator {
    ObjectAdapter adapter;
    Hashtable<String, DeviceType> availableDevices;

    public SmartServantLocator(ObjectAdapter adapter, Hashtable<String, DeviceType> availableDevices) {
        this.adapter = adapter;
        this.availableDevices = availableDevices;
    }

    @Override
    public LocateResult locate(Current current) throws UserException {
        String name = current.id.name;
        String category = current.id.category;
        System.out.println("Looking for: " + name + " / " + category);
        if(!availableDevices.containsKey(name) || !availableDevices.get(name).getCategory().equals(category)){
            System.out.println("Can't find: " + name + " / " + category);
            return new LocateResult(null, null);
        }

        DeviceHandler handler = HandlerFactory.CreateHandler(name, availableDevices.get(name));

        adapter.add(handler, new Identity(name, category));

        System.out.println("Created: " + name + " / " + category);

        return new LocateResult(handler, null);
    }

    @Override
    public void finished(Current current, Object object, java.lang.Object o) throws UserException {

    }

    @Override
    public void deactivate(String s) {

    }
}
