package com.simibubi.create.content.contraptions.processing.fan;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import com.simibubi.create.content.contraptions.components.fan.AirCurrent;
import com.simibubi.create.content.contraptions.particle.AirFlowParticle;
import com.simibubi.create.content.contraptions.processing.InWorldProcessing;
import com.simibubi.create.content.contraptions.processing.burner.BlazeBurnerBlock;

import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.BlastingRecipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SmeltingRecipe;
import net.minecraft.world.item.crafting.SmokingRecipe;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.Vec3;

public class BlastingFanType extends FanProcessingType {

	public BlastingFanType(String name, int priority) {
		super(name, priority);
	}

	@Override
	public boolean isApplicable(BlockState blockState, FluidState fluidState) {
		Block block = blockState.getBlock();
		if (block == Blocks.LAVA) {
			return true;
		}
		return BlazeBurnerBlock.getHeatLevelOf(blockState).isAtLeast(BlazeBurnerBlock.HeatLevel.FADING);
	}

	@Override
	public void affectEntity(Entity entity, Level world) {
		if (!entity.fireImmune()) {
			entity.setSecondsOnFire(10);
			entity.hurt(AirCurrent.damageSourceLava, 4);
		}
	}

	@Override
	public boolean canProcess(ItemStack stack, Level world) {
		WRAPPER.setItem(0, stack);
		Optional<SmeltingRecipe> smeltingRecipe = world.getRecipeManager()
				.getRecipeFor(RecipeType.SMELTING, WRAPPER, world);

		if (smeltingRecipe.isPresent())
			return true;

		WRAPPER.setItem(0, stack);
		Optional<BlastingRecipe> blastingRecipe = world.getRecipeManager()
				.getRecipeFor(RecipeType.BLASTING, WRAPPER, world);

		if (blastingRecipe.isPresent())
			return true;

		return !stack.getItem()
				.isFireResistant();
	}

	@Override
	public List<ItemStack> process(ItemStack stack, Level world) {

		WRAPPER.setItem(0, stack);
		Optional<SmokingRecipe> smokingRecipe = world.getRecipeManager()
				.getRecipeFor(RecipeType.SMOKING, WRAPPER, world);
		if (smokingRecipe.isEmpty()) {
			WRAPPER.setItem(0, stack);
			Optional<SmeltingRecipe> smeltingRecipe = world.getRecipeManager()
					.getRecipeFor(RecipeType.SMELTING, WRAPPER, world);

			if (smeltingRecipe.isPresent())
				return InWorldProcessing.applyRecipeOn(stack, smeltingRecipe.get());

			WRAPPER.setItem(0, stack);
			Optional<BlastingRecipe> blastingRecipe = world.getRecipeManager()
					.getRecipeFor(RecipeType.BLASTING, WRAPPER, world);

			if (blastingRecipe.isPresent())
				return InWorldProcessing.applyRecipeOn(stack, blastingRecipe.get());
		}

		return Collections.emptyList();
	}

	@Override
	public void spawnParticlesForProcessing(Level world, Vec3 vec) {
		world.addParticle(ParticleTypes.LARGE_SMOKE, vec.x, vec.y + .25f, vec.z, 0, 1 / 16f, 0);
	}

	@Override
	public void morphType(AirFlowParticle particle, double distance) {
		particle.setAppearance(0xFF4400, 0xFF8855, .5f, 3);
		particle.dupeParticle(1 / 32f, ParticleTypes.FLAME, 0.25f);
		particle.dupeParticle(1 / 16f, new BlockParticleOption(ParticleTypes.BLOCK, Blocks.LAVA.defaultBlockState()), 0.25f);
	}
}
