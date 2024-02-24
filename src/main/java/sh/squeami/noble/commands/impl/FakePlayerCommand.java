package sh.squeami.noble.commands.impl;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import sh.squeami.noble.commands.api.types.Command;

import java.util.HashMap;

public class FakePlayerCommand extends Command {

    private final HashMap<Integer, EntityOtherPlayerMP> fakePlayers = new HashMap<>();

    public FakePlayerCommand() {
        super("fakeplayer",
                "Manage fake-players within the world via a command.",
                "fakeplayer add [id (optional)] / fakeplayer remove [id]",
                "fp");
    }

    @Override
    public void execute(String[] args) {
        if (args.length != 2 && args.length != 3) {
            sendChatMessage("Usage: " + getUsage());
            return;
        }

        if (args[1].equalsIgnoreCase("add")) {
            handleAdd(args);
        } else if (args[1].equalsIgnoreCase("remove")) {
            handleRemove(args);
        }
    }

    private void handleAdd(String[] args) {
        Minecraft minecraft = Minecraft.getMinecraft();

        // create the fake player
        EntityOtherPlayerMP fakePlayer = new EntityOtherPlayerMP(
                minecraft.theWorld,
                new GameProfile(null, "FakePlayer")
        );

        // set the fake player's name
        fakePlayer.setCustomNameTag("FakePlayer");

        final int id;

        if (args.length == 2) {
            System.out.println("No id provided, using -100 as default.");
            id = -100;
        } else {
            id = Integer.parseInt(args[2]);
        }

        // add the fake player to the world
        minecraft.theWorld.addEntityToWorld(id, fakePlayer);

        // set the fake player's position
        fakePlayer.setPosition(minecraft.thePlayer.posX, minecraft.thePlayer.posY, minecraft.thePlayer.posZ);

        // set the fake player's rotation
        fakePlayer.rotationYaw = minecraft.thePlayer.rotationYaw;
        fakePlayer.rotationPitch = minecraft.thePlayer.rotationPitch;

        // set the fake player's health
        fakePlayer.setHealth(20);

        fakePlayers.put(id, fakePlayer);

        sendChatMessage("Added fake player with the id \"" + id + "\".");
    }

    private void handleRemove(String[] args) {
        Minecraft minecraft = Minecraft.getMinecraft();

        // remove the fake player from the world
        if (fakePlayers.containsKey(Integer.parseInt(args[2]))) {
            minecraft.theWorld.removeEntityFromWorld(Integer.parseInt(args[2]));
            fakePlayers.remove(Integer.parseInt(args[2]));
            sendChatMessage("Removed fake player with the id \"" + args[2] + "\".");
        } else {
            sendChatMessage("No fake player with the id \"" + args[2] + "\" found.");
        }
    }
}
