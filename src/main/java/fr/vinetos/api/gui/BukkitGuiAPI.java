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

import org.bukkit.plugin.Plugin;

import java.util.Optional;

public class BukkitGuiAPI {

    public static void init(Plugin plugin) {
        plugin.getServer().getPluginManager().registerEvents(new InventoryListener(), plugin);
    }

    public static <T extends AbstractInventory> T getOrCreateInventory(Class<T> inventoryClass) {
        Optional<AbstractInventory> any = AbstractInventory.getAbstractInventories().stream().filter(typeClass -> typeClass.getClass().equals(inventoryClass))
                .findAny();

        return (T) any.orElseGet(() -> {
            try {
                return inventoryClass.newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                throw new Error(e);
            }
        });
    }

}
