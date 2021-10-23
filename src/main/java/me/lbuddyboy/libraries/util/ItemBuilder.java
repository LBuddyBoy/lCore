package me.lbuddyboy.libraries.util;

import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author LBuddyBoy (lbuddyboy.me)
 * 30/06/2021 / 7:13 PM
 * xParty / services.xenlan.party.util
 */

public class ItemBuilder {

	@Getter
	private Material material;

	@Getter
	private ItemStack stack;

	public ItemBuilder(Material material) {
		this.material = material;

		stack = new ItemStack(getMaterial());
	}


	/*
	Sets the display name of the provided ItemStack when creating a new instance of the ItemBuilder.java Class.
	 */
	public ItemBuilder setDisplayName(String newName) {
		ItemMeta meta = this.stack.getItemMeta();
		meta.setDisplayName(CC.translate(newName));
		this.stack.setItemMeta(meta);
		return this;
	}

	/*
	Add to the lore of the provided ItemStack when creating a new instance of the ItemBuilder.java Class.
 	*/
	public ItemBuilder addLore(String toAdd) {
		ItemMeta meta = this.stack.getItemMeta();

		List<String> newLore = new ArrayList<>();
		if (meta.getLore() == null) {
			meta.setLore(CC.translate(Collections.singletonList(toAdd)));
		} else {
			newLore.add(CC.translate(toAdd));
			meta.setLore(newLore);
		}
		this.stack.setItemMeta(meta);
		return this;
	}

	/*
	Sets the lore of the provided ItemStack when creating a new instance of the ItemBuilder.java Class.
 	*/
	public ItemBuilder setLore(List<String> toSet) {
		ItemMeta meta = this.stack.getItemMeta();

		meta.setLore(toSet);

		this.stack.setItemMeta(meta);
		return this;
	}

	/*
	Sets the data of the provided ItemStack when creating a new instance of the ItemBuilder.java Class.
	*/
	public ItemBuilder setData(int newData) {
		this.stack.setDurability((short) newData);
		return this;
	}

	/*
	Sets the amount of the provided ItemStack when creating a new instance of the ItemBuilder.java Class.
	*/
	public ItemBuilder setAmount(int newAmount) {
		this.stack.setAmount(newAmount);
		return this;
	}

	public ItemStack create() {
		return this.stack;
	}

}
