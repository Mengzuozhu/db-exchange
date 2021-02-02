package com.github.mzz.exchange.es.search;

import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;

/**
 * @author mengzz
 **/
public class SearchRequestUtil {

    public static SearchRequest getMatchAllRequest(String index) {
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder()
                .query(QueryBuilders.matchAllQuery());
        return new SearchRequest(index)
                .source(sourceBuilder);
    }

    public static SearchSourceBuilder getIdsQueryBuilder(String... ids) {
        return new SearchSourceBuilder().query(QueryBuilders.idsQuery()
                .addIds(ids));
    }

}
