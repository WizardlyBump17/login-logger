package com.wizardlybump17.loginlogger.api.persister;

import com.j256.ormlite.field.FieldType;
import com.j256.ormlite.field.SqlType;
import com.j256.ormlite.field.types.BaseDataType;
import com.j256.ormlite.support.DatabaseResults;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;

public class InstantType extends BaseDataType {

    public static final @NotNull InstantType INSTANCE = new InstantType();

    public InstantType() {
        super(SqlType.DATE, new Class[]{Instant.class});
    }

    @Override
    public Object parseDefaultString(FieldType fieldType, String defaultStr) {
        return Timestamp.valueOf(defaultStr).toInstant();
    }

    @Override
    public Object resultToSqlArg(FieldType fieldType, DatabaseResults results, int columnPos) throws SQLException {
        String string = results.getString(columnPos);
        if (string == null)
            return null;
        return Timestamp.valueOf(string).toInstant();
    }

    @Override
    public Object sqlArgToJava(FieldType fieldType, Object sqlArg, int columnPos) {
        if (sqlArg == null)
            return null;
        if (sqlArg instanceof String string)
            return Instant.parse(string);
        if (sqlArg instanceof Instant instant)
            return instant;
        throw new IllegalArgumentException("Could not parse '" + sqlArg + "' to Instant");
    }

    public static @NotNull InstantType getSingleton() {
        return INSTANCE;
    }
}
