package ch.heig.amt.g4mify.dsl.api

import java.util.stream.Collectors

/**
 * @author ldavid
 * @created 1/9/17
 */
class ExpressionAnd extends Expression {

    @Override
    boolean eval(Context ctx) {
        return checks.stream().map{ it.eval(ctx) }.allMatch{ it }
    }

    @Override
    String dump() {
        return String.format("(& %s)", checks.stream().map{ it.dump() }.collect(Collectors.joining(" ")))
    }
}
