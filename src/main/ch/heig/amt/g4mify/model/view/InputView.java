package ch.heig.amt.g4mify.model.view;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import static ch.heig.amt.g4mify.model.view.ViewUtils.fieldsOf;

/**
 * @author ldavid
 * @created 12/7/16
 */
public class InputView<TOut> {

    private final Class<TOut> inputClass;
    private final Map<String, Function<Object, Object>> mappers;

    public InputView(Class<TOut> inputClass) {
        this.inputClass = inputClass;
        this.mappers = new HashMap<>();
    }

    public InputView<TOut> map(String prop, Function<Object, Object> mapper) {
        mappers.put(prop, mapper);
        return this;
    }

    public TOut from(Object fromObj) {
        TOut toObj;

        try {
            toObj = inputClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new ViewException(String.format(
                    "Unable to create view %s, missing no-args constructor",
                    inputClass.getSimpleName()
            ));
        }

        ViewUtils.copy(toObj, fromObj, fieldsOf(fromObj), mappers);

        return toObj;
    }
}
