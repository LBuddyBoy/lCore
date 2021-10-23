package me.lbuddyboy.libraries.util.qlib.pagination;

import me.lbuddyboy.libraries.util.qlib.Button;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import java.beans.ConstructorProperties;
import java.util.List;

public class JumpToPageButton extends Button {

    private final int page;
    private final PaginatedMenu menu;

    @Override
    public String getName(Player player) {
        return "\u00a7ePage " + this.page;
    }

    @Override
    public List<String> getDescription(Player player) {
        return null;
    }

    @Override
    public Material getMaterial(Player player) {
        return Material.BOOK;
    }

    @Override
    public int getAmount(Player player) {
        return this.page;
    }

    @Override
    public byte getDamageValue(Player player) {
        return 0;
    }

    @Override
    public void clicked(Player player, int i, ClickType clickType) {
        this.menu.modPage(player, this.page - this.menu.getPage());
        playNeutral(player);
    }

    @ConstructorProperties(value={"page", "net/frozenorb/qlib/menu"})
    public JumpToPageButton(int page, PaginatedMenu menu) {
        this.page=page;
        this.menu=menu;
    }
}

