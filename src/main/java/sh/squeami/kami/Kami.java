package sh.squeami.kami;

import sh.squeami.kami.events.api.EventBus;
import sh.squeami.kami.managers.impl.CommandManager;
import sh.squeami.kami.managers.impl.FeatureManager;
import sh.squeami.kami.managers.impl.FontManager;
import sh.squeami.kami.managers.impl.SettingManager;

public final class Kami {

    public final static Kami INSTANCE = new Kami();

    private final String name, version;
    private final EventBus eventBus;
    private final SettingManager settingManager;
    private final FeatureManager featureManager;
    private final CommandManager commandManager;
    private final FontManager fontManager;

    private Kami() {
        this.name = "Kami";
        this.version = "1.0.0";
        this.eventBus = new EventBus();
        this.settingManager = new SettingManager();
        this.featureManager = new FeatureManager();
        getEventBus().register(getFeatureManager());
        this.commandManager = new CommandManager();
        getEventBus().register(getCommandManager());
        this.fontManager = new FontManager();
    }

    public void initialize() {
        getFeatureManager().initialize();
        getCommandManager().initialize();
        getFontManager().initialize();
    }

    public String getName() {
        return name;
    }

    public String getVersion() {
        return version;
    }

    public EventBus getEventBus() {
        return eventBus;
    }

    public SettingManager getSettingManager() {
        return settingManager;
    }

    public FeatureManager getFeatureManager() {
        return featureManager;
    }

    public CommandManager getCommandManager() {
        return commandManager;
    }

    public FontManager getFontManager() {
        return fontManager;
    }
}
