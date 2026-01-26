package de.gilljan.gworld.commands;

import de.gilljan.gworld.GWorld;
import de.gilljan.gworld.commands.tabcompletion.CompletionNode;
import de.gilljan.gworld.data.world.WorldData;
import de.gilljan.gworld.utils.SendMessageUtil;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class GWorldsCommand extends ArgsCommand {

    public GWorldsCommand() {
        super("gworlds", 0, "Worlds.use", "gworld.commands.gworlds");
    }

    @Override
    public boolean executeCommandForPlayer(Player player, String[] args) {
        sendMessages(player);
        return true;
    }

    @Override
    public boolean executeConsoleCommand(ConsoleCommandSender console, String[] args) {
        sendMessages(console);
        return true;
    }

    @Override
    protected CompletionNode createCompletions() {
        return new CompletionNode("gworlds");
    }

    private void sendMessages(CommandSender sender) {
        sender.sendMessage(SendMessageUtil.sendMessage("Worlds.loadedMaps"));
        for (WorldData world : GWorld.getInstance().getDataHandler().getWorlds().values()) {
            if(world.isLoaded()) {
                TextComponent component = new TextComponent();
                component.setText(" §7- §a" + world.getGeneralInformation().worldName());
                component.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new net.md_5.bungee.api.chat.hover.content.Text(
                        SendMessageUtil.sendMessage("Worlds.hoverTeleport").replace("%world%", world.getGeneralInformation().worldName())
                )));
                component.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/gtp " + world.getGeneralInformation().worldName()));
                sender.spigot().sendMessage(component);
            }
        }
    }
}
