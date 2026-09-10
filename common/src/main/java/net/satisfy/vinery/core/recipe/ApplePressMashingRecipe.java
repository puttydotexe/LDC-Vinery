package net.satisfy.vinery.core.recipe;

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
import net.satisfy.vinery.core.recipe.input.ApplePressMashingRecipeInput;
import net.satisfy.vinery.core.registry.RecipeTypesRegistry;
import org.jetbrains.annotations.NotNull;

public class ApplePressMashingRecipe implements Recipe<ApplePressMashingRecipeInput> {
    public final Ingredient input;
    private final ItemStackTemplate output;
    private static final RecipeBookCategory RECIPE_BOOK_CATEGORY = new RecipeBookCategory();
    public static RecipeType<ApplePressMashingRecipe> Type = RecipeTypesRegistry.APPLE_PRESS_MASHING_RECIPE_TYPE.get();

    public ApplePressMashingRecipe(Ingredient input, ItemStackTemplate output) {
        this.input = input;
        this.output = output;
    }

    @Override
    public boolean matches(ApplePressMashingRecipeInput inventory, Level world) {
        return input.test(inventory.getItem(0));
    }

    @Override
    public @NotNull ItemStack assemble(ApplePressMashingRecipeInput container) {
        return this.output.create();
    }

    public @NotNull NonNullList<Ingredient> getIngredients() {
        NonNullList<Ingredient> list = NonNullList.create();
        list.add(input);
        return list;
    }

    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    public @NotNull ItemStack getResultItem(HolderLookup.Provider registryAccess) {
        return this.output.create();
    }

    @Override
    public @NotNull RecipeSerializer<ApplePressMashingRecipe> getSerializer() {
        return RecipeTypesRegistry.APPLE_PRESS_MASHING_RECIPE_SERIALIZER.get();
    }

    @Override
    public @NotNull RecipeType<ApplePressMashingRecipe> getType() {
        return RecipeTypesRegistry.APPLE_PRESS_MASHING_RECIPE_TYPE.get();
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
        return PlacementInfo.create(this.input);
    }

    @Override
    public @NotNull RecipeBookCategory recipeBookCategory() {
        return RECIPE_BOOK_CATEGORY;
    }

    public Ingredient getInput() {
        return input;
    }

    public ItemStack getOutput() {
        return output.create();
    }

    private ItemStackTemplate getOutputTemplate() {
        return output;
    }

    public static RecipeSerializer<ApplePressMashingRecipe> serializer() {
        return new RecipeSerializer<>(codec(), streamCodec());
    }

    private static MapCodec<ApplePressMashingRecipe> codec() {
        return RecordCodecBuilder.mapCodec(inst -> inst.group(
                Ingredient.CODEC.fieldOf("input").forGetter(ApplePressMashingRecipe::getInput),
                ItemStackTemplate.CODEC.fieldOf("output").forGetter(ApplePressMashingRecipe::getOutputTemplate)
        ).apply(inst, ApplePressMashingRecipe::new));
    }

    private static StreamCodec<RegistryFriendlyByteBuf, ApplePressMashingRecipe> streamCodec() {
        return StreamCodec.of(
                (buf, recipe) -> {
                    Ingredient.CONTENTS_STREAM_CODEC.encode(buf, recipe.getInput());
                    ItemStackTemplate.STREAM_CODEC.encode(buf, recipe.getOutputTemplate());
                },
                buf -> new ApplePressMashingRecipe(
                        Ingredient.CONTENTS_STREAM_CODEC.decode(buf),
                        ItemStackTemplate.STREAM_CODEC.decode(buf)
                )
        );
    }
}
