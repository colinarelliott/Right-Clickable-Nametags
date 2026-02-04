package rehdpanda.right_clickable_nametags;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.client.input.KeyInput;

public class NametagRenameScreen extends Screen {
    private final ItemStack nametag;
    private TextFieldWidget nameField;
    private final int backgroundWidth = 176;
    private final int backgroundHeight = 90;

    protected NametagRenameScreen(ItemStack nametag) {
        super(Text.literal("Rename Nametag:"));
        this.nametag = nametag;
    }

    @Override
    protected void init() {
        int x = (this.width - this.backgroundWidth) / 2;
        int y = (this.height - this.backgroundHeight) / 2;

        this.nameField = new TextFieldWidget(this.textRenderer, x + 10, y + 30, 156, 20, Text.literal("Name"));
        this.nameField.setMaxLength(50);
        this.nameField.setEditableColor(-1);
        this.nameField.setUneditableColor(-1);
        this.nameField.setDrawsBackground(true);
        this.nameField.setFocusUnlocked(false);
        this.nameField.setFocused(true);
        
        String currentName = nametag.get(DataComponentTypes.CUSTOM_NAME) != null ? nametag.get(DataComponentTypes.CUSTOM_NAME).getString() : "";
        this.nameField.setText(currentName);
        this.nameField.setSelectionStart(0);
        this.nameField.setSelectionEnd(this.nameField.getText().length());
        
        this.addSelectableChild(this.nameField);
        this.setInitialFocus(this.nameField);
        this.setFocused(this.nameField);

        this.addDrawableChild(ButtonWidget.builder(Text.translatable("gui.done"), button -> {
            confirm();
        }).dimensions(x + 10, y + 60, 75, 20).build());

        this.addDrawableChild(ButtonWidget.builder(Text.translatable("gui.cancel"), button -> {
            this.close();
        }).dimensions(x + 91, y + 60, 75, 20).build());
    }

    private void confirm() {
        NametagRenameClient.sendRenamePacket(this.nameField.getText());
        this.close();
    }

    @Override
    public void renderBackground(DrawContext context, int mouseX, int mouseY, float delta) {
        super.renderBackground(context, mouseX, mouseY, delta);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        int x = (this.width - this.backgroundWidth) / 2;
        int y = (this.height - this.backgroundHeight) / 2;
        
        super.render(context, mouseX, mouseY, delta);

        context.drawText(this.textRenderer, this.title, x + 10, y + 10, 0xFFFFFFFF, false);
        this.nameField.render(context, mouseX, mouseY, delta);
    }

    @Override
    public boolean keyPressed(KeyInput input) {
        int keyCode = input.key();
        if (keyCode == 257 || keyCode == 335) { // Enter
            confirm();
            return true;
        }
        return super.keyPressed(input);
    }

    @Override
    public void close() {
        super.close();
        NametagRenameClient.setNametagRename(false);
    }

    @Override
    public boolean shouldPause() {
        return false;
    }
}
