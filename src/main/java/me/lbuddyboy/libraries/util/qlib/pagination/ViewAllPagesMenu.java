package me.lbuddyboy.libraries.util.qlib.pagination;

import lombok.NonNull;
import me.lbuddyboy.libraries.util.qlib.Button;
import me.lbuddyboy.libraries.util.qlib.Menu;
import me.lbuddyboy.libraries.util.qlib.buttons.BackButton;
import org.bukkit.entity.Player;

import java.beans.ConstructorProperties;
import java.util.HashMap;
import java.util.Map;

public class ViewAllPagesMenu extends Menu {

    PaginatedMenu menu;

    @Override
    public String getTitle(Player player) {
        return "Jump to page";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        HashMap<Integer, Button> buttons= new HashMap<>();
        buttons.put(0, new BackButton(this.menu));
        int index=10;
        for ( int i=1; i <= this.menu.getPages(player); ++i ) {
            buttons.put(index++, new JumpToPageButton(i, this.menu));
            if ((index - 8) % 9 != 0) continue;
            index+=2;
        }
        return buttons;
    }

    @Override
    public boolean isAutoUpdate() {
        return true;
    }

    @ConstructorProperties(value={"net/frozenorb/qlib/menu"})
    public ViewAllPagesMenu(@NonNull PaginatedMenu menu) {
        if (menu == null) {
            throw new NullPointerException("net/frozenorb/qlib/menu");
        }
        this.menu=menu;
    }

    @NonNull
    public PaginatedMenu getMenu() {
        return this.menu;
    }
}

