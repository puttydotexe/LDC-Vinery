package net.satisfy.vinery.neoforge.core.registry;

import com.google.common.collect.ImmutableSet;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.npc.villager.VillagerProfession;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.satisfy.vinery.core.Vinery;
import net.satisfy.vinery.core.registry.ObjectRegistry;

import java.util.function.Supplier;
import java.util.stream.Collectors;

public class VineryNeoForgeVillagers {
    public static final DeferredRegister<PoiType> POI_TYPES = DeferredRegister.create(Registries.POINT_OF_INTEREST_TYPE,Vinery.MOD_ID);
    private static final Identifier WINEMAKER_POI_IDENTIFIER = Vinery.identifier("winemaker_poi");
    private static final ResourceKey<PoiType> WINEMAKER_POI_KEY = ResourceKey.create(Registries.POINT_OF_INTEREST_TYPE, WINEMAKER_POI_IDENTIFIER);

    public static final DeferredRegister<VillagerProfession> VILLAGER_PROFESSIONS = DeferredRegister.create(Registries.VILLAGER_PROFESSION,Vinery.MOD_ID);

    public static final Supplier<PoiType> WINEMAKER_POI = POI_TYPES.register("winemaker_poi", () ->
            new PoiType(ObjectRegistry.FERMENTATION_BARREL.get().getStateDefinition().getPossibleStates().stream().collect(Collectors.toSet()), 1, 12));

    public static final Supplier<VillagerProfession> WINEMAKER = VILLAGER_PROFESSIONS.register("winemaker", () ->
            new VillagerProfession(Component.translatable("entity.minecraft.villager.vinery.winemaker"), holder -> holder.is(WINEMAKER_POI_KEY), holder -> holder.is(WINEMAKER_POI_KEY), ImmutableSet.of(), ImmutableSet.of(), SoundEvents.VILLAGER_WORK_FARMER, new Int2ObjectOpenHashMap<>()));


    public static void registerPOIs(){
    }

    public static void register(IEventBus eventBus) {
        POI_TYPES.register(eventBus);
        VILLAGER_PROFESSIONS.register(eventBus);
    }
}
