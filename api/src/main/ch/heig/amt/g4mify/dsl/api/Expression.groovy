package ch.heig.amt.g4mify.dsl.api

/**
 * @author ldavid
 * @created 1/9/17
 */
abstract class Expression {

    List<Expression> checks = new ArrayList<Expression>()

    def add(Expression check) {
        checks.add(check)
    }

    abstract boolean eval(Context ctx)

    abstract String dump()
}
