package com.simibubi.create.content.contraptions.processing.fan;

import java.util.List;
import java.util.Optional;

import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.content.contraptions.components.fan.AirCurrent;
import com.simibubi.create.content.contraptions.particle.AirFlowParticle;
import com.simibubi.create.content.contraptions.processing.InWorldProcessing;
import com.simibubi.create.foundation.gui.element.GuiGameElement;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.Vec3;

public class SoulSmokingFanType extends SpecialFanType<SoulSmokingRecipe> {

	public SoulSmokingFanType(String name, int priority) {
		super(name, priority, new RecipeInfo<>("fan_soul_smoking", Items.SOUL_CAMPFIRE, SoulSmokingRecipe.class,
				AllRecipeTypes.SOUL_SMOKING,
				() -> GuiGameElement.of(Blocks.SOUL_FIRE.defaultBlockState()).scale(24).atLocal(0, 0, 2)));
	}

	@Override
	public boolean isApplicable(BlockState blockState, FluidState fluidState) {
		Block block = blockState.getBlock();
		if (block == Blocks.SOUL_FIRE) {
			return true;
		}
		return block == Blocks.SOUL_CAMPFIRE && blockState.getOptionalValue(CampfireBlock.LIT).orElse(false);
	}

	@Override
	public void affectEntity(Entity entity, Level world) {
		if (entity instanceof LivingEntity livingEntity) {
			livingEntity.addEffect(new MobEffectInstance(MobEffects.GLOWING, 400));
		}
		if (!entity.fireImmune()) {
			entity.setSecondsOnFire(10);
			entity.hurt(AirCurrent.damageSourceFire, 4);
		}
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
