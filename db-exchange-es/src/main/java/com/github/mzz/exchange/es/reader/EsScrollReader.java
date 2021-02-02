package com.github.mzz.exchange.es.reader;

import lombok.Setter;
import lombok.SneakyThrows;
import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.search.*;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.search.SearchHit;

/**
 * @author mengzz
 **/
public class EsScrollReader extends EsReader {
    private static final int DEFAULT_SCROLL_TIME_MILLIS = 60000;
    private final RestHighLevelClient highLevelClient;
    @Setter
    private TimeValue keepAlive;
    @Setter
    private boolean isClearScroll;
    private SearchResponse searchResponse;

    public EsScrollReader(RestHighLevelClient client, SearchRequest searchRequest, int batchSize) {
        super(client, searchRequest, batchSize);
        this.highLevelClient = client;
    }

    @SneakyThrows
    @Override
    public void begin() {
        initQuery();
        searchResponse = highLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        updateSearchHits();
    }

    @Override
    public void finish() {
        super.finish();
        if (searchResponse == null) {
            return;
        }
        String scrollId = searchResponse.getScrollId();
        // 清空快照记录，避免内存占用
        if (isClearScroll && scrollId != null) {
            ClearScrollRequest clearScrollRequest = new ClearScrollRequest();
            clearScrollRequest.addScrollId(scrollId);
            clearScrollAsync(clearScrollRequest);
        }
    }

    @Override
    protected void initQuery() {
        if (keepAlive == null) {
            keepAlive = new TimeValue(DEFAULT_SCROLL_TIME_MILLIS);
        }
        searchRequest.source()
                .size(batchSize);
        searchRequest.scroll(keepAlive);
    }

    @Override
    protected void setNextQuery(SearchHit[] searchHits) {
    }

    @SneakyThrows
    @Override
    protected void searchNext() {
        String scrollId = searchResponse.getScrollId();
        SearchScrollRequest scrollRequest = new SearchScrollRequest(scrollId)
                .scroll(keepAlive);
        searchResponse = highLevelClient.scroll(scrollRequest, RequestOptions.DEFAULT);
        updateSearchHits();
    }

    private void updateSearchHits() {
        searchHits = searchResponse == null ? new SearchHit[0] : searchResponse.getHits().getHits();
    }

    private void clearScrollAsync(ClearScrollRequest request) {
        highLevelClient.clearScrollAsync(request, RequestOptions.DEFAULT, new ActionListener<ClearScrollResponse>() {
            @Override
            public void onResponse(ClearScrollResponse clearScrollResponse) {

            }

            @Override
            public void onFailure(Exception e) {
                throw new ElasticsearchException(e);
            }
        });
    }

}
