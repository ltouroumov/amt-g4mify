package ch.heig.amt.g4mify.model.view;

import java.lang.reflect.Field;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * @author ldavid
 * @created 12/7/16
 */
public class ViewUtils {

    public static <TOut> OutputView<TOut> outputView(Class<TOut> outputClass) {
        return new OutputView<>(outputClass);
    }

    public static <TOut> InputView<TOut> inputView(Class<TOut> inputClass) {
        return new InputView<TOut>(inputClass);
    }

    public static <TOut> UpdateView<TOut> updateView(TOut output) {
        return new UpdateView<>(output);
    }

    public static Function<Object, Object> mapList(Function<Object, Object> mapper) {
        return list -> {
            if (list instanceof List<?>) {
                return ((List<?>) list).stream().map(mapper).collect(Collectors.toList());
            } else {
                throw new ViewException(String.format(
                        "List mapper expected list, got %s",
                        list.getClass().getSimpleName()
                ));
            }
        };
    }

    public static Function<Object, Object> viewMap(Class<?> viewClass) {
        return item -> outputView(viewClass).from(item);
    }

    public static Set<String> fieldsOf(Object... obj) {
        return Arrays.stream(obj)
                .map(Object::getClass)
                .map(Class::getDeclaredFields)
                .map(Arrays::stream)
                .map(fields -> fields.map(Field::getName).collect(Collectors.toSet()))
                .reduce((lista, listb) -> {
                    Set<String> res = new HashSet<>(lista);
                    lista.retainAll(listb);
                    return res;
                }).orElseGet(HashSet::new);
    }

    public static void copy(Object dstObj, Object srcObj, Set<String> fields, Map<String, Function<Object, Object>> mappers) {
        Class<?> srcClass = srcObj.getClass();
        Class<?> dstClass = dstObj.getClass();

        for (String field : fields) {
            try {
                Field srcField = srcClass.getDeclaredField(field);
                Field dstField = dstClass.getDeclaredField(field);

                srcField.setAccessible(true);
                dstField.setAccessible(true);

                Object value = srcField.get(srcObj);
                if (mappers.containsKey(srcField.getName())) {
                    Function<Object, Object> mapper = mappers.get(srcField.getName());
                    value = mapper.apply(value);
                }

                /*
                Type check fails with primitive types, found no workaround -_-
                if (!dstField.getType().isAssignableFrom(value.getClass())) {
                    throw new ViewException(String.format(
                            "Type mismatch for property %s (expected: %s, got: %s, hasMapper: %s)",
                            dstField.getName(),
                            srcField.getType().getSimpleName(),
                            dstField.getType().getSimpleName(),
                            hasMapper ? "true" : "false"
                    ));
                }
                */

                dstField.set(dstObj, value);
            } catch (NoSuchFieldException ex) {
                throw new ViewException(String.format(
                        "Input (%s) does not have property %s",
                        srcClass.getSimpleName(),
                        field
                ));
            } catch (IllegalAccessException ex){
                throw new RuntimeException(ex);
            }
        }
    }
}
