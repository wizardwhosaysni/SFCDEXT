/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.text.io;

import com.sfc.sf2.text.TextManager;
import com.sfc.sf2.text.compression.Symbols;
import com.sfc.sf2.text.compression.TextDecoder;
import com.sfc.sf2.text.compression.TextEncoder;
import com.sfc.sf2.vwfont.VWFontManager;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author wiz
 */
public class VWFontOptimizingManager {
    
    private static final int LINE_MAX_PIXEL_LENGTH = 260;
    private static final int CHAR_ADDED_WIDTH = 0;
    
    public static String[] importVWFontAndOptimize(String path, String[] inputscript){
        System.out.println("com.sfc.sf2.text.io.AsmManager.importAsm() - Importing ASM ...");
        String[] outputscript = new String[inputscript.length];
        VWFontManager vwfm = new VWFontManager();
        vwfm.importDisassembly(path);
        byte[][] chars = VWFontManager.vwfontChars; 
        Map<String,Integer> widthMap = new HashMap();
        for(int i=0;i<chars.length;i++){
            int width = (int)(chars[i][1]&0xF);
            widthMap.put(Symbols.TABLE[i+1],width);
            System.out.println("Character "+i+"'"+Symbols.TABLE[i+1]+"'"+" length : "+width);
        }
        
        for(int i=0;i<inputscript.length;i++){
            String line = inputscript[i];
            StringBuilder newLine = new StringBuilder();
            
            if(line.contains("{NAME")
                    || line.contains("{LEADER}")
                    || line.contains("{ITEM}")
                    || line.contains("{SPELL}")
                    || line.contains("{CLASS}")
                    ){
                outputscript[i] = line;
                System.out.println(inputscript[i]+" -> "+line);
            }else{
            
                /* Clean line of any existing  "new line" character */
                line.replace("{N}", " ");

                /* Identify current symbol */
                String string = line;
                byte previousSymbol = (byte)0xFE;
                int stringPointer = 0;
                int symbolsPointer = 0;
                byte[] symbols = new byte[string.length()+1];
                while(stringPointer<string.length()){
                    for(int k=0;k<Symbols.TABLE.length;k++){
                        if(((previousSymbol&0xFF)!=0xFC) && ((previousSymbol&0xFF)!=0xFD)){
                            if(string.substring(stringPointer).indexOf(Symbols.TABLE[k])==0){
                                byte symbol = (byte)k;
                                symbols[symbolsPointer] = symbol;
                                symbolsPointer++;
                                stringPointer = stringPointer + Symbols.TABLE[k].length();
                                previousSymbol = symbol;
                                break;
                            }                            
                        }else{
                            String numberString = string.substring(stringPointer+1,string.indexOf("}",stringPointer+1));
                            byte symbol = Byte.valueOf(numberString);
                            symbols[symbolsPointer] = symbol;
                            symbolsPointer++;
                            stringPointer = stringPointer + 2 + numberString.length();
                            previousSymbol = symbol;
                            break;                        
                        }
                        if(k+1==Symbols.TABLE.length){
                            System.err.println("Current character "+string.charAt(stringPointer)+" is not recognized as the beginning of a known symbol, and will be ignored.");
                            stringPointer++;
                        }
                    }
                }
                symbols[symbolsPointer] = (byte)0xFE;
                if(symbolsPointer<stringPointer){
                    symbols = Arrays.copyOf(symbols,symbolsPointer+1);
                }
                System.out.println("Symbol bytes : "+Arrays.toString(symbols));
                
                
                
                
                
                
                
                
                /* Insert new "New Line" characters where needed */
                int cursor = 0;
                int length = 0;
                while(cursor<line.length()){
                    char c = line.charAt(cursor);
                    if(!" ".equals(c)){
                        newLine.append(c);
                        int width = 0;
                        if(widthMap.get(c)!=null){
                            width = widthMap.get(c);
                        }else{

                        }
                        //length+=;

                    }else{

                    }
                    cursor++;
                }
                
                
                
                outputscript[i] = line;
                System.out.println(inputscript[i]+" -> "+line);
            }
            
            
        }
        
        
        /*
        int textCursor=0;
        int baseIndex=-1;
        int scanLineNumber = 0;
        try{
            File file = new File(path);
            Scanner scan = new Scanner(file);
            while(scan.hasNext()){
                String line = scan.nextLine();
                scanLineNumber++;
                try{
                    line = removeName(line).trim(); 
                    if(line.trim().startsWith("txt")&&line.contains(";")&&line.contains("\"")){
                        String indexString = line.substring(3, line.indexOf(';')).trim();
                        String textLine = line.substring(line.indexOf("\"")+1,line.lastIndexOf("\""));
                        int index = 0;
                        if(indexString.startsWith("$")){
                            index = Integer.parseInt(indexString.substring(1), 16);
                        }else{
                            index = Integer.parseInt(indexString);
                        }
                        outputscript = updateScript(textLine, index, outputscript);
                    }else if(line.trim().startsWith("textCursor")){
                        String indexString = line.trim().substring(line.trim().indexOf(" ")).trim();
                        int index = 0;
                        if(indexString.startsWith("$")){
                            index = Integer.parseInt(indexString.substring(1), 16);
                        }else{
                            index = Integer.parseInt(indexString);
                        }
                        textCursor = index;
                        System.out.println("textCursor=$"+Integer.toHexString(textCursor));
                    }else if(
                            (
                            line.trim().startsWith("nextSingleText")
                            ||line.trim().startsWith("nextText")
                            ||line.trim().startsWith("nextSingleTextVar")
                            ||line.trim().startsWith("nextTextVar")
                            )
                            &&line.contains(";")&&line.contains("\"")){
                        String textLine = line.substring(line.indexOf("\"")+1,line.lastIndexOf("\""));
                        outputscript = updateScript(textLine, textCursor, outputscript);
                        textCursor++;
                    }else if(line.trim().startsWith("move.w")&&line.contains(",d3")){
                        String indexString = line.substring(line.indexOf('#')+1, line.indexOf(',')).trim();
                        int index = 0;
                        if(indexString.startsWith("$")){
                            index = Integer.parseInt(indexString.substring(1), 16);
                        }else{
                            index = Integer.parseInt(indexString);
                        }
                        baseIndex = index;
                        System.out.println("baseIndex=$"+Integer.toHexString(baseIndex));
                    }else if(line.trim().startsWith("msDesc")&&line.contains(";")&&line.contains("\"")&&baseIndex!=-1){
                        String[] params = line.substring(7,line.indexOf(";")).split(",");
                        String investigationLineIndexString = params[2].trim();
                        int investigationLineIndex = 0;
                        if(investigationLineIndexString.startsWith("$")){
                            investigationLineIndex = Integer.parseInt(investigationLineIndexString.substring(1), 16);
                        }else{
                            investigationLineIndex = Integer.parseInt(investigationLineIndexString);
                        }
                        String investigationLine = line.substring(line.indexOf("\"")+1,line.lastIndexOf("\""));
                        outputscript = updateScript(investigationLine, INVESTIGATION_LINE_BASE_INDEX+investigationLineIndex, outputscript);
                        String secondLine = scan.nextLine();
                        scanLineNumber++;
                        if(secondLine.trim().startsWith(";")&&secondLine.contains("\"")){
                            String descriptionLineIndexString = params[3].trim();
                            String descriptionLine = secondLine.substring(secondLine.indexOf("\"")+1,secondLine.lastIndexOf("\""));
                            int descriptionLineIndex = 0;
                            if(descriptionLineIndexString.startsWith("$")){
                                descriptionLineIndex = Integer.parseInt(descriptionLineIndexString.substring(1), 16);
                            }else{
                                descriptionLineIndex = Integer.parseInt(descriptionLineIndexString);
                            }
                            outputscript = updateScript(descriptionLine, baseIndex+descriptionLineIndex, outputscript);
                        }
                    }
                }catch(Exception e){
                    System.err.println("com.sfc.sf2.text.io.AsmManager.importAsm() - Error while parsing line at index "+scanLineNumber+" : "+e);
                    e.printStackTrace();
                }
            }          
        }catch(Exception e){
             System.err.println("com.sfc.sf2.text.io.AsmManager.importAsm() - Error while parsing line at index "+scanLineNumber+" : "+e);
             e.printStackTrace();
        }    
        for(int i=0;i<outputscript.length;i++){
            if(outputscript[i]==null){
                outputscript[i]="";
            }
        }
        System.out.println("com.sfc.sf2.text.io.AsmManager.importAsm() - ASM imported.");
        */

        return outputscript;
    }

    
    private static String[] updateScript(String line, int index, String[] script){
        if(index<script.length){
            script[index] = line;
        }else{
            String[] newScript = new String[index+1];
            System.arraycopy(script, 0, newScript, 0, script.length);
            newScript[index] = line;
            script = newScript;
        }   
        System.out.println(getLineIndexString(index)+"="+line);
        return script;
    }
    
    
    private static String getLineIndexString(int index){
        String indexString = Integer.toHexString(index);
        while(indexString.length()<4){
            indexString="0"+indexString;
        }
        return indexString;
    }
    
    private static String removeName(String line){
        if(line.indexOf(":")>=0 && 
                (
                line.indexOf(":")<line.indexOf(" ")
                || line.indexOf(":")<line.indexOf("\t")
                || line.indexOf(":")<line.indexOf(";")
                )
                    ){
            line = line.substring(line.indexOf(":")+1);
        }
        return line;
    }
    
}
