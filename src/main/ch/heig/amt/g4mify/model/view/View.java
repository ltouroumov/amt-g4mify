package ch.heig.amt.g4mify.model.view;

import java.lang.reflect.Field;

/**
 * @author ldavid
 * @created 11/28/16
 */
public class View {

    public static <TOut, TIn> TOut to(Class<TOut> outputClass, TIn input) {
        try {
            TOut output = outputClass.newInstance();
            update(output, input);
            return output;
        } catch (InstantiationException | IllegalAccessException e) {
            throw new ViewException("Cannot instantiate view");
        }
    }

    public static <TIn, TOut> void update(TOut output, TIn input) {
        Class<?> outputClass = output.getClass();
        Class<?> inputClass = input.getClass();
        for (Field inputFld : inputClass.getDeclaredFields()) {
            try {
                Field outputFld = outputClass.getDeclaredField(inputFld.getName());

                if (!outputFld.getType().equals(inputFld.getType())) {
                    throw new ViewException(
                            String.format(
                                    "Property copy from %s to %s has incompatible type for field %s",
                                    inputClass.getName(),
                                    outputClass.getName(),
                                    outputFld.getName()
                            )
                    );
                }
                inputFld.setAccessible(true);
                outputFld.setAccessible(true);

                try {
                    Object value = inputFld.get(input);
                    outputFld.set(output, value);
                } catch (IllegalAccessException e) {
                    throw new ViewException("Impossible Exception!");
                }
            } catch (NoSuchFieldException ex) {
                // This allows views with less properties than the parent view
                /* throw new ViewException(
                        String.format(
                                "Property copy from %s to %s is missing field %s",
                                inputClass.getName(),
                                outputClass.getName(),
                                inputFld.getName()
                        )
                ); */
            }
        }
    }

}
