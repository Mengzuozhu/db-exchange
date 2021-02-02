package com.github.mzz.exchange.es.reader;

import com.github.mzz.exchange.core.model.CommonDataContext;
import com.github.mzz.exchange.core.model.DataContext;
import com.github.mzz.exchange.core.reader.DataReader;
import com.github.mzz.exchange.es.search.EsSearchWrapper;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.search.SearchHit;

/**
 * @author mengzz
 **/
public abstract class EsReader implements DataReader<SearchHit> {
    protected final SearchRequest searchRequest;
    protected final int batchSize;
    private final EsSearchWrapper searchWrapper;
    protected SearchHit[] searchHits;
    private int index;

    public EsReader(RestHighLevelClient client, SearchRequest searchRequest, int batchSize) {
        this.searchWrapper = EsSearchWrapper.of(client);
        this.searchRequest = searchRequest;
        this.batchSize = batchSize;
    }

    @Override
    public void begin() {
        initQuery();
        search();
    }

    @Override
    public DataContext<SearchHit> read() {
        if (index >= searchHits.length) {
            searchNext();
            index = 0;
        }
        if (searchHits.length == 0) {
            return null;
        } else {
            SearchHit searchHit = searchHits[index++];
            return CommonDataContext.of(searchHit);
        }
    }

    @Override
    public int getBatchSize() {
        return batchSize;
    }

    @Override
    public void finish() {
        searchHits = null;
    }

    /**
     * Init query.
     */
    protected abstract void initQuery();

    /**
     * Sets next query.
     *
     * @param searchHits the search hits
     */
    protected abstract void setNextQuery(SearchHit[] searchHits);

    protected void searchNext() {
        if (searchHits.length == 0) {
            return;
        }
        setNextQuery(searchHits);
        search();
    }

    private void search() {
        SearchResponse searchResponse = searchWrapper.search(searchRequest);
        searchHits = searchResponse == null ? new SearchHit[0] : searchResponse.getHits().getHits();
    }
}
