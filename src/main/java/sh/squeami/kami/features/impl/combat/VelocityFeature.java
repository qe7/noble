package sh.squeami.kami.features.impl.combat;

import net.minecraft.client.Minecraft;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import sh.squeami.kami.events.api.interfaces.EventSubscribe;
import sh.squeami.kami.events.impl.packet.PacketReceivedEvent;
import sh.squeami.kami.features.api.enums.FeatureCategory;
import sh.squeami.kami.features.api.interfaces.FeatureAnnotation;
import sh.squeami.kami.features.api.types.Feature;
import sh.squeami.kami.settings.impl.NumberSetting;

@FeatureAnnotation(name = "Velocity", description = "Reduces knockback from attacks", category = FeatureCategory.COMBAT)
public class VelocityFeature extends Feature {

    private final NumberSetting<Integer> horizontal = new NumberSetting<>("Horizontal", 0, 0, 100, 1);
    private final NumberSetting<Integer> vertical = new NumberSetting<>("Vertical", 0, 0, 100, 1);

    @Override
    public void onEnable() {
        super.onEnable();
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

    @EventSubscribe
    public void onPacketReceivedListener(final PacketReceivedEvent event) {
        if (!(event.getPacket() instanceof S12PacketEntityVelocity packet)) return;

        // cancels the event as if we dont and just set motion to 0, it'll slow us down
        if (horizontal.getValue() == 0 && vertical.getValue() == 0) {
            event.setCancelled(true);
        } else if (packet.getEntityID() == Minecraft.getMinecraft().thePlayer.getEntityId()) {
            // basic math to reduce the knockback as a percentage of the original knockback
            // using our settings
            packet.setMotionX((int) (packet.getMotionX() * (horizontal.getValue() / 100.0)));
            packet.setMotionY((int) (packet.getMotionY() * (vertical.getValue() / 100.0)));
            packet.setMotionZ((int) (packet.getMotionZ() * (horizontal.getValue() / 100.0)));
        }
    }
}
