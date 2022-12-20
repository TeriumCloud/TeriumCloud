package cloud.terium.cloudsystem.template;

import cloud.terium.cloudsystem.TeriumCloud;
import cloud.terium.cloudsystem.console.commands.TemplateCommand;
import cloud.terium.cloudsystem.utils.logger.Logger;
import cloud.terium.teriumapi.node.INode;
import cloud.terium.teriumapi.console.LogType;
import cloud.terium.teriumapi.template.ITemplateFactory;
import lombok.SneakyThrows;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.nio.file.Path;

public class TemplateFactory implements ITemplateFactory {

    @Override
    public void createTemplate(String name) {
        File file = new File("templates//" + name);

        if(file.exists()) {
            Logger.log("A template with this name already exist.", LogType.ERROR);
            return;
        }
        file.mkdirs();
        TeriumCloud.getTerium().getTemplateProvider().getAllTemplates().add(new Template(name, Path.of(file.getPath())));
        TeriumCloud.getTerium().getCommandManager().registerCommand(new TemplateCommand(TeriumCloud.getTerium().getTemplateProvider().getAllTemplatesAsString().toArray(String[]::new)));
        Logger.log("Successfully created template '" + name + "'.", LogType.INFO);
    }

    @Override
    public void createTemplate(String name, INode node) {
    }

    @SneakyThrows
    @Override
    public void deleteTemplate(String name) {
        FileUtils.deleteDirectory(new File("templates//" + name));
        TeriumCloud.getTerium().getTemplateProvider().getAllTemplates().remove(TeriumCloud.getTerium().getTemplateProvider().getTemplateByName(name));
        TeriumCloud.getTerium().getCommandManager().registerCommand(new TemplateCommand(TeriumCloud.getTerium().getTemplateProvider().getAllTemplatesAsString().toArray(String[]::new)));
    }
}
