
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ScanResult;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import lombok.extern.log4j.Log4j;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Log4j
public class ServerStatusHandler
        implements RequestHandler<Map<String, Object>, JSONElement> {

    private AmazonDynamoDB dynamoDb;
    private static final String TABLE_NAME = "servers";
    private static final List<String> ATTRIBUTES_TO_GET = Arrays.asList("host", "timestamp", "state", "pair");
    private static final String REGION = Regions.EU_CENTRAL_1.getName();

    public JSONElement handleRequest(Map<String, Object> input, Context context) {
        this.initDynamoDbClient();
        return retrieveData();
    }


    private JSONElement retrieveData() {
        ScanResult scan = dynamoDb.scan(TABLE_NAME, ATTRIBUTES_TO_GET);
        List<Server> servers = new ArrayList<>();
        List<Map<String, AttributeValue>> items = scan.getItems();
        items.forEach(item -> {
            AttributeValue hostAttribute = item.get("host");
            AttributeValue timestampAttribute = item.get("timestamp");
            AttributeValue stateAttribute = item.get("state");
            AttributeValue pairAttribute = item.get("pair");

            String host = hostAttribute == null ? null : hostAttribute.getS();
            String timestamp = timestampAttribute == null ? null : timestampAttribute.getN();
            String state = stateAttribute == null ? null : stateAttribute.getS();
            String pair = pairAttribute == null ? null : pairAttribute.getS();
            Server server = new Server(host, timestamp, state, pair);
            servers.add(server);
        });
        return JSONElementBuilder.build(servers);
    }

    private void initDynamoDbClient() {
        AmazonDynamoDBClientBuilder amazonDynamoDBClientBuilder = AmazonDynamoDBClientBuilder.standard()
                                                                                             .withRegion(REGION);
        this.dynamoDb = amazonDynamoDBClientBuilder.build();
    }
}