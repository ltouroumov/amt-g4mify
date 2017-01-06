package ch.heig.amt.g4mify.dsl

/**
 * @author ldavid
 * @created 1/6/17
 */
class CounterUpdate {

    public String counter
    public long amount
    public boolean set

    CounterUpdate(String counter, long amount, boolean set) {
        this.counter = counter
        this.amount = amount
        this.set = set
    }
}
