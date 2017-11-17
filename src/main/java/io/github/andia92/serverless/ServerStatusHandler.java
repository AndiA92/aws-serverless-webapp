package io.github.andia92.serverless;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ScanResult;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import io.github.andia92.serverless.factory.GroupBuilderFactory;
import io.github.andia92.serverless.functions.GroupBuilder;
import io.github.andia92.serverless.functions.ServerListBuilder;
import io.github.andia92.serverless.models.Group;
import io.github.andia92.serverless.models.Server;
import lombok.extern.log4j.Log4j;
import org.json.JSONObject;

import java.util.*;
import java.util.function.Function;

import static spark.Spark.post;


@Log4j
public class ServerStatusHandler
        implements RequestHandler<Map<String, Object>, List<Group>> {

    private AmazonDynamoDB dynamoDb;
    private static final String TABLE_NAME = "servers";
    private static final List<String> ATTRIBUTES_TO_GET = Arrays.asList("group", "host", "timestamp", "state", "pair");
    private static final String REGION = Regions.EU_CENTRAL_1.getName();

    public List<Group> handleRequest(Map<String, Object> input, Context context) {
        this.initDynamoDbClient();
        return retrieveData();
    }

    private void initDynamoDbClient() {
        AmazonDynamoDBClientBuilder amazonDynamoDBClientBuilder = AmazonDynamoDBClientBuilder.standard()
                                                                                             .withRegion(REGION);
        this.dynamoDb = amazonDynamoDBClientBuilder.build();
    }

    public static void main(String[] args) {
        List<String> groups = Collections.singletonList("a");
        post("http://localhost:4567/chat", (req, res) ->
                new JSONObject("hello")
        );
    }

    private List<Group> retrieveData() {
        ScanResult scan = dynamoDb.scan(TABLE_NAME, ATTRIBUTES_TO_GET);
        Function<List<Map<String, AttributeValue>>, List<Server>> serverListBuilder = new ServerListBuilder();
        List<Server> servers = serverListBuilder.apply(scan.getItems());
        GroupBuilder builder = GroupBuilderFactory.getInstance();

        List<Group> groups = builder.apply(servers);
        post("/servers", (req, res) -> new JSONObject(groups));

        return groups;
    }
}