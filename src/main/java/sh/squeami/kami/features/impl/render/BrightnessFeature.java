package sh.squeami.kami.features.impl.render;

import net.minecraft.client.Minecraft;
import net.minecraft.potion.PotionEffect;
import sh.squeami.kami.events.api.interfaces.EventSubscribe;
import sh.squeami.kami.events.impl.render.Render2DEvent;
import sh.squeami.kami.features.api.enums.FeatureCategory;
import sh.squeami.kami.features.api.interfaces.FeatureAnnotation;
import sh.squeami.kami.features.api.types.Feature;
import sh.squeami.kami.settings.impl.EnumSetting;
import sh.squeami.kami.settings.impl.interfaces.IEnumSetting;

@FeatureAnnotation(name = "Brightness", description = "Increases the brightness of the game", category = FeatureCategory.RENDER)
public class BrightnessFeature extends Feature {

    private final EnumSetting<BrightnessMode> mode = new EnumSetting<>("Mode", BrightnessMode.GAMMA);

    private BrightnessMode previousMode;

    private float oldGamma;

    @Override
    public void onEnable() {
        super.onEnable();

        oldGamma = Minecraft.getMinecraft().gameSettings.gammaSetting;
    }

    @Override
    public void onDisable() {
        super.onDisable();

        final Minecraft minecraft = Minecraft.getMinecraft();

        if (minecraft.gameSettings.gammaSetting != oldGamma) {
            minecraft.gameSettings.gammaSetting = oldGamma;
        }

        if (mode.getValue() == BrightnessMode.POTION && minecraft.thePlayer.isPotionActive(16)) {
            minecraft.thePlayer.removePotionEffect(16);
        }
    }

    @EventSubscribe
    public void onRender2DListener(final Render2DEvent event) {
        final Minecraft minecraft = Minecraft.getMinecraft();

        if (previousMode != null && previousMode != mode.getValue()) {
            if (previousMode == BrightnessMode.GAMMA) {
                minecraft.gameSettings.gammaSetting = oldGamma;
            }
            if (previousMode == BrightnessMode.POTION) {
                minecraft.thePlayer.removePotionEffect(16);
            }
        }

        switch (mode.getValue()) {
            case GAMMA -> minecraft.gameSettings.gammaSetting = 100;
            case POTION -> minecraft.thePlayer.addPotionEffect(new PotionEffect(16, 1000000, 1));
        }

        previousMode = mode.getValue();
    }

    private enum BrightnessMode implements IEnumSetting {
        GAMMA("Gamma"),
        POTION("Potion");

        private final String name;

        BrightnessMode(String name) {
            this.name = name;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public Enum<?> getEnum(String name) {
            return BrightnessMode.valueOf(name);
        }
    }
}
