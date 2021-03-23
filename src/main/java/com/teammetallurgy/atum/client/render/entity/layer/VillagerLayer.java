package com.teammetallurgy.atum.client.render.entity.layer;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.teammetallurgy.atum.entity.villager.AtumVillagerData;
import com.teammetallurgy.atum.entity.villager.AtumVillagerEntity;
import com.teammetallurgy.atum.entity.villager.AtumVillagerProfession;
import com.teammetallurgy.atum.entity.villager.Race;
import com.teammetallurgy.atum.misc.AtumRegistry;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.villager.IVillagerDataHolder;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import net.minecraft.util.math.MathHelper;

import javax.annotation.Nonnull;

public class VillagerLayer <T extends LivingEntity & IVillagerDataHolder, M extends EntityModel<T>> extends LayerRenderer<T, M> {
    private static final Int2ObjectMap<ResourceLocation> TIERS = Util.make(new Int2ObjectOpenHashMap<>(), (m) -> {
        m.put(1, new ResourceLocation("stone"));
        m.put(2, new ResourceLocation("iron"));
        m.put(3, new ResourceLocation("gold"));
        m.put(4, new ResourceLocation("emerald"));
        m.put(5, new ResourceLocation("diamond"));
    });
    private final String path;

    public VillagerLayer(IEntityRenderer<T, M> entityRenderer, String path) {
        super(entityRenderer);
        this.path = path;
    }

    @Override
    public void render(@Nonnull MatrixStack matrixStack, @Nonnull IRenderTypeBuffer buffer, int packedLight, T villager, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if (!villager.isInvisible() && villager instanceof AtumVillagerEntity) {
            AtumVillagerData villagerdata = ((AtumVillagerEntity) villager).getAtumVillagerData();
            Race race = villagerdata.getRace();
            AtumVillagerProfession profession = villagerdata.getAtumProfession();
            M m = this.getEntityModel();
            if (profession != AtumVillagerProfession.NONE.get() && !villager.isChild()) {
                ResourceLocation professionLocation = this.getLocation("profession", AtumRegistry.VILLAGER_PROFESSION.get().getKey(profession));
                renderCutoutModel(m, professionLocation, matrixStack, buffer, packedLight, villager, 1.0F, 1.0F, 1.0F);
                if (profession != AtumVillagerProfession.NITWIT.get()) {
                    ResourceLocation professionLevelLocation = this.getLocation("profession_level", TIERS.get(MathHelper.clamp(villagerdata.getLevel(), 1, TIERS.size())));
                    renderCutoutModel(m, professionLevelLocation, matrixStack, buffer, packedLight, villager, 1.0F, 1.0F, 1.0F);
                }
            }
        }
    }

    private ResourceLocation getLocation(String type, ResourceLocation location) {
        return new ResourceLocation(location.getNamespace(), "textures/entity/" + this.path + "/" + type + "/" + location.getPath() + ".png");
    }
}