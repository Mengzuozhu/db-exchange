package com.github.mzz.exchange.es.reader;

import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;

/**
 * @author mengzz
 **/
public class EsSearchAfterReader extends EsReader {
    private static final String UID = "_id";

    public EsSearchAfterReader(RestHighLevelClient client, SearchRequest searchRequest, int batchSize) {
        super(client, searchRequest, batchSize);
    }

    @Override
    protected void initQuery() {
        searchRequest.source()
                .size(batchSize)
                .sort(UID, SortOrder.ASC);
    }

    @Override
    protected void setNextQuery(SearchHit[] searchHits) {
        SearchSourceBuilder sourceBuilder = searchRequest.source();
        SearchHit last = searchHits[searchHits.length - 1];
        sourceBuilder.searchAfter(last.getSortValues());
    }

}
