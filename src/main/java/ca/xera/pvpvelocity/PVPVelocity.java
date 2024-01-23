package ca.xera.pvpvelocity;

import ca.xera.pvpvelocity.chat.WhisperCommand;
import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import lombok.Getter;
import org.slf4j.Logger;

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
        proxy.getCommandManager().register(proxy.getCommandManager().metaBuilder("whisper").aliases("w", "msg", "message", "t", "tell").plugin(this).build(), new WhisperCommand(this));
    }

    public Player getPlayer(String name) {
        return proxy.getAllPlayers().stream().filter(player -> player.getUsername().equalsIgnoreCase(name)).findAny().orElse(null);
    }
}
