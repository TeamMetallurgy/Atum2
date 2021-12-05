package com.teammetallurgy.atum.items.artifacts.nepthys;

import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.api.God;
import com.teammetallurgy.atum.api.IArtifact;
import com.teammetallurgy.atum.init.AtumItems;
import com.teammetallurgy.atum.items.artifacts.RingItem;
import net.minecraft.entity.CreatureAttribute;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.apache.commons.lang3.tuple.ImmutableTriple;
import top.theillusivec4.curios.api.CuriosApi;

import java.util.Optional;

@Mod.EventBusSubscriber(modid = Atum.MOD_ID)
public class NepthysGuardItem extends RingItem implements IArtifact {

    @Override
    public God getGod() {
        return God.NEPTHYS;
    }

    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event) {
        Optional<ImmutableTriple<String, Integer, ItemStack>> optional = CuriosApi.getCuriosHelper().findEquippedCurio(AtumItems.NEPTHYS_GUARD, event.getEntityLiving());
        if (optional.isPresent()) {
            Entity source = event.getSource().getImmediateSource();
            if (source instanceof LivingEntity && ((LivingEntity) source).getCreatureAttribute() == CreatureAttribute.UNDEAD) {
                event.setAmount(event.getAmount() * 0.5F);
            }
        }
    }
}