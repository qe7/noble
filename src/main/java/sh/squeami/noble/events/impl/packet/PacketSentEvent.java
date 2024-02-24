package sh.squeami.noble.events.impl.packet;

import sh.squeami.noble.events.api.types.EventCancellable;
import net.minecraft.network.Packet;

public class PacketSentEvent extends EventCancellable {

    private final Packet<?> packet;

    public PacketSentEvent(final Packet<?> packet) {
        this.packet = packet;
    }

    public Packet<?> getPacket() {
        return packet;
    }
}
