package sh.squeami.kami.commands.impl;

import sh.squeami.kami.Kami;
import sh.squeami.kami.commands.api.types.Command;
import sh.squeami.kami.features.api.types.Feature;

public class ToggleCommand extends Command {

    public ToggleCommand() {
        super("Toggle", "Toggles a given feature.", "toggle [feature]", "t");
    }

    @Override
    public void execute(String [] args) {
        if (args.length != 2) {
            sendChatMessage("Usage: " + getUsage());
            return;
        }

        for (Feature feature : Kami.INSTANCE.getFeatureManager().getMap().values()) {
            if (feature.getFeatureAnnotation().name().equalsIgnoreCase(args[1])) {
                feature.setEnabled(!feature.isEnabled());
                sendChatMessage(feature.getFeatureAnnotation().name() + " has been " + (feature.isEnabled() ? "enabled" : "disabled") + ".");
                return;
            }
        }

        sendChatMessage("Feature '" + args[1] + "' not found.");
    }
}
