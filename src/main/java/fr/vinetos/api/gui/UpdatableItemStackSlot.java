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

import org.bukkit.inventory.ItemStack;

public abstract class UpdatableItemStackSlot implements InventoryItemSlot {

    private final int slot;
    private ItemStack itemStack;

    public UpdatableItemStackSlot(ItemStack itemStack, int slot) {
        this.itemStack = itemStack;
        this.slot = slot;
    }

    @Override
    public ItemStack getItemStack() {
        return itemStack;
    }

    public void setItemStack(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    @Override
    public int getSlot() {
        return slot;
    }

    public abstract void update();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UpdatableItemStackSlot that = (UpdatableItemStackSlot) o;

        if (slot != that.slot) return false;
        return itemStack != null ? itemStack.equals(that.itemStack) : that.itemStack == null;
    }

    @Override
    public int hashCode() {
        int result = itemStack != null ? itemStack.hashCode() : 0;
        result = 31 * result + slot;
        return result;
    }

    @Override
    public String toString() {
        return "UpdatableItemStackSlot{" + "itemStack=" + itemStack
                + ",slot=" + slot + '}';
    }
}
