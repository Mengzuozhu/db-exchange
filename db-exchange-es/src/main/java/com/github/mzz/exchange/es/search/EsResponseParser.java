package com.github.mzz.exchange.es.search;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.SearchHit;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Consumer;

/**
 * The type Es response parser.
 *
 * @author mengzz
 */
public class EsResponseParser {

    /**
     * For each hits.
     *
     * @param searchResponse the search response
     * @param consumer       the consumer
     */
    public static void forEachHits(SearchResponse searchResponse, Consumer<SearchHit> consumer) {
        if (searchResponse == null) {
            return;
        }
        SearchHit[] searchHits = searchResponse.getHits().getHits();
        for (SearchHit searchHit : searchHits) {
            consumer.accept(searchHit);
        }
    }

    /**
     * For each hits parallel.
     *
     * @param searchResponse the search response
     * @param consumer       the consumer
     */
    public static void forEachHitsParallel(SearchResponse searchResponse, Consumer<SearchHit> consumer) {
        if (searchResponse == null) {
            return;
        }
        Arrays.stream(searchResponse.getHits().getHits())
                .parallel()
                .forEach(consumer);
    }

    /**
     * For each hits as map.
     *
     * @param searchResponse the search response
     * @param consumer       the consumer
     */
    public static void forEachHitsAsMap(SearchResponse searchResponse, Consumer<Map<String, Object>> consumer) {
        forEachHits(searchResponse, hit -> consumer.accept(hit.getSourceAsMap()));
    }
}
