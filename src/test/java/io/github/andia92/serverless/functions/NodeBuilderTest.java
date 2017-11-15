package io.github.andia92.serverless.functions;

import io.github.andia92.serverless.models.Group;
import io.github.andia92.serverless.models.Node;
import io.github.andia92.serverless.models.Server;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class NodeBuilderTest {

    @Mock
    private Function<List<Server>, Map<String, List<Server>>> serverGrouper;

    @Mock
    private BiFunction<String, List<Server>, Group> graphBuilder;

    private NodeBuilder builder;

    @Before
    public void before() {
        builder = new NodeBuilder(serverGrouper, graphBuilder);
    }

    @Test(expected = IllegalArgumentException.class)
    public void applyNullServerListThrowsException() throws Exception {
        builder.apply(null);
    }

    @Test
    public void applyEmptyServerListReturnsEmptyGroups() throws Exception {
        final List<Server> actualServers = Collections.emptyList();
        when(serverGrouper.apply(actualServers)).thenReturn(Collections.emptyMap());
        final List<Group> actualGroups = builder.apply(actualServers);
        Assert.assertTrue(actualGroups.isEmpty());
    }

    @Test
    public void applyAllServersInOneGroupReturnsOneGroup() throws Exception {
        final String groupName = "groupName";
        final List<Server> servers = ServerGenerator.generateServersInAGroup(groupName, 5);
        final Map<String, List<Server>> serversByGroup = new HashMap<>();
        serversByGroup.put(groupName, servers);
        when(serverGrouper.apply(servers)).thenReturn(serversByGroup);

        final Group expectedGroup = mock(Group.class);
        when(graphBuilder.apply(groupName, servers)).thenReturn(expectedGroup);

        final List<Group> actualGroups = builder.apply(servers);
        Assert.assertEquals(1, actualGroups.size());
        Assert.assertEquals(expectedGroup, actualGroups.get(0));
    }

    @Test
    public void applyServersInMultipleGroupsReturnsMultipleGroups() throws Exception {
        String groupNames = "groupName";
        int nrOfGroups = 2;
        int nrOfServerPerGroup = 5;
        final Map<String, List<Server>> servers = ServerGenerator.generateServers(groupNames, nrOfGroups, nrOfServerPerGroup);

        final List<Server> allServers = servers.values()
                .stream()
                .flatMap(Collection::stream)
                .collect(Collectors.toList());

        when(serverGrouper.apply(allServers)).thenReturn(servers);

        final Set<Group> expectedGroups = new HashSet<>();

        when(graphBuilder.apply(anyString(), anyObject())).thenAnswer(invocationOnMock -> {
            final String nameOfGroup = invocationOnMock.getArgumentAt(0, String.class);
            final Node node = mock(Node.class);
            final Group group = new Group(nameOfGroup, node);
            expectedGroups.add(group);
            return group;
        });

        final List<Group> actualGroups = builder.apply(allServers);

        Assert.assertEquals(expectedGroups, new HashSet<>(actualGroups));
    }


}