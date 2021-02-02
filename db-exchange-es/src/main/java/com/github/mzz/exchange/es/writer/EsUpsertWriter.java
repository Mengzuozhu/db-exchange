package com.github.mzz.exchange.es.writer;

import com.github.mzz.exchange.core.model.DataContext;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * @author mengzz
 **/
public class EsUpsertWriter extends EsWriter {
    private final Function<Map<String, Object>, String> idExtractor;

    public EsUpsertWriter(String index, RestHighLevelClient client, Function<Map<String, Object>, String> idExtractor) {
        super(index, client);
        this.idExtractor = idExtractor;
    }

    @Override
    protected void fillBulk(BulkRequest bulkRequest, List<DataContext<Map<String, Object>>> dataContexts) {
        for (DataContext<Map<String, Object>> dataContext : dataContexts) {
            Map<String, Object> data = dataContext.getData();
            UpdateRequest updateRequest = new UpdateRequest(index, idExtractor.apply(data))
                    .doc(data, XContentType.JSON)
                    .upsert(data, XContentType.JSON);
            bulkRequest.add(updateRequest);
        }
    }

}
