package ch.heig.amt.g4mify.model;

/**
 * @author ldavid
 * @created 11/21/16
 */
public class CounterUpdate {

    private String counter;

    private long amount;

    public CounterUpdate() {
    }

    public String getCounter() {
        return counter;
    }

    public void setCounter(String counter) {
        this.counter = counter;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }
}
