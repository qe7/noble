package sh.squeami.noble.commands.impl;

import sh.squeami.noble.Noble;
import sh.squeami.noble.commands.api.types.Command;
import sh.squeami.noble.features.api.types.Feature;
import org.lwjgl.input.Keyboard;

public class BindCommand extends Command {

    public BindCommand() {
        super("Bind",
                "Binds a given feature.",
                "bind add [feature] [key] / bind remove [feature]",
                "b");
    }

    @Override
    public void execute(String [] args) {
        if (args.length != 4 && args.length != 3) {
            sendChatMessage("Usage: " + getUsage());
            return;
        }

        if (args[1].equalsIgnoreCase("add")) {
            if (handleAdd(args)) return;
        } else if (args[1].equalsIgnoreCase("remove")) {
            if (handleRemove(args)) return;
        }

        sendChatMessage("Feature '" + args[2] + "' not found.");
    }

    private boolean handleAdd(String[] args) {
        for (Feature feature : Noble.INSTANCE.getFeatureManager().getMap().values()) {
            if (feature.getFeatureAnnotation().name().equalsIgnoreCase(args[2])) {
                feature.setKeyCode(Keyboard.getKeyIndex(args[3].toUpperCase()));
                sendChatMessage("Bound " + feature.getFeatureAnnotation().name() + " to " + args[3] + ".");
                return true;
            }
        }
        return false;
    }

    private boolean handleRemove(String[] args) {
        for (Feature feature : Noble.INSTANCE.getFeatureManager().getMap().values()) {
            if (feature.getFeatureAnnotation().name().equalsIgnoreCase(args[2])) {
                feature.setKeyCode(0);
                sendChatMessage("Unbound " + feature.getFeatureAnnotation().name() + ".");
                return true;
            }
        }
        return false;
    }
}
