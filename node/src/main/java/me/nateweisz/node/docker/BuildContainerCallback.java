package me.nateweisz.node.docker;

import com.github.dockerjava.api.model.Container;

import java.util.List;

public class BuildContainerCallback extends Thread {
    private final DockerManager dockerManager;
    
    public BuildContainerCallback(DockerManager dockerManager) {
        this.dockerManager = dockerManager;
    }
    
    @Override
    public void run() {
        while (true) {
            try {
                sleep(1000L);
                tick();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
    
    private void tick() {
        List<Container> containers = dockerManager.getClient().listContainersCmd()
            .withExitedFilter(0) // 0 means the container has finished
            .exec();
        
        for (Container container : containers) {
            String label = container.getLabels().get("atlas-type");
            
            if (label == null) continue;
            if (!label.equals("build")) continue;
            
            dockerManager.getClient().removeContainerCmd(container.getId())
                    .exec();
            
            dockerManager.onBuildComplete(container.getId());
        }
    }
}