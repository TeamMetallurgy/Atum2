package com.teammetallurgy.atum;

import com.teammetallurgy.atum.client.gui.AtumGuiHandler;
import com.teammetallurgy.atum.commands.AtumWeather;
import com.teammetallurgy.atum.init.AtumRecipes;
import com.teammetallurgy.atum.integration.IntegrationHandler;
import com.teammetallurgy.atum.network.NetworkHandler;
import com.teammetallurgy.atum.proxy.CommonProxy;
import com.teammetallurgy.atum.utils.AtumConfig;
import com.teammetallurgy.atum.utils.AtumCreativeTab;
import com.teammetallurgy.atum.utils.Constants;
import com.teammetallurgy.atum.world.AtumDimension;
import com.teammetallurgy.atum.world.gen.structure.girafitomb.GirafiTombPieces;
import com.teammetallurgy.atum.world.gen.structure.lighthouse.LighthousePieces;
import com.teammetallurgy.atum.world.gen.structure.mineshaft.StructureAtumMineshaftPieces;
import com.teammetallurgy.atum.world.gen.structure.pyramid.PyramidPieces;
import com.teammetallurgy.atum.world.gen.structure.ruins.RuinPieces;
import com.teammetallurgy.atum.world.gen.structure.tomb.TombPieces;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
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
        IntegrationHandler.INSTANCE.initModIntegration();
        AtumDimension.register();
        NetworkHandler.register();
        StructureAtumMineshaftPieces.registerMineshaft();
        PyramidPieces.registerPyramid();
        RuinPieces.registerRuins();
        TombPieces.registerTomb();
        GirafiTombPieces.registerGirafiTomb();
        LighthousePieces.registerLighthouse();
        IntegrationHandler.INSTANCE.preInit();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.init();
        AtumRecipes.addKilnRecipes();
        NetworkRegistry.INSTANCE.registerGuiHandler(Atum.instance, new AtumGuiHandler());
        IntegrationHandler.INSTANCE.init();
    }

    @Mod.EventHandler
	public static void init(FMLServerStartingEvent event)
	{
		event.registerServerCommand(new AtumWeather());
	}
}