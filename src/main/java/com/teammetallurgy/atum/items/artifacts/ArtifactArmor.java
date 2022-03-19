package com.teammetallurgy.atum.items.artifacts;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.mojang.blaze3d.platform.InputConstants;
import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.api.IArtifact;
import com.teammetallurgy.atum.api.IFogReductionItem;
import com.teammetallurgy.atum.items.TexturedArmorItem;
import com.teammetallurgy.atum.misc.StackHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingKnockBackEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.UUID;

@Mod.EventBusSubscriber(modid = Atum.MOD_ID)
public abstract class ArtifactArmor extends TexturedArmorItem implements IArtifact, IFogReductionItem {
    private static final AttributeModifier SPEED_BOOST = new AttributeModifier(UUID.fromString("2aa9e06c-cc77-4c0a-b832-58d8aaef1500"), "Artifact boots speed boost", 0.02D, AttributeModifier.Operation.ADDITION);

    public ArtifactArmor(ArmorMaterial material, String name, EquipmentSlot slot, Properties properties) {
        super(material, name, slot, properties.tab(Atum.GROUP));
    }

    public abstract Item getHelmet();

    public abstract Item getChestplate();

    public abstract Item getLeggings();

    public abstract Item getBoots();

    public boolean hasFullSet(LivingEntity livingEntity) {
        return StackHelper.hasFullArmorSet(livingEntity, getHelmet(), getChestplate(), getLeggings(), getBoots());
    }

    public ItemStack[] getArmorSet() {
        return new ItemStack[]{
                new ItemStack(getHelmet()),
                new ItemStack(getChestplate()),
                new ItemStack(getLeggings()),
                new ItemStack(getBoots())
        };
    }

    public boolean hasArmorSetPiece(Player player, EquipmentSlot slotType) {
        ItemStack stack = player.getItemBySlot(slotType);
        return switch (slotType) {
            case HEAD -> stack.getItem() == getHelmet();
            case CHEST -> stack.getItem() == getChestplate();
            case LEGS -> stack.getItem() == getLeggings();
            case FEET -> stack.getItem() == getBoots();
            default -> false;
        };
    }

    private int getArmorPiecesEquipped(Player player) {
        int pieces = 0;
        for (EquipmentSlot slotType : EquipmentSlot.values()) {
            if (slotType.getType() == EquipmentSlot.Type.ARMOR && hasArmorSetPiece(player, slotType)) {
                pieces++;
            }
        }
        return pieces;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(@Nonnull ItemStack stack, Level world, @Nonnull List<Component> tooltip, @Nonnull TooltipFlag flag) {
        Player player = Minecraft.getInstance().player;
        if (player != null) {
            tooltip.add(new TranslatableComponent(Atum.MOD_ID + ".armorset." + this.getSlot().getName()).withStyle(ChatFormatting.GRAY));
            tooltip.add(new TextComponent(""));
            String baseLang = Atum.MOD_ID + ".armorset." + this.getGod().getName() + ".";
            ItemStack[] stacks = getArmorSet();
            tooltip.add(new TranslatableComponent(baseLang + "name").append(" (" + getArmorPiecesEquipped(player) + "/" + stacks.length + ") ").setStyle(this.getDescription().getStyle().withColor(this.getGod().getColor()))
                    .append(new TranslatableComponent(Atum.MOD_ID + ".tooltip.shift").withStyle(ChatFormatting.DARK_GRAY)));
            if (InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), GLFW.GLFW_KEY_LEFT_SHIFT)) {
                for (int i = 0; i < stacks.length; i++) {
                    tooltip.add(stacks[i].getHoverName().plainCopy().withStyle((hasArmorSetPiece(player, EquipmentSlot.byTypeAndIndex(EquipmentSlot.Type.ARMOR, stacks.length - i - 1)) ? ChatFormatting.YELLOW : ChatFormatting.DARK_GRAY)));
                }
                tooltip.add(new TextComponent(""));
                tooltip.add(new TranslatableComponent(baseLang + "desc").withStyle(hasFullSet(player) ? ChatFormatting.YELLOW : ChatFormatting.DARK_GRAY));
            }
        }
    }

    //Artifact Chestplate
    @Override
    @Nonnull
    public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(@Nonnull EquipmentSlot slotType) {
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        builder.putAll(super.getDefaultAttributeModifiers(slotType));

        if (this.getChestplate() == this && slotType == EquipmentSlot.CHEST) {
            builder.put(Attributes.ARMOR_TOUGHNESS, new AttributeModifier(UUID.fromString("dfdc4a46-06ab-4a7c-b726-1c53e56036d6"), "Armor toughness", 3.0D, AttributeModifier.Operation.ADDITION));
        }
        return builder.build();
    }

    //Artifact Legs
    @SubscribeEvent
    public static void onKnockback(LivingKnockBackEvent event) {
        LivingEntity livingEntity = event.getEntityLiving();
        Item legs = livingEntity.getItemBySlot(EquipmentSlot.LEGS).getItem();
        if (legs instanceof ArtifactArmor && legs == ((ArtifactArmor) legs).getLeggings() && livingEntity.isOnGround()) {
            event.setStrength(event.getStrength() * 0.5F);
        }
    }

    //Artifact Boots
    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        Player player = event.player;

        AttributeInstance attribute = player.getAttribute(Attributes.MOVEMENT_SPEED);
        if (attribute != null) {
            Item feet = player.getItemBySlot(EquipmentSlot.FEET).getItem();
            if (player.isAlive() && feet instanceof ArtifactArmor && feet == ((ArtifactArmor) feet).getBoots()) {
                if (!attribute.hasModifier(SPEED_BOOST)) {
                    attribute.addTransientModifier(SPEED_BOOST);
                }
            } else if (attribute.hasModifier(SPEED_BOOST)) {
                attribute.removeModifier(SPEED_BOOST);
            }
        }
    }

    @Override
    public float getFogReduction(float fogDensity, ItemStack armorItem) {
        return fogDensity * 3.25F;
    }
}