package com.github.mzz.exchange.es.writer;

import com.github.mzz.exchange.core.exchange.ExchangeTemplate;
import com.github.mzz.exchange.core.reader.IterableReader;
import com.github.mzz.exchange.es.EsTestUtil;
import com.github.mzz.exchange.es.common.EsConstant;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.support.WriteRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * @author mengzz
 **/
@ExtendWith(MockitoExtension.class)
class EsWriterTest {
    @Mock
    private RestHighLevelClient client;

    @Test
    void esIndexWriter() throws IOException {
        EsWriter esWriter = new EsIndexWriter(EsTestUtil.WRITE_INDEX, client)
                .refreshPolicy(WriteRequest.RefreshPolicy.IMMEDIATE);
        execute(esWriter);
        verify(client, times(1))
                .bulk(any(BulkRequest.class), eq(RequestOptions.DEFAULT));
    }

    @Test
    void esUpsertWriter() throws IOException {
        EsWriter esWriter = new EsUpsertWriter(EsTestUtil.WRITE_INDEX, client,
                data -> Objects.toString(data.get(EsConstant.ID)))
                .refreshPolicy(WriteRequest.RefreshPolicy.IMMEDIATE);
        execute(esWriter);
        verify(client, times(1))
                .bulk(any(BulkRequest.class), eq(RequestOptions.DEFAULT));
    }

    private void execute(EsWriter esWriter) {
        List<Map<String, Object>> docs = getDocs();
        IterableReader<Map<String, Object>> reader = IterableReader.of(docs);
        ExchangeTemplate.from(reader)
                .execute(esWriter)
                .await();
    }

    private List<Map<String, Object>> getDocs() {
        Map<String, Object> doc = getDoc();
        return Lists.newArrayList(doc);
    }

    private Map<String, Object> getDoc() {
        return ImmutableMap.of(EsConstant.ID, EsTestUtil.DOC_ID);
    }


}
