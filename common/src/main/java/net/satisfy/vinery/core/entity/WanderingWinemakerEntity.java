package net.satisfy.vinery.core.entity;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.npc.wanderingtrader.WanderingTrader;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.ItemCost;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.item.trading.MerchantOffers;
import net.minecraft.world.level.Level;
import net.satisfy.vinery.core.registry.ObjectRegistry;
import net.satisfy.vinery.core.util.VillagerUtil;

import java.util.HashMap;

public class WanderingWinemakerEntity extends WanderingTrader {
	public static final HashMap<Integer, VillagerUtil.TradeFactory[]> TRADES = createTrades();

	private static  HashMap<Integer, VillagerUtil.TradeFactory[]> createTrades() {
		HashMap<Integer, VillagerUtil.TradeFactory[]> trades = new HashMap<>();
		trades.put(1, new VillagerUtil.TradeFactory[]{
				sellForEmeralds(ObjectRegistry.RED_GRAPE_SEEDS.get(), 1, 1, 8, 1),
				sellForEmeralds(ObjectRegistry.WHITE_GRAPE_SEEDS.get(), 1, 1, 8, 1),
				sellForEmeralds(ObjectRegistry.TAIGA_RED_GRAPE_SEEDS.get(), 1, 1, 8, 1),
				sellForEmeralds(ObjectRegistry.TAIGA_WHITE_GRAPE_SEEDS.get(), 1, 1, 8, 1),
				sellForEmeralds(ObjectRegistry.SAVANNA_RED_GRAPE_SEEDS.get(), 1, 1, 8, 1),
				sellForEmeralds(ObjectRegistry.SAVANNA_WHITE_GRAPE_SEEDS.get(), 1, 1, 8, 1),
				sellForEmeralds(ObjectRegistry.JUNGLE_RED_GRAPE_SEEDS.get(), 1, 1, 8, 1),
				sellForEmeralds(ObjectRegistry.JUNGLE_WHITE_GRAPE.get(), 1, 1, 8, 1),
				sellForEmeralds(ObjectRegistry.DARK_CHERRY_SAPLING.get(), 3, 1, 8, 1),
				sellForEmeralds(ObjectRegistry.APPLE_TREE_SAPLING.get(), 5, 1, 8, 1),
				sellForEmeralds(ObjectRegistry.RED_GRAPE.get(), 2, 1, 8, 1),
				sellForEmeralds(ObjectRegistry.RED_GRAPEJUICE.get(), 4, 1, 8, 1),
				sellForEmeralds(ObjectRegistry.WHITE_GRAPEJUICE.get(), 4, 1, 8, 1),
				sellForEmeralds(ObjectRegistry.RED_SAVANNA_GRAPEJUICE.get(), 4, 1, 8, 1),
				sellForEmeralds(ObjectRegistry.WHITE_TAIGA_GRAPEJUICE.get(), 4, 1, 8, 1),
				sellForEmeralds(ObjectRegistry.RED_JUNGLE_GRAPEJUICE.get(), 4, 1, 8, 1),
				sellForEmeralds(ObjectRegistry.COARSE_DIRT_SLAB.get(), 1, 3, 8, 1),
				sellForEmeralds(ObjectRegistry.GRASS_SLAB.get(), 1, 3, 8, 1),
				sellForEmeralds(ObjectRegistry.DARK_CHERRY_PLANKS.get(), 3, 4, 8, 1),
				sellForEmeralds(ObjectRegistry.CHERRY_WINE.get(), 1, 1, 8, 1)
		});
		return trades;
	}

	private static VillagerUtil.TradeFactory sellForEmeralds(net.minecraft.world.level.ItemLike item, int emeraldCost, int count, int maxUses, int experience) {
		return (entity, random) -> new MerchantOffer(new ItemCost(Items.EMERALD, emeraldCost), new ItemStack(item, count), maxUses, experience, 0.05F);
	}

	public WanderingWinemakerEntity(EntityType<? extends WanderingWinemakerEntity> entityType, Level world) {
		super(entityType, world);
	}

	@Override
	protected void updateTrades(ServerLevel serverLevel) {
		if (this.offers == null) {
			this.offers = new MerchantOffers();
		}
		VillagerUtil.TradeFactory[] trades = TRADES.get(1);
		for (int i = 0; i < Math.min(8, trades.length); i++) {
			MerchantOffer offer = trades[i].getOffer(this, this.getRandom());
			if (offer != null) {
				this.offers.add(offer);
			}
		}
	}
}
