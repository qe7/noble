package sh.squeami.noble.features.api.enums;

import java.awt.*;

public enum FeatureCategory {

    COMBAT("Combat", new Color(120, 0, 0)),
    MOVEMENT("Movement", new Color(193, 18, 31)),
    RENDER("Visual", new Color(60, 110, 113)),
    MISC("Miscellaneous", new Color(217, 217, 217)),
    AUTO("Automation", new Color(40, 75, 99));

    private final String name;
    private final Color color;

    /**
     * @param name The name of the category.
     * @param color The color of the category.
     */
    FeatureCategory(String name, Color color) {
        this.name = name;
        this.color = color;
    }

    /**
     * @return The name of the category.
     */
    public String getName() {
        return name;
    }

    /**
     * @return The color of the category.
     */
    public Color getColor() {
        return color;
    }
}
