package rehdpanda.right_clickable_nametags;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.ActionResult;
import net.minecraft.world.World;

public class NametagRename implements ModInitializer {
    public static final Identifier RENAME_PACKET_ID = Identifier.of("nametag-rename", "rename");

    /**
     * Payload for renaming a nametag.
     */
    public record RenamePayload(String name) implements CustomPayload {
        public static final Id<RenamePayload> ID = new Id<>(RENAME_PACKET_ID);
        public static final PacketCodec<RegistryByteBuf, RenamePayload> CODEC = PacketCodec.tuple(PacketCodecs.STRING, RenamePayload::name, RenamePayload::new);

        /**
         * Returns the ID of the payload.
         */
        @Override
        public Id<? extends CustomPayload> getId() {
            return ID;
        }
    }

    /**
     * Initializes the mod, registers network packets and item use events.
     */
    @Override
    public void onInitialize() {
        PayloadTypeRegistry.playC2S().register(RenamePayload.ID, RenamePayload.CODEC);

        ServerPlayNetworking.registerGlobalReceiver(RenamePayload.ID, (payload, context) -> {
            context.server().execute(() -> {
                PlayerEntity player = context.player();
                ItemStack stack = player.getMainHandStack();
                if (!stack.isOf(Items.NAME_TAG)) {
                    stack = player.getOffHandStack();
                }

                if (stack.isOf(Items.NAME_TAG)) {
                    String newName = payload.name();
                    if (newName.isEmpty()) {
                        stack.remove(DataComponentTypes.CUSTOM_NAME);
                        stack.remove(DataComponentTypes.ENCHANTMENT_GLINT_OVERRIDE);
                    } else {
                        stack.set(DataComponentTypes.CUSTOM_NAME, net.minecraft.text.Text.literal(newName));
                        stack.set(DataComponentTypes.ENCHANTMENT_GLINT_OVERRIDE, true);
                    }
                }
            });
        });

        UseItemCallback.EVENT.register((PlayerEntity player, World world, Hand hand) -> {
            ItemStack stack = player.getStackInHand(hand);

            if (stack.isOf(Items.NAME_TAG)) {
                if (world.isClient()) {
                    NametagRenameClient.openRenameScreen(stack, hand);
                }
                return ActionResult.SUCCESS;
            }

            return ActionResult.PASS;
        });
    }
}
