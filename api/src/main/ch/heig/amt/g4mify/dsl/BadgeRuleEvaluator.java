package ch.heig.amt.g4mify.dsl;

import ch.heig.amt.g4mify.dsl.api.Expression;
import ch.heig.amt.g4mify.dsl.api.ExpressionAnd;
import ch.heig.amt.g4mify.dsl.api.ExpressionOr;
import ch.heig.amt.g4mify.dsl.api.ExpressionTest;
import ch.heig.amt.g4mify.model.BadgeRule;
import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import org.codehaus.groovy.control.CompilerConfiguration;

import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

/**
 * @author ldavid
 * @created 1/9/17
 */
public class BadgeRuleEvaluator {

    private static final Logger LOG = Logger.getLogger(BadgeRuleEvaluator.class.getName());

    private final CompilerConfiguration configuration;

    public BadgeRuleEvaluator() {
        configuration = new CompilerConfiguration();
        configuration.setScriptBaseClass("ch.heig.amt.g4mify.dsl.api.BadgeRuleScript");
    }

    public Expression evaluate(BadgeRule rule) {
        Binding binding = new Binding();

        //TODO: Implement basic security -_-
        //TODO: Setup a secure class loader
        GroovyShell shell = new GroovyShell(getClass().getClassLoader(), binding, configuration);
        return (Expression) shell.evaluate(rule.getCondition());
    }

    public Set<String> findCounters(BadgeRule rule) {
        Expression expr = evaluate(rule);
        LOG.info("Expression: " + expr.dump());
        return findCountersIn(expr);
    }

    private Set<String> findCountersIn(Expression expr) {
        Set<String> counters = new HashSet<>();
        for (Expression child : expr.getChecks()) {
            if (child instanceof ExpressionTest) {
                counters.add(((ExpressionTest) child).getCounter());
            } else if (child instanceof ExpressionOr || child instanceof ExpressionAnd) {
                counters.addAll(findCountersIn(child));
            }
        }
        return counters;
    }

}
