package com.github.mzz.exchange.es.writer;

import com.github.mzz.exchange.core.model.DataContext;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;

import java.util.List;
import java.util.Map;

/**
 * @author mengzz
 **/
public class EsIndexWriter extends EsWriter {

    public EsIndexWriter(String index, RestHighLevelClient client) {
        super(index, client);
    }

    @Override
    protected void fillBulk(BulkRequest bulkRequest, List<DataContext<Map<String, Object>>> dataContexts) {
        for (DataContext<Map<String, Object>> dataContext : dataContexts) {
            IndexRequest indexRequest = new IndexRequest(index)
                    .source(dataContext.getData(), XContentType.JSON);
            bulkRequest.add(indexRequest);
        }
    }

}
