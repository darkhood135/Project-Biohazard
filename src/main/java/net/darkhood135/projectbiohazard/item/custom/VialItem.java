package net.darkhood135.projectbiohazard.item.custom;

import net.darkhood135.projectbiohazard.component.ModDataComponentTypes;
import net.darkhood135.projectbiohazard.item.ModItems;
import net.darkhood135.projectbiohazard.item.custom.effects.HerbEffects;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BeehiveBlock;
import net.minecraft.world.level.block.LayeredCauldronBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

public class VialItem extends Item {
    public VialItem(Properties properties) {
        super(properties);
    }

    public enum Herb {
        GREEN('g', false, "herb.projectbiohazard.green"),
        RED('r', true, "herb.projectbiohazard.red"),
        YELLOW('y', false, "herb.projectbiohazard.yellow"),
        BLUE('b', true, "herb.projectbiohazard.blue");

        private final char key;
        private final boolean unique;
        private final String translationKey;

        Herb(char key, boolean unique, String translationKey) {
            this.key = key;
            this.unique = unique;
            this.translationKey = translationKey;
        }

        public char getKey() {
            return key;
        }

        public Component getDisplayName() {
            return Component.translatable(translationKey);
        }

        public boolean isUnique() {
            return unique;
        }

        public static Herb fromKey(char key) {
            for (Herb herb : values()) {
                if(herb.key == key) {
                    return herb;
                }
            }
            throw new IllegalArgumentException("Unknown herb key: " + key);
        }

        public static List<Herb> parse(String combination) {
            List<Herb> herbs = new ArrayList<>();
            for (char c : combination.toCharArray()) {
                herbs.add(fromKey(c));
            }
            return herbs;
        }

        public static String toKey(List<Herb> herbs) {
            List<Herb> sorted = new ArrayList<>(herbs);
            Collections.sort(sorted);
            StringBuilder builder = new StringBuilder();
            for (Herb herb : sorted) {
                builder.append(herb.key);
            }
            return builder.toString();
        }
    }

    public static void applyVialEffects(ServerPlayer player, List<Herb> herbs) {
        int greens  = Collections.frequency(herbs, Herb.GREEN);
        int yellows = Collections.frequency(herbs, Herb.YELLOW);
        boolean red  = herbs.contains(Herb.RED);    // the amplifier
        boolean blue = herbs.contains(Herb.BLUE);

        // Green healing, amplified when together or by red
        if (greens > 0) {
            HerbEffects.useGreenHerb(player, greens, red);
        }

        // Blue cleansing
        if (blue) {
            HerbEffects.useBlueHerb(player, red);
        }

        // Yellow absorption or max HP increase
        if (yellows > 0) {
            // Uses a FOR loop to ensure no config bypass
            for (int i = 0; i < yellows; i++) {
                HerbEffects.useYellowHerb(player, red);
            }
        }
    }

    @Override
    public Component getName(ItemStack itemStack) {
        String combo = itemStack.get(ModDataComponentTypes.HERB_VIAL_COMBINATION);
        if (combo != null && !combo.isEmpty()) {
            return Component.translatable("item.projectbiohazard.herb_vial");
        } else {
            return super.getName(itemStack);
        }
    }

    @Override
    public void appendHoverText(ItemStack itemStack, TooltipContext context, TooltipDisplay display, Consumer<Component> builder, TooltipFlag tooltipFlag) {
        String combo = itemStack.get(ModDataComponentTypes.HERB_VIAL_COMBINATION);
        if (combo != null && !combo.isEmpty()) {
            List<Herb> herbs = Herb.parse(combo);
            MutableComponent line = Component.empty();
            for (int i = 0; i < herbs.size(); i++) {
                if (i > 0) {
                    line.append("§r§o§7, ");
                }
                line.append(herbs.get(i).getDisplayName());
            }
            builder.accept(line);
        }

        super.appendHoverText(itemStack, context, display, builder, tooltipFlag);
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        String combo = stack.get(ModDataComponentTypes.HERB_VIAL_COMBINATION.get());

        if (combo == null || combo.isEmpty()) {
            // empty vial -> try to fill from a water source
            BlockHitResult hit = getPlayerPOVHitResult(level, player, ClipContext.Fluid.SOURCE_ONLY);
            if (hit.getType() == HitResult.Type.BLOCK
                    && level.getFluidState(hit.getBlockPos()).is(FluidTags.WATER)) {
                if (!level.isClientSide()) {
                    ItemStack waterVial = PotionContents.createItemStack(ModItems.WATER_VIAL.get(), Potions.WATER);
                    player.setItemInHand(hand, ItemUtils.createFilledResult(stack, player, waterVial));
                    level.playSound(null, player.blockPosition(), SoundEvents.BOTTLE_FILL, SoundSource.PLAYERS, 1f, 1f);
                }
                return InteractionResult.SUCCESS;
            }
            return InteractionResult.PASS;          // empty + not aiming at water -> nothing
        }

        player.startUsingItem(hand);                 // herb-filled -> drink (unchanged)
        return InteractionResult.CONSUME;
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
        if (!level.isClientSide() && entity instanceof ServerPlayer player) {
            String combo = stack.get(ModDataComponentTypes.HERB_VIAL_COMBINATION.get());
            applyVialEffects(player, VialItem.Herb.parse(combo));
        }
        return new ItemStack(ModItems.GLASS_VIAL.get());   // back to an empty container
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        BlockState state = level.getBlockState(pos);
        ItemStack stack = context.getItemInHand();
        Player player = context.getPlayer();

        String combo = stack.get(ModDataComponentTypes.HERB_VIAL_COMBINATION.get());
        boolean empty = combo == null || combo.isEmpty();

        if (empty && state.getBlock() instanceof BeehiveBlock
                && state.getValue(BeehiveBlock.HONEY_LEVEL) >= 5) {        // full hive
            if (!level.isClientSide() && player != null) {
                ItemStack honeyVial = new ItemStack(ModItems.HONEY_VIAL.get());
                player.setItemInHand(context.getHand(), ItemUtils.createFilledResult(stack, player, honeyVial));
                level.setBlockAndUpdate(pos, state.setValue(BeehiveBlock.HONEY_LEVEL, 0));   // empty the hive
                level.playSound(null, pos, SoundEvents.BOTTLE_FILL, SoundSource.BLOCKS, 1f, 1f);
            }
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;   // not a full hive -> let other behavior proceed
    }

    public static InteractionResult fillFromCauldron(BlockState state, Level level, BlockPos pos,
                                                     Player player, InteractionHand hand, ItemStack stack) {
        if (!level.isClientSide()) {
            ItemStack waterVial = PotionContents.createItemStack(ModItems.WATER_VIAL.get(), Potions.WATER);
            player.setItemInHand(hand, ItemUtils.createFilledResult(stack, player, waterVial));
            LayeredCauldronBlock.lowerFillLevel(state, level, pos);   // verified — drops the water level
            level.playSound(null, pos, SoundEvents.BOTTLE_FILL, SoundSource.BLOCKS, 1f, 1f);
        }
        return InteractionResult.SUCCESS;
    }
}
