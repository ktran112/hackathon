package hackathon.qolmods.mixin;

import hackathon.qolmods.ui.InventorySorter;
import hackathon.qolmods.ui.SortInventoryC2SPacket;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.mixin.screen.ScreenAccessor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(InventoryScreen.class)
public abstract class InventoryScreenMixin extends HandledScreen<PlayerScreenHandler> {

    public InventoryScreenMixin(PlayerScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
    }

    @Inject(method = "init", at = @At("TAIL"))
    private void addSortButton(CallbackInfo ci) {
        int buttonX = this.x + 140;
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
