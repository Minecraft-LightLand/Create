package com.simibubi.create.content.contraptions.processing.fan;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import com.simibubi.create.content.contraptions.particle.AirFlowParticle;
import com.simibubi.create.content.contraptions.processing.InWorldProcessing;

import com.simibubi.create.content.contraptions.processing.ProcessingRecipe;
import com.simibubi.create.foundation.item.ItemHelper;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.RecipeWrapper;

/**
 * lower priority will be checked first
 */
public abstract class FanProcessingType extends InWorldProcessing.Type implements Comparable<FanProcessingType>{

	public static final TreeSet<FanProcessingType> ALL_TYPES = new TreeSet<>();

	public static final RecipeWrapper WRAPPER = new RecipeWrapper(new ItemStackHandler(1));

	public static final SplashingFanType SPLASHING = new SplashingFanType("SPLASHING", -2000);
	public static final SoulSmokingFanType SOUL_SMOKING = new SoulSmokingFanType("SOUL_SMOKING", -1000);
	public static final SmokingFanType SMOKING = new SmokingFanType("SMOKING", 0);
	public static final BlastingFanType BLASTING = new BlastingFanType("BLASTING", 1000);

	private final int priority;

	public FanProcessingType(String name, int priority) {
		super(name);
		this.priority = priority;
		ALL_TYPES.add(this);
	}

	public abstract boolean isApplicable(BlockState blockState, FluidState fluidState);

	public abstract void affectEntity(Entity entity, Level world);

	public abstract boolean canProcess(ItemStack stack, Level world);

	public abstract List<ItemStack> process(ItemStack stack, Level world);

	public abstract void spawnParticlesForProcessing(Level world, Vec3 vec);

	public abstract void morphType(AirFlowParticle particle, double distance);

	public int compareTo(FanProcessingType o) {
		return Integer.compare(priority, o.priority);
	}

}
