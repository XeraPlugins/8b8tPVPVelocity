package ca.xera.pvpvelocity;

import ca.xera.pvpvelocity.chat.WhisperCommand;
import ca.xera.pvpvelocity.file.TomlConfig;
import ca.xera.pvpvelocity.sync.ServerConnectListener;
import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.messages.MinecraftChannelIdentifier;
import lombok.Getter;
import lombok.SneakyThrows;
import org.slf4j.Logger;

import java.io.File;
import java.nio.file.Path;

@Getter
@Plugin(id = "pvpvelocity", name = "PVPVelocity", version = "1.0", authors = "SevJ6")
public class PVPVelocity {

    public static final MinecraftChannelIdentifier IDENTIFIER_REQUEST = MinecraftChannelIdentifier.from("xera:invrequest");
    public static final MinecraftChannelIdentifier IDENTIFIER_REPLY = MinecraftChannelIdentifier.from("xera:invreply");
    private static PVPVelocity plugin;
    private final Logger logger;
    private final ProxyServer proxy;

    @SneakyThrows
    @Inject
    public PVPVelocity(ProxyServer proxy, Logger logger) {
        this.proxy = proxy;
        this.logger = logger;
        plugin = this;

        // ensure config folder has been generated
        File dataFolder = new File("plugins/PVPVelocity");
        if (!dataFolder.exists()) dataFolder.mkdirs();

        // toml config test
        TomlConfig config = new TomlConfig(Path.of(dataFolder.getAbsolutePath() + "/config.toml"), getClass().getResourceAsStream("/config.toml"));
        logger.info(String.format("%s, %s, %s, %s", config.getString("testString"), config.getStringList("testArray"), config.getLong("testLong"), config.getDouble("testDouble")));

    }

    public static PVPVelocity get() {
        return plugin;
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        proxy.getCommandManager().register(proxy.getCommandManager().metaBuilder("whisper").aliases("w", "msg", "message", "t", "tell").plugin(this).build(), new WhisperCommand(this));
        proxy.getChannelRegistrar().register(IDENTIFIER_REPLY, IDENTIFIER_REQUEST);
        proxy.getEventManager().register(this, new ServerConnectListener());
    }

    public Player getPlayer(String name) {
        return proxy.getAllPlayers().stream().filter(player -> player.getUsername().equalsIgnoreCase(name)).findAny().orElse(null);
    }
}
