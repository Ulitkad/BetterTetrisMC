package space.ulitka.bettertetrismc.mixin;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.widget.TextIconButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import space.ulitka.bettertetrismc.BetterTetrisMC;
import space.ulitka.bettertetrismc.config.TetrisConfig;
import space.ulitka.bettertetrismc.screen.TetrisScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(TitleScreen.class)
public abstract class TitleScreenMixin extends Screen {
    protected TitleScreenMixin(Text title) {
        super(title);
    }

    @Inject(at = @At("RETURN"), method = "addNormalWidgets")
    private void addMinigameButton(int y, int spacingY, CallbackInfoReturnable<Integer> cir) {
        TextIconButtonWidget textIconButtonWidget = TextIconButtonWidget.builder(Text.empty(), (button) -> this.client.setScreen(new TetrisScreen(this)), true).width(20).texture(Identifier.of(BetterTetrisMC.MOD_ID, "icon/button"), 16, 16).build();
        textIconButtonWidget.setPosition(this.width / 2 - 100 + 204, y);
        if (TetrisConfig.loadConfig().mod_enabled) this.addDrawableChild(textIconButtonWidget);
    }
}
