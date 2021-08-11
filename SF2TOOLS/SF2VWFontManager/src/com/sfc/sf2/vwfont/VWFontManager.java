/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.vwfont;

import com.sfc.sf2.vwfont.io.DisassemblyManager;
import com.sfc.sf2.vwfont.io.PngManager;
import com.sfc.sf2.vwfont.io.RomManager;

/**
 *
 * @author wiz
 */
public class VWFontManager {
       
    public static byte[][] vwfontChars;
       
    public static void importDisassembly(String filePath){
        System.out.println("com.sfc.sf2.vwfont.VWFontManager.importDisassembly() - Importing disassembly ...");
        VWFontManager.vwfontChars = DisassemblyManager.importDisassembly(filePath);
        System.out.println("com.sfc.sf2.vwfont.VWFontManager.importDisassembly() - Disassembly imported.");
    }
    
    public static void exportDisassembly(String filePath){
        System.out.println("com.sfc.sf2.vwfont.VWFontManager.importDisassembly() - Exporting disassembly ...");
        DisassemblyManager.exportDisassembly(VWFontManager.vwfontChars, filePath);
        System.out.println("com.sfc.sf2.vwfont.VWFontManager.importDisassembly() - Disassembly exported.");        
    }   
    
    public static void importOriginalRom(String originalRomFilePath){
        System.out.println("com.sfc.sf2.vwfont.VWFontManager.importOriginalRom() - Importing original ROM ...");
        VWFontManager.vwfontChars = RomManager.importRom(RomManager.ORIGINAL_ROM_TYPE,originalRomFilePath);
        System.out.println("com.sfc.sf2.vwfont.VWFontManager.importOriginalRom() - Original ROM imported.");
    }
    
    public static void exportOriginalRom(String originalRomFilePath){
        System.out.println("com.sfc.sf2.vwfont.VWFontManager.exportOriginalRom() - Exporting original ROM ...");
        RomManager.exportRom(RomManager.ORIGINAL_ROM_TYPE, VWFontManager.vwfontChars, originalRomFilePath);
        System.out.println("com.sfc.sf2.vwfont.VWFontManager.exportOriginalRom() - Original ROM exported.");        
    }   
    
    public static void importCaravanRom(String caravanRomFilePath){
        System.out.println("com.sfc.sf2.vwfont.VWFontManager.importCaravanRom() - Importing Caravan ROM ...");
        VWFontManager.vwfontChars = RomManager.importRom(RomManager.CARAVAN_ROM_TYPE,caravanRomFilePath);
        System.out.println("com.sfc.sf2.vwfont.VWFontManager.importCaravanRom() - Caravan ROM imported.");
    }
    
    public static void exportCaravanRom(String caravanRomFilePath){
        System.out.println("com.sfc.sf2.vwfont.VWFontManager.exportCaravanRom() - Exporting original ROM ...");
        RomManager.exportRom(RomManager.CARAVAN_ROM_TYPE, VWFontManager.vwfontChars, caravanRomFilePath);
        System.out.println("com.sfc.sf2.vwfont.VWFontManager.exportCaravanRom() - Caravan ROM exported.");        
    }    
    
    public static void importPng(String basepath){
        System.out.println("com.sfc.sf2.vwfont.VWFontManager.importPng() - Importing PNG ...");
        VWFontManager.vwfontChars = PngManager.importPng(basepath);
        System.out.println("com.sfc.sf2.vwfont.VWFontManager.importPng() - PNG imported.");
    }
    
    public static void exportPng(String basepath){
        System.out.println("com.sfc.sf2.vwfont.VWFontManager.exportPng() - Exporting PNG ...");
        PngManager.exportPng(VWFontManager.vwfontChars, basepath);
        System.out.println("com.sfc.sf2.vwfont.VWFontManager.exportPng() - PNG exported.");       
    }
}
