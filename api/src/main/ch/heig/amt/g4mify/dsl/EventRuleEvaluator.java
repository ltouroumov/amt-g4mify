package ch.heig.amt.g4mify.dsl;

import ch.heig.amt.g4mify.dsl.api.Changeset;
import ch.heig.amt.g4mify.model.Event;
import ch.heig.amt.g4mify.model.EventRule;
import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import org.codehaus.groovy.control.CompilerConfiguration;

/**
 * @author ldavid
 * @created 1/9/17
 */
public class EventRuleEvaluator {
    private CompilerConfiguration configuration;

    public EventRuleEvaluator() {
        configuration = new CompilerConfiguration();
        configuration.setScriptBaseClass("ch.heig.amt.g4mify.dsl.api.EventRuleScript");
    }

    public Changeset evaluate(EventRule rule, Event event) {
        Binding binding = new Binding();
        binding.setVariable("event", event);

        //TODO: Implement basic security -_-
        //TODO: Setup a secure class loader
        GroovyShell shell = new GroovyShell(getClass().getClassLoader(), binding, configuration);
        return (Changeset)shell.evaluate(rule.getScript());
    }

}
