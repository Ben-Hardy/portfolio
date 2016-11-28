/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.stage.Modality;
import javafx.stage.Stage;


public class EAPWindow implements EAPWidget{
    double x1,width;
    double y1,height;
    String color;
    String type;
    Rectangle temp;
    Rectangle body;
    Rectangle xbutton;
    Line line;
    Line line2;
    
    /**
     * @param x1 - the first x coordinate the button will be drawn at
     * @param y1 - the first y coordinate the button will be drawn at
     * @param x2 - the second x coordinate the button will be drawn at
     * @param y2 - the second y coordinate the button will be drawn at
     * @param color - the color in string to describe the color of the body
     * @param copyStage - simply used so that we can make our text prompts be separated 
     * @precon the width, or x1-x1 and the height, or y1-y2 should be not less than 30
     */
    public EAPWindow(double x1, double y1, double x2, double y2, String color, Stage copyStage) {
        if (Math.abs(x1 -x2) < 30 || Math.abs(y1 - y2) < 30) {
            type = "Window";
            Stage dialog = new Stage();
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.initOwner(copyStage);
            VBox vbox = new VBox(10);
            vbox.getChildren().add(new Label("Dimensions too small! Can't Make Scrollable pane that tiny!"));
            Button b = new Button("OK");
            b.setOnAction((ActionEvent event1) -> {
                dialog.close();
            });
            vbox.getChildren().add(b);
            vbox.alignmentProperty().setValue(Pos.CENTER);
            Scene dialogScene = new Scene(vbox, 350, 100);
            dialog.setScene(dialogScene);
            dialog.show();
        }
        else {
            this.x1 = x1;
            this.y1 = y1;
            this.width = x2 - x1;
            this.height = y2 - y1;
            this.color = color;
            this.type = "Window";
        }
    }
    
    /**
     * @param p - the pane to add EAPWindow
     * @precon the width, or x1-x1 and the height, or y1-y2 should be not less than 30
     */
    @Override
    public void paint(Pane p) {
       
        //Pane tempPane = new Pane();
        //tempPane.setLayoutX(Math.min(x1, x2));
        //tempPane.setLayoutY(Math.min(y1, y2));

        // this rectangle is for the top bar
        temp = new Rectangle(x1, y1 , width , 30);

        // this is the actual body of the window
        body = new Rectangle(x1, y1,  width, height);

        // this one is  for the exit button
        xbutton = new Rectangle(x1 + width - 30, y1, 30, 30);

        line = new Line(x1 + width - 25, y1 + 5, x1 + width - 5, y1 + 25 );
        line2 = new Line(x1 + width - 25, y1 + 25, x1 + width - 5, y1 + 5 );
        line.setStyle("-fx-stroke-width:2;");
        line2.setStyle("-fx-stroke-width:2;");
        body.setStyle("-fx-fill: " + color + "; -fx-stroke: #000000; -fx-stroke-width:2;");
        temp.setStyle("-fx-fill: #FFFFFF; -fx-stroke: #000000; -fx-stroke-width:2;");
        xbutton.setStyle("-fx-fill: #FFFFFF; -fx-stroke: #000000; -fx-stroke-width:2;");

        //tempPane.getChildren().addAll(temp, body, xbutton, line, line2);
        p.getChildren().addAll(body, temp, xbutton, line, line2);
    }
    
    @Override
    public void removeFromPane(Pane p) {
        p.getChildren().removeAll(body,temp,xbutton,line,line2);
    }
    
    public void moveXButton(double dx, double dy) {
        xbutton.setX(dx);
        xbutton.setY(dy);
        line.setStartX(dx + 5);
        line2.setStartX(dx + 25);
        line.setStartY(dy + 5);
        line2.setStartY(dy + 5);
        line.setEndX(dx + 25);
        line2.setEndX(dx + 5);
        line.setEndY(dy + 25);
        line2.setEndY(dy + 25);
    }

    public void moveBar(double dx, double dy) {
        temp.setX(temp.getX() + dx);
        temp.setY(temp.getY() + dy);
    }
    
    public void setBarWidth(double x) {
        temp.setWidth(x);
    }
    public double getBarWidth() { return temp.getWidth(); }
    
     @Override
    public boolean contains(double x, double y) {
        return temp.contains(x, y) || body.contains(x,y) || xbutton.contains(x, y);
    }
    
    public void setHeight(double height) {
        this.height = height;
        body.setHeight(height);
    }
    
    public void setWidth(double width) {
        this.width = width;
        temp.setWidth(width - 30);
        body.setWidth(width);
    }
    
    public void setX(double x) {
        x1 = x;
        temp.setX(x);
        body.setX(x);
        
    }
    
    public void setY(double y) {
        y1 = y;
        temp.setY(y);
        body.setY(y);
        
    }
    
    public double getX() { return x1; }
    public double getY() { return y1; }
    public double getWidth() { return width; }
    public double getHeight() { return height; }
        
    @Override
    public String toString() {
        return ("EAPWindow " 
                + this.x1 + " "
                + this.y1 + " "
                + this.width + " "
                + this.height + " "
                + this.color);
    }

    @Override
    public String getWidgetType() {
        return type;
    }
}
