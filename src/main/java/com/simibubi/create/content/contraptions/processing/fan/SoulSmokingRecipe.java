package com.simibubi.create.content.contraptions.processing.fan;

import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.content.contraptions.processing.ProcessingRecipe;
import com.simibubi.create.content.contraptions.processing.ProcessingRecipeBuilder.ProcessingRecipeParams;

import net.minecraft.world.level.Level;
import net.minecraftforge.items.wrapper.RecipeWrapper;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class SoulSmokingRecipe extends FanProcessingRecipe {

	public SoulSmokingRecipe(ProcessingRecipeParams params) {
		super(AllRecipeTypes.SOUL_SMOKING, params);
	}

}
