package cloud.terium.cloudsystem.module;

import cloud.terium.teriumapi.service.CloudServiceType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Module {

    private String name;
    private String author;
    private String version;
    private CloudServiceType serviceType;
}
