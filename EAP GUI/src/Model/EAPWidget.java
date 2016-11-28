/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.io.Serializable;
import javafx.scene.layout.Pane;

/**
 * an interface for the items
 * @author Hao
 */
public interface EAPWidget extends Cloneable, Serializable{
    
    /**
     * @return a string which records the information of the widget
     */
    @Override
    public String toString();
    
    /**
     * paint this item to the pane
     * @param p 
     */
    public void paint(Pane p);
    
    /**
     * remove this item from the pane
     * @param p 
     */
    public void removeFromPane(Pane p);
    
    public String getWidgetType();
    
    /**
     * Checks if a point is inside the widget
     * @param x - the x coordinate of the event
     * @param y - the y coordinate of the event
     * @return 
     */
    public boolean contains(double x, double y);
    
}
