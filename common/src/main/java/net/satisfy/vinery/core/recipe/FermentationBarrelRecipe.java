package net.satisfy.vinery.core.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.PlacementInfo;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeBookCategory;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.satisfy.vinery.core.recipe.input.FermentationBarrelRecipeInput;
import net.satisfy.vinery.core.registry.ObjectRegistry;
import net.satisfy.vinery.core.registry.RecipeTypesRegistry;
import org.jetbrains.annotations.NotNull;

public class FermentationBarrelRecipe implements Recipe<FermentationBarrelRecipeInput> {
    private final NonNullList<Ingredient> inputs;
    private final ItemStackTemplate output;
    private final FermentationBarrelRecipeInput.JuiceData juiceData;
    private final boolean wineBottleRequired;
    private static final RecipeBookCategory RECIPE_BOOK_CATEGORY = new RecipeBookCategory();
    public static RecipeType<FermentationBarrelRecipe> Type = RecipeTypesRegistry.FERMENTATION_BARREL_RECIPE_TYPE.get();

    public FermentationBarrelRecipe(NonNullList<Ingredient> inputs, FermentationBarrelRecipeInput.JuiceData data, ItemStackTemplate output, boolean wineBottleRequired) {
        this.inputs = inputs;
        this.juiceData = data;
        this.output = output;
        this.wineBottleRequired = wineBottleRequired;
    }

    public FermentationBarrelRecipeInput.JuiceData getJuiceData() {
        return this.juiceData;
    }

    public boolean isWineBottleRequired() {
        return wineBottleRequired;
    }

    @Override
    public boolean matches(FermentationBarrelRecipeInput input, Level world) {
        if (this.juiceData.amount() > 0) {
            if (input.data().amount() < this.juiceData.amount()) return false;
            if (!this.juiceData.type().equals(input.data().type())) return false;
        }

        if (this.wineBottleRequired) {
            ItemStack wineBottle = input.getItem(FermentationBarrelRecipeInput.WINE_BOTTLE_SLOT);
            if (wineBottle.isEmpty() || !wineBottle.is(ObjectRegistry.WINE_BOTTLE.get())) return false;
        }

        boolean[] matchedIngredients = new boolean[this.inputs.size()];
        for (ItemStack itemStack : input.getIngredientSlots()) {
            if (itemStack.isEmpty()) continue;

            boolean matched = false;
            for (int ingredientIndex = 0; ingredientIndex < this.inputs.size(); ingredientIndex++) {
                if (!matchedIngredients[ingredientIndex] && this.inputs.get(ingredientIndex).test(itemStack)) {
                    matchedIngredients[ingredientIndex] = true;
                    matched = true;
                    break;
                }
            }
            if (!matched) return false;
        }

        for (boolean matched : matchedIngredients) {
            if (!matched) return false;
        }
        return true;
    }

    @Override
    public @NotNull ItemStack assemble(FermentationBarrelRecipeInput input) {
        return this.output.create();
    }

    public @NotNull NonNullList<Ingredient> getIngredients() {
        return this.inputs;
    }

    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    public @NotNull ItemStack getResultItem(HolderLookup.Provider registryAccess) {
        return this.output.create();
    }

    public NonNullList<Ingredient> getInputs() {
        return inputs;
    }

    private ItemStackTemplate getOutputTemplate() {
        return this.output;
    }

    @Override
    public @NotNull RecipeSerializer<FermentationBarrelRecipe> getSerializer() {
        return RecipeTypesRegistry.FERMENTATION_BARREL_RECIPE_SERIALIZER.get();
    }

    @Override
    public @NotNull RecipeType<FermentationBarrelRecipe> getType() {
        return RecipeTypesRegistry.FERMENTATION_BARREL_RECIPE_TYPE.get();
    }

    @Override
    public boolean isSpecial() {
        return true;
    }

    @Override
    public boolean showNotification() {
        return false;
    }

    @Override
    public @NotNull String group() {
        return "";
    }

    @Override
    public @NotNull PlacementInfo placementInfo() {
        return PlacementInfo.create(this.inputs);
    }

    @Override
    public @NotNull RecipeBookCategory recipeBookCategory() {
        return RECIPE_BOOK_CATEGORY;
    }

    public static RecipeSerializer<FermentationBarrelRecipe> serializer() {
        return new RecipeSerializer<>(codec(), streamCodec());
    }

    private static MapCodec<FermentationBarrelRecipe> codec() {
        return RecordCodecBuilder.mapCodec(instance -> instance.group(
                Ingredient.CODEC.listOf().fieldOf("ingredients")
                        .xmap(list -> {
                                    NonNullList<Ingredient> ingredients = NonNullList.create();
                                    ingredients.addAll(list);
                                    return ingredients;
                                },
                                ingredients -> ingredients)
                        .forGetter(FermentationBarrelRecipe::getInputs),
                FermentationBarrelRecipeInput.JuiceData.CODEC.fieldOf("juice").forGetter(FermentationBarrelRecipe::getJuiceData),
                ItemStackTemplate.CODEC.fieldOf("result").forGetter(FermentationBarrelRecipe::getOutputTemplate),
                wineBottleCodec().fieldOf("wine_bottle").forGetter(FermentationBarrelRecipe::isWineBottleRequired)
        ).apply(instance, FermentationBarrelRecipe::new));
    }

    private static MapCodec<Boolean> wineBottleCodec() {
        return RecordCodecBuilder.mapCodec(inst ->
                inst.group(Codec.BOOL.fieldOf("required").forGetter(required -> required))
                        .apply(inst, required -> required)
        );
    }

    private static StreamCodec<RegistryFriendlyByteBuf, FermentationBarrelRecipe> streamCodec() {
        return StreamCodec.of(
                (buf, recipe) -> {
                    buf.writeVarInt(recipe.inputs.size());
                    for (Ingredient ingredient : recipe.inputs) {
                        Ingredient.CONTENTS_STREAM_CODEC.encode(buf, ingredient);
                    }
                    FermentationBarrelRecipeInput.JuiceData.STREAM_CODEC.encode(buf, recipe.getJuiceData());
                    ItemStackTemplate.STREAM_CODEC.encode(buf, recipe.output);
                    buf.writeBoolean(recipe.wineBottleRequired);
                },
                buf -> {
                    int size = buf.readVarInt();
                    NonNullList<Ingredient> inputs = NonNullList.create();
                    for (int i = 0; i < size; i++) {
                        inputs.add(Ingredient.CONTENTS_STREAM_CODEC.decode(buf));
                    }

                    FermentationBarrelRecipeInput.JuiceData juiceData =
                            FermentationBarrelRecipeInput.JuiceData.STREAM_CODEC.decode(buf);
                    ItemStackTemplate output = ItemStackTemplate.STREAM_CODEC.decode(buf);
                    boolean wineBottleRequired = buf.readBoolean();

                    return new FermentationBarrelRecipe(inputs, juiceData, output, wineBottleRequired);
                }
        );
    }
}
