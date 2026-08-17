package net.darkhood135.projectbiohazard;

import net.darkhood135.projectbiohazard.client.ModArmPoses;
import net.darkhood135.projectbiohazard.entity.ModEntities;
import net.darkhood135.projectbiohazard.entity.client.TZombieRenderer;
import net.darkhood135.projectbiohazard.item.ModItems;
import net.darkhood135.projectbiohazard.item.custom.FirstAidSprayItem;
import net.darkhood135.projectbiohazard.keymapping.ModKeyMappings;
import net.darkhood135.projectbiohazard.particle.AidMistParticle;
import net.darkhood135.projectbiohazard.particle.ModParticles;
import net.darkhood135.projectbiohazard.sound.client.AidSpraySoundInstance;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

// This class will not load on dedicated servers. Accessing client side code from here is safe.
@Mod(value = ProjectBiohazard.MOD_ID, dist = Dist.CLIENT)
// You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
@EventBusSubscriber(modid = ProjectBiohazard.MOD_ID, value = Dist.CLIENT)
public class ProjectBiohazardClient {

    private static final java.util.Set<java.util.UUID> SPRAYING = new java.util.HashSet<>();

    public ProjectBiohazardClient(ModContainer container) {
        // Allows NeoForge to create a config screen for this mod's configs.
        // The config screen is accessed by going to the Mods screen > clicking on your mod > clicking on config.
        // Do not forget to add translations for your config options to the en_us.json file.
        container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);

        ModKeyMappings.register();
    }

    @SubscribeEvent
    static void onClientSetup(FMLClientSetupEvent event) {
        // Some client setup code
        EntityRenderers.register(ModEntities.T_VIRUS_ZOMBIE.get(), TZombieRenderer::new);
    }


    @SubscribeEvent
    public static void registerKeybind(RegisterKeyMappingsEvent event) {
        event.register(ModKeyMappings.PRESS_RELOAD_KEY.get());
    }

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post event) {
        Minecraft mc = Minecraft.getInstance();

        if (ModKeyMappings.PRESS_RELOAD_KEY.get().consumeClick()) {
            // On the client in here.
            Minecraft.getInstance().player.sendSystemMessage(Component.literal("This will eventually do something!"));
            //ClientPacketDistributor.sendToServer(new TestPacketC2S("Joe", 67));
        }

        if (mc.level != null) {
            for (Player p : mc.level.players()) {
                boolean spraying = p.isUsingItem() && p.getUseItem().getItem() instanceof FirstAidSprayItem;
                if (spraying) {
                    if (SPRAYING.add(p.getUUID())) {                       // add() true only the first tick they spray
                        mc.getSoundManager().play(new AidSpraySoundInstance(p));
                    }
                } else {
                    SPRAYING.remove(p.getUUID());                          // let their next spray re-trigger
                }
            }
        }
    }

    @SubscribeEvent
    public static void registerParticleFactories(RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(ModParticles.AID_MIST_PARTICLES.get(), AidMistParticle.Provider::new);
    }

    @SubscribeEvent
    static void registerClientExtensions(RegisterClientExtensionsEvent event) {
        event.registerItem(new IClientItemExtensions() {
            @Override
            public HumanoidModel.ArmPose getArmPose(LivingEntity entity, InteractionHand hand, ItemStack stack) {
                return ModArmPoses.PISTOL_AIM.getValue();
            }
        }, ModItems.FIRST_AID_SPRAY.get());
    }



}
