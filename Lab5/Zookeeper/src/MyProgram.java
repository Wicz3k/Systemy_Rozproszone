import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashSet;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

public class MyProgram implements Watcher, Runnable, DataMonitor.DataMonitorListener {
    String znode;
    DataMonitor dm;
    ZooKeeper zk;
    String exec[];
    Process child;
    HashSet<String> nodes = new HashSet<String>();

    public MyProgram(String hostPort, String znode, String exec[]) throws KeeperException, IOException {
        this.exec = exec;
        this.znode = znode;
        zk = new ZooKeeper(hostPort, 30000, this);
        dm = new DataMonitor(zk, znode, null, this);
    }

    public static void main(String[] args) {
        if (args.length < 3) {
            System.err.println("USAGE: Executor hostPort znode program [args ...]");
            System.exit(2);
        }
        String hostPort = args[0];
        String znode = args[1];
        String exec[] = new String[args.length - 2];
        System.arraycopy(args, 2, exec, 0, exec.length);
        try {
            new MyProgram(hostPort, znode, exec).run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void process(WatchedEvent event) {
        dm.process(event);
    }

    public void run() {
        while (!dm.dead) {
            try {
                char a = (char) System.in.read();
                if(a == 'p') {
                    for (String path : nodes) {
                        System.out.println(path);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void closing(int rc) {
        System.out.println("Press key to close program");
        if(child!=null){
            child.destroy();
            try {
                child.waitFor();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    static class StreamWriter extends Thread {
        OutputStream os;

        InputStream is;

        StreamWriter(InputStream is, OutputStream os) {
            this.is = is;
            this.os = os;
            start();
        }

        public void run() {
            byte b[] = new byte[80];
            int rc;
            try {
                while ((rc = is.read(b)) > 0) {
                    os.write(b, 0, rc);
                }
            } catch (IOException e) {
            }

        }
    }

    public void exists(boolean alived, String path) {
        //System.out.println(path + " : " + (alived?"alived":"dead"));
        if (alived) {
            if (child == null && path.equals(znode)) {
                try {
                    System.out.println("Starting child");
                    child = Runtime.getRuntime().exec(exec);
                    //new StreamWriter(child.getInputStream(), System.out);
                    //new StreamWriter(child.getErrorStream(), System.err);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(nodes.add(path)) {
                System.out.println("Actually have: " + (nodes.size() - 1) + " childs.");
            }
        } else {
            if (child != null && path.equals(znode)) {
                System.out.println("Killing process");
                child.destroy();
                try {
                    child.waitFor();
                } catch (InterruptedException e) {
                }
                child = null;
                nodes.clear();
            }
            if(nodes.remove(path) && nodes.size()>0){
                System.out.println("Actually have: " + (nodes.size() - 1) + " childs.");
            }
        }
    }
}