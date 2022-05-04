/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sfcd.extractor;

import com.sfc.sf2.background.Background;
import com.sfc.sf2.background.BackgroundManager;
import com.sfc.sf2.battlesprite.BattleSprite;
import com.sfc.sf2.battlesprite.BattleSpriteManager;
import com.sfc.sf2.graphics.GraphicsManager;
import com.sfc.sf2.graphics.Tile;
import com.sfc.sf2.map.Map;
import com.sfc.sf2.map.MapManager;
import com.sfc.sf2.map.block.MapBlock;
import com.sfc.sf2.map.block.MapBlockManager;
import com.sfc.sf2.map.block.layout.MapBlockLayout;
import com.sfc.sf2.map.gui.MapPanel;
import com.sfc.sf2.map.layout.MapLayoutManager;
import com.sfc.sf2.mapsprite.MapSprite;
import com.sfc.sf2.mapsprite.MapSpriteManager;
import com.sfc.sf2.mapsprite.io.DisassemblyManager;
import com.sfc.sf2.mapsprite.io.PngManager;
import com.sfc.sf2.mapsprite.io.GifManager;
import com.sfc.sf2.mapsprite.io.SFCDBankManager;
import com.sfc.sf2.palette.PaletteManager;
import com.sfc.sf2.portrait.Portrait;
import com.sfc.sf2.portrait.PortraitManager;
import com.sfc.sf2.text.TextManager;
import com.sfc.sf2.weaponsprite.WeaponSprite;
import com.sfc.sf2.weaponsprite.WeaponSpriteManager;
import java.awt.Color;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 *
 * @author wiz
 */
public class SFCDExtractionManager {
    
    private static final int MAINCPU_WORDRAM_LOADING_OFFSET = 0x23DE00;
    private static final int SUBCPU_LOADING_OFFSET = 0x10000;
       
    private PaletteManager paletteManager = new PaletteManager();
    private GraphicsManager graphicsManager = new GraphicsManager();
    private MapSpriteManager mapSpriteManager = new MapSpriteManager();
    private BattleSpriteManager battleSpriteManager = new BattleSpriteManager();
    private PortraitManager portraitManager = new PortraitManager();
    private WeaponSpriteManager weaponSpriteManager = new WeaponSpriteManager();
    private BackgroundManager backgroundManager = new BackgroundManager();
    private MapManager mapManager = new MapManager();
    private MapBlockManager mapBlockManager = new MapBlockManager();
    private MapLayoutManager mapLayoutManager = new MapLayoutManager();
    
       
    public void extractSFCDBanks(String basepalettePath, String folderPath){
        System.out.println("com.sfc.sf2.mapsprite.SFCDExtractionManager.importSFCDBanks() - Extracting SFCD Banks ...");
        
        
        
        for(int fileIndex = 0;fileIndex<=0x48;fileIndex++){
            
            try{
                String fileIndexString = String.format("%02x", fileIndex).toUpperCase();
                String filePath = folderPath + "BANKD" + fileIndexString + ".X";
                Path path = Paths.get(filePath);
                if(path.toFile().exists()){
                    System.out.println("Extracting file : "+filePath);
                    byte[] fileData = Files.readAllBytes(path);
                            
                    File bankFolder = new File(folderPath+"BANKD"+fileIndexString);
                    if (!bankFolder.exists()){
                        bankFolder.mkdirs();
                    }

                    /* MAPSPRITES */
                    /*try{
                        String mapspritesFolderString = folderPath+"BANKD"+fileIndexString+File.separator+"mapsprites";
                        File mapspritesFolder = new File(mapspritesFolderString);
                        if (!mapspritesFolder.exists()){
                            mapspritesFolder.mkdirs();
                        }
                        mapSpriteManager.importSFCDBank(basepalettePath, filePath, "10000", "", "384");
                        mapSpriteManager.exportDisassembly(mapspritesFolderString);
                        mapSpriteManager.exportPng(mapspritesFolderString);
                        System.out.println("  - Mapsprites extracted.");
                    }catch(Exception e){
                        System.out.println("  Mapsprite exception : "+e.getMessage());
                    }*/
                    
                    
                    /* ALLY BATTLESPRITES */
                    /*try{
                        String battlespritesFolderString = folderPath+"BANKD"+fileIndexString+File.separator+"battlesprites";
                        File battlespritesFolder = new File(battlespritesFolderString);
                        if (!battlespritesFolder.exists()){
                            battlespritesFolder.mkdirs();
                        }
                        String allyBattlespritesFolderString = folderPath+"BANKD"+fileIndexString+File.separator+"battlesprites"+File.separator+"allies";
                        File allyBattlespritesFolder = new File(allyBattlespritesFolderString);
                        if (!allyBattlespritesFolder.exists()){
                            allyBattlespritesFolder.mkdirs();
                        }
                        for(int i=0;i<64;i++){
                            String iS = String.format("%02d", i);
                            try{
                                battleSpriteManager.importSFCDBank(filePath, "10000", "", i);
                            }catch(Exception e){
                                break;
                            }
                            BattleSprite bs = battleSpriteManager.getBattleSprite();
                            battleSpriteManager.exportDisassembly(allyBattlespritesFolderString+File.separator+"allybattlesprite"+iS+".bin", Integer.toString(bs.getAnimSpeed(),10), Integer.toString(bs.getUnknown(),10));
                            battleSpriteManager.exportPng(allyBattlespritesFolderString+File.separator+"allybattlesprite"+iS,0);
                        }
                        System.out.println("  - Ally BattleSprites extracted.");
                    }catch(Exception e){
                        System.out.println("  BattleSprite exception : "+e.getMessage());
                    }*/
                    
                    /* MAP TILESETS */
                    /*try{
                        String mapFolderString = folderPath+"BANKD"+fileIndexString+File.separator+"map";
                        File mapFolder = new File(mapFolderString);
                        if (!mapFolder.exists()){
                            mapFolder.mkdirs();
                        }
                        String maptilesetsFolderString = folderPath+"BANKD"+fileIndexString+File.separator+"map"+File.separator+"maptilesets";
                        File maptilesetsFolder = new File(maptilesetsFolderString);
                        if (!maptilesetsFolder.exists()){
                            maptilesetsFolder.mkdirs();
                        }
                        for(int i=0;i<5;i++){
                            String iS = String.format("%03d", i);
                            int ptOffset = ((fileData[0x8+0]&0xFF)<<24) + ((fileData[0x8+1]&0xFF)<<16) + ((fileData[0x8+2]&0xFF)<<8) + ((fileData[0x8+3]&0xFF)) - 0x10000;
                            int pointerByte1 = fileData[ptOffset+i*4+0]&0xFF;
                            int pointerByte2 = fileData[ptOffset+i*4+1]&0xFF;
                            int pointerByte3 = fileData[ptOffset+i*4+2]&0xFF;
                            int pointerByte4 = fileData[ptOffset+i*4+3]&0xFF;
                            int pointer = (pointerByte1<<24) + (pointerByte2<<16) + (pointerByte3<<8) + (pointerByte4) - 0x10000;
                            try{
                                graphicsManager.importRom(filePath, basepalettePath, "16", Integer.toHexString(pointer), "16000", GraphicsManager.COMPRESSION_STACK);
                            }catch(Exception e){
                                break;
                            }
                            graphicsManager.exportDisassembly(maptilesetsFolderString+File.separator+"maptileset"+iS+".bin", GraphicsManager.COMPRESSION_STACK);
                            graphicsManager.exportPng(maptilesetsFolderString+File.separator+"maptileset"+iS+".png", "16");
                        }
                        System.out.println("  - Map tilesets extracted.");
                    }catch(Exception e){
                        System.out.println("  Map Tileset exception : "+e.getMessage());
                    }*/
                    
                    //* PORTRAITS */
                    /*try{
                        String portraitsFolderString = folderPath+"BANKD"+fileIndexString+File.separator+"portraits";
                        File portraitsFolder = new File(portraitsFolderString);
                        if (!portraitsFolder.exists()){
                            portraitsFolder.mkdirs();
                        }
                        for(int i=0;i<64;i++){
                            String iS = String.format("%02d", i);
                            int ptOffset = ((fileData[0x10+0]&0xFF)<<24) + ((fileData[0x10+1]&0xFF)<<16) + ((fileData[0x10+2]&0xFF)<<8) + ((fileData[0x10+3]&0xFF)) - 0x10000;
                            int pointerByte1 = fileData[ptOffset+i*4+0]&0xFF;
                            int pointerByte2 = fileData[ptOffset+i*4+1]&0xFF;
                            int pointerByte3 = fileData[ptOffset+i*4+2]&0xFF;
                            int pointerByte4 = fileData[ptOffset+i*4+3]&0xFF;
                            int pointer = (pointerByte1<<24) + (pointerByte2<<16) + (pointerByte3<<8) + (pointerByte4) - 0x10000;
                            Portrait portrait = null;
                            try{
                                portrait = com.sfc.sf2.portrait.io.DisassemblyManager.importDisassembly(filePath, pointer);
                            }catch(Exception e){
                                break;
                            }
                            com.sfc.sf2.portrait.io.DisassemblyManager.exportDisassembly(portrait, portraitsFolderString+File.separator+"portrait"+iS+".bin");
                            com.sfc.sf2.portrait.io.PngManager.exportPng(portrait, portraitsFolderString+File.separator+"portrait"+iS+".png");
                        }
                        System.out.println("  - Portraits extracted.");
                    }catch(Exception e){
                        System.out.println("  Portrait exception : "+e.getMessage());
                    }*/
                    
                    
                    
                    
                    /* ENEMY BATTLESPRITES */
                    /*try{
                        String battlespritesFolderString = folderPath+"BANKD"+fileIndexString+File.separator+"battlesprites";
                        File battlespritesFolder = new File(battlespritesFolderString);
                        if (!battlespritesFolder.exists()){
                            battlespritesFolder.mkdirs();
                        }
                        String allyBattlespritesFolderString = folderPath+"BANKD"+fileIndexString+File.separator+"battlesprites"+File.separator+"enemies";
                        File allyBattlespritesFolder = new File(allyBattlespritesFolderString);
                        if (!allyBattlespritesFolder.exists()){
                            allyBattlespritesFolder.mkdirs();
                        }
                        com.sfc.sf2.battlesprite.io.SFCDBankManager.clearProcessedPointersList();
                        for(int i=0;i<64;i++){
                            String iS = String.format("%02d", i);
                            BattleSprite bs = null;
                            try{
                                bs = com.sfc.sf2.battlesprite.io.SFCDBankManager.importSFCDBank(filePath, 0x10000, 0, i);
                            }catch(Exception e){
                                System.out.println("Exception for i="+i+" : "+e.getMessage());
                                break;
                            }
                            if(bs!=null){
                                battleSpriteManager.setBattleSprite(bs);
                                battleSpriteManager.exportDisassembly(allyBattlespritesFolderString+File.separator+"enemybattlesprite"+iS+".bin", Integer.toString(bs.getAnimSpeed(),10), Integer.toString(bs.getUnknown(),10));
                                battleSpriteManager.exportPng(allyBattlespritesFolderString+File.separator+"enemybattlesprite"+iS,0);
                            }
                        }
                        System.out.println("  - Ally BattleSprites extracted.");
                    }catch(Exception e){
                        System.out.println("  BattleSprite exception : "+e.getMessage());
                    }*/
                    
                    
                    
                    /* WEAPON SPRITES */
                    /*try{
                        String weaponspritesFolderString = folderPath+"BANKD"+fileIndexString+File.separator+"weaponsprites";
                        File weaponspritesFolder = new File(weaponspritesFolderString);
                        if (!weaponspritesFolder.exists()){
                            weaponspritesFolder.mkdirs();
                        }
                        com.sfc.sf2.weaponsprite.io.SFCDBankManager.clearProcessedPointersList();
                        for(int i=0;i<64;i++){
                            String iS = String.format("%02d", i);
                            WeaponSprite ws = null;
                            try{
                                ws = com.sfc.sf2.weaponsprite.io.SFCDBankManager.importSFCDBank("C:\\SEGADEV\\GITHUB\\SF2DISASM\\disasm\\data\\graphics\\battles\\weapons\\palettes\\weaponpalette",filePath, 0x10000, i);
                            }catch(Exception e){
                                System.out.println("Exception for i="+i+" : "+e.getMessage());
                                break;
                            }
                            if(ws!=null){
                                weaponSpriteManager.setWeaponsprite(ws);
                                weaponSpriteManager.exportDisassembly(weaponspritesFolderString+File.separator+"weaponsprite"+iS+".bin");
                                weaponSpriteManager.exportPng(weaponspritesFolderString+File.separator+"weaponsprite"+iS+".png");
                            }
                        }
                        System.out.println("  - WeaponSprites extracted.");
                    }catch(Exception e){
                        System.out.println("  WeaponSprite exception : "+e.getMessage());
                    }*/
                    
                    
                    
                    /* BACKGROUNDS */
                    /*try{
                        String backgroundsFolderString = folderPath+"BANKD"+fileIndexString+File.separator+"backgrounds";
                        File backgroundsFolder = new File(backgroundsFolderString);
                        if (!backgroundsFolder.exists()){
                            backgroundsFolder.mkdirs();
                        }
                        com.sfc.sf2.weaponsprite.io.SFCDBankManager.clearProcessedPointersList();
                        for(int i=0;i<64;i++){
                            String iS = String.format("%02d", i);
                            Background b = null;
                            try{
                                b = com.sfc.sf2.background.io.SFCDBankManager.importSFCDBank(filePath, 0x10000, 0, i);
                            }catch(Exception e){
                                System.out.println("Exception for i="+i+" : "+e.getMessage());
                                break;
                            }
                            if(b!=null){
                                Background[] bs = new Background[1];
                                backgroundManager.setBackgrounds(bs);
                                backgroundManager.exportSingleDisassembly(b, backgroundsFolderString+File.separator+"background"+iS+".bin");
                                backgroundManager.exportSinglePng(b, backgroundsFolderString+File.separator+"background"+iS+".png");
                            }
                        }
                        System.out.println("  - WeaponSprites extracted.");
                    }catch(Exception e){
                        System.out.println("  WeaponSprite exception : "+e.getMessage());
                    }*/
                    
                    
                    
                    
                    
                    
                    /* MAP TILESETS */
                    /*try{
                        String mapFolderString = folderPath+"BANKD"+fileIndexString+File.separator+"map";
                        File mapFolder = new File(mapFolderString);
                        if (!mapFolder.exists()){
                            mapFolder.mkdirs();
                        }
                        String maptilesetsFolderString = folderPath+"BANKD"+fileIndexString+File.separator+"map"+File.separator+"assets";
                        File maptilesetsFolder = new File(maptilesetsFolderString);
                        if (!maptilesetsFolder.exists()){
                            maptilesetsFolder.mkdirs();
                        }

                        int mapOffset =  ((fileData[0x80+0]&0xFF)<<24) + ((fileData[0x80+1]&0xFF)<<16) + ((fileData[0x80+2]&0xFF)<<8) + ((fileData[0x80+3]&0xFF)) - SUBCPU_LOADING_OFFSET;
                        int palettePointerByte1 = fileData[mapOffset+2+0*4+0]&0xFF;
                        int palettePointerByte2 = fileData[mapOffset+2+0*4+1]&0xFF;
                        int palettePointerByte3 = fileData[mapOffset+2+0*4+2]&0xFF;
                        int palettePointerByte4 = fileData[mapOffset+2+0*4+3]&0xFF;
                        int palettePointer = (palettePointerByte1<<24) + (palettePointerByte2<<16) + (palettePointerByte3<<8) + (palettePointerByte4) - MAINCPU_WORDRAM_LOADING_OFFSET + mapOffset;                          
                        paletteManager.importRom(filePath, Integer.toHexString(palettePointer), "32");
                        Color[] palette = paletteManager.getPalette();
                        paletteManager.exportDisassembly(maptilesetsFolderString+File.separator+"mappalette00.bin", palette);
                        
                        for(int i=0;i<5;i++){
                            String iS = String.format("%03d", i);
                            int ptOffset = ((fileData[0x8+0]&0xFF)<<24) + ((fileData[0x8+1]&0xFF)<<16) + ((fileData[0x8+2]&0xFF)<<8) + ((fileData[0x8+3]&0xFF)) - SUBCPU_LOADING_OFFSET;
                            int pointerByte1 = fileData[ptOffset+i*4+0]&0xFF;
                            int pointerByte2 = fileData[ptOffset+i*4+1]&0xFF;
                            int pointerByte3 = fileData[ptOffset+i*4+2]&0xFF;
                            int pointerByte4 = fileData[ptOffset+i*4+3]&0xFF;
                            int pointer = (pointerByte1<<24) + (pointerByte2<<16) + (pointerByte3<<8) + (pointerByte4) - 0x10000;
                            try{
                                graphicsManager.importRom(filePath, Integer.toHexString(palettePointer), "32", Integer.toHexString(pointer), "16000", GraphicsManager.COMPRESSION_STACK);
                            }catch(Exception e){
                                e.printStackTrace();
                                break;
                            }
                            graphicsManager.exportDisassembly(maptilesetsFolderString+File.separator+"maptileset"+iS+".bin", GraphicsManager.COMPRESSION_STACK);
                            graphicsManager.exportPng(maptilesetsFolderString+File.separator+"maptileset"+iS+".png", "16");
                        }
                        System.out.println("  - Map tilesets extracted.");
                    }catch(Exception e){
                        System.out.println("  Map Tileset exception : "+e.getMessage());
                    } 
                    */
                    
                    /* MAP */
                    /*try{
                        
                        paletteManager = new PaletteManager();
                        graphicsManager = new GraphicsManager();
                        mapBlockManager = new MapBlockManager();
                        mapLayoutManager = new MapLayoutManager();
                        mapManager = new MapManager();
                        
                        String mapFolderString = folderPath+"BANKD"+fileIndexString+File.separator+"map";
                        File mapFolder = new File(mapFolderString);
                        if (!mapFolder.exists()){
                            mapFolder.mkdirs();
                        }
                        
                        int mapOffset =  ((fileData[0x80+0]&0xFF)<<24) + ((fileData[0x80+1]&0xFF)<<16) + ((fileData[0x80+2]&0xFF)<<8) + ((fileData[0x80+3]&0xFF)) - SUBCPU_LOADING_OFFSET;
                        int palettePointerByte1 = fileData[mapOffset+2+0*4+0]&0xFF;
                        int palettePointerByte2 = fileData[mapOffset+2+0*4+1]&0xFF;
                        int palettePointerByte3 = fileData[mapOffset+2+0*4+2]&0xFF;
                        int palettePointerByte4 = fileData[mapOffset+2+0*4+3]&0xFF;
                        int palettePointer = (palettePointerByte1<<24) + (palettePointerByte2<<16) + (palettePointerByte3<<8) + (palettePointerByte4) - MAINCPU_WORDRAM_LOADING_OFFSET + mapOffset;                          
                        paletteManager.importRom(filePath, Integer.toHexString(palettePointer), "32");
                        Color[] palette = paletteManager.getPalette();                        
                        Tile[] tileset = new Tile[5*128];
                        for(int i=0;i<5;i++){
                            int ptOffset = ((fileData[0x8+0]&0xFF)<<24) + ((fileData[0x8+1]&0xFF)<<16) + ((fileData[0x8+2]&0xFF)<<8) + ((fileData[0x8+3]&0xFF)) - SUBCPU_LOADING_OFFSET;
                            int pointerByte1 = fileData[ptOffset+i*4+0]&0xFF;
                            int pointerByte2 = fileData[ptOffset+i*4+1]&0xFF;
                            int pointerByte3 = fileData[ptOffset+i*4+2]&0xFF;
                            int pointerByte4 = fileData[ptOffset+i*4+3]&0xFF;
                            int pointer = (pointerByte1<<24) + (pointerByte2<<16) + (pointerByte3<<8) + (pointerByte4) - 0x10000;
                            try{
                                graphicsManager.importRom(filePath, Integer.toHexString(palettePointer), "32", Integer.toHexString(pointer), "16000", GraphicsManager.COMPRESSION_STACK);
                            }catch(Exception e){
                                e.printStackTrace();
                                break;
                            }
                            System.arraycopy(graphicsManager.getTiles(), 0, tileset, i*128, graphicsManager.getTiles().length);
                        }
                        for(int i=0;i<tileset.length;i++){
                            if(tileset[i]!=null){
                                tileset[i].setId(i);
                            }
                        }                        
                        
                        Map map = null;
                        
                        try{
                            map = mapManager.importSFCDBank(fileData, tileset);
                        }catch(Exception e){
                            e.printStackTrace();
                            break;
                        }
                        
                        int ptOffset =  ((fileData[0x80+0]&0xFF)<<24) + ((fileData[0x80+1]&0xFF)<<16) + ((fileData[0x80+2]&0xFF)<<8) + ((fileData[0x80+3]&0xFF)) - SUBCPU_LOADING_OFFSET;
                        int pointerByte1;
                        int pointerByte2;
                        int pointerByte3;
                        int pointerByte4;
                        int blockPointer;
                        int layoutPointer;
                        int pointerIndex = 1;
                        pointerByte1 = fileData[ptOffset+2+pointerIndex*4+0]&0xFF;
                        pointerByte2 = fileData[ptOffset+2+pointerIndex*4+1]&0xFF;
                        pointerByte3 = fileData[ptOffset+2+pointerIndex*4+2]&0xFF;
                        pointerByte4 = fileData[ptOffset+2+pointerIndex*4+3]&0xFF;
                        blockPointer = (pointerByte1<<24) + (pointerByte2<<16) + (pointerByte3<<8) + (pointerByte4) - MAINCPU_WORDRAM_LOADING_OFFSET + ptOffset;
                        pointerIndex = 2;
                        pointerByte1 = fileData[ptOffset+2+pointerIndex*4+0]&0xFF;
                        pointerByte2 = fileData[ptOffset+2+pointerIndex*4+1]&0xFF;
                        pointerByte3 = fileData[ptOffset+2+pointerIndex*4+2]&0xFF;
                        pointerByte4 = fileData[ptOffset+2+pointerIndex*4+3]&0xFF;
                        layoutPointer = (pointerByte1<<24) + (pointerByte2<<16) + (pointerByte3<<8) + (pointerByte4) - MAINCPU_WORDRAM_LOADING_OFFSET + ptOffset;
                        byte[] blockData = new byte[layoutPointer-blockPointer];
                        System.arraycopy(fileData, (int)blockPointer, blockData, 0, layoutPointer-blockPointer);
                        byte[] layoutData = new byte[0x1000];
                        System.arraycopy(fileData, (int)layoutPointer, layoutData, 0, 0x1000); 

                        /*Path blocksFilePath = Paths.get(mapFolderString+File.separator+"0-blocks.bin");
                        Files.write(blocksFilePath,blockData);*/
                        
                        /*mapBlockManager.importSFCDBank(tileset, blockData);
                        mapBlockManager.setTiles(tileset);
                        mapBlockManager.setBlocks(map.getBlocks());
                        mapBlockManager.exportSFCDBank(mapFolderString+File.separator+"0-blocks.bin");*/
                        /*mapManager.exportSFCDBank(tileset,
                                                     mapFolderString+File.separator+"0-blocks.bin", 
                                                     mapFolderString+File.separator+"1-layout.bin", 
                                                     mapFolderString+File.separator+"2-areas.asm", 
                                                     mapFolderString+File.separator+"3-flag-events.asm", 
                                                     mapFolderString+File.separator+"4-step-events.asm", 
                                                     mapFolderString+File.separator+"5-roof-events.asm", 
                                                     mapFolderString+File.separator+"6-warp-events.asm", 
                                                     mapFolderString+File.separator+"7-chest-items.asm", 
                                                     mapFolderString+File.separator+"8-other-items.asm", 
                                                     mapFolderString+File.separator+"9-animations.asm");
                        MapBlockLayout blockLayout = new MapBlockLayout();
                        MapBlock[] blocks = map.getBlocks();                        
                        blockLayout.setBlocks(blocks);
                        blockLayout.setTilesPerRow(3*(int)Math.round(Math.sqrt(blocks.length)));
                        mapManager.exportPngBlockset(blockLayout, mapFolderString+File.separator+"0-blocks.png");
                        
                        MapPanel mapPanel = new MapPanel();
                        mapPanel.setMap(map);
                        mapPanel.setMapLayout(map.getLayout());
                        mapPanel.setBlockset(map.getBlocks()); 
                        mapManager.exportPngMapLayout(mapPanel, 
                                                        mapFolderString+File.separator+"1-layout.png", 
                                                        mapFolderString+File.separator+"1-layoutflags.txt", 
                                                        mapFolderString+File.separator+"1-layouthptiles.txt");

                        System.out.println("  - Maps extracted.");
                    }catch(Exception e){
                        e.printStackTrace();
                        System.out.println("  Maps exception : "+e.getMessage());
                    }*/   
                        
                        

                    
                    //* TEXT */
                    try{
                        String textFolderString = folderPath+"BANKD"+fileIndexString+File.separator+"text";
                        File textFolder = new File(textFolderString);
                        if (!textFolder.exists()){
                            textFolder.mkdirs();
                        };
                        int ptOffset = ((fileData[0x68+0]&0xFF)<<24) + ((fileData[0x68+1]&0xFF)<<16) + ((fileData[0x68+2]&0xFF)<<8) + ((fileData[0x68+3]&0xFF)) - 0x10000;
                        int pointerByte1 = fileData[ptOffset+0]&0xFF;
                        int pointerByte2 = fileData[ptOffset+1]&0xFF;
                        int pointerByte3 = fileData[ptOffset+2]&0xFF;
                        int pointerByte4 = fileData[ptOffset+3]&0xFF;
                        int textBankPointer = (pointerByte1<<24) + (pointerByte2<<16) + (pointerByte3<<8) + (pointerByte4) - 0x22E000 + ptOffset;
                        pointerByte1 = fileData[ptOffset+0x40+0]&0xFF;
                        pointerByte2 = fileData[ptOffset+0x40+1]&0xFF;
                        pointerByte3 = fileData[ptOffset+0x40+2]&0xFF;
                        pointerByte4 = fileData[ptOffset+0x40+3]&0xFF;
                        int treeOffsetsPointer = (pointerByte1<<24) + (pointerByte2<<16) + (pointerByte3<<8) + (pointerByte4) - 0x22E000 + ptOffset;
                        pointerByte1 = fileData[ptOffset+0x4A+0]&0xFF;
                        pointerByte2 = fileData[ptOffset+0x4A+1]&0xFF;
                        pointerByte3 = fileData[ptOffset+0x4A+2]&0xFF;
                        pointerByte4 = fileData[ptOffset+0x4A+3]&0xFF;
                        int treeDataPointer = (pointerByte1<<24) + (pointerByte2<<16) + (pointerByte3<<8) + (pointerByte4) - 0x22E000 + ptOffset;
                        
                        System.out.println("ptOffset=0x"+Integer.toHexString(ptOffset)
                                +", treeOffsetsPointer=0x"+Integer.toHexString(treeOffsetsPointer)
                                +", treeDataPointer=0x"+Integer.toHexString(treeDataPointer)
                                +", textBankPointer=0x"+Integer.toHexString(textBankPointer));
                        TextManager.importSFCDBank(filePath, ptOffset, treeOffsetsPointer, treeDataPointer, treeDataPointer, textBankPointer, ptOffset, 1024);
                        TextManager.exportTxt(folderPath+"BANKD"+fileIndexString+File.separator+"text"+File.separator+"textbank.txt");
                        
                        System.out.println("  - Text extracted.");
                    }catch(Exception e){
                        System.out.println("  Text exception : "+e.getMessage());
                        e.printStackTrace();
                    }                        
                    
                    System.out.println("Extraction done : "+filePath);        
                }
            }catch(Exception e){
                System.out.println("Exception caught for file index "+Integer.toString(fileIndex, 16)+" : "+e.getMessage());
            }        
        }
        
        

        System.out.println("com.sfc.sf2.mapsprite.SFCDExtractionManager.importSFCDBanks() - SFCD Banks extracted.");
    }
       
}
