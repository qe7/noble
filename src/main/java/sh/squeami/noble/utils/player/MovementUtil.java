package sh.squeami.noble.utils.player;

import net.minecraft.client.Minecraft;

public class MovementUtil {

    /**
     * Set the player's speed.
     *
     * @param moveSpeed The speed to set.
     */
    public static void setSpeed(double moveSpeed) {
        setSpeed(moveSpeed, Minecraft.getMinecraft().thePlayer.rotationYaw, Minecraft.getMinecraft().thePlayer.moveStrafing, Minecraft.getMinecraft().thePlayer.moveForward);
    }

    /**
     * Set the player's speed.
     *
     * @param moveSpeed The speed to set.
     * @param yaw       The yaw to set.
     * @param strafe    The strafe to set.
     * @param forward   The forward to set.
     */
    public static void setSpeed(double moveSpeed, float yaw, double strafe, double forward) {
        // If the player is moving forward or backward
        if (forward != 0.0D) {
            // Adjust the yaw based on the direction of strafe and forward
            yaw += (strafe > 0.0D) ? ((forward > 0.0D) ? -45 : 45) : (strafe < 0.0D) ? ((forward > 0.0D) ? 45 : -45) : 0;
            // Reset strafe to 0 as we have already adjusted the yaw
            strafe = 0.0D;
            // Normalize forward to 1 or -1 based on its direction
            forward = (forward > 0.0D) ? 1.0D : -1.0D;
        }

        // Normalize strafe to 1 or -1 based on its direction, or 0 if it's not moving
        strafe = (strafe > 0.0D) ? 1.0D : (strafe < 0.0D) ? -1.0D : 0.0D;

        // Convert the yaw to radians and offset it by 90 degrees
        double radians = Math.toRadians(yaw + 90.0F);
        // Calculate the motion vector based on the yaw
        double motionX = Math.cos(radians);
        double motionZ = Math.sin(radians);

        // Set the player's motion in the X and Z directions based on the calculated motion vector, speed, and direction of strafe and forward
        Minecraft.getMinecraft().thePlayer.motionX = forward * moveSpeed * motionX + strafe * moveSpeed * motionZ;
        Minecraft.getMinecraft().thePlayer.motionZ = forward * moveSpeed * motionZ - strafe * moveSpeed * motionX;
    }
}
