package sh.squeami.kami.features.impl.movement;

import sh.squeami.kami.events.api.interfaces.EventSubscribe;
import sh.squeami.kami.events.impl.player.TickUpdateEvent;
import sh.squeami.kami.features.api.enums.FeatureCategory;
import sh.squeami.kami.features.api.interfaces.FeatureAnnotation;
import sh.squeami.kami.features.api.types.Feature;
import sh.squeami.kami.settings.impl.EnumSetting;
import sh.squeami.kami.settings.impl.interfaces.IEnumSetting;
import net.minecraft.client.Minecraft;
import org.lwjgl.input.Keyboard;

@FeatureAnnotation(name = "Sprint", description = "Automatically sprints for you", category = FeatureCategory.MOVEMENT, enabled = true)
public class SprintFeature extends Feature {

    public static final EnumSetting<ModeEnum> mode = new EnumSetting<>("Mode", ModeEnum.LEGIT);

    @Override
    public void onDisable() {
        final boolean shouldSprint = Keyboard.isKeyDown(Minecraft.getMinecraft().gameSettings.keyBindSprint.getKeyCode());
        Minecraft.getMinecraft().gameSettings.keyBindSprint.setPressed(shouldSprint);
    }
    
    @EventSubscribe
    public void onTickUpdateListener(TickUpdateEvent ignored) {
        final Minecraft minecraft = Minecraft.getMinecraft();

        switch (mode.getValue()) {
            case RAGE -> {
                final boolean shouldSprint = minecraft.thePlayer.moveForward != 0 || minecraft.thePlayer.moveStrafing != 0;
                minecraft.thePlayer.setSprinting(shouldSprint);
            }
            case LEGIT -> {
                final boolean shouldSprint = minecraft.gameSettings.keyBindForward.isKeyDown();
                minecraft.gameSettings.keyBindSprint.setPressed(shouldSprint);
            }
        }
    }

    public enum ModeEnum implements IEnumSetting {
        RAGE("Rage"),
        LEGIT("Legit");

        private final String name;

        ModeEnum(String name) {
            this.name = name;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public Enum<?> getEnum(String name) {
            return ModeEnum.valueOf(name);
        }
    }
}
