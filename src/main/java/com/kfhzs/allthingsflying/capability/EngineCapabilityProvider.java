package com.kfhzs.allthingsflying.capability;

import com.kfhzs.allthingsflying.AllThingsFlying;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class EngineCapabilityProvider implements ICapabilitySerializable<CompoundTag> {

    public static final ResourceLocation ENGINE_CAPABILITY_ID = ResourceLocation.tryBuild(AllThingsFlying.MODID,"engine_capability");

    private final IEngineCapability capability;
    private final LazyOptional<IEngineCapability> optional;

    public EngineCapabilityProvider() {
        this.capability = new EngineCapabilityImpl();
        this.optional = LazyOptional.of(() -> capability);
    }

    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == ModCapabilities.ENGINE_CAPABILITY) {
            return optional.cast();
        }
        return LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT() {
        return capability.serializeNBT();
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        capability.deserializeNBT(nbt);
    }

    public void invalidate() {
        optional.invalidate();
    }
}