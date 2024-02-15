package sh.squeami.kami.features.impl.render;

import sh.squeami.kami.Kami;
import sh.squeami.kami.events.api.interfaces.EventSubscribe;
import sh.squeami.kami.events.impl.render.Render2DEvent;
import sh.squeami.kami.features.api.enums.FeatureCategory;
import sh.squeami.kami.features.api.interfaces.FeatureAnnotation;
import sh.squeami.kami.features.api.types.Feature;
import sh.squeami.kami.fonts.api.CFontRenderer;
import sh.squeami.kami.settings.impl.BooleanSetting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import sh.squeami.kami.settings.impl.NumberSetting;

import java.util.function.BooleanSupplier;

@FeatureAnnotation(name = "Interface", category = FeatureCategory.RENDER, enabled = true, drawn = false)
public class InterfaceFeature extends Feature {

    private final BooleanSetting watermark = new BooleanSetting("Watermark", true);
    private final BooleanSetting arrayList = new BooleanSetting("ArrayList", true);
    private final BooleanSetting info = new BooleanSetting("Info", true);
    private final BooleanSupplier booleanSupplier = info::getValue;
    private final BooleanSetting FPS = new BooleanSetting("FPS", true).supplyIf(booleanSupplier);
    private final BooleanSetting MS = new BooleanSetting("MS", true).supplyIf(booleanSupplier);
    private final BooleanSetting BPS = new BooleanSetting("BPS", true).supplyIf(booleanSupplier);

    private final NumberSetting<Double> test = new NumberSetting<>("Test", 0.0, 0.0, 100.0, 0.1);

    @EventSubscribe
    public void onRender2DListener(Render2DEvent event) {
        ScaledResolution scaledResolution = event.scaledResolution();
        CFontRenderer fontRenderer = Kami.INSTANCE.getFontManager().getFontRenderer("OPEN_SANS_18");

        float offset = 2;

        if (watermark.getValue()) {
            fontRenderer.drawStringWithShadow(String.format("%s v%s", Kami.INSTANCE.getName(), Kami.INSTANCE.getVersion()), 2, offset, 0xFFFFFFFF);
        }

        offset = 0;
        if (arrayList.getValue()) {
            for (Feature feature : Kami.INSTANCE.getFeatureManager().getMap().values()) {
                if (feature.getFeatureAnnotation().drawn() && feature.isEnabled()) {
                    String displayName = feature.getFeatureAnnotation().name();

                    if (feature.getSuffix() != null) {
                        displayName += " §7" + feature.getSuffix();
                    }
                    fontRenderer.drawStringWithShadow(displayName, scaledResolution.getScaledWidth() - fontRenderer.getStringWidth(displayName) - 2, 2 + offset, feature.getFeatureAnnotation().category().getColor().getRGB());
                    offset += 10;
                }
            }
        }

        offset = scaledResolution.getScaledHeight() - 10 - 2;

        if (info.getValue()) {
            if (FPS.getValue()) {
                final String FPS = "FPS: " + Minecraft.getDebugFPS();
                fontRenderer.drawStringWithShadow(FPS, scaledResolution.getScaledWidth() - fontRenderer.getStringWidth(FPS) - 2, offset, 0xFFFFFFFF);
                offset -= 10;
            }

            if (BPS.getValue()) {
                final double DBPS = Math.hypot(
                        Minecraft.getMinecraft().thePlayer.posX - Minecraft.getMinecraft().thePlayer.prevPosX,
                        Minecraft.getMinecraft().thePlayer.posZ - Minecraft.getMinecraft().thePlayer.prevPosZ
                ) * Minecraft.getMinecraft().getTimer().timerSpeed * 20.0;
                final String SBps = "BPS: " + Math.round(DBPS * 100.0) / 100.0;
                fontRenderer.drawStringWithShadow(SBps, scaledResolution.getScaledWidth() - fontRenderer.getStringWidth(SBps) - 2, offset, 0xFFFFFFFF);
                offset -= 10;
            }

            if (MS.getValue()) {
                final String MS = "MS: " + (Minecraft.getMinecraft().getCurrentServerData() != null ? Minecraft.getMinecraft().getCurrentServerData().pingToServer : "0");
                fontRenderer.drawStringWithShadow(MS, scaledResolution.getScaledWidth() - fontRenderer.getStringWidth(MS) - 2, offset, 0xFFFFFFFF);
            }
        }
    }
}
