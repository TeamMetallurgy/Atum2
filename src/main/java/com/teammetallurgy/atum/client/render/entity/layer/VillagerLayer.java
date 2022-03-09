package com.teammetallurgy.atum.client.render.entity.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.entity.villager.AtumVillagerData;
import com.teammetallurgy.atum.entity.villager.AtumVillagerEntity;
import com.teammetallurgy.atum.init.AtumVillagerProfession;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.Util;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.npc.VillagerDataHolder;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;

public class VillagerLayer <T extends LivingEntity & VillagerDataHolder, M extends EntityModel<T>> extends RenderLayer<T, M> {
    private static final Int2ObjectMap<ResourceLocation> TIERS = Util.make(new Int2ObjectOpenHashMap<>(), (m) -> {
        m.put(1, new ResourceLocation(Atum.MOD_ID, "gold"));
        m.put(2, new ResourceLocation(Atum.MOD_ID, "sapphire"));
        m.put(3, new ResourceLocation(Atum.MOD_ID, "ruby"));
        m.put(4, new ResourceLocation(Atum.MOD_ID, "emerald"));
        m.put(5, new ResourceLocation(Atum.MOD_ID, "diamond"));
    });
    private final String path;

    public VillagerLayer(RenderLayerParent<T, M> entityRenderer, String path) {
        super(entityRenderer);
        this.path = path;
    }

    @Override
    public void render(@Nonnull PoseStack matrixStack, @Nonnull MultiBufferSource buffer, int packedLight, T villager, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if (!villager.isInvisible() && villager instanceof AtumVillagerEntity) {
            AtumVillagerData data = ((AtumVillagerEntity) villager).getAtumVillagerData();
            AtumVillagerProfession profession = data.getAtumProfession();
            M m = this.getParentModel();
            if (profession != AtumVillagerProfession.NONE.get() && !villager.isBaby()) {
                ResourceLocation professionLocation = this.getLocation("profession", ((AtumVillagerEntity) villager).isFemale() ? "female" : "male", Objects.requireNonNull(Atum.villagerProfession.get().getKey(profession)));
                renderColoredCutoutModel(m, professionLocation, matrixStack, buffer, packedLight, villager, 1.0F, 1.0F, 1.0F);
                if (profession != AtumVillagerProfession.NITWIT.get()) {
                    ResourceLocation professionLevelLocation = this.getLocation("profession_level", null, TIERS.get(Mth.clamp(data.getLevel(), 1, TIERS.size())));
                    renderColoredCutoutModel(m, professionLevelLocation, matrixStack, buffer, packedLight, villager, 1.0F, 1.0F, 1.0F);
                }
            }
        }
    }

    private ResourceLocation getLocation(String type, @Nullable String gender, ResourceLocation location) {
        return new ResourceLocation(location.getNamespace(), "textures/entity/" + this.path + "/" + type + "/" + (gender != null ? gender + "/" : "") + location.getPath() + ".png");
    }
}