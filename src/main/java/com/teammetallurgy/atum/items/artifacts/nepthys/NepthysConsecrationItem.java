package com.teammetallurgy.atum.items.artifacts.nepthys;

import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.api.God;
import com.teammetallurgy.atum.api.IArtifact;
import com.teammetallurgy.atum.init.AtumItems;
import com.teammetallurgy.atum.init.AtumParticles;
import com.teammetallurgy.atum.items.tools.AtumShieldItem;
import it.unimi.dsi.fastutil.objects.Object2BooleanMap;
import it.unimi.dsi.fastutil.objects.Object2BooleanOpenHashMap;
import net.minecraft.entity.CreatureAttribute;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nonnull;

@Mod.EventBusSubscriber(modid = Atum.MOD_ID)
public class NepthysConsecrationItem extends AtumShieldItem implements IArtifact {
    private static final Object2BooleanMap<LivingEntity> IS_BLOCKING = new Object2BooleanOpenHashMap<>();

    public NepthysConsecrationItem() {
        super(500, new Item.Properties().rarity(Rarity.RARE));
        this.setRepairItem(AtumItems.NEBU_INGOT);
    }

    @Override
    public God getGod() {
        return God.NEPTHYS;
    }

    @Override
    public void onUse(@Nonnull World world, @Nonnull LivingEntity livingEntity, @Nonnull ItemStack stack, int count) {
        super.onUse(world, livingEntity, stack, count);
        IS_BLOCKING.putIfAbsent(livingEntity, true);
    }

    @Override
    public void onPlayerStoppedUsing(@Nonnull ItemStack stack, @Nonnull World world, @Nonnull LivingEntity livingEntity, int timeLeft) {
        super.onPlayerStoppedUsing(stack, world, livingEntity, timeLeft);
        IS_BLOCKING.removeBoolean(livingEntity);
    }

    @SubscribeEvent
    public static void onHurt(LivingHurtEvent event) {
        Entity source = event.getSource().getImmediateSource();
        LivingEntity livingEntity = event.getEntityLiving();
        if (source instanceof LivingEntity && IS_BLOCKING.containsKey(livingEntity) && ((LivingEntity) source).getCreatureAttribute() == CreatureAttribute.UNDEAD /*&& random.nextFloat() <= 0.50F*/) {
            source.setFire(8);
            source.attackEntityFrom(DamageSource.GENERIC, 2.0F);
            if (livingEntity.world instanceof ServerWorld) {
                ServerWorld serverWorld = (ServerWorld) livingEntity.world;
                serverWorld.spawnParticle(AtumParticles.LIGHT_SPARKLE, livingEntity.getPosX(), livingEntity.getPosY() + 1.0D, livingEntity.getPosZ(), 40, 0.1D, 0.0D, 0.1D, 0.01D);
            }
            IS_BLOCKING.removeBoolean(livingEntity);
        }
    }
}