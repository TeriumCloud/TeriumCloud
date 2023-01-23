package cloud.terium.cloudsystem.template;

import cloud.terium.cloudsystem.TeriumCloud;
import cloud.terium.cloudsystem.utils.logger.Logger;
import cloud.terium.teriumapi.console.LogType;
import cloud.terium.teriumapi.node.INode;
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
        TeriumCloud.getTerium().getTemplateProvider().getAllTemplates().add(template);
        Logger.log("Successfully created template '" + name + "'.", LogType.INFO);
        return template;
    }

    @SneakyThrows
    @Override
    public void deleteTemplate(String name) {
        FileUtils.deleteDirectory(new File("templates//" + name));
        TeriumCloud.getTerium().getTemplateProvider().getAllTemplates().remove(TeriumCloud.getTerium().getTemplateProvider().getTemplateByName(name));
    }
}
