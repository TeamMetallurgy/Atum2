package com.teammetallurgy.atum.items.artifacts.thoth;

import com.teammetallurgy.atum.utils.AtumConfig;
import com.teammetallurgy.atum.utils.Constants;
import com.teammetallurgy.atum.world.AtumDimensionRegistration;
import com.teammetallurgy.atum.world.gen.structure.pyramid.PyramidPieces;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemFrameEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;

public class ThothsDirectionItem extends CompassItem { //Revisit later
    private BlockPos pyramidPos;
    private int searchTime;
    private boolean isSearching = false;

    public ThothsDirectionItem() {
        super(new Item.Properties().maxStackSize(1).rarity(Rarity.RARE));
        this.addPropertyOverride(new ResourceLocation("angle"), new IItemPropertyGetter() {
            @OnlyIn(Dist.CLIENT)
            double rotation;
            @OnlyIn(Dist.CLIENT)
            double rota;
            @OnlyIn(Dist.CLIENT)
            long lastUpdateTick;

            @Override
            @OnlyIn(Dist.CLIENT)
            public float call(@Nonnull ItemStack stack, @Nullable World world, @Nullable LivingEntity livingBase) {
                if (livingBase == null && !stack.isOnItemFrame()) {
                    return 0.0F;
                } else {
                    boolean livingNotNull = livingBase != null;
                    Entity entity = (livingNotNull ? livingBase : stack.getItemFrame());

                    if (world == null) {
                        world = entity.world;
                    }
                    double angle;

                    if (world.getDimension().getType() == AtumDimensionRegistration.ATUM) {
                        if (isSearching) {
                            angle = this.spin(world);
                        } else {
                            double d1 = livingNotNull ? (double) entity.rotationYaw : this.getFrameRotation((ItemFrameEntity) entity);
                            d1 = MathHelper.positiveModulo(d1 / 360.0D, 1.0D);
                            double pyramidAngle = this.getPyramidToAngle(world, entity) / (Math.PI * 2D);
                            angle = 0.5D - (d1 - 0.25D - pyramidAngle);
                        }
                    } else {
                        angle = Math.random();
                    }
                    if (livingNotNull) {
                        angle = this.wobble(world, angle);
                    }
                    return MathHelper.positiveModulo((float) angle, 1.0F);
                }
            }

            @OnlyIn(Dist.CLIENT)
            private double spin(World world) {
                if (world.getGameTime() != this.lastUpdateTick) {
                    long delta = world.getGameTime() - this.lastUpdateTick;
                    this.lastUpdateTick = world.getGameTime();
                    this.rotation += (1 / 20D) * delta;
                    this.rotation = MathHelper.positiveModulo(this.rotation, 1.0D);
                }
                return this.rotation;
            }

            @OnlyIn(Dist.CLIENT)
            private double wobble(World world, double angle) {
                if (world.getGameTime() != this.lastUpdateTick) {
                    this.lastUpdateTick = world.getGameTime();
                    double wobbleAngle = angle - this.rotation;
                    wobbleAngle = MathHelper.positiveModulo(wobbleAngle + 0.5D, 1.0D) - 0.5D;
                    this.rota += wobbleAngle * 0.1D;
                    this.rota *= 0.8D;
                    this.rotation = MathHelper.positiveModulo(this.rotation + this.rota, 1.0D);
                }
                return this.rotation;
            }

            @OnlyIn(Dist.CLIENT)
            private double getFrameRotation(ItemFrameEntity frame) {
                return MathHelper.wrapDegrees(180 + frame.getHorizontalFacing().getHorizontalIndex() * 90);
            }

            @OnlyIn(Dist.CLIENT)
            private double getPyramidToAngle(World world, Entity entity) {
                if (isSearching) {
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
    @OnlyIn(Dist.CLIENT)
    public boolean hasEffect(@Nonnull ItemStack stack) {
        return true;
    }

    @Override
    @Nonnull
    public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, @Nonnull Hand hand) {
        player.sendStatusMessage(new TranslationTextComponent(Constants.MOD_ID + "." + Objects.requireNonNull(this.getRegistryName()).getPath() + ".searching").applyTextStyle(TextFormatting.YELLOW), true);
        this.searchTime = 60;
        return super.onItemRightClick(world, player, hand);
    }

    @Override
    public void inventoryTick(@Nonnull ItemStack stack, World world, Entity entity, int itemSlot, boolean isSelected) {
        if (AtumConfig.WORLD_GEN.pyramidEnabled.get()) {
            if (this.searchTime > 1) {
                this.isSearching = true;
                this.searchTime--;
            }
            if (this.searchTime == 1) {
                this.searchTime = 0;
                this.isSearching = false;
                if (!world.isRemote) {
                    if (entity instanceof PlayerEntity) {
                        PlayerEntity player = (PlayerEntity) entity;
                        ServerWorld worldServer = (ServerWorld) world;
                        BlockPos pos = worldServer.getChunkProvider().getChunkGenerator().findNearestStructure(worldServer, String.valueOf(PyramidPieces.PYRAMID), player.getPosition(), 5000, true);
                        String translationKey = Constants.MOD_ID + "." + Objects.requireNonNull(this.getRegistryName()).getPath();
                        if (pos != null) {
                            player.sendStatusMessage(new TranslationTextComponent(translationKey + ".found", player.isCreative() ? "X=" + pos.getX() + " Y=" + pos.getY() + " Z=" + pos.getZ() : "").setStyle(new Style().setColor(TextFormatting.AQUA)), true);
                            this.pyramidPos = pos;
                        } else {
                            player.sendStatusMessage(new TranslationTextComponent(translationKey + ".searchingFail").setStyle(new Style().setColor(TextFormatting.RED)), true);
                        }
                    }
                }
            }
        }
        super.inventoryTick(stack, world, entity, itemSlot, isSelected);
    }
}