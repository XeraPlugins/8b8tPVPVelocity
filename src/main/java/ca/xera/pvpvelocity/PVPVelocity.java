package ca.xera.pvpvelocity;

import ca.xera.pvpvelocity.chat.WhisperListener;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.PluginMessageEvent;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.ServerConnection;
import com.velocitypowered.api.proxy.messages.MinecraftChannelIdentifier;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.slf4j.Logger;

import java.util.Optional;

@Getter
@Plugin(id = "pvpvelocity", name = "PVPVelocity", version = "1.0", authors = "SevJ6")
public class PVPVelocity {

    private final Logger logger;
    private final ProxyServer proxy;

    @Inject
    public PVPVelocity(ProxyServer proxy, Logger logger) {
        this.proxy = proxy;
        this.logger = logger;
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        proxy.getEventManager().register(this, new WhisperListener(this));
    }

    public Player getPlayer(String name) {
        return proxy.getAllPlayers().stream().filter(player -> player.getUsername().equalsIgnoreCase(name)).findAny().orElse(null);
    }
}
