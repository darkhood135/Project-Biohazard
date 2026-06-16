package net.darkhood135.projectbiohazard.keymapping;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.neoforged.neoforge.common.util.Lazy;
import org.lwjgl.glfw.GLFW;

public class ModKeyMappings {

    private static final KeyMapping RELOAD_KEYMAPPING = new KeyMapping("key.projectbiohazard.reload_key",
            InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_R, KeyMapping.Category.MISC);

    public static final Lazy<KeyMapping> PRESS_RELOAD_KEY = Lazy.of(() -> RELOAD_KEYMAPPING);

    public static void register() {

    }
}
