package space.ulitka.bettertetrismc.config;

import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import me.shedaniel.clothconfig2.gui.entries.BooleanListEntry;
import me.shedaniel.clothconfig2.gui.entries.IntegerListEntry;
import me.shedaniel.clothconfig2.gui.entries.DoubleListEntry;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import space.ulitka.bettertetrismc.BetterTetrisMC;

public class ConfigScreen {

    public static Screen getScreen(Screen parent) {
        TetrisConfig config = TetrisConfig.loadConfig();

        ConfigBuilder builder = ConfigBuilder.create()
                .setParentScreen(parent)
                .setTitle(Text.translatable(BetterTetrisMC.MOD_ID + ":config.title"));

        ConfigCategory general = builder.getOrCreateCategory(Text.translatable(BetterTetrisMC.MOD_ID + ":config.general"));

        ConfigEntryBuilder entryBuilder = builder.entryBuilder();

        BooleanListEntry modEnabledEntry = entryBuilder
                .startBooleanToggle(Text.translatable(BetterTetrisMC.MOD_ID + ":config.mod"), config.mod_enabled)
                .setSaveConsumer(newValue -> {
                    config.mod_enabled = newValue;
                    TetrisConfig.saveConfig();
                })
                .build();
        general.addEntry(modEnabledEntry);

        IntegerListEntry hardDropEntry = entryBuilder
                .startIntField(Text.translatable(BetterTetrisMC.MOD_ID + ":tetris.hard_drop"), config.tetris_hard_drop)
                .setDefaultValue(0)
                .setSaveConsumer(newValue -> {
                    if (newValue < 0) newValue = 0;
                    if (newValue > 3) newValue = 3;
                    config.tetris_hard_drop = newValue;
                    TetrisConfig.saveConfig();
                })
                .setTooltip(Text.translatable(BetterTetrisMC.MOD_ID + ":tetris.hard_drop.tooltip"))
                .build();
        general.addEntry(hardDropEntry);

        BooleanListEntry randomTexturesEntry = entryBuilder
                .startBooleanToggle(Text.translatable(BetterTetrisMC.MOD_ID + ":tetris.random_texture"), config.tetris_random_textures)
                .setSaveConsumer(newValue -> {
                    config.tetris_random_textures = newValue;
                    TetrisConfig.saveConfig();
                })
                .build();
        general.addEntry(randomTexturesEntry);

        DoubleListEntry volumeEntry = entryBuilder
                .startDoubleField(Text.translatable(BetterTetrisMC.MOD_ID + ":config.volume"), config.tetris_volume)
                .setDefaultValue(1.0)
                .setSaveConsumer(newValue -> {
                    config.tetris_volume = newValue.floatValue();
                    TetrisConfig.saveConfig();
                })
                .setMin(0.0)
                .setMax(1.0)
                .build();
        general.addEntry(volumeEntry);

        return builder.build();
    }
}
