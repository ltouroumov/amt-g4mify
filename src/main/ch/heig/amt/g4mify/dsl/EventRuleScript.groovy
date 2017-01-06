package ch.heig.amt.g4mify.dsl

import java.util.logging.Logger

/**
 * @author ldavid
 * @created 1/6/17
 */
abstract class EventRuleScript extends Script {

    private static final Logger LOG = Logger.getLogger(EventRuleScript.class.simpleName)

    abstract def process()

    public Changeset changeset

    def update(String counter) {
        [add: { long amount -> changeset.update(counter, amount) },
         sub: { long amount -> changeset.update(counter, -amount) },
         set: { long amount -> changeset.update(counter, amount, true) }]
    }

    def award(String badgeType) {
        changeset.award(badgeType)
    }

    Changeset run() {
        changeset = new Changeset()
        process()
        return changeset
    }
}
