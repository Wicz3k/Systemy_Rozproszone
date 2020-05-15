import org.apache.zookeeper.*;
import org.apache.zookeeper.AsyncCallback.StatCallback;
import org.apache.zookeeper.AsyncCallback.ChildrenCallback;
import org.apache.zookeeper.KeeperException.Code;
import org.apache.zookeeper.data.Stat;

import java.util.List;

public class DataMonitor implements Watcher, StatCallback, ChildrenCallback {

    ZooKeeper zk;
    String znode;
    Watcher chainedWatcher;
    boolean dead;
    DataMonitorListener listener;
    byte prevData[];

    public DataMonitor(ZooKeeper zk, String znode, Watcher chainedWatcher,
                       DataMonitorListener listener) {
        this.zk = zk;
        this.znode = znode;
        this.chainedWatcher = chainedWatcher;
        this.listener = listener;
        zk.exists(znode, true, this, null);
    }

    public interface DataMonitorListener {

        void exists(boolean alived, String path);

        void closing(int rc);
    }

    public void process(WatchedEvent event) {
        String path = event.getPath();
        //System.out.println("Event: path = " + path + " type = " + event.getType() + " state = " + event.getState());
        if (event.getType() == Event.EventType.None) {
            // state of the connection has changed
            switch (event.getState()) {
                case SyncConnected:
                    break;
                case Expired:
                    // It's all over
                    dead = true;
                    listener.closing(KeeperException.Code.SessionExpired);
                    break;
            }
        }
        else if(event.getType() == Event.EventType.NodeDeleted) {
            listener.exists(false, path);
            if (path != null && path.equals(znode)) {
                zk.exists(znode, true, this, null);
            } else {
                zk.removeWatches(path, this, WatcherType.Any, true, null, null);
            }
        }
        else {
            if (path != null && path.startsWith(znode)) {
                zk.exists(path, true, this, null);
            }
        }
        if (chainedWatcher != null) {
            chainedWatcher.process(event);
        }
    }

    public void processResult(int rc, String path, Object ctx, Stat stat) {
        boolean exists;
        //System.out.println("Stat result: rc= " + rc + " path = " + path + " stat = " + stat);
        switch (rc) {
            case Code.Ok:
                exists = true;
                zk.getChildren(path, true, this, null);
                break;
            case Code.NoNode:
                exists = false;
                break;
            case Code.SessionExpired:
            case Code.NoAuth:
                dead = true;
                listener.closing(rc);
                return;
            default:
                // Retry errors
                zk.exists(znode, true, this, null);
                return;
        }
        listener.exists(exists, path);
    }

    @Override
    public void processResult(int i, String s, Object o, List<String> list) {
        //System.out.println("Child result: i= " + i + " s = " + s + " o = " + o);
        for (String child : list) {
            //System.out.println("Child result child: name= " + child);
            zk.exists(s + "/" + child, true, this, null);
        }
    }
}

