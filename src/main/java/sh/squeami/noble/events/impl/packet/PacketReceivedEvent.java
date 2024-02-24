package sh.squeami.noble.events.impl.packet;

import sh.squeami.noble.events.api.types.EventCancellable;
import net.minecraft.network.Packet;

public class PacketReceivedEvent extends EventCancellable {

    private final Packet<?> packet;

    public PacketReceivedEvent(final Packet<?> packet) {
        this.packet = packet;
    }

    public Packet<?> getPacket() {
        return packet;
    }
}
