package dev.obscuria.archogenum.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import dev.obscuria.archogenum.registry.ArchoAttributes;
import dev.obscuria.archogenum.world.genetics.Xenotype;
import dev.obscuria.archogenum.world.genetics.XenotypeSerializer;
import dev.obscuria.archogenum.world.genetics.resource.ILivingExtension;
import dev.obscuria.archogenum.world.genetics.resource.XenotypeHandler;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.storage.loot.LootParams;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
@SuppressWarnings("WrongEntityDataParameterClass")
public abstract class MixinLivingEntity extends Entity implements ILivingExtension {

    @Unique private static final EntityDataAccessor<Xenotype> archo$XENOTYPE;

    @Shadow public abstract long getLootTableSeed();

    private MixinLivingEntity(EntityType<?> type, Level level) {
        super(type, level);
    }

    @Override
    public Xenotype archo$getXenotype() {
        return this.entityData.get(archo$XENOTYPE);
    }

    @Override
    public void archo$setXenotype(Xenotype xenotype) {
        this.entityData.set(archo$XENOTYPE, xenotype);
    }

    @Inject(method = "defineSynchedData", at = @At("TAIL"))
    private void onDefineSyncedData(CallbackInfo info) {
        this.entityData.define(archo$XENOTYPE, Xenotype.EMPTY);
    }

    @Inject(method = "baseTick", at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/LivingEntity;tickEffects()V"))
    private void onTick(CallbackInfo info) {
        final var self = (LivingEntity) (Object) this;
        XenotypeHandler.tick(self);
    }

    @ModifyVariable(method = "hurt(Lnet/minecraft/world/damagesource/DamageSource;F)Z", at = @At(
            value = "FIELD",
            target = "Lnet/minecraft/world/entity/LivingEntity;noActionTime:I"),
            index = 2, argsOnly = true)
    private float modifyHurtAmount(float amount, @Local(argsOnly = true) DamageSource source) {
        final var self = (LivingEntity) (Object) this;
        return XenotypeHandler.modifyHurtAmount(self, source, amount);
    }

    @Inject(method = "dropFromLootTable", at = @At("TAIL"))
    private void onLootDrop(DamageSource source, boolean hitByPlayer, CallbackInfo info, @Local LootParams params) {
        final var self = (LivingEntity) (Object) this;
        XenotypeHandler.dropLoot(self, source, params, this.getLootTableSeed(), this::spawnAtLocation);
    }

    @Inject(method = "canStandOnFluid", at = @At("RETURN"), cancellable = true)
    private void modifyCanStandOnFluid(FluidState state, CallbackInfoReturnable<Boolean> info) {
        if (info.getReturnValue()) return;
        final var self = (LivingEntity) (Object) this;
        if (!XenotypeHandler.canStandOnFluid(self, state)) return;
        info.setReturnValue(true);
    }

    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    private void onSave(CompoundTag compound, CallbackInfo info) {
        final var self = (LivingEntity) (Object) this;
        XenotypeHandler.save(self, compound);
    }

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    private void onLoad(CompoundTag compound, CallbackInfo info) {
        final var self = (LivingEntity) (Object) this;
        XenotypeHandler.load(self, compound);
    }

    @Inject(method = "createLivingAttributes", at = @At("RETURN"))
    private static void createCustomAttributes(CallbackInfoReturnable<AttributeSupplier.Builder> info) {
        ArchoAttributes.createLivingAttributes(info.getReturnValue());
    }

    static {
        archo$XENOTYPE = SynchedEntityData.defineId(LivingEntity.class, XenotypeSerializer.INSTANCE);
    }
}