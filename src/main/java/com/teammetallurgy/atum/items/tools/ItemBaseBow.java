package com.teammetallurgy.atum.items.tools;

import com.teammetallurgy.atum.entity.arrow.CustomArrow;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.*;
import net.minecraft.stats.StatList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.Objects;

public class ItemBaseBow extends ItemBow {
    private ItemStack repairItem = ItemStack.EMPTY;

    public ItemBaseBow() {
        this.maxStackSize = 1;
        this.setMaxDamage(384);
        this.addPropertyOverride(new ResourceLocation("pull"), new IItemPropertyGetter() {
            @Override
            @SideOnly(Side.CLIENT)
            public float apply(@Nonnull ItemStack stack, World world, EntityLivingBase entity) {
                if (entity == null) {
                    return 0.0F;
                } else {
                    ItemStack activeStack = entity.getActiveItemStack();
                    return !(activeStack.getItem() instanceof ItemBaseBow) ? 0.0F : getDrawbackSpeed(stack, entity);
                }
            }
        });
        this.addPropertyOverride(new ResourceLocation("pulling"), new IItemPropertyGetter() {
            @Override
            @SideOnly(Side.CLIENT)
            public float apply(@Nonnull ItemStack stack, World world, EntityLivingBase entity) {
                return entity != null && entity.isHandActive() && entity.getActiveItemStack() == stack ? 1.0F : 0.0F;
            }
        });
    }

    @Override
    public void onPlayerStoppedUsing(@Nonnull ItemStack stack, World world, EntityLivingBase entityLiving, int timeLeft) {
        if (entityLiving instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) entityLiving;
            boolean infinity = player.capabilities.isCreativeMode || EnchantmentHelper.getEnchantmentLevel(Enchantments.INFINITY, stack) > 0;
            ItemStack ammoStack = this.findAmmo(player);

            int maxUses = this.getMaxItemUseDuration(stack) - timeLeft;
            maxUses = ForgeEventFactory.onArrowLoose(stack, world, player, maxUses, !ammoStack.isEmpty() || infinity);
            if (maxUses < 0) return;

            if (!ammoStack.isEmpty() || infinity) {
                if (ammoStack.isEmpty()) {
                    ammoStack = new ItemStack(Items.ARROW);
                }
                float velocity = getArrowVelocity(maxUses);

                this.onVelocity(world, player, velocity);

                if ((double) velocity >= 0.1D) {
                    boolean hasArrow = player.capabilities.isCreativeMode || (ammoStack.getItem() instanceof ItemArrow && ((ItemArrow) ammoStack.getItem()).isInfinite(ammoStack, stack, player));

                    if (!world.isRemote) {
                        EntityArrow arrow = setArrow(stack, world, player, velocity);
                        onShoot(arrow, player, velocity);

                        if (velocity == 1.0F) {
                            arrow.setIsCritical(true);
                        }
                        int powerLevel = EnchantmentHelper.getEnchantmentLevel(Enchantments.POWER, stack);

                        if (powerLevel > 0) {
                            arrow.setDamage(arrow.getDamage() + (double) powerLevel * 0.5D + 0.5D);
                        }
                        int punchLevel = EnchantmentHelper.getEnchantmentLevel(Enchantments.PUNCH, stack);

                        if (punchLevel > 0) {
                            arrow.setKnockbackStrength(punchLevel);
                        }
                        if (EnchantmentHelper.getEnchantmentLevel(Enchantments.FLAME, stack) > 0) {
                            arrow.setFire(100);
                        }
                        stack.damageItem(1, player);

                        if (hasArrow || player.capabilities.isCreativeMode && (ammoStack.getItem() == Items.SPECTRAL_ARROW || ammoStack.getItem() == Items.TIPPED_ARROW)) {
                            arrow.pickupStatus = CustomArrow.PickupStatus.CREATIVE_ONLY;
                        }
                        world.spawnEntity(arrow);
                    }
                    world.playSound(null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_ARROW_SHOOT, SoundCategory.PLAYERS, 1.0F, 1.0F / (itemRand.nextFloat() * 0.4F + 1.2F) + velocity * 0.5F);

                    if (!hasArrow && !player.capabilities.isCreativeMode) {
                        ammoStack.shrink(1);

                        if (ammoStack.isEmpty()) {
                            player.inventory.deleteStack(ammoStack);
                        }
                    }
                    player.addStat(Objects.requireNonNull(StatList.getObjectUseStats(this)));
                }
            }
        }
    }

    protected EntityArrow setArrow(ItemStack stack, World world, EntityPlayer player, float velocity) {
        ItemArrow itemArrow = (ItemArrow) (stack.getItem() instanceof ItemArrow ? stack.getItem() : Items.ARROW);
        return itemArrow.createArrow(world, stack, player);
    }

    protected void onVelocity(World world, EntityPlayer player, float velocity) {
    }

    protected void onShoot(EntityArrow arrow, EntityPlayer player, float velocity) {
        arrow.shoot(player, player.rotationPitch, player.rotationYaw, 0.0F, velocity * 3.0F, 1.0F);
    }

    protected float getDrawbackSpeed(@Nonnull ItemStack stack, EntityLivingBase entity) {
        return (float) (stack.getMaxItemUseDuration() - entity.getItemInUseCount()) / 20.0F;
    }

    protected ItemBaseBow setRepairItem(Item item) {
        this.repairItem = new ItemStack(item);
        return this;
    }

    @Override
    public boolean getIsRepairable(@Nonnull ItemStack toRepair, @Nonnull ItemStack repair) {
        return repair == this.repairItem;
    }
}