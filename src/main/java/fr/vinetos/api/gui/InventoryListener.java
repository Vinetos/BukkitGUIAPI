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

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class InventoryListener implements Listener {

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onInteractWithItem(PlayerInteractEvent event) {
        // Interact with anything
        if (event.getPlayer().getInventory().getItemInMainHand() == null
                || event.getPlayer().getInventory().getItemInMainHand().getType() == Material.AIR)
            return;

        // No GUI register
        if (AbstractInventory.getAbstractInventories().isEmpty())
            return;

        // React and call abstract methods
        AbstractInventory.getAbstractInventories().stream()
                // Filter inventories to find which use the item
                .filter(inventory -> inventory.isValidOpener(event.getPlayer().getInventory().getItemInMainHand()))
                // Call abstract methods
                .forEach(inventory -> inventory.onItemInteract(event.getPlayer(), event));

    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onClickOnItem(InventoryClickEvent event) {
        // Click on anything
        if (event.getCurrentItem() == null || event.getCurrentItem().getType() == Material.AIR || event.getWhoClicked() == null)
            return;

        // No GUI register
        if (AbstractInventory.getAbstractInventories().isEmpty())
            return;

        final ItemStackSlot itemStackSlot = new ItemStackSlot(event.getCurrentItem(), event.getSlot());

        // React and call abstract methods
        AbstractInventory.getAbstractInventories().stream()
                // Filter inventories to find which use the item
                .filter(inventory -> inventory.getDisplayName().equals(event.getInventory().getName()) || inventory.getName().equals(event.getInventory().getName()))
                .filter(inventory -> inventory.getItems().contains(itemStackSlot))
                // Call abstract methods
                .forEach(inventory ->
                        inventory.onItemClick(
                                (Player) event.getWhoClicked(),
                                event.getCurrentItem(),
                                ClickType.get(
                                        event.isLeftClick(),
                                        event.isShiftClick(),
                                        event.isRightClick()
                                ),
                                event

                ));

    }

}
