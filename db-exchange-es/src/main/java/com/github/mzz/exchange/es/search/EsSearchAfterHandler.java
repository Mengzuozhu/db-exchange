package com.github.mzz.exchange.es.search;

import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;

import java.io.IOException;
import java.util.function.Consumer;

/**
 * ES Search After
 *
 * @author mengzz
 **/
public class EsSearchAfterHandler {
    private static final String UID = "_id";

    /**
     * Search for hit.
     *
     * @param searchRequest the search request
     * @param consumer      the consumer
     */
    public static void searchForHit(RestHighLevelClient client, SearchRequest searchRequest,
                                    Consumer<SearchHit> consumer) throws IOException {
        searchForResponse(client, searchRequest,
                searchResponse -> EsResponseParser.forEachHits(searchResponse, consumer));
    }

    /**
     * Search for response.
     *
     * @param searchRequest the search request
     * @param consumer      the consumer
     */
    public static void searchForResponse(RestHighLevelClient client, SearchRequest searchRequest,
                                         Consumer<SearchResponse> consumer) throws IOException {
        if (searchRequest == null || consumer == null) {
            return;
        }
        SearchSourceBuilder sourceBuilder = searchRequest.source();
        //在构建查询条件时，即可设置批量大小
        //sourceBuilder.size(100);
        //默认排序字段
        sourceBuilder.sort(UID, SortOrder.ASC);
        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
        SearchHit[] searchHits = searchResponse.getHits().getHits();
        while (searchHits.length > 0) {
            consumer.accept(searchResponse);
            SearchHit last = searchHits[searchHits.length - 1];
            sourceBuilder.searchAfter(last.getSortValues());
            searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
            searchHits = searchResponse.getHits().getHits();
        }
    }
}
