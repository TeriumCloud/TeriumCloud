package cloud.terium.cloudsystem.module;

import cloud.terium.teriumapi.service.CloudServiceType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.File;

@Getter
@Setter
@AllArgsConstructor
public class Module {

    private String name;
    private String author;
    private String version;
    private File file;
    private CloudServiceType serviceType;
}
