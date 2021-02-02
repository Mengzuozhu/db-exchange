package com.github.mzz.exchange.es.reader;

import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.search.SearchHit;

/**
 * @author mengzz
 **/
public class EsFromPageReader extends EsReader {
    private int from = 0;

    public EsFromPageReader(RestHighLevelClient client, SearchRequest searchRequest, int batchSize) {
        super(client, searchRequest, batchSize);
    }

    @Override
    protected void initQuery() {
        searchRequest.source()
                .size(batchSize);
    }

    @Override
    protected void setNextQuery(SearchHit[] searchHits) {
        from += searchHits.length;
        searchRequest.source()
                .size(batchSize)
                .from(from);
    }

}
