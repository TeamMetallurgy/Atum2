package com.teammetallurgy.atum.items.artifacts.nepthys;

import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.api.God;
import com.teammetallurgy.atum.api.IArtifact;
import com.teammetallurgy.atum.init.AtumItems;
import com.teammetallurgy.atum.items.artifacts.RingItem;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotResult;

import java.util.Optional;

@Mod.EventBusSubscriber(modid = Atum.MOD_ID)
public class NepthysGuardItem extends RingItem implements IArtifact {

    @Override
    public God getGod() {
        return God.NEPTHYS;
    }

    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event) {
        Optional<SlotResult> optional = CuriosApi.getCuriosHelper().findFirstCurio(event.getEntity(), AtumItems.NEPTHYS_GUARD.get());
        if (optional.isPresent()) {
            Entity source = event.getSource().getDirectEntity();
            if (source instanceof LivingEntity && ((LivingEntity) source).getMobType() == MobType.UNDEAD) {
                event.setAmount(event.getAmount() * 0.5F);
            }
        }
    }
}