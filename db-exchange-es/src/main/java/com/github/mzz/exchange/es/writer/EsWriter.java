package com.github.mzz.exchange.es.writer;

import com.github.mzz.exchange.core.model.DataContext;
import com.github.mzz.exchange.core.writer.DataWriter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.support.WriteRequest.RefreshPolicy;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;

import java.util.List;
import java.util.Map;

/**
 * @author mengzz
 **/
@RequiredArgsConstructor
public abstract class EsWriter implements DataWriter<Map<String, Object>> {
    protected final String index;
    private final RestHighLevelClient client;
    private RefreshPolicy refreshPolicy;

    public EsWriter refreshPolicy(RefreshPolicy refreshPolicy) {
        this.refreshPolicy = refreshPolicy;
        return this;
    }

    @Override
    public void begin() {

    }

    @SneakyThrows
    @Override
    public void write(List<DataContext<Map<String, Object>>> dataContexts) {
        BulkRequest bulkRequest = new BulkRequest(index);
        fillBulk(bulkRequest, dataContexts);
        if (refreshPolicy != null) {
            bulkRequest.setRefreshPolicy(refreshPolicy);
        }
        client.bulk(bulkRequest, RequestOptions.DEFAULT);
    }

    @Override
    public void finish() {

    }

    /**
     * Fill bulk.
     *
     * @param bulkRequest  the bulk request
     * @param dataContexts the data contexts
     */
    protected abstract void fillBulk(BulkRequest bulkRequest, List<DataContext<Map<String, Object>>> dataContexts);
}
