package ca.xera.pvpvelocity.sync;

import ca.xera.pvpvelocity.PVPVelocity;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.PluginMessageEvent;
import com.velocitypowered.api.event.player.ServerPostConnectEvent;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ServerConnection;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ServerConnectListener {

    private final Map<Player, byte[]> players = new HashMap<>();

    @Subscribe
    public void onPluginMessageReceived(PluginMessageEvent event) {
        if (event.getSource() instanceof ServerConnection && event.getIdentifier().equals(PVPVelocity.IDENTIFIER_REQUEST)) {
            players.putIfAbsent(((ServerConnection) event.getSource()).getPlayer(), event.getData());
        }
    }

    @Subscribe
    @SuppressWarnings("UnstableApiUsage")
    public void onConnect(ServerPostConnectEvent event) {
        Player player = event.getPlayer();
        if (players.containsKey(player)) {
            Optional<ServerConnection> connection = player.getCurrentServer();
            connection.ifPresent(serverConnection -> {
                serverConnection.sendPluginMessage(PVPVelocity.IDENTIFIER_REPLY, players.get(player));
                PVPVelocity.get().getLogger().info("Plugin message sent to " + serverConnection.getServerInfo().toString());
            });
            players.remove(player);
        }
    }
}
