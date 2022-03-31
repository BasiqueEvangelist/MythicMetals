package nourl.mythicmetals.mixin;

import com.google.common.collect.ImmutableMap;
import net.minecraft.client.resource.language.TranslationStorage;
import nourl.mythicmetals.MythicMetals;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashMap;
import java.util.Map;

@Mixin(TranslationStorage.class)
public class TranslationStorageMixin {

    @Mutable
    @Shadow
    @Final
    private Map<String, String> translations;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void addTranslations(Map<String, String> translations, boolean rightToLeft, CallbackInfo ci) {
        if(!MythicMetals.CONFIG.disableFunny) return;

        var builder = new HashMap<>(translations);
        builder.put("item.mythicmetals.durasteel_ingot", "Dura-Chan");
        builder.put("item.mythicmetals.banglum_chunk", "Windy Made This");
        builder.put("item.mythicmetals.osmium_ingot", "Glisconium");
        builder.put("item.mythicmetals.unobtainium", "Obamium");
        builder.put("item.mythicmetals.runite_ingot", "Mint Chocolate Bar");
        builder.put("item.mythicmetals.stormyx_ingot", "Candy Bar");
        builder.put("item.mythicmetals.raw_stormyx", "Bubble Gum");
        builder.put("item.mythicmetals.midas_gold_ingot", "Butter");

        this.translations = builder;

    }

}
