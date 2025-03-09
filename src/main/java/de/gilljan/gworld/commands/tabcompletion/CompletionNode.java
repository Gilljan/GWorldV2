package de.gilljan.gworld.commands.tabcompletion;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CompletionNode {
    private final String value;
    private final Map<String, CompletionNode> children;

    public CompletionNode(String value) {
        this.value = value;
        this.children = new HashMap<>();
    }

    public CompletionNode(String value, Map<String, CompletionNode> children) {
        this.value = value;
        this.children = children;
    }

    public String getValue() {
        return value;
    }

    public Map<String, CompletionNode> getChildren() {
        return children;
    }

    public void addChild(CompletionNode node) {
        children.put(node.getValue(), node);
    }

    public void addChildren(List<CompletionNode> nodes) {
        for (CompletionNode node : nodes) {
            this.addChild(node);
        }
    }

    public void addForAllChildren(List<CompletionNode> nodes) {
        for(CompletionNode child : children.values()) {
            child.addChildren(nodes);
        }
    }

    public CompletionNode getChild(String value) {
        return children.get(value);
    }

    public List<CompletionNode> getChildStartsWith(String prefix) {
        return children.values().stream()
                .filter(node -> node.getValue().toLowerCase().startsWith(prefix.toLowerCase()))
                .toList();
    }

    public boolean isLeaf() {
        return children.isEmpty();
    }

}
