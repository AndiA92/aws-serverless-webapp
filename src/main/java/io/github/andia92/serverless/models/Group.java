package io.github.andia92.serverless.models;

import lombok.Data;

@Data
public class Group {

    private final String name;

    private final Node entryPoint;
}
