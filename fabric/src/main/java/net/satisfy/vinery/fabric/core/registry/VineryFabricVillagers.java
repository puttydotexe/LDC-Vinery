package net.satisfy.vinery.fabric.core.registry;

import me.shedaniel.autoconfig.AutoConfig;
import com.google.common.collect.ImmutableSet;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.server.MinecraftServer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.npc.villager.VillagerProfession;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.satisfy.vinery.core.Vinery;
import net.satisfy.vinery.core.registry.ObjectRegistry;
import net.satisfy.vinery.fabric.config.VineryFabricConfig;

import java.util.stream.Collectors;

public class VineryFabricVillagers {

    private static final Identifier WINEMAKER_POI_IDENTIFIER = Vinery.identifier("winemaker_poi");
    private static final ResourceKey<PoiType> WINEMAKER_POI_KEY = ResourceKey.create(Registries.POINT_OF_INTEREST_TYPE, WINEMAKER_POI_IDENTIFIER);
    public static final PoiType WINEMAKER_POI;
    public static final VillagerProfession WINEMAKER;

    static {
        WINEMAKER_POI = Registry.register(
                BuiltInRegistries.POINT_OF_INTEREST_TYPE,
                WINEMAKER_POI_IDENTIFIER,
                new PoiType(ObjectRegistry.FERMENTATION_BARREL.get().getStateDefinition().getPossibleStates().stream().collect(Collectors.toSet()), 1, 12)
        );

        WINEMAKER = Registry.register(
                BuiltInRegistries.VILLAGER_PROFESSION,
                Vinery.identifier("winemaker"),
                new VillagerProfession(
                        net.minecraft.network.chat.Component.translatable("entity.minecraft.villager.vinery.winemaker"),
                        holder -> holder.is(WINEMAKER_POI_KEY),
                        holder -> holder.is(WINEMAKER_POI_KEY),
                        ImmutableSet.of(),
                        ImmutableSet.of(),
                        SoundEvents.VILLAGER_WORK_FARMER,
                        new Int2ObjectOpenHashMap<>()
                )
        );
    }

    public static void registerPOIAndProfession() {
    }

    public static void init(MinecraftServer server) {
        VineryFabricConfig config = AutoConfig.getConfigHolder(VineryFabricConfig.class).getConfig();
        RegistryAccess registryAccess = server.registryAccess();

        registerTradesForLevel(config.villager.level1, 1, registryAccess);
        registerTradesForLevel(config.villager.level2, 2, registryAccess);
        registerTradesForLevel(config.villager.level3, 3, registryAccess);
        registerTradesForLevel(config.villager.level4, 4, registryAccess);
        registerTradesForLevel(config.villager.level5, 5, registryAccess);
    }

    private static void registerTradesForLevel(VineryFabricConfig.VillagerSettings.TradeLevelSettings tradeLevelSettings, int level, RegistryAccess registryAccess) {
    }
}
