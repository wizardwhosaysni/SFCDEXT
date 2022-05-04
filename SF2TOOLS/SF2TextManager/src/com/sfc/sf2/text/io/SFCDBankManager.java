/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.text.io;

import com.sfc.sf2.text.TextManager;
import com.sfc.sf2.text.compression.TextDecoder;
import com.sfc.sf2.text.compression.TextEncoder;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author wiz
 */
public class SFCDBankManager {
    
    private static final int LINES_PER_BANK = 256;
    private static final int TEXTBANK_LOADING_BUFFER_SIZE = 8192*4;
    
    private static File romFile;  
    private static byte[] romData;
    
    public static String[] importSFCDBank(String romFilePath, int ptOffset, int huffmanTreeOffsetsBegin, int huffmanTreeOffsetsEnd, int huffmanTreesOffsetsBegin, int huffmanTreesOffsetsEnd, int textbankOffset, int lastLineIndex){
        System.out.println("com.sfc.sf2.text.io.SFCDBankManager.importSFCDBank() - Importing SFCD Bank ...");
        SFCDBankManager.openFile(romFilePath);
        SFCDBankManager.parseOffsets(huffmanTreeOffsetsBegin, huffmanTreeOffsetsEnd);
        SFCDBankManager.parseTrees(huffmanTreesOffsetsBegin, huffmanTreesOffsetsEnd);
        String[] gamescript = SFCDBankManager.parseAllTextbanks(ptOffset, textbankOffset, lastLineIndex);        
        System.out.println("com.sfc.sf2.text.io.SFCDBankManager.importSFCDBank() - SFCD Bank imported.");
        return gamescript;
    }
    
    private static void openFile(String romFilePath){
        try {
            System.out.println("com.sfc.sf2.text.io.SFCDBankManager.openFiles() - SFCD Bank file path : " + romFilePath);
            romFile = new File(romFilePath);
            romData = Files.readAllBytes(Paths.get(romFile.getAbsolutePath()));
            System.out.println("com.sfc.sf2.text.io.SFCDBankManager.openFiles() - File opened.");
        } catch (IOException ex) {
            Logger.getLogger(SFCDBankManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private static void parseOffsets(int huffmanTreeOffsetsBegin, int huffmanTreeOffsetsEnd){
        System.out.println("com.sfc.sf2.text.io.SFCDBankManager.parseOffsets() - Parsing offsets ...");
        byte[] data = Arrays.copyOfRange(romData,huffmanTreeOffsetsBegin,huffmanTreeOffsetsEnd);
        TextDecoder.parseOffsets(data);
        System.out.println("com.sfc.sf2.text.io.SFCDBankManager.parseOffsets() - Offsets parsed.");
    }
    
    private static void parseTrees(int huffmanTreesOffsetsBegin, int huffmanTreesOffsetsEnd){
        System.out.println("com.sfc.sf2.text.io.SFCDBankManager.parseTrees() - Parsing trees ...");
        byte[] data = Arrays.copyOfRange(romData,huffmanTreesOffsetsBegin,huffmanTreesOffsetsEnd);
        TextDecoder.parseTrees(data);
        System.out.println("com.sfc.sf2.text.io.SFCDBankManager.parseTrees() - Trees parsed.");
    }
    
    private static String[] parseTextbank(int textbankOffset, int lastLineIndex){
        System.out.println("com.sfc.sf2.text.io.SFCDBankManager.parseTextbank() - Parsing textbank ...");
        String[] gamescript = new String[0]; 
        int numberOfTextbanks = (lastLineIndex+1 + LINES_PER_BANK-1) / LINES_PER_BANK;
        int lastTextbankLines = ((lastLineIndex+1) % LINES_PER_BANK);
        int i=0;
            int linesToParse = (i==numberOfTextbanks-1)? lastTextbankLines : LINES_PER_BANK;
            byte[] data = Arrays.copyOfRange(romData,textbankOffset,textbankOffset+TEXTBANK_LOADING_BUFFER_SIZE); 
            String[] textbankStrings = TextDecoder.parseTextbank(data, i, linesToParse);
            String[] workingStringArray = Arrays.copyOf(gamescript, gamescript.length + textbankStrings.length);
            System.arraycopy(textbankStrings, 0, workingStringArray, gamescript.length, textbankStrings.length);
            gamescript = workingStringArray;
        System.out.println("com.sfc.sf2.text.io.SFCDBankManager.parseTextbank() - Textbanks all parsed.");
        return gamescript;
    }
    
    private static String[] parseAllTextbanks(int ptOffset, int textbanksOffsetsPointerOffset, int lastLineIndex){
        System.out.println("com.sfc.sf2.text.io.CustomManager.parseTextbank() - Parsing textbank ...");
        String[] gamescript = new String[0]; 
        byte[] textbanksOffsetsPointerBytes = Arrays.copyOfRange(romData,textbanksOffsetsPointerOffset,textbanksOffsetsPointerOffset+4);
        int textbanksOffsetsPointer = bytesToInt(textbanksOffsetsPointerBytes);
        int numberOfTextbanks = (lastLineIndex+1 + LINES_PER_BANK-1) / LINES_PER_BANK;
        int lastTextbankLines = ((lastLineIndex+1) % LINES_PER_BANK);
        for(int i=0;i<numberOfTextbanks;i++){ 
            String index = Integer.toString(i);
            while(index.length()<2){
                index = "0"+index;
            }
            int linesToParse = (i==numberOfTextbanks-1)? lastTextbankLines : LINES_PER_BANK;
            int start = textbanksOffsetsPointer+(4*i) - 0x22E000 + ptOffset;
            int end = textbanksOffsetsPointer+(4*i)+4 - 0x22E000 + ptOffset;
            int textbankBegin = bytesToInt(Arrays.copyOfRange(romData,start,end));
            byte[] data = Arrays.copyOfRange(romData,textbankBegin - 0x22E000 + ptOffset,textbankBegin - 0x22E000 + ptOffset+TEXTBANK_LOADING_BUFFER_SIZE); 
            String[] textbankStrings = TextDecoder.parseTextbank(data, i, linesToParse);
            String[] workingStringArray = Arrays.copyOf(gamescript, gamescript.length + textbankStrings.length);
            System.arraycopy(textbankStrings, 0, workingStringArray, gamescript.length, textbankStrings.length);
            gamescript = workingStringArray;
        }
        System.out.println("com.sfc.sf2.text.io.CustomManager.parseTextbank() - Textbanks all parsed.");
        return gamescript;
    }
    
    private static int bytesToInt(byte[] bytes){
        ByteBuffer bb = ByteBuffer.wrap(bytes);
        bb.order(ByteOrder.BIG_ENDIAN);
        return bb.getInt();
    }
    
    public static  byte[] intToFourByteArray(int value){
        return ByteBuffer.allocate(4).order(ByteOrder.BIG_ENDIAN).putInt(value).array();
    }
    
}
