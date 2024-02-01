package me.nateweisz.node.code.impl;

import me.nateweisz.node.code.ICodeProvider;
import me.nateweisz.node.docker.BuildSpec;

public class GitCodeProvider implements ICodeProvider {
    @Override
    public String getDockerImageTag(BuildSpec buildSpec) {
        String[] split = buildSpec.getPath().split("/");
        
        if (split.length < 2) {
            return null;
        }
        
        return "github_" + split[0] + "_" + split[1];
    }
}
