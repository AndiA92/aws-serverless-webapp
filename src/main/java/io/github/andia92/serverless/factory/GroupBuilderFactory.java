package io.github.andia92.serverless.factory;

import io.github.andia92.serverless.functions.*;
import io.github.andia92.serverless.models.Group;
import io.github.andia92.serverless.models.Server;
import io.github.andia92.serverless.models.ServerLink;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;

public class GroupBuilderFactory {

    public static GroupBuilder getInstance() {
        BiFunction<String, List<Server>, Optional<Server>> serverRetriever = new ServerRetriever();
        BiFunction<Server, List<Server>, Optional<ServerLink>> linkConstructor = new LinkConstructor(serverRetriever);
        BiFunction<String, List<Server>, Group> graphBuilder = new GraphBuilder(linkConstructor);
        Function<List<Server>, Map<String, List<Server>>> serverGrouper = new ServerGrouper();
        return new GroupBuilder(serverGrouper, graphBuilder);
    }
}
