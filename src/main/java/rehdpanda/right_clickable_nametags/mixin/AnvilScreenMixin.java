package rehdpanda.right_clickable_nametags.mixin;

import net.minecraft.client.gui.screen.ingame.AnvilScreen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.input.KeyInput;
import rehdpanda.right_clickable_nametags.NametagRenameClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AnvilScreen.class)
public class AnvilScreenMixin {
    @Shadow private TextFieldWidget nameField;

    @Inject(method = "onRenamed", at = @At("HEAD"), cancellable = true)
    private void onRenamed(String name, CallbackInfo ci) {
        if (NametagRenameClient.isNametagRename()) {
            // When renamed in our virtual anvil, we send the packet and close
            NametagRenameClient.sendRenamePacket(name);
            // We might want to close the screen here or wait for a button
        }
    }

    @Inject(method = "keyPressed", at = @At("HEAD"), cancellable = true)
    private void onKeyPressed(KeyInput input, CallbackInfoReturnable<Boolean> cir) {
        if (NametagRenameClient.isNametagRename()) {
            int keyCode = input.key();
            if (keyCode == 257 || keyCode == 335) { // Enter keys
                NametagRenameClient.sendRenamePacket(nameField.getText());
                ((AnvilScreen)(Object)this).close();
                cir.setReturnValue(true);
            }
        }
    }
}
