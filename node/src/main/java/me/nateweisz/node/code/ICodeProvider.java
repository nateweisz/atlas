package me.nateweisz.node.code;

import me.nateweisz.node.docker.BuildSpec;

public interface ICodeProvider {
    String getDockerImageTag(BuildSpec buildSpec);
}
