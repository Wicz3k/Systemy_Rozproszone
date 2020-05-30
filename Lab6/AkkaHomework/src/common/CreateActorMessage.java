package common;

public class CreateActorMessage {

    private final String product;
    private final int amount;
    private final int asks;

    public CreateActorMessage(int amount, String product, int asks) {
        this.amount = amount;
        this.product = product;
        this.asks = asks;
    }

    public String getProduct() {
        return product;
    }

    public int getAmount() {
        return amount;
    }

    public int getAsks() {
        return asks;
    }
}
