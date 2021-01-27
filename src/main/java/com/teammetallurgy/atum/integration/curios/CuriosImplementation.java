package com.teammetallurgy.atum.integration.curios;

import com.teammetallurgy.atum.Atum;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import top.theillusivec4.curios.api.CuriosCapability;
import top.theillusivec4.curios.api.type.capability.ICurio;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@Mod.EventBusSubscriber(modid = Atum.MOD_ID)
public class CuriosImplementation { //TODO Remove in 1.17

    //Curios capability registering
    @SubscribeEvent
    public static void attachStackCapabilities(AttachCapabilitiesEvent<ItemStack> event) {
        ItemStack stack = event.getObject();

        if (stack.getItem() instanceof ISimpleCurioItem) {
            ISimpleCurioItem item = (ISimpleCurioItem) stack.getItem();
            if (item.hasCurioCapability(stack)) {
                SimpleCurioItem simpleCurioItem = new SimpleCurioItem(item, stack);
                event.addCapability(CuriosCapability.ID_ITEM, CuriosImplementation.createProvider(simpleCurioItem));
            }
        }
    }

    public static ICapabilityProvider createProvider(final ICurio curio) {
        return new Provider(curio);
    }

    public static class Provider implements ICapabilityProvider {
        final LazyOptional<ICurio> capability;

        Provider(ICurio curio) {
            this.capability = LazyOptional.of(() -> curio);
        }

        @Override
        @Nonnull
        public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction direction) {
            return CuriosCapability.ITEM.orEmpty(cap, this.capability);
        }
    }
}