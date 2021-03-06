package utils;

import org.bukkit.ChatColor;
import org.bukkit.DyeColor;

import java.util.*;

/**
 * Created by takumus on 2017/04/30.
 */
public class ColorMap {
    private static Map<DyeColor, ChatColor> dyeChatMap = new HashMap<>();
    private static Map<DyeColor, String> dyeNameMap = new HashMap<>();
    private static Map<String, DyeColor> nameDyeMap = new HashMap<>();
    private static List<DyeColor> colors = new ArrayList<>();
    static {
        dyeChatMap.put(DyeColor.BLACK, ChatColor.DARK_GRAY);
        dyeChatMap.put(DyeColor.BLUE, ChatColor.DARK_BLUE);
        dyeChatMap.put(DyeColor.BROWN, ChatColor.GOLD);
        dyeChatMap.put(DyeColor.CYAN, ChatColor.AQUA);
        dyeChatMap.put(DyeColor.GRAY, ChatColor.GRAY);
        dyeChatMap.put(DyeColor.GREEN, ChatColor.DARK_GREEN);
        dyeChatMap.put(DyeColor.LIGHT_BLUE, ChatColor.BLUE);
        dyeChatMap.put(DyeColor.LIME, ChatColor.GREEN);
        dyeChatMap.put(DyeColor.MAGENTA, ChatColor.LIGHT_PURPLE);
        dyeChatMap.put(DyeColor.ORANGE, ChatColor.GOLD);
        dyeChatMap.put(DyeColor.PINK, ChatColor.LIGHT_PURPLE);
        dyeChatMap.put(DyeColor.PURPLE, ChatColor.DARK_PURPLE);
        dyeChatMap.put(DyeColor.RED, ChatColor.DARK_RED);
        dyeChatMap.put(DyeColor.SILVER, ChatColor.GRAY);
        dyeChatMap.put(DyeColor.WHITE, ChatColor.WHITE);
        dyeChatMap.put(DyeColor.YELLOW, ChatColor.YELLOW);

        dyeNameMap.put(DyeColor.BLACK, "Black");
        dyeNameMap.put(DyeColor.BLUE, "Blue");
        dyeNameMap.put(DyeColor.BROWN, "Brown");
        dyeNameMap.put(DyeColor.CYAN, "Cyan");
        dyeNameMap.put(DyeColor.GRAY, "Gray");
        dyeNameMap.put(DyeColor.GREEN, "Green");
        dyeNameMap.put(DyeColor.LIGHT_BLUE, "LightBlue");
        dyeNameMap.put(DyeColor.LIME, "Lime");
        dyeNameMap.put(DyeColor.MAGENTA, "Magenta");
        dyeNameMap.put(DyeColor.ORANGE, "Orange");
        dyeNameMap.put(DyeColor.PINK, "Pink");
        dyeNameMap.put(DyeColor.PURPLE, "Purple");
        dyeNameMap.put(DyeColor.RED, "Red");
        dyeNameMap.put(DyeColor.SILVER, "Silver");
        dyeNameMap.put(DyeColor.WHITE, "White");
        dyeNameMap.put(DyeColor.YELLOW, "Yellow");

        dyeNameMap.entrySet().forEach((set) -> {
            nameDyeMap.put(set.getValue().toLowerCase(), set.getKey());
            colors.add(set.getKey());
        });
    }
    public static ChatColor getChatColor(DyeColor dyeColor) {
        return dyeChatMap.get(dyeColor);
    }
    public static ChatColor getChatColor(String name) {
        return dyeChatMap.get(getDyeColor(name));
    }
    public static String getName(DyeColor dyeColor) {
        return dyeNameMap.get(dyeColor);
    }
    public static DyeColor getDyeColor(String name) {
        return nameDyeMap.get(name.toLowerCase());
    }
    public static DyeColor getRandomDyeColor() {
        return colors.get((int)(colors.size() * Math.random()));
    }
}
