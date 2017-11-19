package io.github.andia92.serverless;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ScanResult;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.andia92.serverless.factory.GroupBuilderFactory;
import io.github.andia92.serverless.functions.GroupBuilder;
import io.github.andia92.serverless.functions.ServerListBuilder;
import io.github.andia92.serverless.models.Group;
import io.github.andia92.serverless.models.Server;
import lombok.extern.log4j.Log4j;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.IOException;
import java.util.*;
import java.util.function.Function;


@Log4j
public class ServerStatusHandler
        implements RequestHandler<Map<String, Object>, String> {

    private AmazonDynamoDB dynamoDb;
    private static final String TABLE_NAME = "servers";
    private static final List<String> ATTRIBUTES_TO_GET = Arrays.asList("group", "host", "timestamp", "state", "pair");
    private static final String REGION = Regions.EU_CENTRAL_1.getName();

    public String handleRequest(Map<String, Object> input, Context context) {
        this.initDynamoDbClient();
        try {
            return retrieveData();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void initDynamoDbClient() {
        AmazonDynamoDBClientBuilder amazonDynamoDBClientBuilder = AmazonDynamoDBClientBuilder.standard()
                                                                                             .withRegion(REGION);
        this.dynamoDb = amazonDynamoDBClientBuilder.build();
    }

    private String retrieveData() throws IOException {
        ScanResult scan = dynamoDb.scan(TABLE_NAME, ATTRIBUTES_TO_GET);
        Function<List<Map<String, AttributeValue>>, List<Server>> serverListBuilder = new ServerListBuilder();
        List<Server> servers = serverListBuilder.apply(scan.getItems());
        GroupBuilder builder = GroupBuilderFactory.getInstance();

        List<Group> groups = builder.apply(servers);

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(groups);

        System.out.println("json: " + json);

        HttpClient client = HttpClientBuilder.create().build();
        String url = "http://<ip>/servers";
        HttpPost post = new HttpPost(url);
        post.setEntity(new StringEntity(json));
        client.execute(post);

        return json;
    }


}