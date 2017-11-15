package io.github.andia92.serverless.functions;


import io.github.andia92.serverless.models.Server;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ServerGrouperTest {

    private ServerGrouper serverGrouper;

    @Before
    public void before() {
        serverGrouper = new ServerGrouper();
    }

    @Test(expected = IllegalArgumentException.class)
    public void applyNullServerListThrowsException() {
        serverGrouper.apply(null);
    }

    @Test
    public void applyEmptyListReturnsEmptyGroups() {
        final Map<String, List<Server>> actualMaps = serverGrouper.apply(Collections.emptyList());
        Assert.assertTrue(actualMaps.isEmpty());
    }

    @Test
    public void applyServersInMultipleGroupsReturnsMultipleGroups() {
        String groupName = "groupName";
        int numberOfGroups = 2;
        int numberOfServersInGroup = 5;

        final Map<String, List<Server>> servers = ServerGenerator.generateServers(groupName, numberOfGroups, numberOfServersInGroup);
        final List<Server> allServers = servers.values()
                .stream()
                .flatMap(Collection::stream)
                .collect(Collectors.toList());

        final Map<String, List<Server>> serversByGroup = serverGrouper.apply(allServers);

        Assert.assertEquals(servers, serversByGroup);
    }
}