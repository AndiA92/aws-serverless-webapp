package io.github.andia92.serverless;


import lombok.NonNull;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ServerGrouper implements Function<List<Server>, Map<String, List<Server>>> {

    @Override
    public Map<String, List<Server>> apply(@NonNull List<Server> servers) {
        return servers.stream()
                .collect(Collectors.groupingBy(Server::getGroup));
    }
}
