package sh.squeami.kami.utils.player;

import net.minecraft.client.Minecraft;

public class MovementUtil {

    public static void setSpeed(double moveSpeed) {
        setSpeed(moveSpeed, Minecraft.getMinecraft().thePlayer.rotationYaw, Minecraft.getMinecraft().thePlayer.moveStrafing, Minecraft.getMinecraft().thePlayer.moveForward);
    }

    public static void setSpeed(double moveSpeed, float yaw, double strafe, double forward) {
        if (forward != 0.0D) {
            yaw += (strafe != 0.0D) ? ((forward > 0.0D) ? -45 : 45) : 0;
            strafe = 0.0D;
            forward = (forward > 0.0D) ? 1.0D : -1.0D;
        }

        strafe = (strafe > 0.0D) ? 1.0D : (strafe < 0.0D) ? -1.0D : 0.0D;

        double radians = Math.toRadians(yaw + 90.0F);
        double motionX = Math.cos(radians);
        double motionZ = Math.sin(radians);

        Minecraft.getMinecraft().thePlayer.motionX = forward * moveSpeed * motionX + strafe * moveSpeed * motionZ;
        Minecraft.getMinecraft().thePlayer.motionZ = forward * moveSpeed * motionZ - strafe * moveSpeed * motionX;
    }
}
