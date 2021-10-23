package me.lbuddyboy.libraries.util.qlib.buttons;

import me.lbuddyboy.libraries.util.qlib.Button;
import me.lbuddyboy.libraries.util.qlib.Menu;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import java.beans.ConstructorProperties;
import java.util.ArrayList;
import java.util.List;

public class BackButton extends Button {
    private final Menu back;

    @Override
    public Material getMaterial(Player player) {
        return Material.BED;
    }

    @Override
    public byte getDamageValue(Player player) {
        return 0;
    }

    @Override
    public String getName(Player player) {
        return "\u00a7cGo back";
    }

    @Override
    public List<String> getDescription(Player player) {
        return new ArrayList<>();
    }

    @Override
    public void clicked(Player player, int i, ClickType clickType) {
        Button.playNeutral(player);
        this.back.openMenu(player);
    }

    @ConstructorProperties(value={"back"})
    public BackButton(Menu back) {
        this.back=back;
    }
}

