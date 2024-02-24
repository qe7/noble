package sh.squeami.noble.features.impl.combat;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.input.Mouse;
import sh.squeami.noble.Noble;
import sh.squeami.noble.events.api.enums.EventPriority;
import sh.squeami.noble.events.api.interfaces.EventSubscribe;
import sh.squeami.noble.events.impl.player.MotionEvent;
import sh.squeami.noble.events.impl.render.Render2DEvent;
import sh.squeami.noble.features.api.enums.FeatureCategory;
import sh.squeami.noble.features.api.interfaces.FeatureAnnotation;
import sh.squeami.noble.features.api.types.Feature;
import sh.squeami.noble.settings.impl.BooleanSetting;
import sh.squeami.noble.settings.impl.EnumSetting;
import sh.squeami.noble.settings.impl.NumberSetting;
import sh.squeami.noble.settings.impl.interfaces.IEnumSetting;
import sh.squeami.noble.utils.math.TimerUtil;
import sh.squeami.noble.utils.player.InteractionUtil;
import sh.squeami.noble.utils.player.RotationUtil;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.function.BooleanSupplier;

@FeatureAnnotation(name = "KillAura", description = "Automatically attacks entities around you", category = FeatureCategory.COMBAT)
public class KillAuraFeature extends Feature {

    private final EnumSetting<AttackMode> attackMode = new EnumSetting<>("Attack Mode", AttackMode.SINGLE);
    private final BooleanSupplier attackSwitchModeSupplier = () -> attackMode.getValue() == AttackMode.SWITCH;

    private final EnumSetting<TargetMode> targetMode = new EnumSetting<>("Target Mode", TargetMode.DISTANCE);

    private final EnumSetting<RotationMode> rotationMode = new EnumSetting<>("Rotation Mode", RotationMode.STATIC);
    private final BooleanSupplier rotationSmoothModeSupplier = () -> rotationMode.getValue() == RotationMode.SMOOTH;

    private final BooleanSetting silent = new BooleanSetting("Silent Aim", true).supplyIf(() -> rotationMode.getValue() != RotationMode.NONE);

    private final BooleanSetting rayTrace = new BooleanSetting("Ray Trace", true);

    private final BooleanSetting disableOnDeath = new BooleanSetting("Disable On Death", true);

    private final BooleanSetting targetPlayers = new BooleanSetting("Players", true);
    private final BooleanSetting targetMobs = new BooleanSetting("Mobs", true);
    private final BooleanSetting targetAnimals = new BooleanSetting("Animals", true);
    private final BooleanSetting targetInvisible = new BooleanSetting("Invisible", false);

    private final NumberSetting<Integer> switchDelay = new NumberSetting<>("Switch Delay", 250, 0, 1000, 25).supplyIf(attackSwitchModeSupplier);

    private final NumberSetting<Float> range = new NumberSetting<>("Range", 4.5f, 1.0f, 6.0f, 0.1f);

    private final NumberSetting<Integer> maxCPS = new NumberSetting<>("Max CPS", 10, 1, 20, 1);
    private final NumberSetting<Integer> minCPS = new NumberSetting<>("Min CPS", 10, 1, 20, 1);

    private final NumberSetting<Integer> maxSmooth = new NumberSetting<>("Max Smooth", 30, 0, 100, 1).supplyIf(rotationSmoothModeSupplier);
    private final NumberSetting<Integer> minSmooth = new NumberSetting<>("Min Smooth", 30, 0, 100, 1).supplyIf(rotationSmoothModeSupplier);

    private final TimerUtil attackTimer = new TimerUtil();

    private final HashMap<Entity, Long> attackDelayMap = new HashMap<>();

    private List<Entity> entityList;

    private Entity target;

    @Override
    public void onEnable() {
        super.onEnable();

        this.attackTimer.reset();
        this.attackDelayMap.clear();
        this.entityList = null;
        this.target = null;
    }

    @Override
    public void onDisable() {
        super.onDisable();

        InteractionUtil.sendClick(0, Mouse.isButtonDown(0));
    }

    @EventSubscribe
    public void onRender2DListener(final Render2DEvent render2DEvent) {
        final Minecraft minecraft = Minecraft.getMinecraft();
        final FontRenderer fontRenderer = minecraft.fontRendererObj;

        // sanity
        if (minecraft.theWorld == null) return;
        if (minecraft.thePlayer == null) return;

        // set the suffix to the attack mode
        this.setSuffix(this.attackMode.getValue().getName());

        if (entityList == null) return;

        int y = 12;
        for (Entity entity : entityList) {
            if (entity instanceof EntityLivingBase entityLivingBase) {
                final String name = entityLivingBase.getName();
                final String health = String.format("%.1f", entityLivingBase.getHealth());
                final String distance = String.format("%.1f", entityLivingBase.getDistanceToEntity(minecraft.thePlayer));

                fontRenderer.drawStringWithShadow(name, 2, y, 0xFFFFFF);
                fontRenderer.drawStringWithShadow(health, 2, y + 10, 0xFFFFFF);
                fontRenderer.drawStringWithShadow(distance, 2, y + 20, 0xFFFFFF);

                y += 30;
            }
        }
    }

    @EventSubscribe(priority = EventPriority.HIGH)
    public void onMotionListener(final MotionEvent ignored) {
        final Minecraft minecraft = Minecraft.getMinecraft();

        // sanity
        if (minecraft.theWorld == null) return;
        if (minecraft.thePlayer == null) return;
        if ((minecraft.thePlayer.isDead || minecraft.thePlayer.getHealth() <= 0) && disableOnDeath.getValue()) {
            this.setEnabled(false);
            return;
        }

        // update entity list
        this.entityList = new ArrayList<>(minecraft.theWorld.getLoadedEntityList());
        this.entityList.remove(minecraft.thePlayer);
        this.entityList.removeIf(entity -> !entity.isEntityAlive() ||
                        !(
                            (entity instanceof EntityPlayer && targetPlayers.getValue()) ||
                            (entity instanceof EntityMob && targetMobs.getValue()) ||
                            (entity instanceof EntityAnimal && targetAnimals.getValue()) ||
                            (entity.isInvisible() && targetInvisible.getValue())
                        ) ||
                        entity.getDistanceToEntity(minecraft.thePlayer) > range.getValue());
        this.entityList.sort(handleComparator(targetMode.getValue()));

        // sanity
        if (this.entityList.isEmpty()) return;

        // update target
        if (this.attackMode.getValue() == AttackMode.SWITCH) {
            // check if we want to switch target
            if (System.currentTimeMillis() - attackDelayMap.getOrDefault(target, 0L) >= switchDelay.getValue()) {
                this.target = handleTarget(this.entityList);
            }
        } else {
            this.target = handleTarget(this.entityList);
        }

        // sanity
        if (this.target == null) return;

        // check if the target is still alive/valid, if not, reset the target
        if (!this.target.isEntityAlive() || this.target.getDistanceToEntity(minecraft.thePlayer) > range.getValue()) {
            this.target = null;
            return;
        }

        // rotate to the target
        if (this.rotationMode.getValue() != RotationMode.NONE) {
            handleRotations(this.target);
        }

        // attack the target
        final float randomCPS = (float) (Math.random() * (maxCPS.getValue() - minCPS.getValue()) + minCPS.getValue());
        if (attackTimer.hasReached((long) (1000 / randomCPS))) {
            if (rayTrace.getValue())
                InteractionUtil.sendClick(0, true);
            else {
                minecraft.thePlayer.swingItem();
                minecraft.playerController.attackEntity(minecraft.thePlayer, target);
                attackTimer.reset();
            }
        }

        if (attackTimer.hasReached((long) (1000 / randomCPS) + 50)) {
            if (rayTrace.getValue()) {
                InteractionUtil.sendClick(0, false);
                attackTimer.reset();
            }
        }
    }

    private static Comparator<Entity> handleComparator(TargetMode targetMode) {
        return switch (targetMode) {
            case DISTANCE -> Comparator.comparingDouble(entity -> ((EntityLivingBase) entity).getDistanceToEntity(Minecraft.getMinecraft().thePlayer));
            case HEALTH -> Comparator.comparingDouble(entity -> ((EntityLivingBase) entity).getHealth());
        };
    }

    private Entity handleTarget(final List<Entity> entityList) {
        if (entityList.isEmpty()) return null;

        if (attackMode.getValue() == AttackMode.SINGLE) {
            return entityList.getFirst();
        }

        if (attackMode.getValue() == AttackMode.SWITCH) {
            // Update hashmap with new entities if not already in the map
            for (Entity entity : entityList) {
                attackDelayMap.putIfAbsent(entity, 0L);
            }

            // Remove entities that are no longer in the world/dead
            attackDelayMap.keySet().removeIf(entity -> entity == null || !entity.isEntityAlive() || entity.getDistanceToEntity(Minecraft.getMinecraft().thePlayer) > range.getValue());

            // Iterate through the entities and find the one with the oldest timestamp (i.e., the one that was targeted the longest time ago)
            Entity target = null;
            long oldestTimestamp = Long.MAX_VALUE;
            for (Entity entity : entityList) {
                long timestamp = attackDelayMap.get(entity);
                if (timestamp < oldestTimestamp) {
                    oldestTimestamp = timestamp;
                    target = entity;
                }
            }

            // If we found a target and the switch delay has passed, update the timestamp for the target
            if (target != null && System.currentTimeMillis() - oldestTimestamp >= switchDelay.getValue()) {
                attackDelayMap.put(target, System.currentTimeMillis()); // Update the timestamp
            } else {
                target = null; // If the switch delay has not passed, don't switch target
            }

            return target;
        }

        throw new IllegalStateException("Invalid attack mode");
    }

    private void handleRotations(final Entity entity) {
        float[] rotations = RotationUtil.getRotations(entity);

        if (rotationMode.getValue() == RotationMode.SMOOTH) {
            final float randomSmooth = (float) (Math.random() * (maxSmooth.getValue() - minSmooth.getValue()) + minSmooth.getValue());
            rotations = RotationUtil.getSmoothRotations(Minecraft.getMinecraft().thePlayer.getPreviousRotation(),
                    rotations,
                    randomSmooth);
        }

        rotations = RotationUtil.applySanity(rotations);

        rotations = RotationUtil.applyGCD(rotations);

        if (silent.getValue()) {
            Noble.INSTANCE.getRotationComponent().setRotation(rotations);
        } else {
            Minecraft.getMinecraft().thePlayer.rotationYaw = rotations[0];
            Minecraft.getMinecraft().thePlayer.rotationPitch = rotations[1];
        }
    }

    private enum AttackMode implements IEnumSetting {
        SINGLE("Single"),
        SWITCH("Switch");

        private final String name;

        AttackMode(String name) {
            this.name = name;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public Enum<?> getEnum(String name) {
            return AttackMode.valueOf(name);
        }
    }

    private enum TargetMode implements IEnumSetting {
        DISTANCE("Distance"),
        HEALTH("Health");

        private final String name;

        TargetMode(String name) {
            this.name = name;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public Enum<?> getEnum(String name) {
            return TargetMode.valueOf(name);
        }
    }

    private enum RotationMode implements IEnumSetting {
        NONE("None"),
        STATIC("Static"),
        SMOOTH("Smooth");

        private final String name;

        RotationMode(String name) {
            this.name = name;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public Enum<?> getEnum(String name) {
            return RotationMode.valueOf(name);
        }
    }
}
