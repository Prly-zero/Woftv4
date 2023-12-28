package club.windofall.woftv4.recipes;

import club.windofall.woftv4.Woftv4;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class CraftingRecipes {
    private static final List<String> recipes = new ArrayList<>();
    private static final NamespacedKey TYPE = new NamespacedKey(Woftv4.getPlugin(Woftv4.class),"book_type");
    public static void RegisterAll(JavaPlugin plugin){
        CraftedBook(plugin);
    }
    public static void RemoveAll(JavaPlugin plugin){
        for(String s: recipes){
            plugin.getServer().removeRecipe(new NamespacedKey(plugin,s));
        }
    }
    private static void CraftedBook(JavaPlugin plugin){
        ItemStack result = new ItemStack(Material.BOOK);
        ItemMeta meta = result.getItemMeta();
        if (meta != null) {
            meta.getPersistentDataContainer().set(TYPE, PersistentDataType.STRING,"craft");
            meta.setDisplayName(ChatColor.AQUA+"合成书");
            List<String> lore = new ArrayList<>();
            lore.add("右键打开合成");
            meta.setLore(lore);
            result.setItemMeta(meta);
        }
        ShapelessRecipe recipe = new ShapelessRecipe(new NamespacedKey(plugin,"crafted_book"),result);
        recipe.addIngredient(Material.BOOK);
        recipe.addIngredient(Material.CRAFTING_TABLE);
        plugin.getServer().addRecipe(recipe);
        recipes.add("crafted_book");
    }
}
