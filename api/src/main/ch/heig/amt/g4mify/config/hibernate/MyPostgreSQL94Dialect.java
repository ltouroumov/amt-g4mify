package ch.heig.amt.g4mify.config.hibernate;

import org.hibernate.dialect.PostgreSQL94Dialect;

import java.sql.Types;

/**
 * @author ldavid
 * @created 11/21/16
 */
public class MyPostgreSQL94Dialect extends PostgreSQL94Dialect {

    public MyPostgreSQL94Dialect() {
        this.registerColumnType(Types.JAVA_OBJECT, "jsonb");
    }

}
