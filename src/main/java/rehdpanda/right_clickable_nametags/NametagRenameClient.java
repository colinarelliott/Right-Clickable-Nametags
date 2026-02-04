package rehdpanda.right_clickable_nametags;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.AnvilScreen;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.AnvilScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.text.Text;
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
            
            PlayerInventory playerInventory = client.player.getInventory();
            SimpleInventory inventory = new SimpleInventory(3);
            inventory.setStack(0, stack.copy());

            AnvilScreenHandler handler = new AnvilScreenHandler(0, playerInventory, ScreenHandlerContext.EMPTY) {
                @Override
                public boolean canUse(net.minecraft.entity.player.PlayerEntity player) {
                    return true;
                }

                @Override
                public void updateResult() {
                    // Stop it from clearing the result slot
                }
                
                @Override
                public ItemStack quickMove(net.minecraft.entity.player.PlayerEntity player, int slot) {
                    return ItemStack.EMPTY;
                }
            };
            
            handler.getSlot(0).setStack(stack.copy());

            AnvilScreen screen = new AnvilScreen(handler, playerInventory, Text.translatable("container.repair")) {
                @Override
                public void removed() {
                    super.removed();
                    NametagRenameClient.setNametagRename(false);
                }
            };
            client.setScreen(screen);
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
