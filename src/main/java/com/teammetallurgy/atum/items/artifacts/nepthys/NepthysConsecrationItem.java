package com.teammetallurgy.atum.items.artifacts.nepthys;

import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.api.God;
import com.teammetallurgy.atum.api.IArtifact;
import com.teammetallurgy.atum.init.AtumItems;
import com.teammetallurgy.atum.init.AtumParticles;
import com.teammetallurgy.atum.items.tools.AtumShieldItem;
import it.unimi.dsi.fastutil.objects.Object2BooleanMap;
import it.unimi.dsi.fastutil.objects.Object2BooleanOpenHashMap;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;
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
    public void onUseTick(@Nonnull Level world, @Nonnull LivingEntity livingEntity, @Nonnull ItemStack stack, int count) {
        super.onUseTick(world, livingEntity, stack, count);
        IS_BLOCKING.putIfAbsent(livingEntity, true);
    }

    @Override
    public void releaseUsing(@Nonnull ItemStack stack, @Nonnull Level world, @Nonnull LivingEntity livingEntity, int timeLeft) {
        super.releaseUsing(stack, world, livingEntity, timeLeft);
        IS_BLOCKING.removeBoolean(livingEntity);
    }

    @SubscribeEvent
    public static void onHurt(LivingHurtEvent event) {
        Entity source = event.getSource().getDirectEntity();
        LivingEntity livingEntity = event.getEntityLiving();
        if (source instanceof LivingEntity && IS_BLOCKING.containsKey(livingEntity) && ((LivingEntity) source).getMobType() == MobType.UNDEAD /*&& random.nextFloat() <= 0.50F*/) {
            source.setSecondsOnFire(8);
            source.hurt(DamageSource.GENERIC, 2.0F);
            if (livingEntity.level instanceof ServerLevel) {
                ServerLevel serverWorld = (ServerLevel) livingEntity.level;
                serverWorld.sendParticles(AtumParticles.LIGHT_SPARKLE, livingEntity.getX(), livingEntity.getY() + 1.0D, livingEntity.getZ(), 40, 0.1D, 0.0D, 0.1D, 0.01D);
            }
            IS_BLOCKING.removeBoolean(livingEntity);
        }
    }
}