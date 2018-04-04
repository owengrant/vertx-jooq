/*
 * This file is generated by jOOQ.
*/
package generated.cf.reactive.regular.vertx.tables.daos;


import generated.cf.reactive.regular.vertx.tables.Somethingwithoutjson;
import generated.cf.reactive.regular.vertx.tables.records.SomethingwithoutjsonRecord;

import io.github.jklingspon.vertx.jooq.shared.reactive.AbstractReactiveVertxDAO;

import java.util.List;

import javax.annotation.Generated;

import org.jooq.Configuration;


import java.util.concurrent.CompletableFuture;
import io.github.jklingsporn.vertx.jooq.completablefuture.VertxDAO;
import io.github.jklingsporn.vertx.jooq.completablefuture.reactivepg.ReactiveCompletableFutureQueryExecutor;
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
public class SomethingwithoutjsonDao extends AbstractReactiveVertxDAO<SomethingwithoutjsonRecord, generated.cf.reactive.regular.vertx.tables.pojos.Somethingwithoutjson, Integer, CompletableFuture<List<generated.cf.reactive.regular.vertx.tables.pojos.Somethingwithoutjson>>, CompletableFuture<generated.cf.reactive.regular.vertx.tables.pojos.Somethingwithoutjson>, CompletableFuture<Integer>, CompletableFuture<Integer>> implements io.github.jklingsporn.vertx.jooq.completablefuture.VertxDAO<SomethingwithoutjsonRecord,generated.cf.reactive.regular.vertx.tables.pojos.Somethingwithoutjson,Integer> {

    /**
     * @param configuration The Configuration used for rendering and query execution.
     * @param vertx the vertx instance
     */
    public SomethingwithoutjsonDao(Configuration configuration, com.julienviet.pgclient.PgClient delegate, io.vertx.core.Vertx vertx) {
        super(Somethingwithoutjson.SOMETHINGWITHOUTJSON, generated.cf.reactive.regular.vertx.tables.pojos.Somethingwithoutjson.class, new ReactiveCompletableFutureQueryExecutor<SomethingwithoutjsonRecord,generated.cf.reactive.regular.vertx.tables.pojos.Somethingwithoutjson,Integer>(configuration,delegate,generated.cf.reactive.regular.vertx.tables.mappers.RowMappers.getSomethingwithoutjsonMapper(),vertx));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Integer getId(generated.cf.reactive.regular.vertx.tables.pojos.Somethingwithoutjson object) {
        return object.getSomeid();
    }

    /**
     * Find records that have <code>someString IN (values)</code> asynchronously
     */
    public CompletableFuture<List<generated.cf.reactive.regular.vertx.tables.pojos.Somethingwithoutjson>> findManyBySomestring(List<String> values) {
        return findManyByCondition(Somethingwithoutjson.SOMETHINGWITHOUTJSON.SOMESTRING.in(values));
    }
}
