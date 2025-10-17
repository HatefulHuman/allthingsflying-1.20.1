package com.kfhzs.allthingsflying.datagen;

import com.kfhzs.allthingsflying.items.ItemsRegister;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.crafting.conditions.IConditionBuilder;

import java.util.function.Consumer;

//合成表
public class ModRecipesProvider extends RecipeProvider implements IConditionBuilder {
    public ModRecipesProvider(PackOutput packOutput) {
        super(packOutput);
    }

    @Override
    protected void buildRecipes(Consumer<FinishedRecipe> consumer) {
        //无序合成
//        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ItemsRegister.UNIVERSAL_FLYING_ITEM.get(),1).requires(Items.STICK,1)
//                .requires(ItemsRegister.AERO_ENGINE.get(),1)
//                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.FOOD, ItemsRegister.AERO_ENGINE.get(),1)
                .define('#', Ingredient.of(Tags.Items.GEMS_LAPIS))
                .pattern(" # ")
                .pattern("# #")
                .pattern("# #")
                .unlockedBy("has_lapis", has(Tags.Items.GEMS_LAPIS)).save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.FOOD, ItemsRegister.THERMAL_ENGINE.get(),1)
                .define('#', Ingredient.of(Tags.Items.GUNPOWDER))
                .pattern("# #")
                .pattern(" # ")
                .pattern("# #")
                .unlockedBy("has_gunpowder", has(Tags.Items.GUNPOWDER)).save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.FOOD, ItemsRegister.MAGIC_ENGINE.get(),1)
                .define('#', Ingredient.of(Tags.Items.GEMS_AMETHYST))
                .pattern("## ")
                .pattern("# #")
                .pattern(" ##")
                .unlockedBy("has_amethyst", has(Tags.Items.GEMS_AMETHYST)).save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.FOOD, ItemsRegister.DRONE_ENGINE.get(),1)
                .define('#', Ingredient.of(Tags.Items.DUSTS_REDSTONE))
                .pattern("###")
                .pattern("# #")
                .pattern("###")
                .unlockedBy("has_redstone", has(Tags.Items.DUSTS_REDSTONE)).save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.FOOD, ItemsRegister.CLOUD_ENGINE.get(),1)
                .define('#', Ingredient.of(Tags.Items.ENDER_PEARLS))
                .pattern("###")
                .pattern("   ")
                .pattern("###")
                .unlockedBy("has_ender_pearls", has(Tags.Items.ENDER_PEARLS)).save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.FOOD, ItemsRegister.UPGRADE_CORE.get(),1)
                .define('#', Ingredient.of(Tags.Items.GEMS_DIAMOND))
                .define('$', Ingredient.of(Tags.Items.GEMS_EMERALD))
                .pattern(" # ")
                .pattern("#$#")
                .pattern(" # ")
                .unlockedBy("has_diamond", has(Tags.Items.GEMS_DIAMOND))
                .unlockedBy("has_emerald", has(Tags.Items.GEMS_EMERALD)).save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.FOOD, ItemsRegister.HEAVY_UPGRADE_CORE.get(),1)
                .define('#', Ingredient.of(Tags.Items.STORAGE_BLOCKS_DIAMOND))
                .define('$', Ingredient.of(Tags.Items.STORAGE_BLOCKS_EMERALD))
                .pattern(" # ")
                .pattern("#$#")
                .pattern(" # ")
                .unlockedBy("has_blocks_diamond", has(Tags.Items.STORAGE_BLOCKS_DIAMOND))
                .unlockedBy("has_blocks_emerald", has(Tags.Items.STORAGE_BLOCKS_EMERALD)).save(consumer);

    }
}
