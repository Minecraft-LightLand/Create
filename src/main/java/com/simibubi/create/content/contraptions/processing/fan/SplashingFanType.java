package com.simibubi.create.content.contraptions.processing.fan;

import java.util.List;
import java.util.Optional;

import com.mojang.math.Vector3f;
import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.compat.jei.category.animations.AnimatedKinetics;
import com.simibubi.create.content.contraptions.particle.AirFlowParticle;
import com.simibubi.create.content.contraptions.processing.InWorldProcessing;
import com.simibubi.create.foundation.gui.element.GuiGameElement;
import com.simibubi.create.foundation.utility.Color;

import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.Vec3;

public class SplashingFanType extends SpecialFanType<SplashingRecipe> {

	public SplashingFanType(String name, int priority) {
		super(name, priority, new RecipeInfo<>("fan_washing", Items.WATER_BUCKET, SplashingRecipe.class,
				AllRecipeTypes.SPLASHING,
				() -> GuiGameElement.of(Fluids.WATER).scale(24).atLocal(0, 0, 2).lighting(AnimatedKinetics.DEFAULT_LIGHTING)));
	}

	@Override
	public boolean isApplicable(BlockState blockState, FluidState fluidState) {
		return fluidState.getType() == Fluids.WATER || fluidState.getType() == Fluids.FLOWING_WATER;
	}

	@Override
	public void affectEntity(Entity entity, Level world) {
		if (entity instanceof EnderMan || entity.getType() == EntityType.SNOW_GOLEM
				|| entity.getType() == EntityType.BLAZE) {
			entity.hurt(DamageSource.DROWN, 2);
		}
		if (!entity.isOnFire())
			return;
		entity.clearFire();
		world.playSound(null, entity.blockPosition(), SoundEvents.GENERIC_EXTINGUISH_FIRE,
				SoundSource.NEUTRAL, 0.7F, 1.6F + (world.random.nextFloat() - world.random.nextFloat()) * 0.4F);
	}

	@Override
	public void spawnParticlesForProcessing(Level world, Vec3 vec) {
		Vector3f color = new Color(0x0055FF).asVectorF();
		world.addParticle(new DustParticleOptions(color, 1), vec.x + (world.random.nextFloat() - .5f) * .5f,
				vec.y + .5f, vec.z + (world.random.nextFloat() - .5f) * .5f, 0, 1 / 8f, 0);
		world.addParticle(ParticleTypes.SPIT, vec.x + (world.random.nextFloat() - .5f) * .5f, vec.y + .5f,
				vec.z + (world.random.nextFloat() - .5f) * .5f, 0, 1 / 8f, 0);
	}

	@Override
	public void morphType(AirFlowParticle particle, double distance) {
		particle.setAppearance(0x4499FF, 0x2277FF, 1f, 3);
		particle.dupeParticle(1 / 32f, ParticleTypes.BUBBLE, .125f);
		particle.dupeParticle(1 / 32f, ParticleTypes.BUBBLE_POP, .125f);
	}
}
