package de.gilljan.gworld.commands;

import de.gilljan.gworld.commands.tabcompletion.CompletionNode;
import de.gilljan.gworld.utils.SendMessageUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class GHelpCommand extends ArgsCommand {

    public GHelpCommand() {
        super("ghelp", 0, "Help.use", "gworld.commands.help");
    }

    @Override
    public boolean executeCommandForPlayer(Player player, String[] args) {
        showHelp(player, args);

        return true;
    }

    @Override
    public boolean executeConsoleCommand(ConsoleCommandSender console, String[] args) {
        showHelp(console, args);

        return true;
    }

    @Override
    protected CompletionNode createCompletions() {
        CompletionNode root = new CompletionNode("ghelp");

        root.addChild(new CompletionNode("1"));
        root.addChild(new CompletionNode("2"));
        root.addChild(new CompletionNode("3"));

        return root;
    }

    private void showHelp(CommandSender sender, String[] args) {
        if(args.length == 0) {
            sender.sendMessage(SendMessageUtil.sendMessage("Help.header").replaceAll("%site%", "1").replaceAll("%maxsite%", "3"));
            sender.sendMessage(SendMessageUtil.sendMessage("Help.gclone"));
            sender.sendMessage(SendMessageUtil.sendMessage("Help.gcreate"));
            sender.sendMessage(SendMessageUtil.sendMessage("Help.gdelete"));
            sender.sendMessage(SendMessageUtil.sendMessage("Help.gimport"));
            return;
        }

        switch (args[0]) {
            case "1":
                sender.sendMessage(SendMessageUtil.sendMessage("Help.header").replaceAll("%site%", "1").replaceAll("%maxsite%", "3"));
                sender.sendMessage(SendMessageUtil.sendMessage("Help.gclone"));
                sender.sendMessage(SendMessageUtil.sendMessage("Help.gcreate"));
                sender.sendMessage(SendMessageUtil.sendMessage("Help.gdelete"));
                sender.sendMessage(SendMessageUtil.sendMessage("Help.gimport"));
                break;
            case "2":
                sender.sendMessage(SendMessageUtil.sendMessage("Help.header").replaceAll("%site%", "2").replaceAll("%maxsite%", "3"));
                sender.sendMessage(SendMessageUtil.sendMessage("Help.ginfo"));
                sender.sendMessage(SendMessageUtil.sendMessage("Help.gload"));
                sender.sendMessage(SendMessageUtil.sendMessage("Help.grecreate"));
                sender.sendMessage(SendMessageUtil.sendMessage("Help.gset"));
                break;
            case "3":
                sender.sendMessage(SendMessageUtil.sendMessage("Help.header").replaceAll("%site%", "3").replaceAll("%maxsite%", "3"));
                sender.sendMessage(SendMessageUtil.sendMessage("Help.gtp"));
                sender.sendMessage(SendMessageUtil.sendMessage("Help.gunload"));
                sender.sendMessage(SendMessageUtil.sendMessage("Help.gworlds"));
                sender.sendMessage(SendMessageUtil.sendMessage("Help.gwreload"));
                break;
            default:
                sender.sendMessage(SendMessageUtil.sendMessage("Help.use"));
                break;
        }
    }
}
