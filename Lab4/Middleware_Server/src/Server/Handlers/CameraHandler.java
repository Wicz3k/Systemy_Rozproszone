package Server.Handlers;

import Smart.ArgumentOutOfRangeException;
import Smart.Camera;
import Smart.cameraPosition;
import com.zeroc.Ice.Current;

public class CameraHandler extends DeviceHandler implements Camera {
    cameraPosition position = new cameraPosition(0,0,0);
    boolean isLocked = false;

    public CameraHandler(String name) {
        super(name);
    }

    @Override
    public boolean setPosition(cameraPosition position, Current current) throws ArgumentOutOfRangeException {
        println("Set position: P: " + position.pan + "\tT: " + position.tilt + "\tZ: " + position.zoom);
        if(isLocked){
            println("Position is locked");
            rememberError(1);
            return false;
        }
        if(position.pan > 90 || position.pan < -90){
            println("Pan out of range");
            throw new ArgumentOutOfRangeException("pan", -90, 90);
        }
        if(position.tilt > 90 || position.tilt < -90){
            println("Tilt out of range");
            throw new ArgumentOutOfRangeException("tilt", -90, 90);
        }
        if(position.zoom > 10 || position.zoom < 0){
            println("Zoom out of range");
            throw new ArgumentOutOfRangeException("zoom", 0, 10);
        }
        try {
            Thread.sleep((long) (1000 * position.zoom));
        }
        catch (InterruptedException e){
            println("interupted");
        }
        this.position = position;
        println("Position set correctly");
        return true;
    }

    @Override
    public cameraPosition getPosition(Current current) {
        println("Get position: P: " + position.pan + "\tT: " + position.tilt + "\tZ: " + position.zoom);
        return position;
    }

    @Override
    public boolean getLocked(Current current) {
        println("Get locked: " + isLocked);
        return isLocked;
    }

    @Override
    public void setLocked(boolean locked, Current current) {
        println("set locked: " + locked);
        this.isLocked = locked;
    }
}
