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
import net.satisfy.vinery.core.recipe.input.ApplePressFermentingRecipeInput;
import net.satisfy.vinery.core.registry.RecipeTypesRegistry;
import org.jetbrains.annotations.NotNull;

public class ApplePressFermentingRecipe implements Recipe<ApplePressFermentingRecipeInput> {
    public final Ingredient input;
    private final ItemStackTemplate output;
    private final boolean requiresBottle;
    private static final RecipeBookCategory RECIPE_BOOK_CATEGORY = new RecipeBookCategory();
    public static RecipeType<ApplePressFermentingRecipe> Type = RecipeTypesRegistry.APPLE_PRESS_FERMENTING_RECIPE_TYPE.get();

    public ApplePressFermentingRecipe(Ingredient input, ItemStackTemplate output, boolean requiresBottle) {
        this.input = input;
        this.output = output;
        this.requiresBottle = requiresBottle;
    }

    public boolean requiresBottle() {
        return requiresBottle;
    }

    @Override
    public boolean matches(ApplePressFermentingRecipeInput inventory, Level world) {
        return input.test(inventory.getItem(0));
    }

    @Override
    public @NotNull ItemStack assemble(ApplePressFermentingRecipeInput container) {
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

    public Ingredient getInput() {
        return input;
    }

    public ItemStack getOutput() {
        return output.create();
    }

    private ItemStackTemplate getOutputTemplate() {
        return output;
    }

    public boolean isRequiresBottle() {
        return requiresBottle;
    }

    @Override
    public @NotNull RecipeSerializer<ApplePressFermentingRecipe> getSerializer() {
        return RecipeTypesRegistry.APPLE_PRESS_FERMENTING_RECIPE_SERIALIZER.get();
    }

    @Override
    public @NotNull RecipeType<ApplePressFermentingRecipe> getType() {
        return RecipeTypesRegistry.APPLE_PRESS_FERMENTING_RECIPE_TYPE.get();
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

    public static RecipeSerializer<ApplePressFermentingRecipe> serializer() {
        return new RecipeSerializer<>(codec(), streamCodec());
    }

    private static MapCodec<ApplePressFermentingRecipe> codec() {
        return RecordCodecBuilder.mapCodec(inst -> inst.group(
                Ingredient.CODEC.fieldOf("input").forGetter(ApplePressFermentingRecipe::getInput),
                ItemStackTemplate.CODEC.fieldOf("output").forGetter(ApplePressFermentingRecipe::getOutputTemplate),
                wineBottleCodec().fieldOf("wine_bottle").forGetter(ApplePressFermentingRecipe::isRequiresBottle)
        ).apply(inst, ApplePressFermentingRecipe::new));
    }

    private static MapCodec<Boolean> wineBottleCodec() {
        return RecordCodecBuilder.mapCodec(inst ->
                inst.group(Codec.BOOL.fieldOf("required").forGetter(required -> required))
                        .apply(inst, required -> required)
        );
    }

    private static StreamCodec<RegistryFriendlyByteBuf, ApplePressFermentingRecipe> streamCodec() {
        return StreamCodec.of(
                (buf, recipe) -> {
                    Ingredient.CONTENTS_STREAM_CODEC.encode(buf, recipe.getInput());
                    ItemStackTemplate.STREAM_CODEC.encode(buf, recipe.getOutputTemplate());
                    buf.writeBoolean(recipe.isRequiresBottle());
                },
                buf -> new ApplePressFermentingRecipe(
                        Ingredient.CONTENTS_STREAM_CODEC.decode(buf),
                        ItemStackTemplate.STREAM_CODEC.decode(buf),
                        buf.readBoolean()
                )
        );
    }
}
