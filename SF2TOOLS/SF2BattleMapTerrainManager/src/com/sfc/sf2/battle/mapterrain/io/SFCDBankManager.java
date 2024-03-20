/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.battle.mapterrain.io;

import com.sfc.sf2.battle.mapterrain.BattleMapTerrain;
import com.sfc.sf2.battle.mapterrain.compression.StackDecoder;
import com.sfc.sf2.battle.mapterrain.compression.StackEncoder;
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
    
    public static BattleMapTerrain importSFCDBank(String sfcdBankPath, int offset){
        System.out.println("com.sfc.sf2.battlemapterrain.io.DisassemblyManager.importDisassembly() - Importing SFCD Bank ...");
        BattleMapTerrain terrain = new BattleMapTerrain();
        try{
            Path path = Paths.get(sfcdBankPath);
            byte[] fileData = Files.readAllBytes(path);
            byte[] data = new byte[fileData.length-offset];
            System.arraycopy(fileData, offset, data, 0, fileData.length-offset);
            
            StackDecoder decoder = new StackDecoder();
            byte[] decodedData = decoder.decodeStackData(data);
            terrain.setData(decodedData);
            for(int i=0;i<32;i++){
                StringBuilder sb = new StringBuilder();
                for(int j=0;j<32;j++){
                    sb.append(decodedData[i*32+j]).append("\t");
                }
                System.out.println(sb);
            }
        }catch(Exception e){
             System.err.println("com.sfc.sf2.battle.mapterrain.io.DisassemblyManager.importDisassembly() - Error while parsing graphics data : "+e);
             e.printStackTrace();
        }    
        System.out.println("com.sfc.sf2.battle.mapterrain.io.DisassemblyManager.importDisassembly() - SFCD Bank imported.");
        return terrain;
    }      

    private static short getWord(byte[] data, int cursor){
        ByteBuffer bb = ByteBuffer.allocate(2);
        bb.order(ByteOrder.LITTLE_ENDIAN);
        bb.put(data[cursor+1]);
        bb.put(data[cursor]);
        short s = bb.getShort(0);
        return s;
    }
    
    private static byte getByte(byte[] data, int cursor){
        ByteBuffer bb = ByteBuffer.allocate(1);
        bb.order(ByteOrder.LITTLE_ENDIAN);
        bb.put(data[cursor]);
        byte b = bb.get(0);
        return b;
    }      
    
    public static void exportDisassembly(BattleMapTerrain terrain, String terrainFilePath){
        System.out.println("com.sfc.sf2.battlemapterrain.io.DisassemblyManager.exportDisassembly() - Exporting disassembly ...");
        try { 
            byte[] terrainBytes = produceTerrainBytes(terrain);
            Path terrainPath = Paths.get(terrainFilePath);
            Files.write(terrainPath,terrainBytes);
            System.out.println(terrainBytes.length + " bytes into " + terrainFilePath);
        } catch (Exception ex) {
            Logger.getLogger(SFCDBankManager.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
            System.out.println(ex);
        }            
        System.out.println("com.sfc.sf2.battlemapterrain.io.DisassemblyManager.exportDisassembly() - Disassembly exported.");        
    }     
   
    private static byte[] produceTerrainBytes(BattleMapTerrain terrain){
        StackEncoder encoder = new StackEncoder();
        encoder.produceData(terrain.getData());
        byte[] terrainBytes = encoder.getNewDataFileBytes();
        return terrainBytes;
    }    
 
    
    
    
}
