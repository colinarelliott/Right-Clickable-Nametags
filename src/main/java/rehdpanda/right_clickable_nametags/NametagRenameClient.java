package rehdpanda.right_clickable_nametags;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;

public class NametagRenameClient implements ClientModInitializer {
    private static boolean isNametagRename = false;

    @Override
    public void onInitializeClient() {
    }

    public static void openRenameScreen(ItemStack stack, Hand hand) {
        NametagRename.LOGGER.info("Opening rename screen for nametag");
        MinecraftClient client = MinecraftClient.getInstance();
        if (client != null && client.player != null) {
            isNametagRename = true;
            client.setScreen(new NametagRenameScreen(stack.copy()));
        }
    }

    public static boolean isNametagRename() {
        return isNametagRename;
    }

    public static void setNametagRename(boolean value) {
        isNametagRename = value;
    }

    public static void sendRenamePacket(String name) {
        ClientPlayNetworking.send(new NametagRename.RenamePayload(name));
        isNametagRename = false;
    }
}
