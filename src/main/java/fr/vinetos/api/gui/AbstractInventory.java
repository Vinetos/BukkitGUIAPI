/*
 * ==============================================================================
 *            _    _______   __________________  _____
 *            | |  / /  _/ | / / ____/_  __/ __ \/ ___/
 *            | | / // //  |/ / __/   / / / / / /\__ \
 *            | |/ // // /|  / /___  / / / /_/ /___/ /
 *            |___/___/_/ |_/_____/ /_/  \____//____/
 *
 * ==============================================================================
 *
 * BukkitGUIAPI Copyright (C) 2017  Vinetos
 * 
 * ==============================================================================
 * 
 * This file is part of BukkitGUIAPI.
 * 
 * BukkitGUIAPI is free software: you can redistribute it and/or modify
 * it under the terms of the MIT License.
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in 
 * the Software without restriction, including without limitation the rights to 
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies 
 * of the Software, and to permit persons to whom the Software is furnished to do so, 
 * subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all 
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR 
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS 
 * FOR A PARTICULAR PURPOSE AND NONINFINGEMENT. IN NO EVENT SHALL THE AUTHORS OR 
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER 
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 *==============================================================================
 */
package fr.vinetos.api.gui;

import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public abstract class AbstractInventory {

    private static final List<AbstractInventory> abstractInventories = new CopyOnWriteArrayList<>();
    private final String name;
    private final int slots;
    private String displayName;
    private List<ItemStackSlot> items;
    protected ItemStack opener;
    private Inventory inventory;
//    private List<Inventory> inventories;

    public AbstractInventory(int slots, String name, String displayName) {
        Validate.isTrue((slots > 0 && slots % 9 == 0) || slots == 5);
        Validate.notEmpty(name, "Name cannot be null or empty");

        this.name = name;
        this.displayName = (displayName == null || displayName.length() == 0) ? this.name : displayName;

        this.slots = slots;
        this.items = new ArrayList<>();
        // TODO: 05/09/2017 Create pages with inventories
//        this.inventories = new ArrayList<>();

        abstractInventories.add(this);
    }

    public static <T extends AbstractInventory> T getOrCreateInventory(Class<T> inventoryClass) {
        return BukkitGUIAPI.getOrCreateInventory(inventoryClass);
    }

    public static List<AbstractInventory> getAbstractInventories() {
        return Collections.unmodifiableList(abstractInventories);
    }

    public String getName() {
        return name;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        Validate.notEmpty(displayName, "Display name cannot be null or empty");

        this.displayName = displayName;
    }

    public ItemStack getOpener() {
        return opener;
    }

    public void setOpener(ItemStack opener) {
        this.opener = opener;
    }

    public boolean isValidOpener(ItemStack itemStack) {
        return itemStack != null && (opener == null || opener.getType() == itemStack.getType() && opener.getData() == itemStack.getData() && opener.getAmount() == itemStack.getAmount() && (opener.hasItemMeta() && itemStack.hasItemMeta() && opener.getItemMeta().getDisplayName().toLowerCase().equals(itemStack.getItemMeta().getDisplayName().toLowerCase())));
    }

    public int getSlots() {
        return slots;
    }

    public final List<ItemStackSlot> getItems() {
        return Collections.unmodifiableList(items);
    }

    public void addItem(int slot, ItemStack stack) {
        Validate.isTrue(slot >= 0 && slot < slots);
        Validate.notNull(stack);

        items.add(new ItemStackSlot(stack, slot));

        if (inventory != null)
            inventory.getViewers().forEach(humanEntity -> ((Player) humanEntity).updateInventory());
    }

    public void addItems(Map<Integer, ItemStack> items) {
        for (Map.Entry<Integer, ItemStack> entry : items.entrySet())
            addItem(entry.getKey(), entry.getValue());
    }

    public void addItems(ItemStackSlot... items) {
        for (ItemStackSlot item : items)
            addItem(item.getSlot(), item.getItemStack());
    }

    public void addItem(ItemStack... stacks) {
        if (stacks == null || stacks.length == 0)
            return;

        if (items.isEmpty()) {
            for (int i = 0; i < stacks.length; i++)
                addItem(i + 1, stacks[i]);

        } else {
            List<Integer> availableSlots = IntStream.rangeClosed(0, inventory.getSize()).boxed().collect(Collectors.toList());
            availableSlots.removeAll(items.stream().map(ItemStackSlot::getSlot).collect(Collectors.toList()));

            if (stacks.length > availableSlots.size())
                throw new IllegalArgumentException("There are too much item to add !");

            for (int i = 0; i < stacks.length; i++)
                addItem(availableSlots.get(i), stacks[i]);

        }
    }

    public void open(Player player) {
        if (inventory == null)
            inventory = Bukkit.createInventory(null, slots, displayName);
        // Fill inventory
        items.forEach(item -> inventory.setItem(item.getSlot(), item.getItemStack()));

        player.openInventory(inventory);
    }

    public void remove() {
        abstractInventories.remove(this);
    }

    public void onItemClick(Player player, ItemStack itemStack, ClickType clickType, InventoryClickEvent event) {
        event.setCancelled(true);
    }

    public void onItemInteract(Player player, PlayerInteractEvent event) {
        event.setCancelled(true);
    }

}
