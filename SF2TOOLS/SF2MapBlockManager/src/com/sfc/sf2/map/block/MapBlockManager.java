/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.map.block;

import com.sfc.sf2.graphics.GraphicsManager;
import com.sfc.sf2.graphics.Tile;
import com.sfc.sf2.graphics.compressed.StackGraphicsDecoder;
import com.sfc.sf2.map.block.io.DisassemblyManager;
import com.sfc.sf2.map.block.io.PngManager;
import com.sfc.sf2.map.block.io.SFCDBankManager;
import com.sfc.sf2.palette.PaletteManager;
import com.sfc.sf2.palette.graphics.PaletteDecoder;
import java.awt.Color;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 *
 * @author wiz
 */
public class MapBlockManager {
       
    private PaletteManager paletteManager = new PaletteManager();
    private GraphicsManager graphicsManager = new GraphicsManager();
    private DisassemblyManager disassemblyManager = new DisassemblyManager();
    private SFCDBankManager sfcdBankManager = new SFCDBankManager();
    private Tile[] tiles;
    private MapBlock[] blocks;

    public Tile[] getTiles() {
        return tiles;
    }

    public void setTiles(Tile[] tiles) {
        this.tiles = tiles;
    }
       
    public void importDisassembly(String palettePath, String tileset1Path, String tileset2Path, String tileset3Path, String tileset4Path, String tileset5Path, String blocksPath){
        System.out.println("com.sfc.sf2.mapblock.MapBlockManager.importDisassembly() - Importing disassembly ...");
        disassemblyManager = new DisassemblyManager();
        blocks = disassemblyManager.importDisassembly(palettePath, tileset1Path, tileset2Path, tileset3Path, tileset4Path, tileset5Path, blocksPath);
        tiles = disassemblyManager.getTileset();
        //graphicsManager.setTiles(tiles);
        System.out.println("com.sfc.sf2.mapblock.MapBlockManager.importDisassembly() - Disassembly imported.");
    }
       
    public void importDisassembly(String palettePath, String tileset1Path, String tileset2Path, String tileset3Path, String tileset4Path, String tileset5Path, String blocksPath, String animTilesetPath, int animTilesetStart, int animTilesetLength, int animTilesetDest){
        System.out.println("com.sfc.sf2.mapblock.MapBlockManager.importDisassembly() - Importing disassembly ...");
        disassemblyManager = new DisassemblyManager();
        blocks = disassemblyManager.importDisassembly(palettePath, tileset1Path, tileset2Path, tileset3Path, tileset4Path, tileset5Path, blocksPath, animTilesetPath, animTilesetStart, animTilesetLength, animTilesetDest);
        tiles = disassemblyManager.getTileset();
        //graphicsManager.setTiles(tiles);
        System.out.println("com.sfc.sf2.mapblock.MapBlockManager.importDisassembly() - Disassembly imported.");
    }    
    
    public void exportDisassembly(String graphicsPath){
        System.out.println("com.sfc.sf2.mapblock.MapBlockManager.importDisassembly() - Exporting disassembly ...");
        disassemblyManager = new DisassemblyManager();
        disassemblyManager.setTileset(tiles);
        disassemblyManager.exportDisassembly(blocks, graphicsPath);
        System.out.println("com.sfc.sf2.mapblock.MapBlockManager.importDisassembly() - Disassembly exported.");        
    }   
    
    public MapBlock[] importSFCDBank(Tile[] tileset, byte[] inputData){
        sfcdBankManager = new SFCDBankManager();
        blocks = sfcdBankManager.importSFCDBank(tileset, inputData);
        tiles = sfcdBankManager.getTileset();         
        return blocks;
    }
    
    public void exportSFCDBank(String filepath){
        System.out.println("com.sfc.sf2.mapblock.MapBlockManager.importDisassembly() - Exporting disassembly ...");
        sfcdBankManager = new SFCDBankManager();
        sfcdBankManager.setTileset(tiles);
        sfcdBankManager.exportSFCDBank(blocks, filepath);
        System.out.println("com.sfc.sf2.mapblock.MapBlockManager.importDisassembly() - Disassembly exported.");        
    }       
    
    public void importRom(String romFilePath, String paletteOffset, String paletteLength, String graphicsOffset, String graphicsLength){
        System.out.println("com.sfc.sf2.mapblock.MapBlockManager.importOriginalRom() - Importing original ROM ...");
        graphicsManager.importRom(romFilePath, paletteOffset, paletteLength, graphicsOffset, graphicsLength,GraphicsManager.COMPRESSION_BASIC);
        tiles = graphicsManager.getTiles();
        System.out.println("com.sfc.sf2.mapblock.MapBlockManager.importOriginalRom() - Original ROM imported.");
    }
    
    public void exportRom(String originalRomFilePath, String graphicsOffset){
        System.out.println("com.sfc.sf2.mapblock.MapBlockManager.exportOriginalRom() - Exporting original ROM ...");
        graphicsManager.exportRom(originalRomFilePath, graphicsOffset, GraphicsManager.COMPRESSION_BASIC);
        System.out.println("com.sfc.sf2.mapblock.MapBlockManager.exportOriginalRom() - Original ROM exported.");        
    }      
    
    public void exportPng(String filepath, int blocksPerRow){
        System.out.println("com.sfc.sf2.mapblock.MapBlockManager.exportPng() - Exporting PNG ...");
        PngManager.exportPng(blocks, filepath, blocksPerRow);
        System.out.println("com.sfc.sf2.mapblock.MapBlockManager.exportPng() - PNG exported.");       
    }

    public MapBlock[] getBlocks() {
        return blocks;
    }

    public void setBlocks(MapBlock[] blocks) {
        this.blocks = blocks;
    }
}
