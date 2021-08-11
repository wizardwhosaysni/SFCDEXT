/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.weaponsprite.io;

import com.sfc.sf2.graphics.Tile;
import com.sfc.sf2.graphics.compressed.BasicGraphicsDecoder;
import com.sfc.sf2.graphics.compressed.BasicGraphicsEncoder;
import com.sfc.sf2.weaponsprite.WeaponSprite;
import com.sfc.sf2.graphics.compressed.StackGraphicsDecoder;
import com.sfc.sf2.graphics.compressed.StackGraphicsEncoder;
import com.sfc.sf2.palette.graphics.PaletteDecoder;
import com.sfc.sf2.palette.graphics.PaletteEncoder;
import java.awt.Color;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author wiz
 */
public class DisassemblyManager {

    private static final int WEAPONSPRITE_TILE_LENGTH = 256;
    
    public static WeaponSprite importDisassembly(String palettesBasePath, String graphicsPath){
        System.out.println("com.sfc.sf2.weaponsprite.io.DisassemblyManager.importDisassembly() - Importing disassembly ...");
        WeaponSprite weaponsprite = null;

        Tile[] tiles = null;
        Color[][] palettes = null;
        

        List<Color[]> paletteList = new ArrayList();
        try{
            for(int i=0;i<100;i++){
                String index = String.format("%02d", i);
                String fullPathString = palettesBasePath + index + ".bin";
                Path palettePath = Paths.get(fullPathString);
                if(palettePath.toFile().exists()){                    
                    byte[] basePaletteData = new byte[32];
                    byte[] paletteData = Files.readAllBytes(palettePath);
                    basePaletteData[2] = 0xE;
                    basePaletteData[3] = (byte)0xEE;
                    basePaletteData[4] = 0;
                    basePaletteData[5] = 0;
                    basePaletteData[28] = paletteData[0];
                    basePaletteData[29] = paletteData[1];
                    basePaletteData[30] = paletteData[2];
                    basePaletteData[31] = paletteData[3];
                    Color[] palette = PaletteDecoder.parsePalette(basePaletteData);
                    palette[0] = new Color(255, 255, 255, 0);
                    paletteList.add(palette);
                }else{
                    System.out.println("com.sfc.sf2.weaponsprite.io.DisassemblyManager.importDisassembly() - Stopping at index " + i); 
                    break;
                }
                palettes = new Color[paletteList.size()][];
                palettes = paletteList.toArray(palettes);
            }
        }catch(Exception e){
             System.err.println("com.sfc.sf2.weaponsprite.io.DisassemblyManager.importDisassembly() - Error while parsing palette data : "+e);
        }         
        
        try{
            Path graphicspath = Paths.get(graphicsPath);
            if(graphicspath.toFile().exists()){
                byte[] graphicsData = Files.readAllBytes(graphicspath);
                if(graphicsData.length>2){
                    tiles = new StackGraphicsDecoder().decodeStackGraphics(graphicsData, palettes[0]);
                }else{
                    System.out.println("com.sfc.sf2.weaponsprite.io.DisassemblyManager.parseGraphics() - File ignored because of too small length (must be a dummy file) " + graphicsData.length + " : " + graphicsPath);
                }
            }            

            if(tiles!=null){
                if(tiles.length==WEAPONSPRITE_TILE_LENGTH){
                   weaponsprite = new WeaponSprite();   
                   weaponsprite.setTiles(tiles);
                   weaponsprite.setPalettes(palettes);
                   System.out.println("Created WeaponSprite with " + tiles.length + " tiles.");                       
                }else{
                    System.out.println("Could not create WeaponSprite because of wrong length : tiles=" + tiles.length);
                }
            }
        }catch(Exception e){
             System.err.println("com.sfc.sf2.weaponsprite.io.PngManager.importPng() - Error while parsing graphics data : "+e);
             e.printStackTrace();
        }         
                
        System.out.println("com.sfc.sf2.weaponsprite.io.DisassemblyManager.importDisassembly() - Disassembly imported.");
        return weaponsprite;
    }
    
    public static void exportDisassembly(WeaponSprite weaponsprite, String graphicsPath){
        System.out.println("com.sfc.sf2.weaponsprite.io.DisassemblyManager.exportDisassembly() - Exporting disassembly ...");
        try {
            Tile[] tileset = weaponsprite.getTiles();
            StackGraphicsEncoder.produceGraphics(tileset);
            byte[] newWeaponSpriteFileBytes = StackGraphicsEncoder.getNewGraphicsFileBytes();
            Path graphicsFilePath = Paths.get(graphicsPath);
            Files.write(graphicsFilePath,newWeaponSpriteFileBytes);
            System.out.println(newWeaponSpriteFileBytes.length + " bytes into " + graphicsFilePath);                
        } catch (Exception ex) {
            Logger.getLogger(DisassemblyManager.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
            System.out.println(ex);
        }            
        System.out.println("com.sfc.sf2.weaponsprite.io.DisassemblyManager.exportDisassembly() - Disassembly exported.");        
    }    
    
    private static Tile[] parseGraphics(String palettesPath, String graphicsPath){
        Tile[] tiles = null;
        Color[][] palettes = null;
        try{
            Path palettespath = Paths.get(palettesPath);
            Path graphicspath = Paths.get(graphicsPath);
            if(palettespath.toFile().exists() && graphicspath.toFile().exists()){
                byte[] basePaletteData = new byte[32];
                byte[] paletteData = Files.readAllBytes(palettespath);
                palettes = new Color[paletteData.length/2][];
                for(int i=0;i<paletteData.length/2;i++){
                    basePaletteData[2] = 0xE;
                    basePaletteData[3] = (byte)0xEE;
                    basePaletteData[4] = 0;
                    basePaletteData[5] = 0;
                    basePaletteData[28] = paletteData[i*2];
                    basePaletteData[29] = paletteData[i*2+1];
                    basePaletteData[30] = paletteData[i*2+2];
                    basePaletteData[31] = paletteData[i*2+3];
                    Color[] palette = PaletteDecoder.parsePalette(basePaletteData);
                    palette[0] = new Color(255, 255, 255, 0);
                    palettes[i] = palette;
                }
                byte[] graphicsData = Files.readAllBytes(graphicspath);
                if(graphicsData.length>2){
                    tiles = new StackGraphicsDecoder().decodeStackGraphics(graphicsData, palettes[0]);
                }else{
                    System.out.println("com.sfc.sf2.weaponsprite.io.DisassemblyManager.parseGraphics() - File ignored because of too small length (must be a dummy file) " + graphicsData.length + " : " + graphicsPath);
                }
            }            
        }catch(Exception e){
             System.err.println("com.sfc.sf2.weaponsprite.io.DisassemblyManager.parseGraphics() - Error while parsing graphics data : "+e);
             e.printStackTrace();
        } 
        return tiles;
    }   
    
    private static short getNextWord(byte[] data, int cursor){
        ByteBuffer bb = ByteBuffer.allocate(2);
        bb.order(ByteOrder.LITTLE_ENDIAN);
        bb.put(data[cursor+1]);
        bb.put(data[cursor]);
        short s = bb.getShort(0);
        return s;
    }

    
}
