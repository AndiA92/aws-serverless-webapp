package io.github.andia92.serverless.functions;

import io.github.andia92.serverless.ServerState;
import io.github.andia92.serverless.models.Server;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


class ServerGenerator {

    static Map<String, List<Server>> generateServers(String name, int numberOfGroups, int numberOfServersPerGroup) {
        return IntStream.range(0, numberOfGroups)
                .boxed()
                .collect(
                        Collectors.toMap(
                                groupIndex -> name + groupIndex,
                                groupIndex -> generateServersInAGroup(name + groupIndex, numberOfServersPerGroup)));
    }

    static List<Server> generateServersInAGroup(String groupName, int nrOfServers) {
        String host = "localhost";
        return IntStream.range(0, nrOfServers)
                .mapToObj(serverIndex -> new Server(groupName, host + serverIndex, Long.toString(System.currentTimeMillis()), ServerState.BOTH.getState(), getNextServer(host, serverIndex, nrOfServers)))
                .collect(Collectors.toList());
    }

    private static String getNextServer(String host, int currentIndex, int nrOfServers) {
        return (currentIndex + 1 == nrOfServers) ? null : host + (currentIndex + 1);
    }
}
