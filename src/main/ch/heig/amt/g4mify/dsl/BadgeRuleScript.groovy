package ch.heig.amt.g4mify.dsl

import java.util.logging.Logger

/**
 * @author ldavid
 * @created 1/6/17
 */
abstract class BadgeRuleScript extends Script {

    private static final Logger LOG = Logger.getLogger(BadgeRuleScript.class.simpleName)

    def when = { counter ->
        LOG.info("Checking $counter")
        [is: { cond ->
            [and: when, or: when]
        }]
    }

}
