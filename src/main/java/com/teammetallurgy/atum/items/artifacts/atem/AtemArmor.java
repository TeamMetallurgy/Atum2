package com.teammetallurgy.atum.items.artifacts.atem;

import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.api.AtumMats;
import com.teammetallurgy.atum.api.God;
import com.teammetallurgy.atum.client.model.armor.AtemArmorModel;
import com.teammetallurgy.atum.init.AtumItems;
import com.teammetallurgy.atum.items.artifacts.ArtifactArmor;
import com.teammetallurgy.atum.misc.StackHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.IItemRenderProperties;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;

@Mod.EventBusSubscriber(modid = Atum.MOD_ID)
public class AtemArmor extends ArtifactArmor {
    protected static final HashMap<Player, Integer> RECALL_TIMER = new HashMap<>();

    public AtemArmor(EquipmentSlot slot) {
        super(AtumMats.NEBU_ARMOR, "atem_armor", slot, new Item.Properties().rarity(Rarity.RARE));
        this.setHasRender();
    }

    @Override
    public void initializeClient(@Nonnull Consumer<IItemRenderProperties> consumer) {
        consumer.accept(new IItemRenderProperties() {
            @Override
            public HumanoidModel<?> getArmorModel(LivingEntity entityLiving, ItemStack itemStack, EquipmentSlot armorSlot, HumanoidModel<?> _default) {
                return new AtemArmorModel(armorSlot, hasFullSet(entityLiving));
            }
        });
    }

    @Override
    public God getGod() {
        return God.ATEM;
    }

    @Override
    public Item getHelmet() {
        return AtumItems.EYES_OF_ATEM;
    }

    @Override
    public Item getChestplate() {
        return AtumItems.BODY_OF_ATEM;
    }

    @Override
    public Item getLeggings() {
        return AtumItems.LEGS_OF_ATEM;
    }

    @Override
    public Item getBoots() {
        return AtumItems.FEET_OF_ATEM;
    }

    @SubscribeEvent
    public static void tick(TickEvent.PlayerTickEvent event) {
        if (event.side == LogicalSide.SERVER && event.phase == TickEvent.Phase.START) {
            Player player = event.player;

            if (player instanceof ServerPlayer) {
                if (RECALL_TIMER.containsKey(player)) {
                    int recallTimer = RECALL_TIMER.get(player);
                    if (recallTimer == 0) {
                        RECALL_TIMER.remove(player);
                    }

                    if (recallTimer > 0) {
                        RECALL_TIMER.replace(player, recallTimer - 1);
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void onLivingDamage(LivingDamageEvent event) {
        LivingEntity livingEntity = event.getEntityLiving();
        if (livingEntity instanceof Player) {
            Player player = (Player) livingEntity;
            if (event.getAmount() >= player.getHealth() && StackHelper.hasFullArmorSet(livingEntity, AtumItems.EYES_OF_ATEM, AtumItems.BODY_OF_ATEM, AtumItems.LEGS_OF_ATEM, AtumItems.FEET_OF_ATEM)) {
                if (!RECALL_TIMER.containsKey(player)) {
                    livingEntity.setHealth(livingEntity.getMaxHealth());
                    AtemsHomecomingItem.recall(livingEntity.level, player);
                    RECALL_TIMER.put(player, 24000);
                }
            }
        }
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(@Nonnull ItemStack stack, Level world, @Nonnull List<Component> tooltip, @Nonnull TooltipFlag flag) {
        super.appendHoverText(stack, world, tooltip, flag);
        Player player = Minecraft.getInstance().player;
        if (player != null && RECALL_TIMER.containsKey(player)) {
            int totalSeconds = RECALL_TIMER.get(player) / 20;
            int minutes = (totalSeconds % 3600) / 60;
            int seconds = totalSeconds % 60;

            tooltip.add(new TranslatableComponent(Atum.MOD_ID + ".recall_cooldown", minutes + ":" + seconds).withStyle(ChatFormatting.GRAY));
        }
    }
}