package sh.squeami.kami;

import sh.squeami.kami.components.impl.RotationComponent;
import sh.squeami.kami.events.api.EventBus;
import sh.squeami.kami.managers.impl.CommandManager;
import sh.squeami.kami.managers.impl.FeatureManager;
import sh.squeami.kami.managers.impl.FontManager;
import sh.squeami.kami.managers.impl.SettingManager;

/**
 * The main class for the Kami client.
 *
 * <p>This class is a singleton and should be accessed through the {@link #INSTANCE} field.
 *
 * <p>It contains the name and version of the client, as well as the event bus, setting manager, feature manager, command manager, and font manager.
 */
public final class Kami {

    public final static Kami INSTANCE = new Kami();

    private final String name, version;
    private final EventBus eventBus;

    private final SettingManager settingManager;
    private final FeatureManager featureManager;
    private final CommandManager commandManager;
    private final FontManager fontManager;

    private final RotationComponent rotationComponent;

    private Kami() {
        this.name = "Kami";
        this.version = "1.0.0";

        this.eventBus = new EventBus();
        this.settingManager = new SettingManager();
        this.featureManager = new FeatureManager();
        this.getEventBus().register(this.getFeatureManager());
        this.commandManager = new CommandManager();
        this.getEventBus().register(this.getCommandManager());
        this.fontManager = new FontManager();

        this.rotationComponent = new RotationComponent();
        this.getEventBus().register(this.getRotationComponent());
    }

    public void initialize() {
        this.getFeatureManager().initialize();
        this.getCommandManager().initialize();
        this.getFontManager().initialize();
    }

    public String getName() {
        return this.name;
    }

    public String getVersion() {
        return this.version;
    }

    public EventBus getEventBus() {
        return this.eventBus;
    }

    public SettingManager getSettingManager() {
        return this.settingManager;
    }

    public FeatureManager getFeatureManager() {
        return this.featureManager;
    }

    public CommandManager getCommandManager() {
        return this.commandManager;
    }

    public FontManager getFontManager() {
        return this.fontManager;
    }

    public RotationComponent getRotationComponent() {
        return this.rotationComponent;
    }
}
