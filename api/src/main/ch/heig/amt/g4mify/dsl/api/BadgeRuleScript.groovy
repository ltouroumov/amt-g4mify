package ch.heig.amt.g4mify.dsl.api

import java.util.logging.Logger

/**
 * @author ldavid
 * @created 1/6/17
 */
abstract class BadgeRuleScript extends Script {

    private static final Logger LOG = Logger.getLogger(BadgeRuleScript.class.simpleName)

    def and = { rules ->
        stack.push(new ExpressionAnd())
        rules()
        def expr = stack.pop()
        stack.peek().add(expr)
    }

    def or = { rules ->
        stack.push(new ExpressionOr())
        rules()
        def expr = stack.pop()
        stack.peek().add(expr)
    }

    def when(String counter) {
        [matches: { Closure<Boolean> check ->
            stack.peek().add(new ExpressionTest(counter, check))
        }]
    }

    def checks = new ExpressionAnd()

    def stack = new Stack<Expression>()

    abstract def rules()

    def run() {
        stack.push(checks)
        rules()
        return checks

    }

}
