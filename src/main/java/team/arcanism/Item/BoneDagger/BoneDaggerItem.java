package team.arcanism.Item.BoneDagger;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class BoneDaggerItem extends Item {
	public BoneDaggerItem(Properties prop) {
		super(prop);
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
		ItemStack itemstack = player.getItemInHand(hand);
		world.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.PLAYER_ATTACK_SWEEP, SoundSource.NEUTRAL, 0.5F, world.random.nextFloat() * 0.25F + 1F);
		if (!world.isClientSide) {
			BoneDaggerEntity dagger = new BoneDaggerEntity(world, player);
			dagger.shootFromRotation(player, player.getXRot(), player.getYRot(), 0F, 1.2F, 1.2F);
			world.addFreshEntity(dagger);
		}

		if (!player.isCreative()) {
			itemstack.shrink(1);
		}
		player.getCooldowns().addCooldown(itemstack.getItem(), 10);
		return InteractionResultHolder.success(itemstack);
	}
}
