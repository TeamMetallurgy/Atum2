package com.teammetallurgy.atum.items.artifacts.atem;

import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.api.AtumMats;
import com.teammetallurgy.atum.api.God;
import com.teammetallurgy.atum.client.model.armor.AtemArmorModel;
import com.teammetallurgy.atum.init.AtumItems;
import com.teammetallurgy.atum.items.artifacts.ArtifactArmor;
import com.teammetallurgy.atum.misc.StackHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.List;

@Mod.EventBusSubscriber(modid = Atum.MOD_ID)
public class AtemArmor extends ArtifactArmor {
    protected static final HashMap<PlayerEntity, Integer> RECALL_TIMER = new HashMap<>();

    public AtemArmor(EquipmentSlotType slot) {
        super(AtumMats.NEBU_ARMOR, "atem_armor", slot, new Item.Properties().rarity(Rarity.RARE));
        this.setHasRender();
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public <A extends BipedModel<?>> A getArmorModel(LivingEntity entityLiving, @Nonnull ItemStack stack, EquipmentSlotType armorSlot, A _default) {
        return (A) new AtemArmorModel(armorSlot, hasFullSet(entityLiving));
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
            PlayerEntity player = event.player;

            if (player instanceof ServerPlayerEntity) {
                if (RECALL_TIMER.containsKey(player)) {
                    int recallTimer = RECALL_TIMER.get(player);
                    System.out.println(recallTimer);
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
        if (livingEntity instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) livingEntity;
            if (event.getAmount() >= player.getHealth() && StackHelper.hasFullArmorSet(livingEntity, AtumItems.EYES_OF_ATEM, AtumItems.BODY_OF_ATEM, AtumItems.LEGS_OF_ATEM, AtumItems.FEET_OF_ATEM)) {
                if (!RECALL_TIMER.containsKey(player)) {
                    livingEntity.heal(1.0F);
                    AtemsHomecomingItem.recall(livingEntity.world, player);
                    RECALL_TIMER.put(player, 24000);
                }
            }
        }
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void addInformation(@Nonnull ItemStack stack, World world, @Nonnull List<ITextComponent> tooltip, @Nonnull ITooltipFlag flag) {
        super.addInformation(stack, world, tooltip, flag);
        PlayerEntity player = Minecraft.getInstance().player;
        if (player != null && RECALL_TIMER.containsKey(player)) {
            int totalSeconds = RECALL_TIMER.get(player) / 20;
            int minutes = (totalSeconds % 3600) / 60;
            int seconds = totalSeconds % 60;

            tooltip.add(new TranslationTextComponent(Atum.MOD_ID + ".recall_cooldown", minutes + ":" + seconds).mergeStyle(TextFormatting.GRAY));
        }
    }
}