package dev.newception.playerStatsLeaderboards.service;

import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

public class ItemService {

    public static Item findItemForIdentifier(Identifier identifier) {
        for (Item item : Registries.ITEM) {
            Identifier itemIdentifier = Registries.ITEM.getId(item);

            if (itemIdentifier.getNamespace().equals(identifier.getNamespace()) && itemIdentifier.getPath().equals(identifier.getPath())) {
                return item;
            }
        }

        return null;
    }
}
