/*
 * This file is generated by jOOQ.
*/
package generated.classic.reactive.guice.vertx.tables.daos;


import generated.classic.reactive.guice.vertx.tables.Somethingwithoutjson;
import generated.classic.reactive.guice.vertx.tables.records.SomethingwithoutjsonRecord;

import io.github.jklingspon.vertx.jooq.shared.reactive.AbstractReactiveVertxDAO;

import java.util.List;

import javax.annotation.Generated;

import org.jooq.Configuration;


import io.vertx.core.Future;
import io.github.jklingsporn.vertx.jooq.classic.reactivepg.ReactiveClassicQueryExecutor;
/**
 * This class is generated by jOOQ.
 */
@Generated(
    value = {
        "http://www.jooq.org",
        "jOOQ version:3.10.6"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
@javax.inject.Singleton
public class SomethingwithoutjsonDao extends AbstractReactiveVertxDAO<SomethingwithoutjsonRecord, generated.classic.reactive.guice.vertx.tables.pojos.Somethingwithoutjson, Integer, Future<List<generated.classic.reactive.guice.vertx.tables.pojos.Somethingwithoutjson>>, Future<generated.classic.reactive.guice.vertx.tables.pojos.Somethingwithoutjson>, Future<Integer>, Future<Integer>> implements io.github.jklingsporn.vertx.jooq.classic.VertxDAO<SomethingwithoutjsonRecord,generated.classic.reactive.guice.vertx.tables.pojos.Somethingwithoutjson,Integer> {
    @javax.inject.Inject

    /**
     * @param configuration Used for rendering, so only SQLDialect must be set and must be one of the POSTGREs types.
     * @param delegate A configured AsyncSQLClient that is used for query execution
     */
    public SomethingwithoutjsonDao(Configuration configuration, com.julienviet.pgclient.PgClient delegate) {
        super(Somethingwithoutjson.SOMETHINGWITHOUTJSON, generated.classic.reactive.guice.vertx.tables.pojos.Somethingwithoutjson.class, new ReactiveClassicQueryExecutor<SomethingwithoutjsonRecord,generated.classic.reactive.guice.vertx.tables.pojos.Somethingwithoutjson,Integer>(configuration,delegate,generated.classic.reactive.guice.vertx.tables.mappers.RowMappers.getSomethingwithoutjsonMapper()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Integer getId(generated.classic.reactive.guice.vertx.tables.pojos.Somethingwithoutjson object) {
        return object.getSomeid();
    }

    /**
     * Find records that have <code>someString IN (values)</code> asynchronously
     */
    public Future<List<generated.classic.reactive.guice.vertx.tables.pojos.Somethingwithoutjson>> findManyBySomestring(List<String> values) {
        return findManyByCondition(Somethingwithoutjson.SOMETHINGWITHOUTJSON.SOMESTRING.in(values));
    }
}
