package cloud.terium.cloudsystem.node.template;

import cloud.terium.cloudsystem.node.NodeStartup;
import cloud.terium.cloudsystem.node.utils.Logger;
import cloud.terium.cloudsystem.common.template.Template;
import cloud.terium.teriumapi.console.LogType;
import cloud.terium.teriumapi.template.ITemplate;
import cloud.terium.teriumapi.template.ITemplateFactory;
import lombok.SneakyThrows;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.nio.file.Path;

public class TemplateFactory implements ITemplateFactory {

    @Override
    public ITemplate createTemplate(String name) {
        File file = new File("templates//" + name);

        if (file.exists()) {
            Logger.log("A template with this name already exist.", LogType.ERROR);
            return null;
        }
        file.mkdirs();
        Template template = new Template(name, Path.of(file.getPath()));
        NodeStartup.getNode().getTemplateProvider().getAllTemplates().add(template);
        Logger.log("Successfully created template '§b" + name + "§f'.", LogType.INFO);
        return template;
    }

    @SneakyThrows
    @Override
    public void deleteTemplate(String name) {
        FileUtils.deleteDirectory(new File("templates//" + name));
        NodeStartup.getNode().getTemplateProvider().getAllTemplates().remove(NodeStartup.getNode().getTemplateProvider().getTemplateByName(name));
    }
}
