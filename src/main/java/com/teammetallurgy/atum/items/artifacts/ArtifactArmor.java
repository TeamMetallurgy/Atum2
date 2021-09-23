package com.teammetallurgy.atum.items.artifacts;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.api.IArtifact;
import com.teammetallurgy.atum.api.IFogReductionItem;
import com.teammetallurgy.atum.items.TexturedArmorItem;
import com.teammetallurgy.atum.misc.StackHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.client.util.InputMappings;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
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

    public ArtifactArmor(IArmorMaterial material, String name, EquipmentSlotType slot, Properties properties) {
        super(material, name, slot, properties.group(Atum.GROUP));
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

    public boolean hasArmorSetPiece(PlayerEntity player, EquipmentSlotType slotType) {
        ItemStack stack = player.getItemStackFromSlot(slotType);
        switch (slotType) {
            case HEAD:
                return stack.getItem() == getHelmet();
            case CHEST:
                return stack.getItem() == getChestplate();
            case LEGS:
                return stack.getItem() == getLeggings();
            case FEET:
                return stack.getItem() == getBoots();
        }
        return false;
    }

    private int getArmorPiecesEquipped(PlayerEntity player) {
        int pieces = 0;
        for (EquipmentSlotType slotType : EquipmentSlotType.values()) {
            if (slotType.getSlotType() == EquipmentSlotType.Group.ARMOR && hasArmorSetPiece(player, slotType)) {
                pieces++;
            }
        }
        return pieces;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void addInformation(@Nonnull ItemStack stack, World world, @Nonnull List<ITextComponent> tooltip, @Nonnull ITooltipFlag flag) {
        PlayerEntity player = Minecraft.getInstance().player;
        if (player != null) {
            tooltip.add(new TranslationTextComponent(Atum.MOD_ID + ".armorset." + this.getEquipmentSlot().getName()).mergeStyle(TextFormatting.GRAY));
            tooltip.add(new StringTextComponent(""));
            String baseLang = Atum.MOD_ID + ".armorset." + this.getGod().getName() + ".";
            ItemStack[] stacks = getArmorSet();
            tooltip.add(new TranslationTextComponent(baseLang + "name").appendString(" (" + getArmorPiecesEquipped(player) + "/" + stacks.length + ") ").setStyle(this.getName().getStyle().setColor(this.getGod().getColor()))
                    .append(new TranslationTextComponent(Atum.MOD_ID + ".tooltip.shift").mergeStyle(TextFormatting.DARK_GRAY)));
            if (InputMappings.isKeyDown(Minecraft.getInstance().getMainWindow().getHandle(), GLFW.GLFW_KEY_LEFT_SHIFT)) {
                for (int i = 0; i < stacks.length; i++) {
                    tooltip.add(stacks[i].getDisplayName().copyRaw().mergeStyle((hasArmorSetPiece(player, EquipmentSlotType.fromSlotTypeAndIndex(EquipmentSlotType.Group.ARMOR, stacks.length - i - 1)) ? TextFormatting.YELLOW : TextFormatting.DARK_GRAY)));
                }
                tooltip.add(new StringTextComponent(""));
                tooltip.add(new TranslationTextComponent(baseLang + "desc").mergeStyle(hasFullSet(player) ? TextFormatting.YELLOW : TextFormatting.DARK_GRAY));
            }
        }
    }

    //Artifact Chestplate
    @Override
    @Nonnull
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(@Nonnull EquipmentSlotType slotType) {
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        builder.putAll(super.getAttributeModifiers(slotType));

        if (this.getChestplate() == this && slotType == EquipmentSlotType.CHEST) {
            builder.put(Attributes.ARMOR_TOUGHNESS, new AttributeModifier(UUID.fromString("dfdc4a46-06ab-4a7c-b726-1c53e56036d6"), "Armor toughness", 3.0D, AttributeModifier.Operation.ADDITION));
        }
        return builder.build();
    }

    //Artifact Legs
    @SubscribeEvent
    public static void onKnockback(LivingKnockBackEvent event) {
        LivingEntity livingEntity = event.getEntityLiving();
        Item legs = livingEntity.getItemStackFromSlot(EquipmentSlotType.LEGS).getItem();
        if (legs instanceof ArtifactArmor && legs == ((ArtifactArmor) legs).getLeggings() && livingEntity.isOnGround()) {
            event.setStrength(event.getStrength() * 0.5F);
        }
    }

    //Artifact Boots
    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        PlayerEntity player = event.player;

        ModifiableAttributeInstance attribute = player.getAttribute(Attributes.MOVEMENT_SPEED);
        if (attribute != null) {
            Item feet = player.getItemStackFromSlot(EquipmentSlotType.FEET).getItem();
            if (player.isAlive() && feet instanceof ArtifactArmor && feet == ((ArtifactArmor) feet).getBoots()) {
                if (!attribute.hasModifier(SPEED_BOOST)) {
                    attribute.applyNonPersistentModifier(SPEED_BOOST);
                }
            } else if (attribute.hasModifier(SPEED_BOOST)) {
                attribute.removeModifier(SPEED_BOOST);
            }
        }
    }

    @Override
    public float getFogReduction(float fogDensity, ItemStack armorItem) {
        return fogDensity / 3.25F;
    }
}