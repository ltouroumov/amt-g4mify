package ch.heig.amt.g4mify.model.view;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import static ch.heig.amt.g4mify.model.view.ViewUtils.fieldsOf;

/**
 * @author ldavid
 * @created 12/7/16
 */
public class OutputView<TOut> {

    private final Class<? extends TOut> outputClass;
    private final Map<String, Function<Object, Object>> mappers;

    public OutputView(Class<? extends TOut> outputClass) {
        this.outputClass = outputClass;
        this.mappers = new HashMap<>();
    }

    public OutputView<TOut> map(String prop, Function<Object, Object> mapper) {
        mappers.put(prop, mapper);
        return this;
    }

    public TOut from(Object input) {
        TOut output;

        try {
            output = outputClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new ViewException(String.format(
                    "Unable to create view %s, missing no-args constructor",
                    outputClass.getSimpleName()
            ));
        }

        ViewUtils.copy(output, input, fieldsOf(output), mappers);

        return output;
    }

}
