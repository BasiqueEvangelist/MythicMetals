package nourl.mythicmetals.utils;

import io.wispforest.owo.itemgroup.OwoItemSettings;
import net.minecraft.block.Block;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.entity.EntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.ConfiguredFeatures;
import net.minecraft.world.gen.feature.PlacedFeature;
import net.minecraft.world.gen.feature.PlacedFeatures;
import nourl.mythicmetals.MythicMetals;
import nourl.mythicmetals.mixin.AccessorEntityModelLayers;

/*
 * This is a helper class containing methods for registering various blocks and items.
 * @author  Noaaan
 */
public class RegistryHelper {

    public static Identifier id(String path) {
        return new Identifier(MythicMetals.MOD_ID, path);
    }

    public static void item(String path, Item item) {
        Registry.register(Registry.ITEM, id(path), item);
    }

    public static void block(String path, Block block) {
        Registry.register(Registry.BLOCK, id(path), block);
        Registry.register(Registry.ITEM, id(path), new BlockItem(block, new OwoItemSettings().group(MythicMetals.TABBED_GROUP).tab(1)));
    }

    public static void block(String path, Block block, boolean fireproof) {
        if (fireproof) {
            Registry.register(Registry.BLOCK, id(path), block);
            Registry.register(Registry.ITEM, id(path), new BlockItem(block, new OwoItemSettings().group(MythicMetals.TABBED_GROUP).tab(1).fireproof()));
        } else {
            block(path, block);
        }
    }
    public static void block(String path, Block block, ItemGroup group) {
        Registry.register(Registry.BLOCK, id(path), block);
        Registry.register(Registry.ITEM, id(path), new BlockItem(block, new Item.Settings().group(group)));
    }

    public static void block(String path, Block block, ItemGroup group, boolean fireproof) {
        if (fireproof) {
            Registry.register(Registry.BLOCK, id(path), block);
            Registry.register(Registry.ITEM, id(path), new BlockItem(block, new Item.Settings().group(group).fireproof()));
        } else {
            block(path, block, group);
        }
    }

    public static void placedFeature(String name, PlacedFeature feature) {
        PlacedFeatures.register(name, feature);
    }

    public static void configuredFeature(String name, ConfiguredFeature<?, ?> feature) {
        ConfiguredFeatures.register(name, feature);
    }

    /* Shoutouts to williewillus for this implementation:
     * https://github.com/VazkiiMods/Botania/blob/1.18.x-fabric/src/main/java/vazkii/botania/client/model/ModModelLayers.java
     */
    public static EntityModelLayer model(String name, String layer) {
        var result = new EntityModelLayer(id(name), layer);
        AccessorEntityModelLayers.getAllModels().add(result);
        return result;
    }

    public static EntityModelLayer model(String name) {
        return model(name, "main");
    }

    public static void entityType(String path, EntityType<?> type){
        Registry.register(Registry.ENTITY_TYPE, RegistryHelper.id(path), type);
    }

    public static RegistryKey<PlacedFeature> placedFeatureKey(String string) {
        return RegistryKey.of(Registry.PLACED_FEATURE_KEY, RegistryHelper.id(string));
    }


}
