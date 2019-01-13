/*
 * This file is part of Industrial Foregoing.
 *
 * Copyright 2019, Buuz135
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in the
 * Software without restriction, including without limitation the rights to use, copy,
 * modify, merge, publish, distribute, sublicense, and/or sell copies of the Software,
 * and to permit persons to whom the Software is furnished to do so, subject to the
 * following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies
 * or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE
 * FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
 * ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.buuz135.industrial.tile.misc;

import com.buuz135.industrial.api.juicer.JuicerRecipe;
import com.buuz135.industrial.proxy.JuicerRegistry;
import com.buuz135.industrial.tile.CustomElectricMachine;
import com.buuz135.industrial.utils.WorkUtils;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.ndrei.teslacorelib.inventory.BoundingRectangle;
import net.ndrei.teslacorelib.inventory.FluidTankType;

public class JuicerTile extends CustomElectricMachine {

    private IFluidTank fluidTank;

    private IItemHandlerModifiable input;

    public JuicerTile() {
        super(JuicerTile.class.getName().hashCode());
    }

    @Override
    protected float performWork() {
        if (WorkUtils.isDisabled(this.getBlockType())) return 0;
        ItemStack input = this.input.getStackInSlot(0);
        if (!input.isEmpty()) {
            JuicerRecipe recipe = JuicerRegistry.getRecipe(input);
            if (recipe != null) { // Shouldn't null be but this is minecraft.
                FluidStack output = recipe.getOutput();
                if (this.fluidTank.fill(output, false) == output.amount) {
                    this.fluidTank.fill(output, true);
                    input.shrink(1);
                    return 1;
                }
            }
        }
        return 0;
    }

    @Override
    protected void initializeInventories() {
        super.initializeInventories();
        input = this.addSimpleInventory(
                1,
                "input",
                EnumDyeColor.BLUE,
                "input",
                new BoundingRectangle(54, 43, 18, 18),
                (o, i) -> JuicerRegistry.getRecipe(o) != null,
                (o, i) -> true,
                true,
                null
        );
        fluidTank = this.addSimpleFluidTank(
                8000,
                "output",
                EnumDyeColor.RED,
                80, 25,
                FluidTankType.OUTPUT,
                o -> true,
                o -> true
        );
    }

}