package cloud.terium.cloudsystem.cluster.console.commands;

import cloud.terium.cloudsystem.cluster.utils.Logger;
import cloud.terium.teriumapi.TeriumAPI;
import cloud.terium.teriumapi.console.LogType;
import cloud.terium.teriumapi.console.command.Command;
import cloud.terium.teriumapi.service.ICloudService;
import cloud.terium.teriumapi.template.ITemplate;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class CopyCommand extends Command {

    public CopyCommand() {
        super("copy", "Copy service files to a template.");
    }

    @Override
    public void execute(String[] args) {
        if (args.length == 2) {
            TeriumAPI.getTeriumAPI().getProvider().getServiceProvider().getServiceByName(args[0]).ifPresentOrElse(cloudService -> TeriumAPI.getTeriumAPI().getProvider().getTemplateProvider().getTemplateByName(args[1]).ifPresentOrElse(template -> {
                try {
                    Logger.log("Trying to copy '§b" + cloudService.getServiceName() + "§f' into template '§b" + template.getName() + "§f'...", LogType.INFO);
                    FileUtils.copyDirectory(new File((cloudService.getServiceGroup().isStatic() ? "static/" : "servers/") + cloudService.getServiceName()), template.getPath().toFile());
                    Logger.log("Successfully copied service '§b" + cloudService.getServiceName() + "§f'.", LogType.INFO);
                } catch (IOException ignored) {
                }
            }, () -> Logger.log("Specific template not found.", LogType.ERROR)), () -> Logger.log("Specific service not found.", LogType.ERROR));

            return;
        }

        Logger.log("copy [service] [template] | execute command on specific service", LogType.INFO);
    }

    @Override
    public List<String> tabComplete(String[] args) {
        if (args.length == 1)
            return TeriumAPI.getTeriumAPI().getProvider().getServiceProvider().getAllServices().stream().map(ICloudService::getServiceName).toList();

        if (args.length == 2)
            return TeriumAPI.getTeriumAPI().getProvider().getTemplateProvider().getAllTemplates().stream().map(ITemplate::getName).toList();

        return super.tabComplete(args);
    }
}