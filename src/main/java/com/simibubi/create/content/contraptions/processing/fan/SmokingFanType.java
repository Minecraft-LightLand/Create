package com.simibubi.create.content.contraptions.processing.fan;

import java.util.List;
import java.util.Optional;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.contraptions.components.fan.AirCurrent;
import com.simibubi.create.content.contraptions.particle.AirFlowParticle;
import com.simibubi.create.content.contraptions.processing.InWorldProcessing;
import com.simibubi.create.content.contraptions.processing.burner.BlazeBurnerBlock;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SmokingRecipe;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.Vec3;

public class SmokingFanType extends FanProcessingType {

	public SmokingFanType(String name, int priority) {
		super(name, priority);
	}

	@Override
	public boolean isApplicable(BlockState blockState, FluidState fluidState) {
		Block block = blockState.getBlock();
		if (block == Blocks.FIRE) {
			return true;
		}
		if (AllBlocks.LIT_BLAZE_BURNER.has(blockState)) {
			return true;
		}
		if (BlockTags.CAMPFIRES.contains(block) && blockState.getOptionalValue(CampfireBlock.LIT).orElse(false)) {
			return true;
		}
		return BlazeBurnerBlock.getHeatLevelOf(blockState) == BlazeBurnerBlock.HeatLevel.SMOULDERING;
	}

	@Override
	public void affectEntity(Entity entity, Level world) {
		if (!entity.fireImmune()) {
			entity.setSecondsOnFire(2);
			entity.hurt(AirCurrent.damageSourceFire, 2);
		}
	}

	@Override
	public boolean canProcess(ItemStack stack, Level world) {
		WRAPPER.setItem(0, stack);
		Optional<SmokingRecipe> recipe = world.getRecipeManager()
				.getRecipeFor(RecipeType.SMOKING, WRAPPER, world);
		return recipe.isPresent();
	}

	@Override
	public List<ItemStack> process(ItemStack stack, Level world) {
		WRAPPER.setItem(0, stack);
		Optional<SmokingRecipe> smokingRecipe = world.getRecipeManager().getRecipeFor(RecipeType.SMOKING, WRAPPER, world);
		return smokingRecipe.map(recipe -> InWorldProcessing.applyRecipeOn(stack, recipe)).orElse(null);
	}

	@Override
	public void spawnParticlesForProcessing(Level world, Vec3 vec) {
		world.addParticle(ParticleTypes.POOF, vec.x, vec.y + .25f, vec.z, 0, 1 / 16f, 0);
	}

	@Override
	public void morphType(AirFlowParticle particle, double distance) {
		particle.setAppearance(0x0, 0x555555, 1f, 3);
		particle.dupeParticle(1 / 32f, ParticleTypes.SMOKE, .125f);
		particle.dupeParticle(1 / 32f, ParticleTypes.LARGE_SMOKE, .125f);
	}

}
