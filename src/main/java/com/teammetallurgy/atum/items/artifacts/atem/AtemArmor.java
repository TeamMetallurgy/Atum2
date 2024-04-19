package com.teammetallurgy.atum.items.artifacts.atem;

import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.api.God;
import com.teammetallurgy.atum.api.material.AtumArmorMaterials;
import com.teammetallurgy.atum.client.ClientHandler;
import com.teammetallurgy.atum.client.model.armor.AtemArmorModel;
import com.teammetallurgy.atum.init.AtumItems;
import com.teammetallurgy.atum.items.artifacts.ArtifactArmor;
import com.teammetallurgy.atum.misc.StackHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.neoforged.fml.LogicalSide;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import net.neoforged.neoforge.event.TickEvent;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;

@Mod.EventBusSubscriber(modid = Atum.MOD_ID)
public class AtemArmor extends ArtifactArmor {
    protected static final HashMap<Player, Integer> RECALL_TIMER = new HashMap<>();

    public AtemArmor(Type type) {
        super(AtumArmorMaterials.NEBU, "atem_armor", type, new Item.Properties().rarity(Rarity.RARE));
        this.setHasRender();
    }

    @Override
    public void initializeClient(@Nonnull Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            @Override
            public @NotNull HumanoidModel<?> getHumanoidArmorModel(LivingEntity livingEntity, ItemStack itemStack, EquipmentSlot equipmentSlot, HumanoidModel<?> original) {
                return new AtemArmorModel(Minecraft.getInstance().getEntityModels().bakeLayer(ClientHandler.ATEM_ARMOR), equipmentSlot, hasFullSet(livingEntity));
            }
        });
    }

    @Override
    public God getGod() {
        return God.ATEM;
    }

    @Override
    public Item getHelmet() {
        return AtumItems.EYES_OF_ATEM.get();
    }

    @Override
    public Item getChestplate() {
        return AtumItems.BODY_OF_ATEM.get();
    }

    @Override
    public Item getLeggings() {
        return AtumItems.LEGS_OF_ATEM.get();
    }

    @Override
    public Item getBoots() {
        return AtumItems.FEET_OF_ATEM.get();
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
        LivingEntity livingEntity = event.getEntity();
        if (livingEntity instanceof Player player) {
            if (event.getAmount() >= player.getHealth() && StackHelper.hasFullArmorSet(livingEntity, AtumItems.EYES_OF_ATEM.get(), AtumItems.BODY_OF_ATEM.get(), AtumItems.LEGS_OF_ATEM.get(), AtumItems.FEET_OF_ATEM.get())) {
                if (!RECALL_TIMER.containsKey(player)) {
                    livingEntity.setHealth(livingEntity.getMaxHealth());
                    AtemsHomecomingItem.recall(livingEntity.level(), player);
                    RECALL_TIMER.put(player, 24000);
                }
            }
        }
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(@Nonnull ItemStack stack, Level level, @Nonnull List<Component> tooltip, @Nonnull TooltipFlag flag) {
        super.appendHoverText(stack, level, tooltip, flag);
        Player player = Minecraft.getInstance().player;
        if (player != null && RECALL_TIMER.containsKey(player)) {
            int totalSeconds = RECALL_TIMER.get(player) / 20;
            int minutes = (totalSeconds % 3600) / 60;
            int seconds = totalSeconds % 60;

            tooltip.add(Component.translatable(Atum.MOD_ID + ".recall_cooldown", minutes + ":" + seconds).withStyle(ChatFormatting.GRAY));
        }
    }
}