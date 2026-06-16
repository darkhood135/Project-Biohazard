package net.darkhood135.projectbiohazard.item.custom;

import net.darkhood135.projectbiohazard.component.ModDataComponentTypes;
import net.darkhood135.projectbiohazard.item.ModItems;
import net.darkhood135.projectbiohazard.item.custom.effects.HerbEffects;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.Level;

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
            return InteractionResult.PASS;     // empty vial -> right-click does nothing
        }
        player.startUsingItem(hand);            // filled -> begin the drink action
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
}
