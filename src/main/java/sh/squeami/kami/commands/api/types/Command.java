package sh.squeami.kami.commands.api.types;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;

public abstract class Command {

    private final String name, description, usage;
    private final String[] aliases;

    public Command(String name, String description, String usage, String... aliases) {
        this.name = name;
        this.description = description;
        this.usage = usage;
        this.aliases = aliases;
    }

    /**
     * Executes the command with the specified arguments.
     *
     * @param args The arguments.
     */
    public abstract void execute(String[] args);

    /**
     * @return The name of the command.
     */
    public String getName() {
        return name;
    }

    /**
     * @return The description of the command.
     */
    public String getDescription() {
        return description;
    }

    /**
     * @return The usage of the command.
     */
    public String getUsage() {
        return usage;
    }

    /**
     * @return The aliases of the command.
     */
    public String[] getAliases() {
        return aliases;
    }

    /**
     * @param message :)
     */
    public void sendChatMessage(String message) {
        Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("§8[§4§l" + "K" + "§r§8] §r" + message));
    }
}
