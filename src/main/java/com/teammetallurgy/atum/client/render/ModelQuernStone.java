package com.teammetallurgy.atum.client.render;

import com.google.common.collect.ImmutableMap;
import com.teammetallurgy.atum.init.AtumBlocks;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.SimpleModelState;
import net.minecraftforge.common.model.IModelPart;
import net.minecraftforge.common.model.TRSRTransformation;

import java.util.function.Function;

public class ModelQuernStone extends BlockBakedBase {
    private final Function<ResourceLocation, TextureAtlasSprite> textureGetter = location -> {

        assert location != null;
        return Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(location.toString());
    };

    private final ImmutableMap<? extends IModelPart, TRSRTransformation> transforms;

    public ModelQuernStone(IBakedModel standard, IModel tableModel) {
        super(standard, tableModel);
        this.transforms = copyTransforms(standard);
    }

    @Override
    public String getCacheKey(IBlockState state, EnumFacing side) {
        return Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelShapes().getTexture(state.getBlock().getStateFromMeta(new ItemStack(state.getBlock()).getItemDamage())).getIconName();
    }

    @Override
    public String getCacheKey(ItemStack stack, World world, EntityLivingBase entity) {
        return null;
    }

    @Override
    public IBakedModel generateBlockModel(String key) {
        return this.getRaw().bake(new SimpleModelState(this.transforms), DefaultVertexFormats.BLOCK, this.textureGetter);
    }

    public static ImmutableMap<ItemCameraTransforms.TransformType, TRSRTransformation> copyTransforms (IBakedModel model) {

        final ImmutableMap.Builder<ItemCameraTransforms.TransformType, TRSRTransformation> copiedTransforms = ImmutableMap.builder();



        return copiedTransforms.build();
    }
}