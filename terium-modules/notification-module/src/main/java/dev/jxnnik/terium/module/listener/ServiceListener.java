package dev.jxnnik.terium.module.listener;

import cloud.terium.teriumapi.event.Listener;
import cloud.terium.teriumapi.event.Subscribe;
import cloud.terium.teriumapi.events.service.CloudServiceStartedEvent;
import cloud.terium.teriumapi.events.service.CloudServiceStartingEvent;
import cloud.terium.teriumapi.events.service.CloudServiceStoppedEvent;
import dev.jxnnik.terium.module.velocity.NotificationVelocityStartup;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.minimessage.MiniMessage;

public class ServiceListener implements Listener {

    @Subscribe
    public void handleCloudServiceStarting(CloudServiceStartingEvent event) {
        NotificationVelocityStartup.getInstance().getProxyServer().getAllPlayers().stream().filter(player -> player.hasPermission("terium.notification")).forEach(player ->
                player.sendMessage(MiniMessage.miniMessage().deserialize("<gray>[<#FB8148>☀<gray>] <white>" + event.getCloudService().getServiceName())));
    }

    @Subscribe
    public void handleCloudServiceStarting(CloudServiceStartedEvent event) {
        NotificationVelocityStartup.getInstance().getProxyServer().getAllPlayers().stream().filter(player -> player.hasPermission("terium.notification")).forEach(player ->
                player.sendMessage(MiniMessage.miniMessage().deserialize("<gray>[<#0EF269>✔<gray>] <white>" + event.getCloudService().getServiceName()).hoverEvent(HoverEvent.hoverEvent(HoverEvent.Action.SHOW_TEXT, Component.text("§fClick to connect"))).clickEvent(ClickEvent.clickEvent(ClickEvent.Action.RUN_COMMAND, "/server " + event.getCloudService().getServiceName()))));
    }

    @Subscribe
    public void handleCloudServiceStarting(CloudServiceStoppedEvent event) {
        NotificationVelocityStartup.getInstance().getProxyServer().getAllPlayers().stream().filter(player -> player.hasPermission("terium.notification")).forEach(player ->
                player.sendMessage(MiniMessage.miniMessage().deserialize("<gray>[<#D00609>✘<gray>] <white>" + event.getCloudService().getServiceName())));
    }
}