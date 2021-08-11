/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.background;

import com.sfc.sf2.graphics.GraphicsManager;
import com.sfc.sf2.graphics.Tile;
import com.sfc.sf2.background.io.DisassemblyManager;
import com.sfc.sf2.background.io.PngManager;
import com.sfc.sf2.background.io.GifManager;
import com.sfc.sf2.palette.PaletteManager;
import java.awt.Color;

/**
 *
 * @author wiz
 */
public class BackgroundManager {
       
    private PaletteManager paletteManager = new PaletteManager();
    private GraphicsManager graphicsManager = new GraphicsManager();
    private Tile[] tiles;
    private Background[] backgrounds;
       
    public void importDisassembly(String graphicsBasepath){
        System.out.println("com.sfc.sf2.background.BackgroundManager.importDisassembly() - Importing disassembly ...");
        backgrounds = DisassemblyManager.importDisassembly(graphicsBasepath);
        tiles = new Tile[backgrounds.length*32*12];
        for(int i=0;i<backgrounds.length;i++){
            System.arraycopy(backgrounds[i].getTiles(), 0, tiles, i*32*12, 32*12);
        }
        graphicsManager.setTiles(tiles);
        System.out.println("com.sfc.sf2.background.BackgroundManager.importDisassembly() - Disassembly imported.");
    }
    
    public void importSingleDisassembly(String filepath){
        System.out.println("com.sfc.sf2.background.BackgroundManager.importDisassembly() - Importing disassembly ...");
        backgrounds = new Background[1];
        backgrounds[0] = DisassemblyManager.importSingleBackground(filepath);
        tiles = backgrounds[0].getTiles();
        graphicsManager.setTiles(tiles);
        System.out.println("com.sfc.sf2.background.BackgroundManager.importDisassembly() - Disassembly imported.");
    }
    
    public void exportDisassembly(String basepath){
        System.out.println("com.sfc.sf2.background.BackgroundManager.importDisassembly() - Exporting disassembly ...");
        DisassemblyManager.exportDisassembly(backgrounds, basepath);
        System.out.println("com.sfc.sf2.background.BackgroundManager.importDisassembly() - Disassembly exported.");        
    }  
    
    public void exportSingleDisassembly(Background background, String filepath){
        System.out.println("com.sfc.sf2.background.BackgroundManager.importDisassembly() - Exporting disassembly ...");
        DisassemblyManager.exportSingleDisassembly(background, filepath);
        System.out.println("com.sfc.sf2.background.BackgroundManager.importDisassembly() - Disassembly exported.");        
    }   
    
    public void importRom(String romFilePath, String paletteOffset, String paletteLength, String graphicsOffset, String graphicsLength){
        System.out.println("com.sfc.sf2.background.BackgroundManager.importOriginalRom() - Importing original ROM ...");
        graphicsManager.importRom(romFilePath, paletteOffset, paletteLength, graphicsOffset, graphicsLength,GraphicsManager.COMPRESSION_BASIC);
        tiles = graphicsManager.getTiles();
        System.out.println("com.sfc.sf2.background.BackgroundManager.importOriginalRom() - Original ROM imported.");
    }
    
    public void exportRom(String originalRomFilePath, String graphicsOffset){
        System.out.println("com.sfc.sf2.background.BackgroundManager.exportOriginalRom() - Exporting original ROM ...");
        graphicsManager.exportRom(originalRomFilePath, graphicsOffset, GraphicsManager.COMPRESSION_BASIC);
        System.out.println("com.sfc.sf2.background.BackgroundManager.exportOriginalRom() - Original ROM exported.");        
    }      
    
    public void importPng(String basepath){
        System.out.println("com.sfc.sf2.background.BackgroundManager.importPng() - Importing PNG ...");
        backgrounds = PngManager.importPng(basepath);
        tiles = new Tile[backgrounds.length*384];
        for(int i=0;i<backgrounds.length;i++){
            System.arraycopy(backgrounds[i].getTiles(), 0, tiles, i*384, 384);
        }
        graphicsManager.setTiles(tiles);
        System.out.println("com.sfc.sf2.background.BackgroundManager.importPng() - PNG imported.");
    }
    
    public void exportPng(String basepath){
        System.out.println("com.sfc.sf2.background.BackgroundManager.exportPng() - Exporting PNG ...");
        PngManager.exportPng(backgrounds, basepath);
        System.out.println("com.sfc.sf2.background.BackgroundManager.exportPng() - PNG exported.");       
    }
    
    public void exportSinglePng(Background background, String filepath){
        System.out.println("com.sfc.sf2.background.BackgroundManager.exportPng() - Exporting PNG ...");
        PngManager.exportSinglePng(background, filepath);
        System.out.println("com.sfc.sf2.background.BackgroundManager.exportPng() - PNG exported.");       
    }
    
    public Tile[] getTiles() {
        return tiles;
    }

    public void setTiles(Tile[] tiles) {
        this.tiles = tiles;
    }

    public Background[] getBackgrounds() {
        return backgrounds;
    }

    public void setBackgrounds(Background[] backgrounds) {
        this.backgrounds = backgrounds;
    }
        
    public void importGif(String basepath){
        System.out.println("com.sfc.sf2.background.BackgroundManager.importGif() - Importing GIF ...");
        backgrounds = GifManager.importGif(basepath);
        tiles = new Tile[backgrounds.length*384];
        for(int i=0;i<backgrounds.length;i++){
            System.arraycopy(backgrounds[i].getTiles(), 0, tiles, i*384, 384);
        }
        graphicsManager.setTiles(tiles);
        System.out.println("com.sfc.sf2.background.BackgroundManager.importGif() - GIF imported.");
    }
    
    public void exportGif(String basepath){
        System.out.println("com.sfc.sf2.background.BackgroundManager.exportGif() - Exporting GIF ...");
        GifManager.exportGif(backgrounds, basepath);
        System.out.println("com.sfc.sf2.background.BackgroundManager.exportGif() - GIF exported.");       
    }
}
