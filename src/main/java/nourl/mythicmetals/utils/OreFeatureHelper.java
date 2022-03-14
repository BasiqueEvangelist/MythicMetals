package nourl.mythicmetals.utils;

import com.google.common.collect.ImmutableList;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.Block;
import net.minecraft.structure.rule.RuleTest;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.YOffset;
import net.minecraft.world.gen.decorator.*;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraft.world.gen.feature.PlacedFeature;
import nourl.mythicmetals.MythicMetals;
import nourl.mythicmetals.config.MythicConfig;
import nourl.mythicmetals.config.OreConfig;
import nourl.mythicmetals.config.VariantConfig;

import java.util.List;

public class OreFeatureHelper {
    public static final MythicConfig CONFIG = MythicMetals.CONFIG;

    public static void ore(RegistryKey<PlacedFeature> ore) {
        var blacklist = CONFIG.blacklist.stream()
                .map(element -> (RegistryKey.of(Registry.BIOME_KEY, new Identifier(element))))
                .toList();
        BiomeModifications.addFeature(BiomeSelectors.excludeByKey(blacklist), GenerationStep.Feature.UNDERGROUND_ORES, ore);
    }

    public static void netherOre(RegistryKey<PlacedFeature> ore) {
        BiomeModifications.addFeature(BiomeSelectors.foundInTheNether(), GenerationStep.Feature.UNDERGROUND_DECORATION, ore);
    }

    public static void endOre(RegistryKey<PlacedFeature> ore) {
        BiomeModifications.addFeature(BiomeSelectors.foundInTheEnd(), GenerationStep.Feature.UNDERGROUND_DECORATION, ore);
    }

    public static void modBiomeOres(String modId, String path, RegistryKey<PlacedFeature> ore) {
        if (FabricLoader.getInstance().isModLoaded(modId)) {
            BiomeModifications.addFeature(
                    BiomeSelectors.includeByKey(RegistryKey.of(Registry.BIOME_KEY, new Identifier(modId, path))),
                    GenerationStep.Feature.UNDERGROUND_ORES, ore);
        }
    }

    public static ConfiguredFeature<OreFeatureConfig, ?> createConfiguredFeature(String name, ImmutableList<OreFeatureConfig.Target> targets, OreConfig config) {
        var feature = Feature.ORE.configure(
                new OreFeatureConfig(targets, config.veinSize, config.discardChance));
        RegistryHelper.configuredFeature(name, feature);
        return feature;
    }

    public static ConfiguredFeature<OreFeatureConfig, ?> createConfiguredFeature(String name, RuleTest rule, Block block, OreConfig config) {
        var feature = Feature.ORE.configure(
                new OreFeatureConfig(rule, block.getDefaultState(), config.veinSize, config.discardChance));
        RegistryHelper.configuredFeature(name, feature);
        return feature;
    }

    public static ConfiguredFeature<OreFeatureConfig, ?> createConfiguredFeature(String name, ImmutableList<OreFeatureConfig.Target> targets, VariantConfig config) {
        var feature = Feature.ORE.configure(
                new OreFeatureConfig(targets, config.veinSize, config.discardChance));
        RegistryHelper.configuredFeature(name, feature);
        return feature;
    }

    public static ConfiguredFeature<OreFeatureConfig, ?> createConfiguredFeature(String name, RuleTest rule, Block block, VariantConfig config) {
        var feature = Feature.ORE.configure(
                new OreFeatureConfig(rule, block.getDefaultState(), config.veinSize, config.discardChance));
        RegistryHelper.configuredFeature(name, feature);
        return feature;
    }

    public static PlacedFeature range(String name, ImmutableList<OreFeatureConfig.Target> targets, OreConfig config) {
        var b = config.offset && config.trapezoid; // Check if both offset and trapezoid is being used at the same time.
        if (b) {
            throw new IllegalArgumentException(name + " cannot be offset and trapezoid at the same time.");
        }
        if (config.offset)
            return aboveBottom(name, targets, config);
        if (config.trapezoid)
            return trapezoid(name, targets, config);
        return uniform(name, targets, config);
    }

    public static PlacedFeature range(String name, RuleTest rule, Block block, OreConfig config) {
        var b = config.offset && config.trapezoid; // Check if both offset and trapezoid is being used at the same time.
        if (b) {
            throw new IllegalArgumentException(name + " cannot be offset and use trapezoid at the same time.");
        }
        if (config.offset)
            return aboveBottom(name, rule, block, config);
        if (config.trapezoid)
            return trapezoid(name, rule, block, config);
        return uniform(name, rule, block, config);
    }

    public static PlacedFeature range(String name, ImmutableList<OreFeatureConfig.Target> targets, VariantConfig config) {
        var b = config.offset && config.trapezoid; // Check if both offset and trapezoid is being used at the same time.
        if (b) {
            throw new IllegalArgumentException(name + " cannot be offset and trapezoid at the same time.");
        }
        if (config.offset)
            return aboveBottom(name, targets, config);
        if (config.trapezoid)
            return trapezoid(name, targets, config);
        return uniform(name, targets, config);
    }

    public static PlacedFeature range(String name, RuleTest rule, Block block, VariantConfig config) {
        var b = config.offset && config.trapezoid; // Check if both offset and trapezoid is being used at the same time.
        if (b) {
            throw new IllegalArgumentException(name + " cannot be offset and trapezoid at the same time.");
        }
        if (config.offset)
            return aboveBottom(name, rule, block, config);
        if (config.trapezoid)
            return trapezoid(name, rule, block, config);
        return uniform(name, rule, block, config);
    }

    public static PlacedFeature uniform(String name, ImmutableList<OreFeatureConfig.Target> targets, OreConfig config) {
        var feature = createConfiguredFeature(name, targets, config);
        return feature.withPlacement(modifiers(CountPlacementModifier.of(config.perChunk),
                HeightRangePlacementModifier.uniform(YOffset.fixed(config.bottom), YOffset.fixed(config.top))));
    }

    public static PlacedFeature uniform(String name, ImmutableList<OreFeatureConfig.Target> targets, VariantConfig config) {
        var feature = createConfiguredFeature(name, targets, config);
        return feature.withPlacement(modifiers(CountPlacementModifier.of(config.perChunk),
                HeightRangePlacementModifier.uniform(YOffset.fixed(config.bottom), YOffset.fixed(config.top))));
    }

    public static PlacedFeature uniform(String name, RuleTest rule, Block block, OreConfig config) {
        var feature = createConfiguredFeature(name, rule, block, config);
        return feature.withPlacement(modifiers(CountPlacementModifier.of(config.perChunk),
                HeightRangePlacementModifier.uniform(YOffset.fixed(config.bottom), YOffset.fixed(config.top))));
    }

    public static PlacedFeature uniform(String name, RuleTest rule, Block block, VariantConfig config) {
        var feature = createConfiguredFeature(name, rule, block, config);
        return feature.withPlacement(modifiers(CountPlacementModifier.of(config.perChunk),
                HeightRangePlacementModifier.uniform(YOffset.fixed(config.bottom), YOffset.fixed(config.top))));
    }

    public static PlacedFeature aboveBottom(String name, ImmutableList<OreFeatureConfig.Target> targets, OreConfig config) {
        var feature = createConfiguredFeature(name, targets, config);
        return feature.withPlacement(modifiers(CountPlacementModifier.of(config.perChunk),
                HeightRangePlacementModifier.uniform(YOffset.aboveBottom(config.bottom), YOffset.fixed(config.top))));
    }

    public static PlacedFeature aboveBottom(String name, ImmutableList<OreFeatureConfig.Target> targets, VariantConfig config) {
        var feature = createConfiguredFeature(name, targets, config);
        return feature.withPlacement(modifiers(CountPlacementModifier.of(config.perChunk),
                HeightRangePlacementModifier.uniform(YOffset.aboveBottom(config.bottom), YOffset.fixed(config.top))));
    }

    public static PlacedFeature aboveBottom(String name, RuleTest rule, Block block, OreConfig config) {
        var feature = createConfiguredFeature(name, rule, block, config);
        return feature.withPlacement(modifiers(CountPlacementModifier.of(config.perChunk),
                HeightRangePlacementModifier.uniform(YOffset.aboveBottom(config.bottom), YOffset.fixed(config.top))));
    }

    public static PlacedFeature aboveBottom(String name, RuleTest rule, Block block, VariantConfig config) {
        var feature = createConfiguredFeature(name, rule, block, config);
        return feature.withPlacement(modifiers(CountPlacementModifier.of(config.perChunk),
                HeightRangePlacementModifier.uniform(YOffset.aboveBottom(config.bottom), YOffset.fixed(config.top))));
    }

    public static PlacedFeature trapezoid(String name, ImmutableList<OreFeatureConfig.Target> targets, OreConfig config) {
        var feature = createConfiguredFeature(name, targets, config);
        return feature.withPlacement(modifiers(CountPlacementModifier.of(config.perChunk),
                HeightRangePlacementModifier.trapezoid(YOffset.fixed(config.bottom), YOffset.fixed(config.top))));
    }

    public static PlacedFeature trapezoid(String name, RuleTest rule, Block block, OreConfig config) {
        var feature = createConfiguredFeature(name, rule, block, config);
        return feature.withPlacement(modifiers(CountPlacementModifier.of(config.perChunk),
                HeightRangePlacementModifier.trapezoid(YOffset.fixed(config.bottom), YOffset.fixed(config.top))));
    }

    public static PlacedFeature trapezoid(String name, RuleTest rule, Block block, VariantConfig config) {
        var feature = createConfiguredFeature(name, rule, block, config);
        return feature.withPlacement(modifiers(CountPlacementModifier.of(config.perChunk),
                HeightRangePlacementModifier.trapezoid(YOffset.fixed(config.bottom), YOffset.fixed(config.top))));
    }

    public static PlacedFeature trapezoid(String name, ImmutableList<OreFeatureConfig.Target> targets, VariantConfig config) {
        var feature = createConfiguredFeature(name, targets, config);
        return feature.withPlacement(modifiers(CountPlacementModifier.of(config.perChunk),
                HeightRangePlacementModifier.trapezoid(YOffset.fixed(config.bottom), YOffset.fixed(config.top))));
    }

    //From Mojanks OrePlacedFeatures
    private static List<PlacementModifier> modifiers(PlacementModifier first, PlacementModifier second) {
        return List.of(first, SquarePlacementModifier.of(), second, BiomePlacementModifier.of());
    }
}
