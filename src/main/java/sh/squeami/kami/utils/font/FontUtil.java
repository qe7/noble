package sh.squeami.kami.utils.font;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.Sys;

import java.awt.*;

public class FontUtil {

    public static Font getFontFromTTF(ResourceLocation fontLocation, float fontSize, int fontType) {
        Font output = null;
        try {
            output = Font.createFont(fontType, Minecraft.getMinecraft().getResourceManager().getResource(fontLocation).getInputStream());
            output = output.deriveFont(fontSize);
        } catch (Exception e) {
            System.out.println("Error loading font from " + fontLocation.toString());
            System.out.println(e.getMessage());
        }
        return output;
    }
}
