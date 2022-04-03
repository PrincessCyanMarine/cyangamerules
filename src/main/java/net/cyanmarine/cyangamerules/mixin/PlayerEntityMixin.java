package net.cyanmarine.cyangamerules.mixin;

import net.cyanmarine.cyangamerules.CyanGamerules;
import net.cyanmarine.cyangamerules.util.Timer;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.FallingBlockEntity;
import net.minecraft.entity.TntEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.property.Properties;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Random;

import static net.cyanmarine.cyangamerules.CyanGamerules.*;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin {
    private BlockPos lastPos = null;
    private PlayerEntity player = (PlayerEntity) (Object) this;
    private Timer random_item_timer = new Timer("RandomItemTimer");
    private Timer tnt_spawn_timer = new Timer("TntSpawnTimer");
    private Timer random_mob_spawn_timer = new Timer("RandomMobSpawnTimer");

    @Inject(method = "tick()V", at = @At("TAIL"))
    void tick(CallbackInfo ci) {
        World world = player.getWorld();
        if (!world.isClient() && !player.isSpectator()) {
            if (world.getGameRules().getBoolean(CyanGamerules.BLOCK_RAIN))
                blockRain(world);

            if (world.getGameRules().getBoolean(CyanGamerules.MAGICAL_STEPS))
                magicalSteps(world);

            if (world.getGameRules().getBoolean(CyanGamerules.RANDOM_ITEM)) {
                player.sendMessage(Text.of((world.getGameRules().getInt(CyanGamerules.RANDOM_ITEM_INTERVAL) - random_item_timer.getValue())/20  + 1 + ""), true);
                if (random_item_timer.increment(world.getGameRules().getInt(CyanGamerules.RANDOM_ITEM_INTERVAL))) {
                    giveRandomItem(world.getRandom());
                }
            }

            if (world.getGameRules().getBoolean(CyanGamerules.SPAWN_TNT)) {
                if (tnt_spawn_timer.increment(world.getGameRules().getInt(CyanGamerules.TNT_SPAWN_INTERVAL))) {
                    TntEntity tnt = new TntEntity(world, player.getX(), player.getY(), player.getZ(), null);
                    if (world.getGameRules().getBoolean(CyanGamerules.SPAWNED_TNT_RANDOM_FUSE))
                        tnt.setFuse(world.getRandom().nextInt(160) + 1);
                    world.spawnEntity(tnt);
                }
            }

            if (world.getGameRules().getBoolean(CyanGamerules.SPAWN_MOB)) {
                if (random_mob_spawn_timer.increment(world.getGameRules().getInt(CyanGamerules.SPAWN_MOB_INTERVAL))) {
                    Entity entity;
                    do {
                        entity = Registry.ENTITY_TYPE.getRandom(world.getRandom()).get().value().create(world);
                    } while (!(entity instanceof HostileEntity));
                    Vec3d pos = player.getPos();
                    entity.setPos(pos.getX(), pos.getY(), pos.getZ());
                    ((HostileEntity) entity).setAttacking(player);
                    world.spawnEntity(entity);
                }
            }
        }
    }

    @Inject(method = "readCustomDataFromNbt(Lnet/minecraft/nbt/NbtCompound;)V", at = @At("TAIL"))
    void readNBT(NbtCompound nbt, CallbackInfo ci) {
        this.random_item_timer.setValue(nbt.getInt(this.random_item_timer.getKey()));
        this.tnt_spawn_timer.setValue(nbt.getInt(this.tnt_spawn_timer.getKey()));
        this.random_mob_spawn_timer.setValue(nbt.getInt(this.random_mob_spawn_timer.getKey()));
    }

    @Inject(method = "writeCustomDataToNbt(Lnet/minecraft/nbt/NbtCompound;)V", at = @At("TAIL"))
    void writeNBT(NbtCompound nbt, CallbackInfo ci) {
        nbt.putInt(this.random_item_timer.getKey(), this.random_item_timer.getValue());
        nbt.putInt(this.tnt_spawn_timer.getKey(), this.tnt_spawn_timer.getValue());
        nbt.putInt(this.random_mob_spawn_timer.getKey(), this.random_mob_spawn_timer.getValue());
    }

    void blockRain(World world) {
        int radius = world.getGameRules().getInt(CyanGamerules.RAIN_RADIUS);
        Random random = world.getRandom();
        int frequency = world.getGameRules().getInt(CyanGamerules.RAIN_FREQUENCY);
        for (int r = 0; r < frequency / 100 ||  r == 0 && frequency < 100; r++) {
            int chance = frequency - (100 * r);
            if (world.getRandom().nextInt(100) < chance) {
                BlockPos blockPos;
                int i = 0;
                do {
                    blockPos = player.getBlockPos().add(random.nextInt(radius * 2) - radius, 30 + random.nextInt(radius * 2), random.nextInt(radius * 2) - radius);
                } while (!world.getBlockState(blockPos).isAir() && i++ < 30);

                BlockState state = randomBlockState(random);

                FallingBlockEntity fallingBlock = FallingBlockEntity.spawnFromBlock(world, blockPos, state);
                fallingBlock.timeFalling = 1;
                fallingBlock.setHurtEntities(2.0F, 40); // Same as anvil
                fallingBlock.dropItem = world.getGameRules().getBoolean(CyanGamerules.BLOCK_RAIN_DROP_ON_BREAK);
            }
        }
    }

    void magicalSteps(World world) {
        BlockPos blockPos = player.getBlockPos();

        if (world.getBlockState(blockPos).isAir() && !world.getBlockState(blockPos.down()).isAir())
            blockPos = blockPos.down();

        if (world.getBlockState(blockPos).isAir()) {
            if (!world.getGameRules().getBoolean(CyanGamerules.MAGICAL_STEPS_AFTER) && lastPos != null)
                lastPos = null;
        } else if (!blockPos.equals(lastPos)) {
            BlockState state = randomBlockState(world.getRandom());
            if (world.getGameRules().getBoolean(CyanGamerules.MAGICAL_STEPS_AFTER)) {
                if (lastPos != null)
                    world.setBlockState(lastPos, state);
            } else
                world.setBlockState(blockPos, state);
            lastPos = blockPos;
        }
    }

    void giveRandomItem(Random random) {
        player.getInventory().offerOrDrop(randomItemStack(random));
        player.playSound(SoundEvents.BLOCK_NOTE_BLOCK_PLING, SoundCategory.AMBIENT, .8f, player.getSoundPitch());
    }
}
