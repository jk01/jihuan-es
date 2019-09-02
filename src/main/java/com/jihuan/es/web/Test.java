package com.jihuan.es.web;

import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import java.net.InetAddress;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

public class Test {

    public static void main(String[] args) throws Exception{
        TransportAddress transportAddress = new InetSocketTransportAddress(InetAddress.getByName("127.0.0.1"), 9300);

        Settings settings = Settings.builder()
                .put("cluster.name", "jihuan").build();

        TransportClient client = new PreBuiltTransportClient(settings)
                .addTransportAddress(transportAddress);

        BulkRequestBuilder bulkRequest = client.prepareBulk();

        bulkRequest.add(client.prepareIndex("people", "_doc", "2")
                .setSource(jsonBuilder()
                        .startObject()
                        .field("name", "xiaosong")
                        .field("country", "china")
                        .field("age", 20)
                        .field("date", "2019-09-01")
                        .endObject()
                )
        );

        BulkResponse bulkResponse = bulkRequest.get();

        client.close();
    }
}
