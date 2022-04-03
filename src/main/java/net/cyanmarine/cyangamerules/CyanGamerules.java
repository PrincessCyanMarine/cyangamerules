package net.cyanmarine.cyangamerules;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.state.property.Properties;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.GameRules;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;


public class CyanGamerules implements ModInitializer {
    public static Logger LOGGER = LoggerFactory.getLogger("Cyan's Gamerules");

    public static final GameRules.Key<GameRules.BooleanRule> BLOCK_RAIN = GameRuleRegistry.register("cyangamerules:doBlockRain", GameRules.Category.MISC, GameRuleFactory.createBooleanRule(false));
    public static final GameRules.Key<GameRules.BooleanRule> BLOCK_RAIN_DROP_ON_BREAK = GameRuleRegistry.register("cyangamerules:doBlockRainDropOnBreak", GameRules.Category.MISC, GameRuleFactory.createBooleanRule(false));
    public static final GameRules.Key<GameRules.IntRule> RAIN_FREQUENCY = GameRuleRegistry.register("cyangamerules:blockRainFrequency", GameRules.Category.MISC, GameRuleFactory.createIntRule(10, 1, 10000));
    public static final GameRules.Key<GameRules.IntRule> RAIN_RADIUS = GameRuleRegistry.register("cyangamerules:blockRainRadius", GameRules.Category.MISC, GameRuleFactory.createIntRule(5, 1, 50));

    public static final GameRules.Key<GameRules.BooleanRule> MAGICAL_STEPS = GameRuleRegistry.register("cyangamerules:doMagicalFeet", GameRules.Category.MISC, GameRuleFactory.createBooleanRule(false));
    public static final GameRules.Key<GameRules.BooleanRule> MAGICAL_STEPS_AFTER = GameRuleRegistry.register("cyangamerules:magicalFeetAfter", GameRules.Category.MISC, GameRuleFactory.createBooleanRule(false));

    public static final GameRules.Key<GameRules.BooleanRule> RANDOM_ITEM = GameRuleRegistry.register("cyangamerules:doGiveRandomItem", GameRules.Category.MISC, GameRuleFactory.createBooleanRule(false));
    public static final GameRules.Key<GameRules.IntRule> RANDOM_ITEM_INTERVAL = GameRuleRegistry.register("cyangamerules:randomItemInterval", GameRules.Category.MISC, GameRuleFactory.createIntRule(100, 1));

    public static final GameRules.Key<GameRules.BooleanRule> SPAWN_TNT = GameRuleRegistry.register("cyangamerules:doSpawnTnt", GameRules.Category.MISC, GameRuleFactory.createBooleanRule(false));
    public static final GameRules.Key<GameRules.BooleanRule> SPAWNED_TNT_RANDOM_FUSE = GameRuleRegistry.register("cyangamerules:spawnTntRandomFuse", GameRules.Category.MISC, GameRuleFactory.createBooleanRule(true));
    public static final GameRules.Key<GameRules.IntRule> TNT_SPAWN_INTERVAL = GameRuleRegistry.register("cyangamerules:spawnTntInterval", GameRules.Category.MISC, GameRuleFactory.createIntRule(200, 1));

    public static final GameRules.Key<GameRules.BooleanRule> SPAWN_MOB = GameRuleRegistry.register("cyangamerules:doSpawnMob", GameRules.Category.MISC, GameRuleFactory.createBooleanRule(false));
    public static final GameRules.Key<GameRules.IntRule> SPAWN_MOB_INTERVAL = GameRuleRegistry.register("cyangamerules:spawnMobInterval", GameRules.Category.MISC, GameRuleFactory.createIntRule(200, 1));

    @Override
    public void onInitialize() {
        LOGGER.info("Cyan's Gamerules is installed on this server :D");
    }

    public static Item randomItem(Random random) { return Registry.ITEM.getRandom(random).get().value(); }
    public static Block randomBlock(Random random) {
        return Registry.BLOCK.getRandom(random).get().value();
    }

    public static ItemStack randomItemStack(Random random) {
        return randomItem(random).getDefaultStack();
    }

    public static BlockState randomBlockState(Random random) {
        BlockState state;
        do {
            state = randomBlock(random).getDefaultState();
            if (state.contains(Properties.WATERLOGGED))
                state = state.with(Properties.WATERLOGGED, false);
        } while (
            state.isAir() ||
            !state.getFluidState().isEmpty()
        );
        return state;
    }
}
