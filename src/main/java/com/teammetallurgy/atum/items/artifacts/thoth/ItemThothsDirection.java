package com.teammetallurgy.atum.items.artifacts.thoth;

import com.teammetallurgy.atum.utils.AtumConfig;
import com.teammetallurgy.atum.world.gen.structure.PyramidPieces;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.ItemCompass;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class ItemThothsDirection extends ItemCompass {
    private BlockPos pyramidPos;
    private int searchTime;
    private boolean isSearching = false;

    public ItemThothsDirection() {
        this.setMaxStackSize(1);
        this.addPropertyOverride(new ResourceLocation("angle"), new IItemPropertyGetter() {
            @SideOnly(Side.CLIENT)
            double rotation;
            @SideOnly(Side.CLIENT)
            double rota;
            @SideOnly(Side.CLIENT)
            long lastUpdateTick;

            @Override
            @SideOnly(Side.CLIENT)
            public float apply(@Nonnull ItemStack stack, @Nullable World world, @Nullable EntityLivingBase livingBase) {
                if (livingBase == null && !stack.isOnItemFrame()) {
                    return 0.0F;
                } else {
                    boolean livingNotNull = livingBase != null;
                    Entity entity = (livingNotNull ? livingBase : stack.getItemFrame());

                    if (world == null) {
                        world = entity.world;
                    }
                    double angle;

                    if (world.provider.getDimension() == AtumConfig.DIMENSION_ID) {
                        double d1 = livingNotNull ? (double) entity.rotationYaw : this.getFrameRotation((EntityItemFrame) entity);
                        d1 = MathHelper.positiveModulo(d1 / 360.0D, 1.0D);
                        double pyramidAngle = this.getPyramidToAngle(world, entity) / (Math.PI * 2D);
                        angle = 0.5D - (d1 - 0.25D - pyramidAngle);
                    } else {
                        angle = Math.random();
                    }
                    if (livingNotNull) {
                        angle = this.wobble(world, angle);
                    }
                    return MathHelper.positiveModulo((float) angle, 1.0F);
                }
            }

            @SideOnly(Side.CLIENT)
            private double wobble(World world, double angle) {
                if (world.getTotalWorldTime() != this.lastUpdateTick) {
                    this.lastUpdateTick = world.getTotalWorldTime();
                    double wobbleAngle = angle - this.rotation;
                    wobbleAngle = MathHelper.positiveModulo(wobbleAngle + 0.5D, 1.0D) - 0.5D;
                    this.rota += wobbleAngle * 0.1D;
                    this.rota *= 0.8D;
                    this.rotation = MathHelper.positiveModulo(this.rotation + this.rota, 1.0D);
                }
                return this.rotation;
            }

            @SideOnly(Side.CLIENT)
            private double getFrameRotation(EntityItemFrame frame) {
                return (double) MathHelper.wrapDegrees(180 + frame.facingDirection.getHorizontalIndex() * 90);
            }

            @SideOnly(Side.CLIENT)
            private double getPyramidToAngle(World world, Entity entity) {
                if (isSearching) {
                    System.out.println("searching");
                    return 1.0D;
                } else if (pyramidPos != null) {
                    BlockPos structurePos = pyramidPos;
                    return Math.atan2((double) structurePos.getZ() - entity.posZ, (double) structurePos.getX() - entity.posX);
                }
                return MathHelper.positiveModulo(this.wobble(world, Math.random()), 1.0D);
            }
        });
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean hasEffect(@Nonnull ItemStack stack) {
        return true;
    }

    @Override
    @Nonnull
    public EnumRarity getRarity(@Nonnull ItemStack stack) {
        return EnumRarity.RARE;
    }

    @Override
    @Nonnull
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, @Nonnull EnumHand hand) {
        player.sendStatusMessage(new TextComponentTranslation(this.getTranslationKey() + ".searching").setStyle(new Style().setColor(TextFormatting.YELLOW)), true);
        this.searchTime = 60;
        return super.onItemRightClick(world, player, hand);
    }

    @Override
    public void onUpdate(@Nonnull ItemStack stack, World world, Entity entity, int itemSlot, boolean isSelected) {
        if (this.searchTime > 1) {
            this.isSearching = true;
            this.searchTime--;
        }
        if (this.searchTime == 1) {
            this.searchTime = 0;
            this.isSearching = false;
            if (!world.isRemote) {
                if (entity instanceof EntityPlayer) {
                    EntityPlayer player = (EntityPlayer) entity;
                    WorldServer worldServer = (WorldServer) world;
                    BlockPos pos = worldServer.getChunkProvider().chunkGenerator.getNearestStructurePos(worldServer, String.valueOf(PyramidPieces.PYRAMID), player.getPosition(), true);
                    if (pos != null) {
                        player.sendStatusMessage(new TextComponentTranslation(this.getTranslationKey() + ".found", "X=" + pos.getX() + " Y=" + pos.getY() + " Z=" + pos.getZ()).setStyle(new Style().setColor(TextFormatting.AQUA)), true);
                        this.pyramidPos = pos;
                    } else {
                        player.sendStatusMessage(new TextComponentTranslation(this.getTranslationKey() + ".searchingFail").setStyle(new Style().setColor(TextFormatting.RED)), true);
                    }
                }
            }
        }
        super.onUpdate(stack, world, entity, itemSlot, isSelected);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(@Nonnull ItemStack stack, World world, List<String> tooltip, ITooltipFlag tooltipType) {
        if (Keyboard.isKeyDown(42)) {
            tooltip.add(TextFormatting.DARK_PURPLE + I18n.format(this.getTranslationKey() + ".line1"));
            tooltip.add(TextFormatting.DARK_PURPLE + I18n.format(this.getTranslationKey() + ".line2"));
        } else {
            tooltip.add(I18n.format(this.getTranslationKey() + ".line3") + " " + TextFormatting.DARK_GRAY + "[SHIFT]");
        }
    }
}