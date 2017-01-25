/**
 * @author ldavid
 * @created 11/21/16
 */
@TypeDefs({
        @TypeDef(name = "json", typeClass = JsonStringType.class),
        @TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
})
package ch.heig.amt.g4mify.model;

import ch.heig.amt.g4mify.config.hibernate.JsonBinaryType;
import ch.heig.amt.g4mify.config.hibernate.JsonStringType;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;