package com.simibubi.create.content.contraptions.processing.fan;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.content.contraptions.processing.InWorldProcessing;
import com.simibubi.create.foundation.gui.element.GuiGameElement;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;

/**
 * procedure to add new recipe:
 * 1. create a subclass of SpecialFanType
 * 2. create a subclass of FanProcessingRecipe
 * 3. add the new recipe to AllRecipeTypes
 * 4. add translation for JEI (2 entries)
 * */
public abstract class SpecialFanType<R extends FanProcessingRecipe> extends FanProcessingType {

	public record RecipeInfo<R extends Recipe<?>>(String id, ItemLike icon, Class<R> recipeClass,
												  AllRecipeTypes recipeType,
												  Supplier<GuiGameElement.GuiRenderBuilder> display) {
	}

	public final RecipeInfo<R> info;

	public SpecialFanType(String name, int priority, RecipeInfo<R> info) {
		super(name, priority);
		this.info = info;
	}

	@Override
	public boolean canProcess(ItemStack stack, Level world) {
		WRAPPER.setItem(0, stack);
		Optional<R> recipe = info.recipeType.find(WRAPPER, world);
		return recipe.isPresent();
	}

	@Override
	public List<ItemStack> process(ItemStack stack, Level world) {
		WRAPPER.setItem(0, stack);
		Optional<R> recipe = info.recipeType.find(WRAPPER, world);
		return recipe.map(r -> InWorldProcessing.applyRecipeOn(stack, r)).orElse(null);
	}



}
