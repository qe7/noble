package sh.squeami.noble.utils.player;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;

/**
 * A utility class for calculating and applying rotations
 *
 * @author Shae
 */
public class RotationUtil {

    /**
     * Apply the game's mouse sensitivity to the yaw and pitch angles
     *
     * @param rotationVector The yaw and pitch angles to apply the game's mouse sensitivity to
     * @return A Vector containing the yaw and pitch angles with the game's mouse sensitivity applied
     */
    public static float[] applyGCD(final float[] rotationVector) {
        final float[] previousRotation = Minecraft.getMinecraft().thePlayer.getPreviousRotation();
        final float mouseSensitivity = Minecraft.getMinecraft().gameSettings.mouseSensitivity * 0.6F + 0.2F;
        final double multiplier = mouseSensitivity * mouseSensitivity * mouseSensitivity * 8.0F * 0.15D;

        final float yaw = previousRotation[0] + (float) (Math.round((rotationVector[0] - previousRotation[0]) / multiplier) * multiplier);
        final float pitch = MathHelper.clamp_float(previousRotation[1] + (float) (Math.round((rotationVector[1] - previousRotation[1]) / multiplier) * multiplier), -90, 90);

        return new float[]{yaw, pitch};
    }

    /**
     * Apply sanity checks to the yaw and pitch angles
     *
     * @param rotationVector The yaw and pitch angles to apply sanity checks to
     * @return A Vector containing the yaw and pitch angles with sanity checks applied
     */
    public static float[] applySanity(final float[] rotationVector) {
        final float yaw = rotationVector[0];
        final float pitch = MathHelper.clamp_float(rotationVector[1], -90, 90);

        return new float[]{yaw, pitch};
    }

    /**
     * Calculate the yaw and pitch angles required to look at an entity
     *
     * @param entity The entity to look at
     * @return A Float array containing the yaw and pitch angles required to look at the entity
     */
    public static float[] getRotations(final Entity entity) {
        // Calculate the difference in the x, y, and z coordinates between the player and the entity
        final double deltaX = entity.posX - Minecraft.getMinecraft().thePlayer.posX;
        final double deltaY = entity.posY + (entity.getEyeHeight() * 0.8) - (Minecraft.getMinecraft().thePlayer.posY + Minecraft.getMinecraft().thePlayer.getEyeHeight());
        final double deltaZ = entity.posZ - Minecraft.getMinecraft().thePlayer.posZ;

        // Calculate the Euclidean distance in the xz-plane between the player and the entity
        final double distance = Math.sqrt(deltaX*deltaX + deltaZ*deltaZ);

        // Calculate the yaw angle in spherical coordinates
        float yaw = (float) Math.toDegrees(-Math.atan(deltaX / deltaZ));
        // Calculate the pitch angle in spherical coordinates
        final float pitch = (float) -Math.toDegrees(Math.atan(deltaY / distance));

        // Calculate the adjustment angle based on the quadrant of the entity relative to the player
        final double quadrantAdjustment = Math.toDegrees(Math.atan(deltaZ / deltaX));
        // Adjust the yaw angle based on the quadrant of the entity relative to the player
        if (deltaX < 0 && deltaZ < 0) {
            yaw = (float) (90 + quadrantAdjustment);
        } else if (deltaX > 0 && deltaZ < 0) {
            yaw = (float) (-90 + quadrantAdjustment);
        }

        // Return the calculated yaw and pitch angles
        return new float[]{yaw, pitch};
    }

    /**
     * Calculate the yaw and pitch angles required to look at a set of coordinates
     *
     * @param lastRotation The last set of yaw and pitch angles
     * @param targetRotation The target set of yaw and pitch angles
     * @param speed The speed at which to rotate
     * @return A Vector containing the yaw and pitch angles required to look at the set of coordinates
     */
    public static float[] getSmoothRotations(final float[] lastRotation, final float[] targetRotation, double speed) {
        // If speed is zero, return the last rotation as there's no need for any calculations
        if (speed == 0) {
            return lastRotation;
        }

        // Calculate the difference in yaw and pitch between the target and last rotation
        double deltaYaw = MathHelper.wrapAngleTo180_float(targetRotation[0] - lastRotation[0]);
        double deltaPitch = targetRotation[1] - lastRotation[1];

        // Calculate the Euclidean distance between the target and last rotation
        double distance = Math.hypot(deltaYaw, deltaPitch);
        // Calculate the distribution of yaw and pitch in the total distance
        double distributionYaw = Math.abs(deltaYaw / distance);
        double distributionPitch = Math.abs(deltaPitch / distance);

        // Calculate the maximum change in yaw and pitch based on the speed and their respective distributions
        double maxYaw = speed * distributionYaw;
        double maxPitch = speed * distributionPitch;

        // Calculate the actual change in yaw and pitch, ensuring they do not exceed their maximum values
        float moveYaw = (float) Math.max(Math.min(deltaYaw, maxYaw), -maxYaw);
        float movePitch = (float) Math.max(Math.min(deltaPitch, maxPitch), -maxPitch);

        // Calculate the new yaw and pitch by adding the calculated changes to the last rotation
        float yaw = lastRotation[0] + moveYaw;
        float pitch = lastRotation[1] + movePitch;

        // Return the new yaw and pitch as a Vector
        return new float[]{yaw, pitch};
    }
}
