package sh.squeami.noble.managers.impl;

import sh.squeami.noble.commands.api.types.Command;
import sh.squeami.noble.commands.impl.BindCommand;
import sh.squeami.noble.commands.impl.FakePlayerCommand;
import sh.squeami.noble.commands.impl.ToggleCommand;
import sh.squeami.noble.events.api.interfaces.EventSubscribe;
import sh.squeami.noble.events.impl.packet.PacketSentEvent;
import sh.squeami.noble.managers.api.Manager;
import net.minecraft.network.play.client.C01PacketChatMessage;

public class CommandManager extends Manager<Command> {

    private static final Class<? extends Command>[] COMMAND_CLASSES = new Class[]{
            ToggleCommand.class, BindCommand.class, FakePlayerCommand.class
    };

    public void initialize() {
        for (Class<? extends Command> clazz : COMMAND_CLASSES) {
            try {
                register(clazz);
                System.out.println("Registered class " + clazz.getSimpleName() + ".");
            } catch (Exception e) {
                System.out.println("Failed to register class " + clazz.getSimpleName() + ".");
            }
        }
    }

    @EventSubscribe
    public void onPacketSendListener(PacketSentEvent packetSentEvent) {
        if (!(packetSentEvent.getPacket() instanceof C01PacketChatMessage)) return;

        String message = ((C01PacketChatMessage) packetSentEvent.getPacket()).getMessage();

        if (!message.startsWith(".")) return;

        String[] args = message.substring(1).split(" ");

        packetSentEvent.setCancelled(true);

        for (Command command : getMap().values()) {
            if (command.getName().equalsIgnoreCase(args[0])) {
                command.execute(args);
            } else {
                for (String alias : command.getAliases()) {
                    if (alias.equalsIgnoreCase(args[0])) {
                        command.execute(args);
                    }
                }
            }
        }
    }
}
