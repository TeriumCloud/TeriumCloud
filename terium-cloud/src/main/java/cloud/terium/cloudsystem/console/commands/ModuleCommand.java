package cloud.terium.cloudsystem.console.commands;

import cloud.terium.cloudsystem.TeriumCloud;
import cloud.terium.cloudsystem.utils.logger.Logger;
import cloud.terium.teriumapi.console.LogType;
import cloud.terium.teriumapi.console.command.Command;
import cloud.terium.teriumapi.module.ILoadedModule;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class ModuleCommand extends Command {

    public ModuleCommand() {
        super("module", "Manage modules", "modules");
    }

    @Override
    public void execute(String[] args) {
        if(args.length >= 1) {
            if(args.length == 1 && args[0].equalsIgnoreCase("list")) {
                TeriumCloud.getTerium().getModuleProvider().getAllModules().forEach(module -> {
                    Logger.log(module.getName() + "(" + module.getFileName() + ") by " + module.getAuthor() + " version " + module.getVersion() + ".", LogType.INFO);
                });
            }

            if(args.length == 2 && args[0].equalsIgnoreCase("disable")) {
                ILoadedModule module = TeriumCloud.getTerium().getModuleProvider().getModuleByName(args[1]);
                try {
                    TeriumCloud.getTerium().getModuleProvider().unloadModule(module);
                } catch (Exception exception) {
                    Logger.log("A module with this name isn't loaded");
                }
            }
            return;
        }

        Logger.log("module list | list of all loaded modules", LogType.INFO);
        Logger.log("module enable [module] | list of all loaded modules", LogType.INFO);
        Logger.log("module disable [module] | list of all loaded modules", LogType.INFO);
    }

    @Override
    public List<String> tabComplete(String[] args) {
        switch (args.length) {
            case 1 -> {
                return Arrays.asList("list", "enable", "disable");
            }

            case 2 -> {
                if(args[0].equalsIgnoreCase("enable")) {
                    return Arrays.stream(new File("modules//").listFiles()).map(File::getName).filter(s -> !TeriumCloud.getTerium().getModuleProvider().getAllModules().stream().map(ILoadedModule::getFileName).toList().contains(s)).toList();
                }

                if(args[0].equalsIgnoreCase("disable")) {
                    return TeriumCloud.getTerium().getModuleProvider().getAllModules().stream().map(ILoadedModule::getName).toList();
                }
            }
        }

        return super.tabComplete(args);
    }
}