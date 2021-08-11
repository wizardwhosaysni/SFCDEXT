/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.portrait.io;

import com.sfc.sf2.graphics.Tile;
import com.sfc.sf2.graphics.layout.DefaultLayout;
import com.sfc.sf2.palette.graphics.PaletteDecoder;
import com.sfc.sf2.palette.graphics.PaletteEncoder;
import com.sfc.sf2.portrait.Portrait;
import com.sfc.sf2.portrait.layout.PortraitLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.IndexColorModel;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 *
 * @author wiz
 */
public class PngManager {
    
    public static Portrait importPng(String filepath){
        System.out.println("com.sfc.sf2.portrait.io.PngManager.importPng() - Importing PNG files ...");
        Portrait portrait = new Portrait();
        try{
            Tile[] tiles = loadPngFile(filepath);
            if(tiles!=null){
                if(tiles.length==64){                   
                   portrait.setTiles(tiles);
                   System.out.println("Created Portrait with " + tiles.length + " tiles.");                       
                }else{
                    System.out.println("Could not create Portrait because of wrong length : tiles=" + tiles.length);
                }
            }
        }catch(Exception e){
             System.err.println("com.sfc.sf2.portrait.io.PngManager.importPng() - Error while parsing graphics data : "+e);
        }        
        System.out.println("com.sfc.sf2.portrait.io.PngManager.importPng() - PNG files imported.");
        return portrait;                
    }
    
    
    
    public static Tile[] loadPngFile(String filepath) throws IOException{
        Tile[] tiles = null;
        boolean cFound = false;
        try{
            Path path = Paths.get(filepath);
            if(path.toFile().exists()){
                BufferedImage img = ImageIO.read(path.toFile());
                ColorModel cm = img.getColorModel();
                if(!(cm instanceof IndexColorModel)){
                    System.out.println("PNG FORMAT ERROR : COLORS ARE NOT INDEXED AS EXPECTED.");
                }else{
                    IndexColorModel icm = (IndexColorModel) cm;
                    Color[] palette = PaletteDecoder.parsePalette(PaletteEncoder.producePalette(buildColors(icm)));

                    int imageWidth = img.getWidth();
                    int imageHeight = img.getHeight();
                    if(imageWidth%8!=0 || imageHeight%8!=0){
                        System.out.println("PNG FORMAT WARNING : DIMENSIONS ARE NOT MULTIPLES OF 8. (8 pixels per tile)");
                    }else{
                        tiles = new Tile[(imageWidth/8)*(imageHeight/8)];
                        int tileId = 0;
                                    for(int tileLine=0;tileLine<8;tileLine++){
                                        for(int tileColumn=0;tileColumn<8;tileColumn++){
                                            int x = (tileColumn)*8;
                                            int y = (tileLine)*8;
                                            //System.out.println("Building tile from coordinates "+x+":"+y);
                                            Tile tile = new Tile();
                                            tile.setId(tileId);
                                            tile.setPalette(palette);
                                            for(int j=0;j<8;j++){
                                                for(int i=0;i<8;i++){
                                                    Color color = new Color(img.getRGB(x+i,y+j));
                                                    int a = PaletteDecoder.VALUE_MAP.get(PaletteEncoder.VALUE_ARRAY[color.getAlpha()]&0xE);;
                                                    int b = PaletteDecoder.VALUE_MAP.get(PaletteEncoder.VALUE_ARRAY[color.getBlue()]&0xE);;
                                                    int g = PaletteDecoder.VALUE_MAP.get(PaletteEncoder.VALUE_ARRAY[color.getGreen()]&0xE);;
                                                    int r = PaletteDecoder.VALUE_MAP.get(PaletteEncoder.VALUE_ARRAY[color.getRed()]&0xE);;
                                                    Color standardizedColor = new Color(r,g,b);
                                                    cFound = false;
                                                    for(int c=0;c<16;c++){
                                                        if(standardizedColor.equals(palette[c])){
                                                            tile.setPixel(i, j, c);
                                                            cFound = true;
                                                            break;
                                                        }
                                                    }
                                                    if(!cFound){
                                                        //TODO find nearest color with lowest r*g*b diff
                                                        int diff = Integer.MAX_VALUE;
                                                        int index = 0;
                                                        if(a>=128){
                                                            index=0;
                                                        }else{
                                                            for(int c=0;c<16;c++){
                                                                int bDiff = Math.abs(palette[c].getBlue()-color.getBlue())+1;
                                                                int gDiff = Math.abs(palette[c].getGreen()-color.getGreen())+1;
                                                                int rDiff = Math.abs(palette[c].getRed()-color.getRed())+1;
                                                                int candidateDiff = bDiff * gDiff * rDiff;
                                                                if(candidateDiff<=diff){
                                                                    diff = candidateDiff;
                                                                    index = c;
                                                                }
                                                            }
                                                        }

                                                        tile.setPixel(i, j, index);
                                                    }
                                                }
                                            }
                                            //System.out.println(tile);
                                            tiles[tileId] = tile;   
                                            tileId++;
                                        }
                                    }        

                    }
                }                
            }
        }catch(Exception e){
             System.err.println("com.sfc.sf2.portrait.io.PngManager.importPng() - Error while parsing PNG data : "+e);
             throw e;
        }                
        return tiles;                
    }
    
    private static Color[] buildColors(IndexColorModel icm){
        Color[] colors = new Color[16];
        if(icm.getMapSize()>16){
            System.out.println("com.sfc.sf2.portrait.io.PngManager.buildColors() - PNG FORMAT HAS MORE THAN 16 INDEXED COLORS : "+icm.getMapSize());
        }
        byte[] reds = new byte[icm.getMapSize()];
        byte[] greens = new byte[icm.getMapSize()];
        byte[] blues = new byte[icm.getMapSize()];
        icm.getReds(reds);
        icm.getGreens(greens);
        icm.getBlues(blues);
        for(int i=0;i<16;i++){
            colors[i] = new Color((int)(reds[i]&0xff),(int)(greens[i]&0xff),(int)(blues[i]&0xff));
        }
        return colors;
    }
    
    
    public static void exportPng(Portrait portrait, String filepath){
        try {
            System.out.println("com.sfc.sf2.portrait.io.PngManager.exportPng() - Exporting PNG file ...");
            writePngFile(portrait.getTiles(),filepath);                
            System.out.println("com.sfc.sf2.portrait.io.PngManager.exportPng() - PNG file exported.");
        } catch (Exception ex) {
            Logger.getLogger(PngManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        
                
    }    
    
    public static void writePngFile(Tile[] tiles, String filepath){
        try {
            System.out.println("com.sfc.sf2.portrait.io.PngManager.exportPng() - Exporting PNG file ...");
            PortraitLayout pl = new PortraitLayout();
            pl.setCurrentDisplaySize(1);
            BufferedImage image = pl.buildImage(tiles, 8, true);
            File outputfile = new File(filepath);
            System.out.println("File path : "+outputfile.getAbsolutePath());
            ImageIO.write(image, "png", outputfile);
            System.out.println("PNG file exported : " + outputfile.getAbsolutePath());
        } catch (Exception ex) {
            Logger.getLogger(PngManager.class.getName()).log(Level.SEVERE, null, ex);
        }       
    }
       
    
}
