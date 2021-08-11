/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.weaponsprite;

import com.sfc.sf2.graphics.Tile;
import java.awt.Color;

/**
 *
 * @author wiz
 */
public class WeaponSprite {
    
    private int index;
    
    private Color[][] palettes;
    
    private Tile[] tiles;
    
    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }  

    public Color[][] getPalettes() {
        return palettes;
    }

    public void setPalettes(Color[][] palettes) {
        this.palettes = palettes;
    }

    public Tile[] getTiles() {
        return tiles;
    }

    public void setTiles(Tile[] tiles) {
        this.tiles = tiles;
    }
    
}
