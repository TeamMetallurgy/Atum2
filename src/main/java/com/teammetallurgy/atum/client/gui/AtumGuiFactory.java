package com.teammetallurgy.atum.client.gui;

import com.teammetallurgy.atum.utils.AtumConfig;
import com.teammetallurgy.atum.utils.Constants;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.fml.client.DefaultGuiFactory;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.client.config.IConfigElement;

import java.util.ArrayList;
import java.util.List;

import static net.minecraftforge.common.config.Configuration.CATEGORY_GENERAL;

public class AtumGuiFactory extends DefaultGuiFactory {

    public AtumGuiFactory() {
        super(Constants.MOD_ID, GuiConfig.getAbridgedConfigPath(AtumConfig.config.toString()));
    }

    @Override
    public GuiScreen createConfigGui(GuiScreen parentScreen) {
        return new GuiConfig(parentScreen, generateConfigList(), modid, false, false, title);
    }

    private List<IConfigElement> generateConfigList() {
        ArrayList<IConfigElement> elements = new ArrayList<>();

        elements.add(new ConfigElement(AtumConfig.config.getCategory(CATEGORY_GENERAL)));
        elements.add(new ConfigElement(AtumConfig.config.getCategory(AtumConfig.ATUM_START)));
        elements.add(new ConfigElement(AtumConfig.config.getCategory(AtumConfig.WORLDGEN)));
        elements.add(new ConfigElement(AtumConfig.config.getCategory(AtumConfig.MOBS)));
        return elements;
    }
}