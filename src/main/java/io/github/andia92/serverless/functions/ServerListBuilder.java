package io.github.andia92.serverless.functions;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import io.github.andia92.serverless.models.Server;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ServerListBuilder implements Function<List<Map<String, AttributeValue>>, List<Server>> {

    @Override
    public List<Server> apply(List<Map<String, AttributeValue>> items) {
        return items.stream()
                    .map(item -> {
                        AttributeValue groupAttribute = item.get("group");
                        AttributeValue hostAttribute = item.get("host");
                        AttributeValue timestampAttribute = item.get("timestamp");
                        AttributeValue stateAttribute = item.get("state");
                        AttributeValue pairAttribute = item.get("pair");

                        String group = groupAttribute == null ? null : groupAttribute.getS();
                        String host = hostAttribute == null ? null : hostAttribute.getS();
                        String timestamp = timestampAttribute == null ? null : timestampAttribute.getN();
                        String state = stateAttribute == null ? null : stateAttribute.getS();
                        String pair = pairAttribute == null ? null : pairAttribute.getS();
                        return new Server(group, host, timestamp, state, pair);
                    })
                    .collect(Collectors.toList());
    }
}
