/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.map.layout;

import com.sfc.sf2.graphics.GraphicsManager;
import com.sfc.sf2.graphics.Tile;
import com.sfc.sf2.map.block.MapBlock;
import com.sfc.sf2.map.block.MapBlockManager;
import com.sfc.sf2.map.layout.io.DisassemblyManager;
import com.sfc.sf2.map.layout.io.PngManager;
import com.sfc.sf2.map.layout.io.SFCDBankManager;
import com.sfc.sf2.palette.PaletteManager;
import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author wiz
 */
public class MapLayoutManager {
       
    private PaletteManager paletteManager = new PaletteManager();
    private GraphicsManager graphicsManager = new GraphicsManager();
    private MapBlockManager mapBlockManager = new MapBlockManager();
    private DisassemblyManager disassemblyManager = new DisassemblyManager();
    private SFCDBankManager sfcdBankManager = null;
    private MapLayout layout;
    private MapBlock[] blockset;
       
    public void importDisassembly(String palettePath, String tileset1Path, String tileset2Path, String tileset3Path, String tileset4Path, String tileset5Path, String blocksPath, String layoutPath){
        System.out.println("com.sfc.sf2.maplayout.MapLayoutManager.importDisassembly() - Importing disassembly ...");
        disassemblyManager = new DisassemblyManager();
        layout = disassemblyManager.importDisassembly(palettePath, tileset1Path, tileset2Path, tileset3Path, tileset4Path, tileset5Path, blocksPath, layoutPath);
        blockset = disassemblyManager.getBlockset();
        System.out.println("com.sfc.sf2.maplayout.MapLayoutManager.importDisassembly() - Disassembly imported.");
    }
    
    public void importDisassembly(String palettesPath, String tilesetsPath, String tilesetsFilePath, String blocksPath, String layoutPath){
        importDisassembly(palettesPath, tilesetsPath, tilesetsFilePath, blocksPath, layoutPath, null, 0, 0, 0, 0);
    }
    
    public void importDisassembly(String palettesPath, String tilesetsPath, String tilesetsFilePath, String blocksPath, String layoutPath, Integer animTileset, int animLength, int animFrameStart, int animFrameLength, int animFrameDest){
        System.out.println("com.sfc.sf2.maplayout.MapLayoutManager.importDisassembly() - Importing disassembly ...");
        disassemblyManager = new DisassemblyManager();
        layout = disassemblyManager.importDisassembly(palettesPath, tilesetsPath, tilesetsFilePath, blocksPath, layoutPath, animTileset, animLength, animFrameStart, animFrameLength, animFrameDest);
        blockset = disassemblyManager.getBlockset();
        System.out.println("com.sfc.sf2.maplayout.MapLayoutManager.importDisassembly() - Disassembly imported.");
    }
    
    public void importDisassemblyFromEntryFiles(String incbinPath, String paletteEntriesPath, String tilesetEntriesPath, String tilesetsFilePath, String blocksPath, String layoutPath){
        importDisassemblyFromEntryFiles(incbinPath, paletteEntriesPath, tilesetEntriesPath, tilesetsFilePath, blocksPath, layoutPath, null, 0, 0, 0, 0);
    }
    
    public void importDisassemblyFromEntryFiles(String incbinPath, String paletteEntriesPath, String tilesetEntriesPath, String tilesetsFilePath, String blocksPath, String layoutPath, Integer animTileset, int animLength, int animFrameStart, int animFrameLength, int animFrameDest){
        System.out.println("com.sfc.sf2.maplayout.MapLayoutManager.importDisassembly() - Importing disassembly ...");
        disassemblyManager = new DisassemblyManager();
        layout = disassemblyManager.importDisassemblyFromEntryFiles(incbinPath, paletteEntriesPath, tilesetEntriesPath, tilesetsFilePath, blocksPath, layoutPath, animTileset, animLength, animFrameStart, animFrameLength, animFrameDest);
        blockset = disassemblyManager.getBlockset();
        System.out.println("com.sfc.sf2.maplayout.MapLayoutManager.importDisassembly() - Disassembly imported.");
    }
    
    public void importSFCDBank(Tile[] tileset, byte[] blockData, byte[] layoutData){
        mapBlockManager = new MapBlockManager();
        sfcdBankManager = new SFCDBankManager();
        //blockset = mapBlockManager.importSFCDBank(String palettePath, String tileset1Path, String tileset2Path, String tileset3Path, String tileset4Path, String tileset5Path, String blocksPath)
        blockset = mapBlockManager.importSFCDBank(tileset, blockData);
        layout = sfcdBankManager.importSFCDBank(blockset, layoutData);
    }
    
    public void exportSFCDBank(Tile[] tileset, String blocksPath, String layoutPath){
        System.out.println("com.sfc.sf2.maplayout.MapLayoutManager.importDisassembly() - Exporting disassembly ...");
        mapBlockManager.exportSFCDBank(blocksPath);
        sfcdBankManager.exportDisassembly(blockset, layout, layoutPath);
        System.out.println("com.sfc.sf2.maplayout.MapLayoutManager.importDisassembly() - Disassembly exported.");   
    }
    
    public void exportDisassembly(String blocksPath, String layoutPath){
        System.out.println("com.sfc.sf2.maplayout.MapLayoutManager.importDisassembly() - Exporting disassembly ...");
        disassemblyManager.exportDisassembly(blockset, blocksPath, layout, layoutPath);
        System.out.println("com.sfc.sf2.maplayout.MapLayoutManager.importDisassembly() - Disassembly exported.");        
    }   
    
    public void importRom(String romFilePath, String paletteOffset, String paletteLength, String graphicsOffset, String graphicsLength){
        System.out.println("com.sfc.sf2.maplayout.MapLayoutManager.importOriginalRom() - Importing original ROM ...");
        graphicsManager.importRom(romFilePath, paletteOffset, paletteLength, graphicsOffset, graphicsLength,GraphicsManager.COMPRESSION_BASIC);
        System.out.println("com.sfc.sf2.maplayout.MapLayoutManager.importOriginalRom() - Original ROM imported.");
    }
    
    public void exportRom(String originalRomFilePath, String graphicsOffset){
        System.out.println("com.sfc.sf2.maplayout.MapLayoutManager.exportOriginalRom() - Exporting original ROM ...");
        graphicsManager.exportRom(originalRomFilePath, graphicsOffset, GraphicsManager.COMPRESSION_BASIC);
        System.out.println("com.sfc.sf2.maplayout.MapLayoutManager.exportOriginalRom() - Original ROM exported.");        
    }      
    
    public void exportPng(String filepath){
        System.out.println("com.sfc.sf2.maplayout.MapLayoutManager.exportPng() - Exporting PNG ...");
        PngManager.exportPng(layout, filepath);
        System.out.println("com.sfc.sf2.maplayout.MapLayoutManager.exportPng() - PNG exported.");       
    }

    public MapLayout getLayout() {
        return layout;
    }

    public void setLayout(MapLayout layout) {
        this.layout = layout;
    }

    public MapBlock[] getBlockset() {
        return blockset;
    }

    public void setBlockset(MapBlock[] blockset) {
        this.blockset = blockset;
    }
    
    
    
}
