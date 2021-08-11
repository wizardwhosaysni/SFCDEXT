/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.portrait.layout;

import com.sfc.sf2.graphics.Tile;
import com.sfc.sf2.portrait.gui.PortraitTableModel;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.awt.image.IndexColorModel;
import javax.swing.JPanel;

/**
 *
 * @author wiz
 */
public class PortraitLayout extends JPanel  implements MouseListener, MouseMotionListener{
    
    private static final int DEFAULT_TILES_PER_ROW = 8;
    
    private int tilesPerRow = DEFAULT_TILES_PER_ROW;
    private Tile[] tiles;
    
    private PortraitTableModel eyeAnimTable;
    private PortraitTableModel mouthAnimTable;
    
    private int selectedEyeTile = -1;
    private int selectedMouthTile = -1;
    
    private boolean redraw = true;
    private BufferedImage currentImage = null;
    private BufferedImage selectedTileImage = null;
    
    private int currentDisplaySize = 2;
    
    
    public PortraitLayout() {
        addMouseListener(this);
        addMouseMotionListener(this);
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);   
        g.drawImage(buildImage(), 0, 0, this);       
    }
    
    public void updateDisplay(){
        selectedTileImage = null;
        this.redraw = true;
    }
    
    public BufferedImage buildImage(){
        BufferedImage image = buildImage(this.tiles,this.tilesPerRow, false);
        setSize(image.getWidth(), image.getHeight());
        return image;
    }
    
    public BufferedImage buildImage(Tile[] tiles, int tilesPerRow, boolean pngExport){
        int imageHeight = (tiles.length/tilesPerRow)*8;
        if(tiles.length%tilesPerRow!=0){
            imageHeight+=8;
        }
        if(redraw || pngExport){
            IndexColorModel icm = buildIndexColorModel(tiles[0].getPalette());
            currentImage = new BufferedImage(tilesPerRow*8, imageHeight , BufferedImage.TYPE_BYTE_BINARY, icm);
            Graphics graphics = currentImage.getGraphics();     
            for(int i=0;i<8;i++){
                for(int j=0;j<8;j++){
                    graphics.drawImage(tiles[(i*8)+j].getImage(), j*8, i*8, null);
                }
            }
            if(!pngExport){
                if(selectedEyeTile>=0){
                    graphics.drawImage(getSelectedTileImage(eyeAnimTable, selectedEyeTile),0,0,null);
                }
                if(selectedMouthTile>=0){
                    graphics.drawImage(getSelectedTileImage(mouthAnimTable, selectedMouthTile),0,0,null);
                }
                currentImage = resize(currentImage);
            }
            redraw = false;
        }
        return currentImage;
    }
    
    private BufferedImage getSelectedTileImage(PortraitTableModel table, int index){
        if(selectedTileImage==null){
            selectedTileImage = new BufferedImage(8*8, 8*8, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = (Graphics2D) selectedTileImage.getGraphics(); 
            int x1 = table.getTableData()[index][0];
            int y1 = table.getTableData()[index][1];
            int x2 = table.getTableData()[index][2];
            int y2 = table.getTableData()[index][3];
            g2.setColor(Color.WHITE);
            g2.setStroke(new BasicStroke(1));
            g2.drawRect(x1*8, y1*8, 8, 8);
            g2.drawRect(x2*8, y2*8, 8, 8);
        }
        return selectedTileImage;
    }
    
    private static IndexColorModel buildIndexColorModel(Color[] colors){
        byte[] reds = new byte[16];
        byte[] greens = new byte[16];
        byte[] blues = new byte[16];
        byte[] alphas = new byte[16];
        /*reds[0] = (byte)0xFF;
        greens[0] = (byte)0xFF;
        blues[0] = (byte)0xFF;
        alphas[0] = 0;*/
        for(int i=0;i<16;i++){
            reds[i] = (byte)colors[i].getRed();
            greens[i] = (byte)colors[i].getGreen();
            blues[i] = (byte)colors[i].getBlue();
            alphas[i] = (byte)0xFF;
        }
        alphas[0] = 0;
        IndexColorModel icm = new IndexColorModel(4,16,reds,greens,blues,alphas);
        return icm;
    } 
    
    public void resize(int size){
        this.currentDisplaySize = size;
        currentImage = resize(currentImage);
    }
    
    private BufferedImage resize(BufferedImage image){
        BufferedImage newImage = new BufferedImage(image.getWidth()*currentDisplaySize, image.getHeight()*currentDisplaySize, BufferedImage.TYPE_INT_ARGB);
        Graphics g = newImage.getGraphics();
        g.drawImage(image, 0, 0, image.getWidth()*currentDisplaySize, image.getHeight()*currentDisplaySize, null);
        g.dispose();
        return newImage;
    }        
    
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(getWidth(), getHeight());
    }
    
        public Tile[] getTiles() {
        return tiles;
    }

    public void setTiles(Tile[] tiles) {
        this.tiles = tiles;
    }
    
    public int getTilesPerRow() {
        return tilesPerRow;
    }

    public void setTilesPerRow(int tilesPerRow) {
        this.tilesPerRow = tilesPerRow;
    }

    public PortraitTableModel getEyeAnimTable() {
        return eyeAnimTable;
    }

    public void setEyeAnimTable(PortraitTableModel eyeAnimTable) {
        this.eyeAnimTable = eyeAnimTable;
    }

    public PortraitTableModel getMouthAnimTable() {
        return mouthAnimTable;
    }

    public void setMouthAnimTable(PortraitTableModel mouthAnimTable) {
        this.mouthAnimTable = mouthAnimTable;
    }

    public int getSelectedEyeTile() {
        return selectedEyeTile;
    }

    public void setSelectedEyeTile(int selectedEyeTile) {
        this.selectedEyeTile = selectedEyeTile;
        this.selectedMouthTile = -1;
    }

    public int getSelectedMouthTile() {
        return selectedMouthTile;
    }

    public void setSelectedMouthTile(int selectedMouthTile) {
        this.selectedMouthTile = selectedMouthTile;
        this.selectedEyeTile = -1;
    }
    

    @Override
    public void mouseClicked(MouseEvent e) {
    }
    @Override
    public void mouseEntered(MouseEvent e) {

    }
    @Override
    public void mouseExited(MouseEvent e) {

    }
    @Override
    public void mousePressed(MouseEvent e) {
        int x = e.getX() / (currentDisplaySize * 8);
        int y = e.getY() / (currentDisplaySize * 8);  
        switch (e.getButton()) {
            case MouseEvent.BUTTON1:
                if(selectedEyeTile>=0){
                    if(x<6){
                        eyeAnimTable.getTableData()[selectedEyeTile][0] = x;
                        eyeAnimTable.getTableData()[selectedEyeTile][1] = y;
                        eyeAnimTable.fireTableCellUpdated(selectedEyeTile, 0);
                        eyeAnimTable.fireTableCellUpdated(selectedEyeTile, 1);
                    }else{
                        eyeAnimTable.getTableData()[selectedEyeTile][2] = x;
                        eyeAnimTable.getTableData()[selectedEyeTile][3] = y;
                        eyeAnimTable.fireTableCellUpdated(selectedEyeTile, 2);
                        eyeAnimTable.fireTableCellUpdated(selectedEyeTile, 3);
                    }
                    //eyeAnimTable.fireTableDataChanged();
                    eyeAnimTable.fireTableCellUpdated(y, y);
                }
                if(selectedMouthTile>=0){
                    if(x<6){
                        mouthAnimTable.getTableData()[selectedMouthTile][0] = x;
                        mouthAnimTable.getTableData()[selectedMouthTile][1] = y;
                        mouthAnimTable.fireTableCellUpdated(selectedMouthTile, 0);
                        mouthAnimTable.fireTableCellUpdated(selectedMouthTile, 1);
                    }else{
                        mouthAnimTable.getTableData()[selectedMouthTile][2] = x;
                        mouthAnimTable.getTableData()[selectedMouthTile][3] = y;
                        mouthAnimTable.fireTableCellUpdated(selectedMouthTile, 2);
                        mouthAnimTable.fireTableCellUpdated(selectedMouthTile, 3);
                    }
                    //mouthAnimTable.fireTableDataChanged();
                }
                break;
            case MouseEvent.BUTTON2:

                break;
            case MouseEvent.BUTTON3:
                break;
            default:
                break;
        } 
        selectedTileImage = null;
        redraw = true;
        this.revalidate();
        this.repaint();
        System.out.println("Portrait press "+e.getButton()+" "+x+" - "+y);
    }
    @Override
    public void mouseReleased(MouseEvent e) {
       
    }
    
    @Override
    public void mouseDragged(MouseEvent e) {
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        
    }

    public int getCurrentDisplaySize() {
        return currentDisplaySize;
    }

    public void setCurrentDisplaySize(int currentDisplaySize) {
        this.currentDisplaySize = currentDisplaySize;
    }

    
}
