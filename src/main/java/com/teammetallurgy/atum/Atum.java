package com.teammetallurgy.atum;

import com.teammetallurgy.atum.client.gui.AtumGuiHandler;
import com.teammetallurgy.atum.handler.AtumConfig;
import com.teammetallurgy.atum.handler.AtumCreativeTab;
import com.teammetallurgy.atum.init.AtumEntities;
import com.teammetallurgy.atum.items.AtumLoot;
import com.teammetallurgy.atum.proxy.CommonProxy;
import com.teammetallurgy.atum.utils.Constants;
import com.teammetallurgy.atum.world.AtumDimension;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(modid = Constants.MOD_ID, name = Constants.MOD_NAME, version = Constants.VERSION, dependencies = Constants.DEPENDENCIES, guiFactory = Constants.FACTORY)
public class Atum {

    @Mod.Instance(Constants.MOD_ID)
    public static Atum instance;

    @SidedProxy(clientSide = Constants.CLIENT, serverSide = Constants.SERVER)
    public static CommonProxy proxy;

    public static final Logger LOG = LogManager.getLogger(Constants.MOD_NAME);
    public static final CreativeTabs CREATIVE_TAB = new AtumCreativeTab();

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        new AtumConfig(event.getSuggestedConfigurationFile());
        AtumDimension.register();
        proxy.initRenders();
        new AtumEntities();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        NetworkRegistry.INSTANCE.registerGuiHandler(this, new AtumGuiHandler());
        AtumLoot.register();
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
    }
}