package ch.heig.amt.g4mify.dsl.api

/**
 * @author ldavid
 * @created 1/6/17
 */
class Changeset {

    public Map<String, CounterUpdate> updates = new HashMap<>()

    public Set<Award> awards = new HashSet<>()

    def update(String counter, Long amount, boolean set = false) {
        update(new CounterUpdate(counter, amount, set))
    }

    def update(CounterUpdate update) {
        if (updates.containsKey(update.counter)) {
            def other = updates[update.counter]
            if (!other.set) {
                other.amount += update.amount
            }
        } else {
            updates[update.counter] = update
        }
    }

    def award(String id) {
        awards << new Award(id)
    }

    void merge(Changeset other) {
        for (item in other.updates)
            update(item.value)

        for (item in other.awards)
            awards << item
    }
}