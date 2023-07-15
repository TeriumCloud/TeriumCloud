package cloud.terium.module.dockerizedservices.config;

import cloud.terium.module.dockerizedservices.TeriumDockerizedServices;
import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.core.DockerClientImpl;
import com.github.dockerjava.httpclient5.ApacheDockerHttpClient;
import com.github.dockerjava.transport.DockerHttpClient;
import lombok.Getter;

import java.time.Duration;

@Getter
public class DockerizedConfig {

    private final DockerClientConfig clientConfig = DefaultDockerClientConfig.createDefaultConfigBuilder()
            .withDockerHost(TeriumDockerizedServices.getInstance().getConfigLoader().getJson().get("docker-host").getAsString())
            .withDockerCertPath(TeriumDockerizedServices.getInstance().getConfigLoader().getJson().get("docker-cert-path").getAsString().equals("null") ? null : TeriumDockerizedServices.getInstance().getConfigLoader().getJson().get("docker-cert-path").getAsString())
            .withDockerTlsVerify(TeriumDockerizedServices.getInstance().getConfigLoader().getJson().get("docker-cert-path").getAsString().equals("null") ? null : TeriumDockerizedServices.getInstance().getConfigLoader().getJson().get("docker-cert-path").getAsString())
            .withRegistryUsername(TeriumDockerizedServices.getInstance().getConfigLoader().getJson().get("docker-registry-username").getAsString().equals("null") ? null : TeriumDockerizedServices.getInstance().getConfigLoader().getJson().get("docker-cert-path").getAsString())
            .withRegistryEmail(TeriumDockerizedServices.getInstance().getConfigLoader().getJson().get("docker-registry-email").getAsString().equals("null") ? null : TeriumDockerizedServices.getInstance().getConfigLoader().getJson().get("docker-cert-path").getAsString())
            .withRegistryPassword(TeriumDockerizedServices.getInstance().getConfigLoader().getJson().get("docker-registry-password").getAsString().equals("null") ? null : TeriumDockerizedServices.getInstance().getConfigLoader().getJson().get("docker-cert-path").getAsString())
            .withRegistryUrl(TeriumDockerizedServices.getInstance().getConfigLoader().getJson().get("docker-registry-url").getAsString().equals("null") ? null : TeriumDockerizedServices.getInstance().getConfigLoader().getJson().get("docker-cert-path").getAsString())
            .build();

    private final DockerHttpClient dockerHttpClient = new ApacheDockerHttpClient.Builder()
            .dockerHost(clientConfig.getDockerHost())
            .sslConfig(clientConfig.getSSLConfig())
            .connectionTimeout(Duration.ofSeconds(30))
            .responseTimeout(Duration.ofSeconds(30))
            .build();

    private final DockerClient dockerClient = DockerClientImpl.getInstance(clientConfig, dockerHttpClient);

}