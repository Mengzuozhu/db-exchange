package com.github.mzz.exchange.es.reader;

import com.github.mzz.exchange.core.exchange.ExchangeTemplate;
import com.github.mzz.exchange.core.writer.ListDataCollector;
import com.github.mzz.exchange.es.EsTestUtil;
import com.github.mzz.exchange.es.search.SearchRequestUtil;
import org.apache.lucene.search.TotalHits;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

/**
 * @author mengzz
 **/
@ExtendWith(MockitoExtension.class)
class EsReaderTest {
    @Mock
    private RestHighLevelClient client;
    @Mock
    private SearchResponse searchResponse;
    @Mock
    private SearchHit searchHit;
    private SearchRequest matchAllRequest = SearchRequestUtil.getMatchAllRequest(EsTestUtil.READ_INDEX);

    @BeforeEach
    void setUp() throws IOException {
        when(client.search(eq(matchAllRequest), eq(RequestOptions.DEFAULT)))
                .thenReturn(searchResponse);
        SearchHit[] hits = {searchHit, searchHit};
        SearchHits searchHits = new SearchHits(hits, new TotalHits(0, TotalHits.Relation.EQUAL_TO), 1f);
        when(searchResponse.getHits())
                .thenReturn(searchHits);
    }

    @Test
    void readEsFromPageReader() throws IOException {
        SearchRequest nextSearchRequest = ArgumentMatchers.argThat(argument -> argument.source().from() >= 2);
        when(client.search(nextSearchRequest, eq(RequestOptions.DEFAULT)))
                .thenReturn(null);
        execute(new EsFromPageReader(client, matchAllRequest, EsTestUtil.BATCH_SIZE));
    }

    @Test
    void readEsScrollReader() throws IOException {
        when(client.scroll(any(), eq(RequestOptions.DEFAULT)))
                .thenReturn(null);
        execute(new EsScrollReader(client, matchAllRequest, EsTestUtil.BATCH_SIZE));
    }

    @Test
    void readEsSearchAfterReader() throws IOException {
        Object[] objects = {"1"};
        when(searchHit.getSortValues())
                .thenReturn(objects);
        SearchRequest nextSearchRequest =
                ArgumentMatchers.argThat(argument -> Arrays.equals(argument.source().searchAfter(), objects));
        when(client.search(nextSearchRequest, eq(RequestOptions.DEFAULT)))
                .thenReturn(null);
        execute(new EsSearchAfterReader(client, matchAllRequest, EsTestUtil.BATCH_SIZE));
    }

    private void execute(EsReader esReader) {
        ListDataCollector<SearchHit> listDataCollector = new ListDataCollector<>();
        ExchangeTemplate.from(esReader)
                .execute(listDataCollector)
                .await();
        Assertions.assertEquals(2, listDataCollector.getResult().size());
    }
}
