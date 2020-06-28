package com.teammetallurgy.atum.client.model;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.datafixers.util.Pair;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.model.*;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.Direction;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IModelConfiguration;
import net.minecraftforge.client.model.IModelLoader;
import net.minecraftforge.client.model.geometry.IModelGeometry;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Function;

/**
 * Full credit to Gigaherz for this class
 *
 */
public class SeparatePerspectiveModel implements IModelGeometry<SeparatePerspectiveModel> { //TODO Remove in 1.16, as Forge have implemented the system
    private final BlockModel baseModel;
    private final ImmutableMap<ItemCameraTransforms.TransformType, BlockModel> perspectives;

    public SeparatePerspectiveModel(BlockModel baseModel, ImmutableMap<ItemCameraTransforms.TransformType, BlockModel> perspectives) {
        this.baseModel = baseModel;
        this.perspectives = perspectives;
    }

    @Override
    public IBakedModel bake(IModelConfiguration owner, ModelBakery bakery, Function<Material, TextureAtlasSprite> spriteGetter, IModelTransform modelTransform, ItemOverrideList overrides, ResourceLocation modelLocation) {
        return new BakedModel(
                owner.useSmoothLighting(), owner.isShadedInGui(), owner.isSideLit(),
                spriteGetter.apply(owner.resolveTexture("particle")), overrides,
                this.baseModel.bakeModel(bakery, this.baseModel, spriteGetter, modelTransform, modelLocation, owner.isSideLit()),
                ImmutableMap.copyOf(Maps.transformValues(this.perspectives, value -> value.bakeModel(bakery, value, spriteGetter, modelTransform, modelLocation, owner.isSideLit())))
        );
    }

    @Override
    public Collection<Material> getTextures(IModelConfiguration owner, Function<ResourceLocation, IUnbakedModel> modelGetter, Set<Pair<String, String>> missingTextureErrors) {
        Set<Material> textures = Sets.newHashSet();
        textures.addAll(this.baseModel.getTextures(modelGetter, missingTextureErrors));
        for (BlockModel model : this.perspectives.values()) {
            textures.addAll(model.getTextures(modelGetter, missingTextureErrors));
        }
        return textures;
    }

    public static class BakedModel implements IBakedModel {
        private final boolean isAmbientOcclusion;
        private final boolean isGui3d;
        private final boolean isSideLit;
        private final TextureAtlasSprite particle;
        private final ItemOverrideList overrides;
        private final IBakedModel baseModel;
        private final ImmutableMap<ItemCameraTransforms.TransformType, IBakedModel> perspectives;

        public BakedModel(boolean isAmbientOcclusion, boolean isGui3d, boolean isSideLit, TextureAtlasSprite particle, ItemOverrideList overrides, IBakedModel baseModel, ImmutableMap<ItemCameraTransforms.TransformType, IBakedModel> perspectives) {
            this.isAmbientOcclusion = isAmbientOcclusion;
            this.isGui3d = isGui3d;
            this.isSideLit = isSideLit;
            this.particle = particle;
            this.overrides = overrides;
            this.baseModel = baseModel;
            this.perspectives = perspectives;
        }

        @Override
        @Nonnull
        public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @Nonnull Random rand) {
            return Collections.emptyList();
        }

        @Override
        public boolean isAmbientOcclusion() {
            return this.isAmbientOcclusion;
        }

        @Override
        public boolean isGui3d() {
            return this.isGui3d;
        }

        @Override
        public boolean func_230044_c_() {
            return this.isSideLit;
        }

        @Override
        public boolean isBuiltInRenderer() {
            return false;
        }

        @Override
        @Nonnull
        public TextureAtlasSprite getParticleTexture() {
            return this.particle;
        }

        @Override
        @Nonnull
        public ItemOverrideList getOverrides() {
            return this.overrides;
        }

        @Override
        public boolean doesHandlePerspectives() {
            return true;
        }

        @Override
        public IBakedModel handlePerspective(ItemCameraTransforms.TransformType transformType, MatrixStack matrixStack) {
            if (this.perspectives.containsKey(transformType)) {
                IBakedModel perspectiveModel = this.perspectives.get(transformType);
                return perspectiveModel.handlePerspective(transformType, matrixStack);
            }
            return this.baseModel.handlePerspective(transformType, matrixStack);
        }
    }

    public static class Loader implements IModelLoader<SeparatePerspectiveModel> {
        public static final Loader INSTANCE = new Loader();

        private static final ImmutableMap<String, ItemCameraTransforms.TransformType> PERSPECTIVES = ImmutableMap.<String, ItemCameraTransforms.TransformType>builder()
                .put("none", ItemCameraTransforms.TransformType.NONE)
                .put("third_person_left_hand", ItemCameraTransforms.TransformType.THIRD_PERSON_LEFT_HAND)
                .put("third_person_right_hand", ItemCameraTransforms.TransformType.THIRD_PERSON_RIGHT_HAND)
                .put("first_person_left_hand", ItemCameraTransforms.TransformType.FIRST_PERSON_LEFT_HAND)
                .put("first_person_right_hand", ItemCameraTransforms.TransformType.FIRST_PERSON_RIGHT_HAND)
                .put("head", ItemCameraTransforms.TransformType.HEAD)
                .put("gui", ItemCameraTransforms.TransformType.GUI)
                .put("ground", ItemCameraTransforms.TransformType.GROUND)
                .put("fixed", ItemCameraTransforms.TransformType.FIXED)
                .build();

        @Override
        public void onResourceManagerReload(@Nonnull IResourceManager resourceManager) {
        }

        @Override
        @Nonnull
        public SeparatePerspectiveModel read(@Nonnull JsonDeserializationContext deserializationContext, @Nonnull JsonObject modelContents) {
            BlockModel baseModel = deserializationContext.deserialize(JSONUtils.getJsonObject(modelContents, "base"), BlockModel.class);
            JsonObject perspectiveData = JSONUtils.getJsonObject(modelContents, "perspectives");

            ImmutableMap.Builder<ItemCameraTransforms.TransformType, BlockModel> perspectives = ImmutableMap.builder();
            for (Map.Entry<String, ItemCameraTransforms.TransformType> perspective : PERSPECTIVES.entrySet()) {
                if (perspectiveData.has(perspective.getKey())) {
                    BlockModel perspectiveModel = deserializationContext.deserialize(JSONUtils.getJsonObject(perspectiveData, perspective.getKey()), BlockModel.class);
                    perspectives.put(perspective.getValue(), perspectiveModel);
                }
            }
            return new SeparatePerspectiveModel(baseModel, perspectives.build());
        }
    }
}