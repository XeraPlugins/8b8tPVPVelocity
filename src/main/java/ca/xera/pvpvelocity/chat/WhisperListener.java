package ca.xera.pvpvelocity.chat;

import ca.xera.pvpvelocity.PVPVelocity;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.PluginMessageEvent;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ServerConnection;
import com.velocitypowered.api.proxy.messages.MinecraftChannelIdentifier;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;

public class WhisperListener {

    private final PVPVelocity plugin;
    private final MinecraftChannelIdentifier IDENTIFIER_CHAT = MinecraftChannelIdentifier.from("core:chat");

    public WhisperListener(PVPVelocity plugin) {
        this.plugin = plugin;
        plugin.getProxy().getChannelRegistrar().register(IDENTIFIER_CHAT);
    }

    @Subscribe
    public void onPluginMessageRecieved(PluginMessageEvent event) {
        if (!(event.getSource() instanceof ServerConnection)) return;
        if (event.getIdentifier() != IDENTIFIER_CHAT) return;

        // Initialize data output for later use
        @SuppressWarnings("UnstableApiUsage") ByteArrayDataOutput out = ByteStreams.newDataOutput();

        // Read input from plugin message
        @SuppressWarnings("UnstableApiUsage") ByteArrayDataInput in = ByteStreams.newDataInput(event.getData());

        String senderName = in.readUTF();
        String targetName = in.readUTF();
        String message = in.readUTF();

        Player target = plugin.getPlayer(targetName);
        Player sender = plugin.getPlayer(senderName);
        if (target != null) {
            target.sendMessage(Component.text(String.format("From %s: %s", senderName, message), TextColor.fromHexString("#55FFFF")));
            sender.sendMessage(Component.text(String.format("To %s: %s", targetName, message), TextColor.fromHexString("#55FFFF")));
        } else {
            if (sender != null) {
                out.writeUTF("PlayerNotOnline");
                out.writeUTF(senderName);
                out.writeUTF(targetName);
                sender.getCurrentServer().ifPresent(server -> server.sendPluginMessage(IDENTIFIER_CHAT, out.toByteArray()));
            }
        }
    }
}
