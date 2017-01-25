package ch.heig.amt.g4mify.dsl.api

/**
 * @author ldavid
 * @created 1/9/17
 */
class ExpressionTest extends Expression {

    String counter
    Closure<Boolean> check

    ExpressionTest(String counter, Closure<Boolean> check) {
        this.counter = counter
        this.check = check
    }

    @Override
    boolean eval(Context ctx) {
        def value = ctx.get(this.counter)
        return check(value)
    }

    @Override
    String dump() {
        return String.format("(? %s)", counter)
    }
}
