package de.gilljan.gworld.commands.tabcompletion;

import de.gilljan.gworld.GWorld;
import de.gilljan.gworld.enums.WorldTypeMapping;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;

public class TabCompleter implements org.bukkit.command.TabCompleter {
    public static final List<CompletionNode> GENERATORS = GWorld.AVAILABLE_GENERATORS.stream().map(CompletionNode::new).toList();
    public static final List<CompletionNode> WORLD_TYPES = Arrays.stream(WorldTypeMapping.values()).map((type) -> new CompletionNode(type.toString())).toList();

    private final CompletionNode root;

    public TabCompleter(CompletionNode root) {
        this.root = root;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        CompletionNode current = root;

        //System.out.println("ARGS: " + Arrays.toString(args));

        for (String arg : args) {
            List<CompletionNode> possibleChildren = current.getChildStartsWith(arg);

            if (possibleChildren.isEmpty()) {
                if (current.getChildren().containsKey("*")) {
                    current = current.getChild("*");
                    //System.out.println("Continue, possibleChildren.isEmpty(), current: " + current.getValue());
                    continue;
                } else {
                    //System.out.println("return List.of(), possibleChildren.isEmpty(), no *");
                    return List.of();
                }
            }

            if (possibleChildren.size() == 1) {
                current = possibleChildren.getFirst();
                //System.out.println("Continue, possibleChildren.size() == 1, current: " + current.getValue());
                continue;
            }

            List<CompletionNode> equalChildren = possibleChildren.stream()
                    .filter(node -> node.getValue().equals(arg))
                    .toList();

            if (equalChildren.size() == 1) {
                current = equalChildren.getFirst();
                //System.out.println("Continue, equalChildren.size() == 1, current: " + current.getValue());
                continue;
            }

            //System.out.println("return possibleChildren.stream().map(CompletionNode::getValue).toList()");

            return possibleChildren.stream()
                    .map(CompletionNode::getValue)
                    .filter((value) -> !value.equals("*"))
                    .toList();
        }

        if (current.getValue().equals("*")) {
            //System.out.println("Current is *");
            return List.of();
        }

        //System.out.println("return current.getChildren().keySet().stream().toList()");


        return List.of(current.getValue());
    }
}
