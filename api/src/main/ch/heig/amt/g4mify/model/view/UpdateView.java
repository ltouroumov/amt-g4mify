package ch.heig.amt.g4mify.model.view;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

import static ch.heig.amt.g4mify.model.view.ViewUtils.fieldsOf;

/**
 * @author ldavid
 * @created 12/7/16
 */
public class UpdateView<TOut> {

    private final TOut input;
    private final Map<String, Function<Object, Object>> mappers;

    public UpdateView(TOut input) {
        this.input = input;
        this.mappers = new HashMap<>();
    }

    public UpdateView<TOut> map(String prop, Function<Object, Object> mapper) {
        mappers.put(prop, mapper);
        return this;
    }

    public TOut with(Object fromObj) {
        Set<String> fields = fieldsOf(input, fromObj);
        ViewUtils.copy(input, fromObj, fields, mappers);
        return input;
    }
}
