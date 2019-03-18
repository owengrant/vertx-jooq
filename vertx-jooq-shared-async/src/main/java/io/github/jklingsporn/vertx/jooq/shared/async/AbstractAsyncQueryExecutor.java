package io.github.jklingsporn.vertx.jooq.shared.async;

import io.github.jklingsporn.vertx.jooq.shared.internal.AbstractQueryExecutor;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.asyncsql.AsyncSQLClient;
import org.joda.time.DateTimeZone;
import org.jooq.*;
import org.jooq.conf.ParamType;

import java.time.*;
import java.time.temporal.ChronoField;
import java.util.ArrayList;
import java.util.Map;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;
import io.github.jklingsporn.vertx.jooq.shared.JsonType;

/**
 * @param <FIND_MANY_JSON> a type to represent many <code>JsonObject</code>s.
 * @param <FIND_ONE_JSON> a type to represent one <code>JsonObject</code>.
 * @param <EXECUTE> the result type returned for all insert, update and delete-operations. This varies on the AsyncQueryExecutor-subtypes, e.g. {@code Future<Integer>}.
 */
public abstract class AbstractAsyncQueryExecutor<FIND_MANY_JSON, FIND_ONE_JSON, EXECUTE> extends AbstractQueryExecutor implements AsyncQueryExecutor<FIND_MANY_JSON, FIND_ONE_JSON, EXECUTE> {

    private static final Logger logger = LoggerFactory.getLogger(AbstractAsyncQueryExecutor.class);

    protected final AsyncSQLClient delegate;
    protected final boolean isMysql;

    public AbstractAsyncQueryExecutor(Configuration configuration, AsyncSQLClient delegate) {
        super(configuration);
        this.isMysql = configuration.dialect().family().equals(SQLDialect.MYSQL);
        this.delegate = delegate;
    }

    /**
     * The <code>AsyncSQLClient</code> does not know anything about converters you may have set for your entities,
     * so we have to convert them manually on each return.
     * @param table
     * @return a <code>UnaryOperator</code> to map custom values (e.g. nested JsonObjects).
     */
    @SuppressWarnings("unchecked")
    protected UnaryOperator<JsonObject> convertFromSQL(Table<?> table){
        Map<String, Converter<Object, Object>> pojoConverters = table
                .fieldStream()
                .filter(f -> f.getConverter() != null)
                .collect(Collectors.toMap(Field::getName, v -> ((Converter<Object, Object>) v.getConverter())));
        return json -> {
            JsonObject theCopy = new JsonObject();
            for (Map.Entry<String, Object> jsonMap : json.getMap().entrySet()) {
                Converter<Object, Object> converter = pojoConverters.get(jsonMap.getKey());
                if(converter!=null){
                    Object val = converter.from(jsonMap.getValue());
                    if(val instanceof JsonType){
                            theCopy.put(jsonMap.getKey(), ((JsonType)val).toType());
                    }else{	
                            theCopy.put(jsonMap.getKey(), val);
                    }	
                }else{
                    theCopy.put(jsonMap.getKey(), jsonMap.getValue());
                }
            }
            return theCopy;
        };
    }

    protected JsonArray getBindValues(Query query) {
        ArrayList<Object> bindValues = new ArrayList<>();
        for (Param<?> param : query.getParams().values()) {
            if(!param.getParamType().equals(ParamType.INLINED)) {
                Object value = convertToDatabaseType(param);
                bindValues.add(value);
            }
        }
        return new JsonArray(bindValues);
    }



    protected <T> Object convertToDatabaseType(Param<T> param) {
        return convertToAsyncDriverTypes(param.getBinding().converter().to(param.getValue()));
    }

    protected void log(Query query){
        if(logger.isDebugEnabled()){
            logger.debug("Executing {}", query.getSQL(ParamType.INLINED));
        }
    }

    /**
     * Async-driver uses joda-time instead of java-time, so we need to convert it.
     * @param object the object to convert
     * @return a joda-time representation of the object or the object itself
     * @see <a href="https://github.com/jklingsporn/vertx-jooq/issues/31">#31</a>
     * @see <a href="https://github.com/vert-x3/vertx-mysql-postgresql-client/blob/master/src/main/java/io/vertx/ext/asyncsql/impl/ScalaUtils.java">ScalaUtils#convertValue</a>
     */
    protected Object convertToAsyncDriverTypes(Object object){
        if(object instanceof Enum){
            return ((Enum)object).name();
        }else if(object instanceof LocalDateTime){
            LocalDateTime convert = (LocalDateTime) object;
            return new org.joda.time.LocalDateTime(convert.getYear(),convert.getMonthValue(),convert.getDayOfMonth(),convert.getHour(),convert.getMinute(),convert.getSecond(), convert.get(ChronoField.MILLI_OF_SECOND));
        }else if(object instanceof LocalDate){
            LocalDate convert = (LocalDate) object;
            return new org.joda.time.LocalDate(convert.getYear(),convert.getMonthValue(),convert.getDayOfMonth());
        }else if(object instanceof ZonedDateTime){
            ZonedDateTime convert = (ZonedDateTime) object;
            return new org.joda.time.DateTime(convert.getYear(),convert.getMonthValue(),convert.getDayOfMonth(),convert.getHour(),convert.getMinute(),convert.getSecond(), convert.get(ChronoField.MILLI_OF_SECOND), DateTimeZone.forID(convert.getZone().getId()));
        } else if (object instanceof OffsetDateTime) {
            OffsetDateTime obj = (OffsetDateTime) object;

            // Keep the same instant when converting to date time
            ZonedDateTime convert = obj.toZonedDateTime();
            org.joda.time.DateTime dt = new org.joda.time.DateTime(convert.getYear(),
                    convert.getMonthValue(),
                    convert.getDayOfMonth(),
                    convert.getHour(),
                    convert.getMinute(),
                    convert.getSecond(),
                    convert.get(ChronoField.MILLI_OF_SECOND),
                    DateTimeZone.forID(convert.getZone().getId()));
            return dt;
        } else if (object instanceof Instant) {
            Instant convert = (Instant) object;
            org.joda.time.Instant i = org.joda.time.Instant.parse(convert.toString());
            return i.toDateTime();
        }
        return object;
    }

}
