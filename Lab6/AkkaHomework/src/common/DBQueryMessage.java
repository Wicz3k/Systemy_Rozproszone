package common;

public class DBQueryMessage {
    private final int asks;

    public DBQueryMessage(int asks) {
        this.asks = asks;
    }

    public int getAsks() {
        return asks;
    }
}
