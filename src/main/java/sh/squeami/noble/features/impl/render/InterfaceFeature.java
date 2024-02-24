package sh.squeami.noble.features.impl.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import sh.squeami.noble.Noble;
import sh.squeami.noble.events.api.interfaces.EventSubscribe;
import sh.squeami.noble.events.impl.render.Render2DEvent;
import sh.squeami.noble.features.api.enums.FeatureCategory;
import sh.squeami.noble.features.api.interfaces.FeatureAnnotation;
import sh.squeami.noble.features.api.types.Feature;
import sh.squeami.noble.fonts.api.CFontRenderer;
import sh.squeami.noble.settings.impl.BooleanSetting;
import sh.squeami.noble.settings.impl.EnumSetting;
import sh.squeami.noble.settings.impl.interfaces.IEnumSetting;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.function.BooleanSupplier;

@FeatureAnnotation(name = "Interface", category = FeatureCategory.RENDER, enabled = true, drawn = false)
public class InterfaceFeature extends Feature {

    private final BooleanSetting watermark = new BooleanSetting("Watermark", true);
    private final BooleanSetting arrayList = new BooleanSetting("ArrayList", true);
    private final BooleanSupplier arrayListSupplier = arrayList::getValue;
    private final EnumSetting<ArrayListType> arrayListType = new EnumSetting<>("Sort type", ArrayListType.LENGTH).supplyIf(arrayListSupplier);
    private final BooleanSetting info = new BooleanSetting("Info", true);
    private final BooleanSupplier infoSupplier = info::getValue;
    private final BooleanSetting FPS = new BooleanSetting("FPS", true).supplyIf(infoSupplier);
    private final BooleanSetting MS = new BooleanSetting("MS", true).supplyIf(infoSupplier);
    private final BooleanSetting BPS = new BooleanSetting("BPS", true).supplyIf(infoSupplier);

    @EventSubscribe
    public void onRender2DListener(Render2DEvent event) {
        final ScaledResolution scaledResolution = event.scaledResolution();
        final CFontRenderer fontRenderer = Noble.INSTANCE.getFontManager().getFontRenderer("OPEN_SANS_18");

        float offset = 2;

        if (watermark.getValue()) {
            fontRenderer.drawStringWithShadow(String.format("%s v%s", Noble.INSTANCE.getName(), Noble.INSTANCE.getVersion()), 2, offset, 0xFFFFFFFF);
        }

        offset = 0;
        if (arrayList.getValue()) {
            ArrayList<Feature> featureArrayList = new ArrayList<>(Noble.INSTANCE.getFeatureManager().getMap().values());

            switch (arrayListType.getValue()) {
                case ABC -> featureArrayList.sort((o1, o2) -> o1.getFeatureAnnotation().name().compareToIgnoreCase(o2.getFeatureAnnotation().name()));
                case LENGTH -> featureArrayList.sort(Comparator.comparingInt(o -> -fontRenderer.getStringWidth(o.getFeatureAnnotation().name() + (o.getSuffix() != null ? " §7" + o.getSuffix() : ""))));
                case CATEGORY -> featureArrayList.sort((o1, o2) -> o1.getFeatureAnnotation().category().getName().compareToIgnoreCase(o2.getFeatureAnnotation().category().getName()));
            }

            for (Feature feature : featureArrayList) {
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

    private enum ArrayListType implements IEnumSetting {
        ABC("Alphabetical"),
        LENGTH("Length"),
        CATEGORY("Category");

        private final String name;

        ArrayListType(String name) {
            this.name = name;
        }

        @Override
        public String getName() {
            return this.name;
        }

        @Override
        public Enum<?> getEnum(String name) {
            return valueOf(name);
        }
    }
}
