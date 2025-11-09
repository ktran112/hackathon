package hackathon.qolmods.mixin;

import hackathon.qolmods.ui.InventorySorter;
import hackathon.qolmods.ui.SortInventoryC2SPacket;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.mixin.screen.ScreenAccessor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(InventoryScreen.class)
public abstract class InventoryScreenMixin extends HandledScreen<PlayerScreenHandler> {
    private boolean wasMousePressed = false;

    public InventoryScreenMixin(PlayerScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
    }

    @Inject(method = "render", at = @At("HEAD"))
    private void onRender(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        if (GLFW.glfwGetMouseButton(MinecraftClient.getInstance().getWindow().getHandle(), GLFW.GLFW_MOUSE_BUTTON_LEFT) == GLFW.GLFW_PRESS) {
            MinecraftClient.getInstance().execute(() -> {
                this.init(MinecraftClient.getInstance(), this.width, this.height);
            });
        }
    }

    @Inject(method = "init", at = @At("TAIL"))
    private void addSortButton(CallbackInfo ci) {
        int buttonX = this.x + this.width/3;
        int buttonY = this.y + 60;

        this.addDrawableChild(ButtonWidget.builder(
                Text.literal("Sort"),
                button -> {
                    ClientPlayNetworking.send(new SortInventoryC2SPacket());
                    MinecraftClient.getInstance().execute(() -> {
                        this.init(MinecraftClient.getInstance(), this.width, this.height);
                    });
                }
        ).dimensions(buttonX, buttonY, 25, 15).build());

    }
}
