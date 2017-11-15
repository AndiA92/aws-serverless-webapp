package io.github.andia92.serverless.functions;


import io.github.andia92.serverless.models.Group;
import io.github.andia92.serverless.models.Node;
import io.github.andia92.serverless.models.Server;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.util.Collections;
import java.util.List;

public class GraphBuilderTest {

    private GraphBuilder graphBuilder;

    @Before
    public void before(){
        graphBuilder = new GraphBuilder();
    }

    @Test(expected = IllegalArgumentException.class)
    public void applyNullGroupNameThrowsException(){
        graphBuilder.apply(null, Collections.emptyList());
    }

    @Test(expected = IllegalArgumentException.class)
    public void applyNullServerListThrowsException(){
        graphBuilder.apply("test", null);
    }

    @Test
    public void applyConnectedServersReturnsOneConnectedGroup(){
//        String nameOfGroup = "test";
//        final List<Server> servers = ServerGenerator.generateServersInAGroup(nameOfGroup, 2);
//        final Group actualGroup = graphBuilder.apply(nameOfGroup, servers);
//
//        final String actualGroupName = actualGroup.getName();
//        Assert.assertEquals(nameOfGroup, actualGroupName);
//
//        final Node node = actualGroup.getEntryPoint();
    }
}
