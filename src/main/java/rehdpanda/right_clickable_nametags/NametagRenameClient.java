package rehdpanda.right_clickable_nametags;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;

public class NametagRenameClient implements ClientModInitializer {

    /**
     * Initializes the client side of the mod.
     */
    @Override
    public void onInitializeClient() {
    }

    /**
     * Opens the nametag rename screen.
     */
    public static void openRenameScreen(ItemStack stack, Hand hand) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client != null && client.player != null) {
            client.setScreen(new NametagRenameScreen(stack.copy()));
        }
    }

    /**
     * Sends a rename packet to the server.
     */
    public static void sendRenamePacket(String name) {
        ClientPlayNetworking.send(new NametagRename.RenamePayload(name));
    }
}
