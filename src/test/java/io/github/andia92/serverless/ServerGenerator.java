package io.github.andia92.serverless;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


public class ServerGenerator {

    public static Map<String, List<Server>> generateServers(String name, int numberOfGroups, int numberOfServersPerGroup) {
        return IntStream.range(0, numberOfGroups)
                .boxed()
                .collect(
                        Collectors.toMap(
                                groupIndex -> name + groupIndex,
                                groupIndex -> generateServersInAGroup(name + groupIndex, numberOfServersPerGroup)));
    }

    public static List<Server> generateServersInAGroup(String groupName, int nrOfServers) {
        return IntStream.range(0, nrOfServers)
                .mapToObj(serverIndex -> new Server(groupName, "localhost" + serverIndex, Long.toString(System.currentTimeMillis()), ServerState.NONE.getState(), "localhost" + serverIndex))
                .collect(Collectors.toList());
    }
}
