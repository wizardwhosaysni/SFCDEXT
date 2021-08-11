/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.background.io;

import com.sfc.sf2.graphics.Tile;
import com.sfc.sf2.graphics.compressed.BasicGraphicsDecoder;
import com.sfc.sf2.graphics.compressed.BasicGraphicsEncoder;
import com.sfc.sf2.background.Background;
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
public class SFCDBankManager {

    private static final String BASE_FILENAME = "backgroundXX.bin";
    private static final int BACKGROUND_TILE_LENGTH = 384;
    private static List<Integer> processedPointers = new ArrayList();
    
    public static Background importSFCDBank(String filePath, int loadingOffset, int pointerTableOffset, int backgroundIndex) throws Exception{
        System.out.println("com.sfc.sf2.background.io.DisassemblyManager.importDisassembly() - Importing disassembly ...");
        Background background = new Background();
        try{
            Tile[] tiles = parseGraphics(filePath, loadingOffset, pointerTableOffset, backgroundIndex);
            if(tiles!=null){
                if(tiles.length==BACKGROUND_TILE_LENGTH){
                   background = new Background();
                   background.setIndex(backgroundIndex);                   
                   background.setTiles(tiles);
                   System.out.println("Created Background " + backgroundIndex + " with " + tiles.length + " tiles.");                       
                }else{
                    System.out.println("Could not create Background " + backgroundIndex + " because of wrong length : tiles=" + tiles.length);
                }
            }
        }catch(Exception e){
             System.err.println("com.sfc.sf2.background.io.PngManager.importPng() - Error while parsing graphics data : "+e);
             throw e;
        }         
                
        System.out.println("com.sfc.sf2.background.io.DisassemblyManager.importDisassembly() - Disassembly imported.");
        return background;
    }
    
    public static void exportDisassembly(Background[] backgrounds, String basepath){
        System.out.println("com.sfc.sf2.background.io.DisassemblyManager.exportDisassembly() - Exporting disassembly ...");
        try {
            for(Background background : backgrounds){
                String index = String.format("%02d", background.getIndex());
                String filePath = basepath + System.getProperty("file.separator") + BASE_FILENAME.replace("XX.bin", index+".bin");
                Tile[] tileset1 = new Tile[192];
                Tile[] tileset2 = new Tile[192];
                System.arraycopy(background.getTiles(),0,tileset1,0,192);
                System.arraycopy(background.getTiles(),192,tileset2,0,192);
                StackGraphicsEncoder.produceGraphics(tileset1);
                byte[] newTileset1 = StackGraphicsEncoder.getNewGraphicsFileBytes();
                StackGraphicsEncoder.produceGraphics(tileset2);
                byte[] newTileset2 = StackGraphicsEncoder.getNewGraphicsFileBytes(); 
                byte[] newBackgroundFileBytes = new byte[2+2+2+32+newTileset1.length+newTileset2.length];
                short tileset2Offset = (short) (newTileset1.length + 6 + 32 - 2);
                newBackgroundFileBytes[0] = 0;
                newBackgroundFileBytes[1] = 0x26;
                newBackgroundFileBytes[2] = (byte)((tileset2Offset>>8)&0xFF);
                newBackgroundFileBytes[3] = (byte)(tileset2Offset&0xFF);
                newBackgroundFileBytes[4] = 0;
                newBackgroundFileBytes[5] = 2;
                PaletteEncoder.producePalette(tileset1[0].getPalette());
                byte[] palette = PaletteEncoder.getNewPaletteFileBytes();
                System.arraycopy(palette, 0, newBackgroundFileBytes, 6, palette.length);
                System.arraycopy(newTileset1, 0, newBackgroundFileBytes, 0x26, newTileset1.length);
                System.arraycopy(newTileset2, 0, newBackgroundFileBytes, 0x26+newTileset1.length, newTileset2.length);
                Path graphicsFilePath = Paths.get(filePath);
                Files.write(graphicsFilePath,newBackgroundFileBytes);
                System.out.println(newBackgroundFileBytes.length + " bytes into " + graphicsFilePath);                
            }
        } catch (Exception ex) {
            Logger.getLogger(SFCDBankManager.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
            System.out.println(ex);
        }            
        System.out.println("com.sfc.sf2.background.io.DisassemblyManager.exportDisassembly() - Disassembly exported.");        
    }    

    
    private static Tile[] parseGraphics(String filePath, int loadingOffset, int pointerTableOffset, int backgroundIndex) throws Exception{
        Tile[] tiles = null;
        Color[] palette = null;
            Path path = Paths.get(filePath);
            if(path.toFile().exists()){
                byte[] fileData = Files.readAllBytes(path);
                int ptOffset = ((fileData[0x30+0]&0xFF)<<24) + ((fileData[0x30+1]&0xFF)<<16) + ((fileData[0x30+2]&0xFF)<<8) + ((fileData[0x30+3]&0xFF)) - loadingOffset;
                int portraitPointerByte1 = fileData[0x10+0]&0xFF;
                int portraitPointerByte2 = fileData[0x10+1]&0xFF;
                int portraitPointerByte3 = fileData[0x10+2]&0xFF;
                int portraitPointerByte4 = fileData[0x10+3]&0xFF;
                long portraitPointer = (portraitPointerByte1<<24) + (portraitPointerByte2<<16) + (portraitPointerByte3<<8) + (portraitPointerByte4) - loadingOffset;
                if(ptOffset==portraitPointer){
                    System.out.println("No backgrounds");
                    throw new Exception("No backgrounds");
                }
                int pointerByte1 = fileData[ptOffset+backgroundIndex*4+0]&0xFF;
                int pointerByte2 = fileData[ptOffset+backgroundIndex*4+1]&0xFF;
                int pointerByte3 = fileData[ptOffset+backgroundIndex*4+2]&0xFF;
                int pointerByte4 = fileData[ptOffset+backgroundIndex*4+3]&0xFF;
                int pointer = (pointerByte1<<24) + (pointerByte2<<16) + (pointerByte3<<8) + (pointerByte4) - loadingOffset;
                if(!processedPointers.contains(pointer)){
                    processedPointers.add(pointer);
                }else{
                    return null;
                }
                byte[] data = new byte[0x4000];
                System.arraycopy(fileData, (int)pointer, data, 0, 0x4000);
                if(data.length>2){
                    short tileset1Offset = getNextWord(data,0);
                    short tileset2Offset = (short)(getNextWord(data,2)+2);
                    short paletteOffset = (short)(getNextWord(data,4)+4);
                    byte[] tileset1Data = new byte[data.length-tileset1Offset];
                    System.arraycopy(data, tileset1Offset, tileset1Data, 0, tileset1Data.length);
                    byte[] tileset2Data = new byte[data.length-tileset2Offset];
                    System.arraycopy(data, tileset2Offset, tileset2Data, 0, tileset2Data.length);
                    byte[] paletteData = new byte[32];
                    System.arraycopy(data, paletteOffset, paletteData, 0, paletteData.length);
                    palette = PaletteDecoder.parsePalette(paletteData);
                    Tile[] tileset1 = new StackGraphicsDecoder().decodeStackGraphics(tileset1Data, palette);
                    Tile[] tileset2 = new StackGraphicsDecoder().decodeStackGraphics(tileset2Data, palette);
                    tiles = new Tile[tileset1.length+tileset2.length];
                    System.arraycopy(tileset1, 0, tiles, 0, tileset1.length);
                    System.arraycopy(tileset2, 0, tiles, tileset1.length, tileset2.length);
                }else{
                    System.out.println("com.sfc.sf2.background.io.DisassemblyManager.parseGraphics() - File ignored because of too small length (must be a dummy file) " + data.length + " : " + filePath);
                }
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
    
    public static void clearProcessedPointersList(){
        processedPointers.clear();
    }

    
}
