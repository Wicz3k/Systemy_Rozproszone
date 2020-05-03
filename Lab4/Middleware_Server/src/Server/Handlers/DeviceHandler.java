package Server.Handlers;

import Smart.Device;
import Smart.errorCode;
import com.zeroc.Ice.Current;
import javafx.util.Pair;

import java.util.*;

public class DeviceHandler implements Device {
    private HashMap<Integer, Integer> errorCodes = new HashMap<Integer, Integer>();
    String name;

    public DeviceHandler(String name) {
        this.name = name;
    }

    @Override
    public errorCode[] getErrorCodes(Current current) {
        println("Get error codes: " + errorCodes.size());
        errorCode[] errors = new errorCode[errorCodes.size()];
        Iterator<Map.Entry<Integer, Integer>> iterator = errorCodes.entrySet().iterator();
        for(int i = 0; i< errors.length && iterator.hasNext(); i++){
            Map.Entry<Integer, Integer> entry = iterator.next();
            errorCode error = new errorCode(entry.getKey(), entry.getValue());
            errors[i] = error;
        }

        return errors;
    }

    @Override
    public void resetErrorCodes(Current current) {
        println("Reset error codes");
        errorCodes.clear();
    }

    protected void rememberError(int code){
        println("Remember error code: " + code);
        if(!errorCodes.containsKey(code)){
            errorCodes.put(code, 1);
        }
        else{
            errorCodes.replace(code, errorCodes.get(code)+1);
        }
    }

    protected void println(String message){
        System.out.println(name + ": " +message);
    }
}
