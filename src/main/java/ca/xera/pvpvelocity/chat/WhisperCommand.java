package ca.xera.pvpvelocity.chat;

import ca.xera.pvpvelocity.PVPVelocity;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class WhisperCommand implements SimpleCommand {

    private final PVPVelocity plugin;

    @Override
    public void execute(Invocation invocation) {
        CommandSource source = invocation.source();
        String[] args = invocation.arguments();
        if (args.length > 1) {
            Player target = plugin.getPlayer(args[0]);
            if (target != null) {
                if (!Objects.equals(source, target)) {
                    String message = String.join(" ", Arrays.copyOfRange(args, 1, args.length));
                    source.sendMessage(Component.text(String.format("To %s: %s", target.getUsername(), message), NamedTextColor.LIGHT_PURPLE));
                    target.sendMessage(Component.text(String.format("From %s: %s", source instanceof Player ? ((Player) source).getUsername() : "CONSOLE", message), NamedTextColor.LIGHT_PURPLE));
                } else source.sendMessage(Component.text("You cannot message yourself!", NamedTextColor.RED));
            } else source.sendMessage(Component.text(String.format("%s is not online!", args[0]), NamedTextColor.RED));
        } else source.sendMessage(Component.text(String.format("/%s <player> <message>", invocation.alias()), NamedTextColor.RED));
    }

    @Override
    public List<String> suggest(Invocation invocation) {
        return plugin.getProxy().getAllPlayers().stream().map(Player::getUsername).collect(Collectors.toList());
    }

    @Override
    public boolean hasPermission(Invocation invocation) {
        return true;
    }
}
