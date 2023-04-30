package cloud.terium.cloudsystem;

import cloud.terium.cloudsystem.cluster.ClusterStartup;
import cloud.terium.cloudsystem.common.pipe.NetworkHandlerProvider;
import cloud.terium.cloudsystem.common.utils.CloudUtils;
import cloud.terium.cloudsystem.node.NodeStartup;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;

@Getter
@Setter
public class TeriumCloud {

    private static TeriumCloud terium;
    private final CloudUtils cloudUtils;
    private final NetworkHandlerProvider networkHandlerProvider;

    @SneakyThrows
    public TeriumCloud() {
        terium = this;
        this.cloudUtils = new CloudUtils();
        this.networkHandlerProvider = new NetworkHandlerProvider();
    }

    public static void main(String[] args) {
        System.setProperty("org.jline.terminal.dumb", "true");
        new TeriumCloud();

        if ((args.length >= 1 && args[0].equals("--node"))) new NodeStartup();
        else new ClusterStartup();
    }

    public static TeriumCloud getTerium() {
        return terium;
    }
}