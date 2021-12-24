package com.simibubi.create.content.contraptions.processing.fan;

import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.content.contraptions.processing.ProcessingRecipe;

import com.simibubi.create.content.contraptions.processing.ProcessingRecipeBuilder;

import com.simibubi.create.foundation.utility.recipe.IRecipeTypeInfo;

import net.minecraft.world.level.Level;
import net.minecraftforge.items.wrapper.RecipeWrapper;

public class FanProcessingRecipe extends ProcessingRecipe<RecipeWrapper> {

	public FanProcessingRecipe(IRecipeTypeInfo type, ProcessingRecipeBuilder.ProcessingRecipeParams params) {
		super(type, params);
	}

	@Override
	public boolean matches(RecipeWrapper inv, Level worldIn) {
		if (inv.isEmpty())
			return false;
		return ingredients.get(0).test(inv.getItem(0));
	}

	@Override
	protected int getMaxInputCount() {
		return 1;
	}

	@Override
	protected int getMaxOutputCount() {
		return 12;
	}


}
