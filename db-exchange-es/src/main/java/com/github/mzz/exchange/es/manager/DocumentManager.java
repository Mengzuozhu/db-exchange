package com.github.mzz.exchange.es.manager;

import com.github.mzz.exchange.es.common.EsConstant;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.WriteRequest;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.reindex.BulkByScrollResponse;
import org.elasticsearch.index.reindex.DeleteByQueryRequest;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import java.util.List;
import java.util.Map;

/**
 * The type Document manager.
 *
 * @author mengzz
 */
@RequiredArgsConstructor
public class DocumentManager {
    private final RestHighLevelClient restHighLevelClient;
    private WriteRequest.RefreshPolicy refreshPolicy;

    /**
     * Refresh policy.
     *
     * @param refreshPolicy the refresh policy
     * @return the document manager
     */
    public DocumentManager refreshPolicy(WriteRequest.RefreshPolicy refreshPolicy) {
        this.refreshPolicy = refreshPolicy;
        return this;
    }

    /**
     * Upsert doc by bulk.
     *
     * @param index   the index
     * @param sources the sources
     * @return the bulk response
     */
    @SneakyThrows
    public BulkResponse upsertDocByBulk(String index, List<Map<String, Object>> sources) {
        BulkRequest bulkRequest = new BulkRequest(index);
        for (Map<String, Object> source : sources) {
            UpdateRequest updateRequest = new UpdateRequest(index, (String) source.get(EsConstant.ID))
                    .doc(source, XContentType.JSON)
                    .upsert(source, XContentType.JSON);
            bulkRequest.add(updateRequest);
        }
        if (refreshPolicy != null) {
            bulkRequest.setRefreshPolicy(refreshPolicy);
        }
        return restHighLevelClient.bulk(bulkRequest, RequestOptions.DEFAULT);
    }

    /**
     * Delete doc.
     *
     * @param index the index
     * @param id    the id
     * @return the doc write response
     */
    @SneakyThrows
    public DocWriteResponse deleteDoc(String index, String id) {
        DeleteRequest deleteRequest = new DeleteRequest(index, id);
        return restHighLevelClient.delete(deleteRequest, RequestOptions.DEFAULT);
    }

    /**
     * Delete by query.
     *
     * @param index        the index
     * @param queryBuilder the query json
     * @return the bulk by scroll response
     */
    @SneakyThrows
    public BulkByScrollResponse deleteByQuery(String index, QueryBuilder queryBuilder) {
        DeleteByQueryRequest deleteRequest = new DeleteByQueryRequest(index)
                .setQuery(queryBuilder);
        return restHighLevelClient.deleteByQuery(deleteRequest, RequestOptions.DEFAULT);
    }

    /**
     * Search.
     *
     * @param index         the index
     * @param sourceBuilder the source builder
     * @return the search response
     */
    @SneakyThrows
    public SearchResponse search(String index, SearchSourceBuilder sourceBuilder) {
        SearchRequest searchRequest = new SearchRequest(index)
                .source(sourceBuilder);
        return restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
    }

}
