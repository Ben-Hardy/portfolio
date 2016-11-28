/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View;

import static Controller.actions.cloneTo;
import static Controller.actions.promptInput;
import Model.EAPButton;
import Model.EAPCheckbox;
import Model.EAPLabel;
import Model.EAPLine;
import Model.EAPPane;
import Model.EAPScrollablePane;
import Model.EAPWidget;
import Model.EAPWindow;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ObservableValue;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.scene.layout.VBox;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import javax.imageio.ImageIO;

/**
 *
 * @author Hao
 */
public final class WorkPane extends GridPane{
    VBox workBox;
    AnchorPane drawPane;
    
    // made it global for now so I don't have to fight through multiple layers of abstraction
    TreeView<String>  workTree;
    
    // this is used to store the current selected Menu item. It changes when an
    // item on the menu sidebar changes.
    String curSelected="";
    
    // this is used to store whether or not we are double clicking or single clicking.
    boolean firstClick;
    double firstx, firsty;

    //private final Node icon = new ImageView {
    //  new Image(getClass().getResourceAsStream(fileName);
    //);   remove "//" when want to use icons
    
    
    // our placeholder shapes for when widgets are being drawn but the second
    // click hasn't happened
    Rectangle r;
    Line l;
    
    
    // these are used for resizing
    Circle start;
    Circle end;
    Rectangle left;
    Rectangle right;
    Rectangle top;
    Rectangle bottom;
    
    // this rectangle is used for moving the widget
    Rectangle middle;
    
    Shape resize;
    boolean doResize;
    boolean startedResize;
    
    float prevX, prevY;
    double oldX, oldY;
    
    // stores the current widget being modified
    EAPWidget curWidget;
    EAPWidget pasteWidget;
    boolean selected;

    
    private void initializeDrawPane(Stage primaryStage, Toolbar toolbar) {
        
        drawPane = new AnchorPane();
        drawPane.setStyle("-fx-background-color: #777777;");
         // this is going to be a big mess for now. Once I have everything organized I will clean it up a ton
        drawPane.setOnMousePressed((MouseEvent event) -> {
            if (doResize) {
                doResize = false;
                curWidget = null;
                if (drawPane.getChildren().contains(start)) drawPane.getChildren().remove(start);
                if (drawPane.getChildren().contains(end)) drawPane.getChildren().remove(end);
                if (drawPane.getChildren().contains(middle)) drawPane.getChildren().remove(middle);
                if (drawPane.getChildren().contains(left)) drawPane.getChildren().remove(left);
                if (drawPane.getChildren().contains(right)) drawPane.getChildren().remove(right);
                if (drawPane.getChildren().contains(top)) drawPane.getChildren().remove(top);
                if (drawPane.getChildren().contains(bottom)) drawPane.getChildren().remove(bottom);
            }
            else if (event.getButton() == MouseButton.SECONDARY) { // use right click to cancel a draw command for windows and panes
                firstClick = false;
                doResize = false;
                if (drawPane.getChildren().contains(start))
                    drawPane.getChildren().remove(start);
                if (drawPane.getChildren().contains(end))
                    drawPane.getChildren().remove(end);
                if (drawPane.getChildren().contains(middle))
                    drawPane.getChildren().remove(middle);
                if (drawPane.getChildren().contains(left))
                    drawPane.getChildren().remove(left);
                if (drawPane.getChildren().contains(right))
                    drawPane.getChildren().remove(right);
                if (drawPane.getChildren().contains(top))
                    drawPane.getChildren().remove(top);
                if (drawPane.getChildren().contains(bottom))
                    drawPane.getChildren().remove(bottom);
                if (drawPane.getChildren().contains(r))
                    drawPane.getChildren().remove(r);
                if (drawPane.getChildren().contains(l))
                    drawPane.getChildren().remove(l);
                if (workTree.getSelectionModel().getSelectedIndex() > -1)
                    workTree.getSelectionModel().clearSelection();
            }
            else if (!firstClick && workTree.getSelectionModel().isEmpty()) {
                selected = false;
                if (start != null)
                    if (start.contains(event.getX(), event.getY())) {
                        selected = true;
                        resize = start;
                        doResize = true;
                    }
                if (end != null)
                    if (!selected)
                        if (end.contains(event.getX(), event.getY())) {
                            selected = true;
                            resize = end;
                            doResize = true;
                        }
                if (left != null)
                    if (!selected)
                        if (left.contains(event.getX(), event.getY())) {
                            selected = true;
                            resize = left;
                            doResize = true;
                            startedResize = false;
                        }
                if (right != null)
                    if (!selected)
                        if (right.contains(event.getX(), event.getY())) {
                            selected = true;
                            resize = right;
                            doResize = true;
                            startedResize = false;
                        }
                if (top != null)
                    if (!selected)
                        if (top.contains(event.getX(), event.getY())) {
                            selected = true;
                            resize = top;
                            doResize = true;
                            startedResize = false;
                        }
                if (bottom != null)
                    if (!selected)
                        if (bottom.contains(event.getX(), event.getY())) {
                            selected = true;
                            resize = bottom;
                            doResize = true;
                            startedResize = false;
                        }
                if (middle != null)
                    if (!selected)
                        if (middle.contains(event.getX(), event.getY())) {
                            oldX = event.getX();
                            oldY = event.getY();
                            selected = true;
                            resize = middle;
                            doResize = true;
                            startedResize = false;
                        }
                if (!selected)
                    for (EAPWidget w : toolbar.menubar.widgets) {
                        if (w.contains(event.getX(), event.getY())) {
                            System.out.println("Selected: " + w.toString());
                            curWidget = w;
                            selected = true;
                            break;
                        }
                }
                if (!selected)
                    for (EAPWidget w : toolbar.menubar.panes) {
                        if (w.contains(event.getX(), event.getY())) {
                            System.out.println("Selected: " + w.toString());
                            curWidget = w;
                            System.out.println(curWidget.toString());
                            selected = true;
                            break;
                        }
                    }
                if (selected && !doResize) {
                    EAPLine hl;
                    if (drawPane.getChildren().contains(start)) drawPane.getChildren().remove(start);
                    if (drawPane.getChildren().contains(end)) drawPane.getChildren().remove(end);
                    if (drawPane.getChildren().contains(middle)) drawPane.getChildren().remove(middle);
                    if (drawPane.getChildren().contains(left)) drawPane.getChildren().remove(left);
                    if (drawPane.getChildren().contains(right)) drawPane.getChildren().remove(right);
                    if (drawPane.getChildren().contains(top)) drawPane.getChildren().remove(top);
                    if (drawPane.getChildren().contains(bottom)) drawPane.getChildren().remove(bottom);
                    switch (curWidget.getWidgetType()) {
                        case "Line":
                            hl = (EAPLine) toolbar.menubar.widgets.get(toolbar.menubar.widgets.indexOf((EAPLine) curWidget));
                            start = new Circle(hl.getStartX(), hl.getStartY(), 10, Color.RED);
                            end = new Circle(hl.getEndX(), hl.getEndY(), 10, Color.RED);
                            middle = new Rectangle(Math.min(hl.getStartX(), hl.getEndX()) + (0.5 * Math.abs(hl.getStartX() - hl.getEndX()))- 10, Math.min(hl.getStartY(), hl.getEndY()) + (0.5 * Math.abs(hl.getStartY() - hl.getEndY())) -10, 20, 20);
                            middle.setFill(Color.LIME);
                            drawPane.getChildren().addAll(start, end, middle);
                            break;
                        case "Horizontal Line":
                            hl = (EAPLine) toolbar.menubar.widgets.get(toolbar.menubar.widgets.indexOf((EAPLine) curWidget));
                            start = new Circle(hl.getStartX(), hl.getStartY(), 10, Color.RED);
                            end = new Circle(hl.getEndX(), hl.getEndY(), 10, Color.RED);
                            middle = new Rectangle(Math.min(hl.getStartX(), hl.getEndX()) + (0.5 * Math.abs(hl.getStartX() - hl.getEndX()))- 10, hl.getStartY() - 10, 20, 20);
                            middle.setFill(Color.LIME);
                            drawPane.getChildren().addAll(start, end, middle);
                       
                            break;
                        case "Vertical Line":
                            hl = (EAPLine) toolbar.menubar.widgets.get(toolbar.menubar.widgets.indexOf((EAPLine) curWidget));
                            start = new Circle(hl.getStartX(), hl.getStartY(), 10, Color.RED);
                            end = new Circle(hl.getEndX(), hl.getEndY(), 10, Color.RED);
                            middle = new Rectangle(hl.getStartX() - 10, Math.min(hl.getStartY(), hl.getEndY()) + (0.5 * Math.abs(hl.getStartY() - hl.getEndY())) -10, 20, 20);
                            middle.setFill(Color.LIME);
                            drawPane.getChildren().addAll(start, end, middle);
                            break;
                        case "Pane":
                            EAPPane p = (EAPPane) toolbar.menubar.panes.get(toolbar.menubar.panes.indexOf((EAPPane) curWidget));
                            left = new Rectangle(p.getX() - 10, p.getY() + (0.5 * p.getHeight()) - 10, 20, 20);
                            left.setFill(Color.AQUA);
                            right = new Rectangle(p.getX() + p.getWidth() - 10, p.getY() + (0.5 * p.getHeight()) - 10, 20, 20);
                            right.setFill(Color.AQUA);
                            top = new Rectangle(p.getX() + (0.5 * p.getWidth()) - 10, p.getY() -10, 20, 20);
                            top.setFill(Color.CYAN);
                            bottom = new Rectangle(p.getX() + (0.5 * p.getWidth()) - 10, p.getY() + p.getHeight() -10, 20, 20);
                            bottom.setFill(Color.CYAN);
                            middle = new Rectangle(p.getX() + (0.5 * p.getWidth()) - 10, p.getY() + (0.5 * p.getHeight()) - 10, 20, 20);
                            middle.setFill(Color.LIME);
                            drawPane.getChildren().addAll(left, right, top, bottom, middle);
                            break;
                        case "Window":
                            EAPWindow w = (EAPWindow) toolbar.menubar.panes.get(toolbar.menubar.panes.indexOf((EAPWindow) curWidget));
                            left = new Rectangle(w.getX() - 10, w.getY() + (0.5 * w.getHeight()) - 10, 20, 20);
                            left.setFill(Color.AQUA);
                            right = new Rectangle(w.getX() + w.getWidth() - 10, w.getY() + (0.5 * w.getHeight()) - 10, 20, 20);
                            right.setFill(Color.AQUA);
                            top = new Rectangle(w.getX() + (0.5 * w.getWidth()) - 10, w.getY() -10, 20, 20);
                            top.setFill(Color.CYAN);
                            bottom = new Rectangle(w.getX() + (0.5 * w.getWidth()) - 10, w.getY() + w.getHeight() -10, 20, 20);
                            bottom.setFill(Color.CYAN);
                            middle = new Rectangle(w.getX() + (0.5 * w.getWidth()) - 10, w.getY() + (0.5 * w.getHeight()) - 10, 20, 20);
                            middle.setFill(Color.LIME);
                            drawPane.getChildren().addAll(left, right, top, bottom, middle);
                            break;
                        case "Scrollable Pane":
                            EAPScrollablePane s = (EAPScrollablePane) toolbar.menubar.panes.get(toolbar.menubar.panes.indexOf((EAPScrollablePane) curWidget));
                            left = new Rectangle(s.getX() - 10, s.getY() + (0.5 * s.getHeight()) - 10, 20, 20);
                            left.setFill(Color.AQUA);
                            right = new Rectangle(s.getX() + s.getWidth() - 10, s.getY() + (0.5 * s.getHeight()) - 10, 20, 20);
                            right.setFill(Color.AQUA);
                            top = new Rectangle(s.getX() + (0.5 * s.getWidth()) - 10, s.getY() -10, 20, 20);
                            top.setFill(Color.CYAN);
                            bottom = new Rectangle(s.getX() + (0.5 * s.getWidth()) - 10, s.getY() + s.getHeight() -10, 20, 20);
                            bottom.setFill(Color.CYAN);
                            middle = new Rectangle(s.getX() + (0.5 * s.getWidth()) - 10, s.getY() + (0.5 * s.getHeight()) - 10, 20, 20);
                            middle.setFill(Color.LIME);
                            drawPane.getChildren().addAll(left, right, top, bottom, middle);
                            break;
                        case "Button":
                            EAPButton b = (EAPButton) toolbar.menubar.widgets.get(toolbar.menubar.widgets.indexOf((EAPButton) curWidget));
                            middle = new Rectangle(b.getX() + (0.5 * b.getWidth()) - 10, b.getY() + (0.5 * b.getHeight()) - 10, 20, 20);
                            middle.setFill(Color.LIME);
                            drawPane.getChildren().add(middle);
                            break;
                        case "Check Box":
                            EAPCheckbox c = (EAPCheckbox) toolbar.menubar.widgets.get(toolbar.menubar.widgets.indexOf((EAPCheckbox) curWidget));
                            middle = new Rectangle(c.getX() + 10, c.getY(), 15, 15);
                            middle.setFill(Color.LIME);
                            drawPane.getChildren().add(middle);
                            break;
                        case "Label":
                            EAPLabel label = (EAPLabel) toolbar.menubar.widgets.get(toolbar.menubar.widgets.indexOf((EAPLabel) curWidget));
                            middle = new Rectangle(label.getX() + 10, label.getY(), 15, 15);
                            middle.setFill(Color.LIME);
                            drawPane.getChildren().add(middle);
                            break;
                    }    
                }
                else if (!selected && !doResize) {
                    curWidget = null;
                    if (drawPane.getChildren().contains(start)) drawPane.getChildren().remove(start);
                    if (drawPane.getChildren().contains(end)) drawPane.getChildren().remove(end);
                    if (drawPane.getChildren().contains(middle)) drawPane.getChildren().remove(middle);
                    if (drawPane.getChildren().contains(left)) drawPane.getChildren().remove(left);
                    if (drawPane.getChildren().contains(right)) drawPane.getChildren().remove(right);
                    if (drawPane.getChildren().contains(top)) drawPane.getChildren().remove(top);
                    if (drawPane.getChildren().contains(bottom)) drawPane.getChildren().remove(bottom);
                    doResize = false;
                }

            }
            else if (!firstClick && !workTree.getSelectionModel().isEmpty()) {
                firstClick = true;
                firstx = event.getX();
                firsty = event.getY();
                
                if (!(curSelected.equals("Line") || curSelected.equals("Horizontal Line") || curSelected.equals("Vertical Line")
                        || curSelected.equals("Check Box") || curSelected.equals("Label") || curSelected.equals("Button"))) {
                    r = new Rectangle(event.getX(), event.getY(), 1, 1);
                    drawPane.getChildren().add(r);
                    prevX = (float) event.getX();
                    prevY = (float) event.getY();
                    r.setStyle("-fx-fill: " + "#" + toolbar.colorPicker.getValue().toString().substring(2, 8) + "; -fx-stroke: #000000; -fx-stroke-width:2;");
                }
                if (curSelected.equals("Line") || curSelected.equals("Horizontal Line")|| curSelected.equals("Vertical Line")) {
                    l = new Line(firstx, firsty, event.getX(), event.getY());
                    l.setStyle("-fx-stroke-width:2;");
                    prevX = (float) event.getX();
                    prevY = (float) event.getY();
                    drawPane.getChildren().add(l);
                }
            }
            else if (firstClick && !workTree.getSelectionModel().isEmpty()) {
                String curColor = "#" + toolbar.colorPicker.getValue().toString().substring(2, 8);
                int idx = workTree.getSelectionModel().getSelectedIndex();
                firstClick = false;
                if (drawPane.getChildren().contains(r))
                    drawPane.getChildren().remove(r);
                if (drawPane.getChildren().contains(l))
                    drawPane.getChildren().remove(l);
                
                switch (curSelected) {
                    case "Button":
                        EAPButton newButton= new EAPButton(event.getX(), event.getY(), 50, 30, curColor, promptInput("Button", primaryStage), toolbar.font.getFont());
                        newButton.paint(drawPane);
                        toolbar.menubar.widgets.add(newButton);
                        break;
                    case "Check Box":
                        EAPCheckbox newCheckbox = new EAPCheckbox(event.getX(), event.getY(), curColor, promptInput("Button", primaryStage), toolbar.font.getFont());
                        newCheckbox.paint(drawPane);
                        toolbar.menubar.widgets.add(newCheckbox);
                        break;
                    case "Label":
                        EAPLabel newLabel = new EAPLabel(event.getX(), event.getY(), promptInput("Label", primaryStage), toolbar.font.getFont());
                        newLabel.paint(drawPane);
                        toolbar.menubar.widgets.add(newLabel);
                        break;
                    case "Pane":
                        EAPPane newPane = new EAPPane(Math.min(firstx, event.getX()),Math.min(firsty,event.getY()),Math.max(firstx, event.getX()), Math.max(firsty,event.getY()),curColor);
                        newPane.paint(drawPane);
                        toolbar.menubar.panes.add(0, newPane);
                        break;
                    case "Window":
                        EAPWindow newWindow = new EAPWindow(Math.min(firstx, event.getX()),Math.min(firsty,event.getY()),Math.max(firstx, event.getX()), Math.max(firsty,event.getY()),curColor, primaryStage);
                        newWindow.paint(drawPane);
                        toolbar.menubar.panes.add(0, newWindow);
                        break;
                    case "Scrollable Pane":
                        EAPScrollablePane newScrollablePane = new EAPScrollablePane(Math.min(firstx, event.getX()),Math.min(firsty,event.getY()),Math.max(firstx, event.getX()), Math.max(firsty,event.getY()), curColor, primaryStage);
                        newScrollablePane.paint(drawPane);
                        toolbar.menubar.panes.add(0, newScrollablePane);
                        break;
                    case "Line":
                        EAPLine newLine = new EAPLine(firstx, firsty, event.getX(), event.getY(), "Line");
                        newLine.paint(drawPane);
                        toolbar.menubar.widgets.add(newLine);
                        break;
                    case "Horizontal Line":
                        EAPLine newHLine = new EAPLine(firstx, firsty, event.getX(), firsty, "Horizontal Line");
                        newHLine.paint(drawPane);
                        toolbar.menubar.widgets.add(newHLine);
                        break;
                    case "Vertical Line":
                        EAPLine newVLine = new EAPLine(firstx, firsty, firstx, event.getY(), "Vertical Line");
                        newVLine.paint(drawPane);
                        toolbar.menubar.widgets.add(newVLine);
                        break;
                }
            }
        });
        
        drawPane.setOnMouseMoved((MouseEvent event) -> {
            
            if (doResize) {
                if (resize.equals(start)) {
                    if (curWidget != null && curWidget.getWidgetType().equals("Line")) {
                        if (event.getX() > 0 && event.getY() > 0) {
                            start.setCenterX(event.getX());
                            start.setCenterY(event.getY());
                            drawPane.getChildren().remove(middle);
                            EAPLine temp = (EAPLine) toolbar.menubar.widgets.get(toolbar.menubar.widgets.indexOf((EAPLine) curWidget));
                            temp.setStart(event.getX(), event.getY());
                        }
                    }
                    else if (curWidget != null && curWidget.getWidgetType().equals("Horizontal Line")) {
                        EAPLine temp = (EAPLine) toolbar.menubar.widgets.get(toolbar.menubar.widgets.indexOf((EAPLine) curWidget));
                        if (event.getX() > 0) {
                            drawPane.getChildren().remove(middle);
                            start.setCenterX(event.getX());
                            start.setCenterY(temp.getStartY());
                            temp.setStart(event.getX(), temp.getStartY());
                        }
                    }
                    else if (curWidget != null && curWidget.getWidgetType().equals("Vertical Line")) {
                        EAPLine temp = (EAPLine) toolbar.menubar.widgets.get(toolbar.menubar.widgets.indexOf((EAPLine) curWidget));
                        if (event.getY() > 0) {
                            drawPane.getChildren().remove(middle);
                            start.setCenterX(temp.getStartX());
                            start.setCenterY(event.getY());
                            temp.setStart(temp.getStartX(), event.getY());
                        }
                    }
                }
                else if (resize.equals(end)) {
                    if (curWidget != null && curWidget.getWidgetType().equals("Line")) {
                        drawPane.getChildren().remove(middle);
                        end.setCenterX(event.getX());
                        end.setCenterY(event.getY());
                        EAPLine temp = (EAPLine) toolbar.menubar.widgets.get(toolbar.menubar.widgets.indexOf((EAPLine) curWidget));
                        temp.setEnd(event.getX(), event.getY());
                    }
                    else if (curWidget != null && curWidget.getWidgetType().equals("Horizontal Line")) {
                        EAPLine temp = (EAPLine) toolbar.menubar.widgets.get(toolbar.menubar.widgets.indexOf((EAPLine) curWidget));
                        drawPane.getChildren().remove(middle);
                        end.setCenterX(event.getX());
                        end.setCenterY(temp.getEndY());
                        
                        temp.setEnd(event.getX(), temp.getEndY());
                    }
                    else if (curWidget != null && curWidget.getWidgetType().equals("Vertical Line")) {
                        EAPLine temp = (EAPLine) toolbar.menubar.widgets.get(toolbar.menubar.widgets.indexOf((EAPLine) curWidget));
                        drawPane.getChildren().remove(middle);
                        end.setCenterX(temp.getEndX());
                        end.setCenterY(event.getY());
                        
                        temp.setEnd(temp.getEndX(), event.getY());
                    }
                }
                else if (resize.equals(middle)) {
                    if (curWidget != null && (curWidget.getWidgetType().equals("Line") || curWidget.getWidgetType().equals("Horizontal Line") || curWidget.getWidgetType().equals("Vertical Line"))) {
                        middle.setX(event.getX() - 10);
                        middle.setY(event.getY() - 10);
                        double dx = event.getX() - oldX;
                        double dy = event.getY() - oldY;
                        if (start.getCenterX() + dx > 0 && start.getCenterY() + dy > 0 && end.getCenterX() > 0 && end.getCenterY() + dy > 0) {
                            start.setCenterX(start.getCenterX() + dx);
                            start.setCenterY(start.getCenterY() + dy);
                            end.setCenterX(end.getCenterX() + dx);
                            end.setCenterY(end.getCenterY() + dy);
                            EAPLine temp = (EAPLine) toolbar.menubar.widgets.get(toolbar.menubar.widgets.indexOf((EAPLine) curWidget));
                            temp.setEnd(temp.getEndX() + dx, temp.getEndY() + dy);
                            temp.setStart(temp.getStartX() + dx, temp.getStartY() + dy);
                        }
                        oldX = event.getX();
                        oldY = event.getY();
                    }
                    else if (curWidget != null && curWidget.getWidgetType().equals("Pane")) {
                        middle.setX(event.getX() - 10);
                        middle.setY(event.getY() - 10);
                        EAPPane temp = (EAPPane) toolbar.menubar.panes.get(toolbar.menubar.panes.indexOf((EAPPane) curWidget));
                        double dx = event.getX() - oldX;
                        double dy = event.getY() - oldY;
                        if (temp.getX() + dx > 0 && temp.getY() + dy > 0){
                            temp.setX(temp.getX() + dx);
                            temp.setY(temp.getY() + dy);
                        }
                        drawPane.getChildren().removeAll(left, right, top, bottom);
                        oldX = event.getX();
                        oldY = event.getY();
                    }
                    else if (curWidget != null && curWidget.getWidgetType().equals("Window")) {
                        middle.setX(event.getX() - 10);
                        middle.setY(event.getY() - 10);
                        EAPWindow temp = (EAPWindow) toolbar.menubar.panes.get(toolbar.menubar.panes.indexOf((EAPWindow) curWidget));
                        double dx = event.getX() - oldX;
                        double dy = event.getY() - oldY;
                        if (temp.getX() + dx > 0 && temp.getY() + dy > 0){
                            temp.setX(temp.getX() + dx);
                            temp.setY(temp.getY() + dy);
                            temp.moveXButton(temp.getX() + temp.getWidth() - 30, temp.getY());
                            //temp.moveBar(dx, dy);
                        }
                        drawPane.getChildren().removeAll(left, right, top, bottom);
                        oldX = event.getX();
                        oldY = event.getY();
                    }
                    else if (curWidget != null && curWidget.getWidgetType().equals("Scrollable Pane")) {
                        middle.setX(event.getX() - 10);
                        middle.setY(event.getY() - 10);
                        EAPScrollablePane temp = (EAPScrollablePane) toolbar.menubar.panes.get(toolbar.menubar.panes.indexOf((EAPScrollablePane) curWidget));
                        double dx = event.getX() - oldX;
                        double dy = event.getY() - oldY;
                        if (temp.getX() + dx > 0 && temp.getY() + dy > 0){
                            temp.setX(temp.getX() + dx);
                            temp.setY(temp.getY() + dy);
                            temp.moveupButton(temp.getX() + temp.getWidth(), temp.getY());
                            temp.moveleftButton(temp.getX() + temp.getWidth(), temp.getY());
                            temp.movedownButton(temp.getX() + temp.getWidth(), temp.getY());
                            temp.moverightButton(temp.getX() + temp.getWidth(), temp.getY());
                            temp.setVertPos(temp.getX(), temp.getY());
                            temp.setHoriPos(temp.getX(), temp.getY());
                        }
                        drawPane.getChildren().removeAll(left, right, top, bottom);
                        oldX = event.getX();
                        oldY = event.getY();
                    }
                    else if (curWidget != null && curWidget.getWidgetType().equals("Button")) {
                        middle.setX(event.getX() - 10);
                        middle.setY(event.getY() - 10);
                        middle.setFill(Color.LIME);
                        double dx = event.getX() - oldX;
                        double dy = event.getY() - oldY;
                        EAPButton b = (EAPButton) toolbar.menubar.widgets.get(toolbar.menubar.widgets.indexOf((EAPButton) curWidget));
                        if (b.getX() + dx > 0 && b.getY() + dy > 0) {
                            b.setX(b.getX() + dx);
                            b.setY(b.getY() + dy);
                        }
                        oldX = event.getX();
                        oldY = event.getY();
                    }
                    else if (curWidget != null && curWidget.getWidgetType().equals("Check Box")) {
                        middle.setX(event.getX() - 10);
                        middle.setY(event.getY() - 10);
                        middle.setFill(Color.LIME);
                        
                        double dx = event.getX() - oldX;
                        double dy = event.getY() - oldY;
                        EAPCheckbox c = (EAPCheckbox) toolbar.menubar.widgets.get(toolbar.menubar.widgets.indexOf((EAPCheckbox) curWidget));
                        if (c.getX() + dx > 0 && c.getY() + dy > 0) {
                            c.setX(c.getX() + dx); 
                            c.setY(c.getY() + dy);
                            oldX = event.getX();
                            oldY = event.getY();
                        }
                    }
                    else if (curWidget != null && curWidget.getWidgetType().equals("Label")) {
                        middle.setX(event.getX() - 10);
                        middle.setY(event.getY() - 10);
                        middle.setFill(Color.LIME);
                        
                        double dx = event.getX() - oldX;
                        double dy = event.getY() - oldY;
                        EAPLabel label = (EAPLabel) toolbar.menubar.widgets.get(toolbar.menubar.widgets.indexOf((EAPLabel) curWidget));
                        if (label.getX() + dx > 0 && label.getY() + dy > 0) {
                            label.setX(label.getX() + dx); 
                            label.setY(label.getY() + dy);
                            oldX = event.getX();
                            oldY = event.getY();
                        }
                    }
                }
                else if (resize.equals(left)) {
                    if (curWidget != null && curWidget.getWidgetType().equals("Pane")) {     
                        EAPPane temp = (EAPPane) toolbar.menubar.panes.get(toolbar.menubar.panes.indexOf((EAPPane) curWidget));
                        drawPane.getChildren().removeAll(right, top, bottom, middle);
                        double diff = temp.getX() - event.getX();
                        if (event.getX() > 0 && temp.getWidth() + diff > 50){
                            temp.setX(event.getX());
                            temp.setWidth(temp.getWidth() + diff);
                            left.setX(event.getX() - 10);
                        }
                    }
                    else if (curWidget != null && curWidget.getWidgetType().equals("Window")) {     
                        EAPWindow temp = (EAPWindow) toolbar.menubar.panes.get(toolbar.menubar.panes.indexOf((EAPWindow) curWidget));
                        drawPane.getChildren().removeAll(right, top, bottom, middle);
                        double diff = temp.getX() - event.getX();
                        if (event.getX() > 0 && temp.getWidth() + diff > 50){
                            temp.setX(event.getX());
                            temp.setWidth(temp.getWidth() + diff);
                            left.setX(event.getX() - 10);     
                        }
                    }
                    else if (curWidget != null && curWidget.getWidgetType().equals("Scrollable Pane")) {     
                        EAPScrollablePane temp = (EAPScrollablePane) toolbar.menubar.panes.get(toolbar.menubar.panes.indexOf((EAPScrollablePane) curWidget));
                        drawPane.getChildren().removeAll(right, top, bottom, middle);
                        double diff = temp.getX() - event.getX();
                        if (event.getX() > 0 && temp.getWidth() + diff > 90){
                            temp.setX(event.getX());
                            temp.setWidth(temp.getWidth() + diff);
                            temp.moveleftButton(temp.getX() + temp.getWidth(), temp.getY());
                            left.setX(event.getX() - 10); 
                        }
                    }
                }
                else if (resize.equals(top)) {
                    if (curWidget != null && curWidget.getWidgetType().equals("Pane")) { 
                        EAPPane temp = (EAPPane) toolbar.menubar.panes.get(toolbar.menubar.panes.indexOf((EAPPane) curWidget));
                        drawPane.getChildren().removeAll(right, left, bottom, middle);
                        double diff = temp.getY() - event.getY();
                        if (event.getY() > 0 && temp.getHeight() + diff > 50){
                            temp.setY(event.getY());
                            temp.setHeight(temp.getHeight() + diff);
                            top.setY(event.getY() - 10);   
                        }
                    }
                    else if (curWidget != null && curWidget.getWidgetType().equals("Window")) { 
                        EAPWindow temp = (EAPWindow) toolbar.menubar.panes.get(toolbar.menubar.panes.indexOf((EAPWindow) curWidget));
                        drawPane.getChildren().removeAll(right, left, bottom, middle);
                        double diff = temp.getY() - event.getY();
                        if (event.getY() > 0 && temp.getHeight() + diff > 50){
                            temp.setY(event.getY());
                            temp.moveXButton(temp.getX() + temp.getWidth() - 30, temp.getY());

                            temp.setHeight(temp.getHeight() + diff);
                            top.setY(event.getY() - 10); 
                        }
                    }
                    else if (curWidget != null && curWidget.getWidgetType().equals("Scrollable Pane")) {     
                        EAPScrollablePane temp = (EAPScrollablePane) toolbar.menubar.panes.get(toolbar.menubar.panes.indexOf((EAPScrollablePane) curWidget));
                        drawPane.getChildren().removeAll(left, right, bottom, middle);
                        double diff = temp.getY() - event.getY();
                        if (event.getY() > 0 && temp.getHeight() + diff > 60){
                            temp.setY(event.getY());
                            temp.setHeight(temp.getHeight() + diff);
                            temp.moveupButton(temp.getX() + temp.getWidth(), temp.getY());
                            top.setY(event.getY() - 10); 
                        }
                    }
                }
                else if (resize.equals(right)) {
                    if (curWidget != null && curWidget.getWidgetType().equals("Pane")) {                      
                        EAPPane temp = (EAPPane) toolbar.menubar.panes.get(toolbar.menubar.panes.indexOf((EAPPane) curWidget));
                        drawPane.getChildren().removeAll(left, top, bottom, middle);
                        if (Math.abs(event.getX() - temp.getX()) > 50) {
                            temp.setWidth(Math.abs(event.getX() - temp.getX()));
                            right.setX(event.getX() - 10);
                        }
                        oldX = event.getX();
                        oldY = event.getY(); 
                    }
                    else if (curWidget != null && curWidget.getWidgetType().equals("Window")) {                      
                        EAPWindow temp = (EAPWindow) toolbar.menubar.panes.get(toolbar.menubar.panes.indexOf((EAPWindow) curWidget));
                        drawPane.getChildren().removeAll(left, top, bottom, middle);
                        if (Math.abs(event.getX() - temp.getX()) > 50) {
                            temp.setWidth(Math.abs(event.getX() - temp.getX()));
                            temp.moveXButton(temp.getX() + temp.getWidth() - 30, temp.getY());
                            right.setX(event.getX() - 10);
                        }
                        oldX = event.getX();
                        oldY = event.getY();
                    }
                    else if (curWidget != null && curWidget.getWidgetType().equals("Scrollable Pane")) {     
                        EAPScrollablePane temp = (EAPScrollablePane) toolbar.menubar.panes.get(toolbar.menubar.panes.indexOf((EAPScrollablePane) curWidget));
                        drawPane.getChildren().removeAll(left, top, bottom, middle);
                        if (Math.abs(event.getX() - temp.getX()) > 90) {
                            temp.setWidth(Math.abs(event.getX() - temp.getX()));
                            temp.moverightButton(temp.getX() + temp.getWidth(), temp.getY());
                            temp.movedownButton(temp.getX() + temp.getWidth(), temp.getY());
                            temp.moveupButton(temp.getX() + temp.getWidth(), temp.getY());
                            temp.setVertPos(temp.getX() , temp.getY());
                            right.setX(event.getX() - 10); 
                        }
                    }
                }
                else if (resize.equals(bottom)) {
                    if (curWidget != null && curWidget.getWidgetType().equals("Pane")) {
                        EAPPane temp = (EAPPane) toolbar.menubar.panes.get(toolbar.menubar.panes.indexOf((EAPPane) curWidget));
                        drawPane.getChildren().removeAll(left, top, right, middle);
                        if (Math.abs(event.getY() - temp.getY()) > 50) {
                            temp.setHeight(Math.abs(event.getY() - temp.getY()));
                            bottom.setY(event.getY() - 10);
                        }
                        oldX = event.getX();
                        oldY = event.getY();
                        
                    }
                    if (curWidget != null && curWidget.getWidgetType().equals("Window")) {
                        EAPWindow temp = (EAPWindow) toolbar.menubar.panes.get(toolbar.menubar.panes.indexOf((EAPWindow) curWidget));
                        drawPane.getChildren().removeAll(left, top, right, middle);
                        if (Math.abs(event.getY() - temp.getY()) > 50) {
                            temp.setHeight(Math.abs(event.getY() - temp.getY()));
                            bottom.setY(event.getY() - 10);
                        }
                        oldX = event.getX();
                        oldY = event.getY();                       
                    }
                    else if (curWidget != null && curWidget.getWidgetType().equals("Scrollable Pane")) {     
                        EAPScrollablePane temp = (EAPScrollablePane) toolbar.menubar.panes.get(toolbar.menubar.panes.indexOf((EAPScrollablePane) curWidget));
                        drawPane.getChildren().removeAll(left, top, right, middle);
                        if (Math.abs(event.getY() - temp.getY()) > 60) {
                            temp.setHeight(Math.abs(event.getY() - temp.getY()));
                            temp.moverightButton(temp.getX() + temp.getWidth(), temp.getY());
                            temp.movedownButton(temp.getX() + temp.getWidth(), temp.getY());
                            temp.moveleftButton(temp.getX() + temp.getWidth(), temp.getY());
                            temp.setHoriPos(temp.getX() , temp.getY());
                            bottom.setY(event.getY() - 10); 
                        }
                    }
                }
            }
            if (firstClick){
                if (drawPane.getChildren().contains(r)) {
                    if (event.getX() < prevX && event.getY() < prevY) {
                        r.setX(event.getX());
                        r.setY(event.getY());
                        r.setWidth(prevX - event.getX());
                        r.setHeight(prevY - event.getY());
                    }
                    else if (event.getX() < prevX && event.getY() > prevY) {
                        r.setX(event.getX());
                        //r.setY(event.getY());
                        r.setWidth(prevX - event.getX());
                        r.setHeight(event.getY() - prevY);
                    }
                    else if (event.getX() > prevX && event.getY() < prevY) {
                        //r.setX(event.getX());
                        r.setY(event.getY());
                        r.setWidth(event.getX() - prevX);
                        r.setHeight(prevY - event.getY());
                    }
                    else if (event.getX() > prevX && event.getY() > prevY) {
                        //r.setX(event.getX());
                        //r.setY(event.getY());
                        r.setWidth(event.getX() - prevX);
                        r.setHeight(event.getY() - prevY);
                    }
                }
                else if (drawPane.getChildren().contains(l)) {
                    switch (curSelected) {
                        case "Line":
                            l.setEndX(event.getX());
                            l.setEndY(event.getY());
                            break;
                        case "Horizontal Line":
                            l.setEndX(event.getX());
                            break;
                        case "Vertical Line":
                           l.setEndY(event.getY());
                           break;
                    }
                }
            }
        }); 
        
        //now set the menuitems that are in toolbar and highly related to drawpane
        
        toolbar.menubar.openMenuItem.setOnAction((final ActionEvent e) -> {
            toolbar.menubar.newMenuItem.fire();
            
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("EAP GUI Project", "*.eap")
            );
            fileChooser.setTitle("");
            
            File file = fileChooser.showOpenDialog(null);
            if (file != null) {
                toolbar.menubar.workfile=file;
                try {
                    try (BufferedReader bReader = new BufferedReader(new FileReader(toolbar.menubar.workfile))) {
                        String line;
                        while ((line = bReader.readLine()) != null) {
                            if (!line.equals("")) {
                                String part[] = line.split(" ");
                                
                                //local variables for add widgets to the workPane
                                double x1,y1,x2,y2, fontSize;
                                String color, txt,fontName;
                                
                                switch (part[0]) {
                                    case "EAPButton":
                                        x1=Double.parseDouble(part[1]);
                                        y1=Double.parseDouble(part[2]);
                                        x2=Double.parseDouble(part[3]);
                                        y2=Double.parseDouble(part[4]);
                                        color=part[5];
                                        //set content
                                        txt="";
                                        for (int i=6; i<part.length-1; i++)
                                            txt=txt + part[i] + " ";
                                        //set font
                                        fontSize =Double.parseDouble(part[part.length-1]);
                                        fontName=bReader.readLine();
                                        
                                        EAPButton newButton= new EAPButton(x1,y1,x2,y2,color,txt,new Font(fontName, fontSize));
                                        newButton.paint(drawPane);
                                        toolbar.menubar.widgets.add(newButton);
                                        break;
                                    case "EAPCheckBox":
                                        x1=Double.parseDouble(part[1]);
                                        y1=Double.parseDouble(part[2]);
                                        color=part[3];
                                        //set content
                                        txt="";
                                        for (int i=4; i<part.length-1; i++)
                                            txt=txt + part[i] + " ";
                                        //set font
                                        fontSize =Double.parseDouble(part[part.length-1]);
                                        fontName=bReader.readLine();
                                        
                                        EAPCheckbox newCheckbox = new EAPCheckbox(x1, y1, color,txt,  new Font(fontName, fontSize));
                                        newCheckbox.paint(drawPane);
                                        toolbar.menubar.widgets.add(newCheckbox);
                                        break;
                                    case "EAPLabel":
                                        x1=Double.parseDouble(part[1]);
                                        y1=Double.parseDouble(part[2]);
                                        //set content
                                        txt="";
                                        for (int i=3; i<part.length-1; i++)
                                            txt=txt + part[i] + " ";
                                        //set font
                                        fontSize =Double.parseDouble(part[part.length-1]);
                                        fontName=bReader.readLine();
                                        
                                        EAPLabel newLabel = new EAPLabel(x1, y1, txt, new Font(fontName, fontSize));
                                        newLabel.paint(drawPane);
                                        toolbar.menubar.widgets.add(newLabel);
                                        break;
                                    case "EAPPane":
                                        x1=Double.parseDouble(part[1]);
                                        y1=Double.parseDouble(part[2]);
                                        x2=Double.parseDouble(part[3]);
                                        y2=Double.parseDouble(part[4]);
                                        color=part[5];
                                        EAPPane newPane = new EAPPane(x1,y1,x1 + x2,y1 + y2,color);
                                        newPane.paint(drawPane);
                                        toolbar.menubar.panes.add(0, newPane);
                                        break;
                                    case "EAPWindow":
                                        x1=Double.parseDouble(part[1]);
                                        y1=Double.parseDouble(part[2]);
                                        x2=Double.parseDouble(part[3]);
                                        y2=Double.parseDouble(part[4]);
                                        color=part[5];
                                        EAPWindow newWindow = new EAPWindow(x1, y1, x1 + x2, y1 + y2,color, primaryStage);
                                        newWindow.paint(drawPane);
                                        toolbar.menubar.panes.add(0, newWindow);
                                        break;
                                    case "EAPScrollablePane":
                                        x1=Double.parseDouble(part[1]);
                                        y1=Double.parseDouble(part[2]);
                                        x2=Double.parseDouble(part[3]);
                                        y2=Double.parseDouble(part[4]);
                                        color=part[5];
                                        EAPScrollablePane newScrollablePane = new EAPScrollablePane(x1,y1,x1 + x2, y1 + y2,color, primaryStage);
                                        newScrollablePane.paint(drawPane);
                                        toolbar.menubar.panes.add(0, newScrollablePane);
                                        break;
                                    case "Line":
                                        x1=Double.parseDouble(part[1]);
                                        y1=Double.parseDouble(part[2]);
                                        x2=Double.parseDouble(part[3]);
                                        y2=Double.parseDouble(part[4]);
                                        EAPLine newLine = new EAPLine(x1,y1,x2,y2, "Line");
                                        newLine.paint(drawPane);
                                        toolbar.menubar.widgets.add(newLine);
                                        break;
                                        //"Horizontal Line x1 y1 x2 y2": part[1], the second, is not coordinates; part[0] will only be "Line", "Horizontal", or "Vertical"
                                    case "Horizontal":
                                        x1=Double.parseDouble(part[2]);
                                        y1=Double.parseDouble(part[3]);
                                        x2=Double.parseDouble(part[4]);
                                        y2=Double.parseDouble(part[5]);
                                        EAPLine newHLine = new EAPLine(x1,y1,x2,y2, "Horizontal Line");
                                        newHLine.paint(drawPane);
                                        toolbar.menubar.widgets.add(newHLine);
                                        break;
                                    case "Vertical":
                                        x1=Double.parseDouble(part[2]);
                                        y1=Double.parseDouble(part[3]);
                                        x2=Double.parseDouble(part[4]);
                                        y2=Double.parseDouble(part[5]);
                                        EAPLine newVLine = new EAPLine(x1,y1,x2,y2, "Vertical Line");
                                        newVLine.paint(drawPane);
                                        toolbar.menubar.widgets.add(newVLine);
                                        break;
                                }
                            }
                        }
                    bReader.close();
                    }
                } catch (IOException ex) {
                    Logger.getLogger(Toolbar.class.getName()).log(Level.SEVERE, null, ex);
                } 
            }
        });
        
        //create new project will save the existed file in auto
        toolbar.menubar.newMenuItem.setOnAction((ActionEvent event)-> {
            if (toolbar.menubar.workfile!=null) {
                toolbar.menubar.saveMenuItem.fire();
            }
            toolbar.menubar.clearMenuItem.fire();
        });
        
        //the image is asked to be saved as *.png
        toolbar.menubar.exportMenuItem.setOnAction((ActionEvent event) -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Export As Image");
            fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
            fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("PNG", "*.png"));
            File file = fileChooser.showSaveDialog(null);
            try {
                WritableImage writableImage = drawPane.snapshot(null, null);
                ImageIO.write(SwingFXUtils.fromFXImage(writableImage, null), "png", file);
            } catch(IOException ex) {
                Logger.getLogger(WorkPane.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        
        toolbar.menubar.clearMenuItem.setOnAction((ActionEvent event)-> {
            toolbar.menubar.workfile=null;
            toolbar.menubar.widgets.clear();
            toolbar.menubar.panes.clear();
            this.drawPane.getChildren().clear();
        });
        
        toolbar.menubar.deleteMenuItem.setOnAction((ActionEvent event)-> {
            if (this.curWidget!=null) {
                curWidget.removeFromPane(this.drawPane);
                this.drawPane.getChildren().removeAll(right, top, left, middle,bottom);
                if (toolbar.menubar.widgets.contains(curWidget)) 
                    toolbar.menubar.widgets.remove(curWidget);
                if (toolbar.menubar.panes.contains(curWidget))
                    toolbar.menubar.panes.remove(curWidget);
                
            }       
        });
        
        toolbar.menubar.cutMenuItem.setOnAction((ActionEvent event)-> {
            try {
                //deep clone, so temp= EAPWidget temp=curWidget.clone();
                pasteWidget=cloneTo(curWidget);
                toolbar.menubar.pasteMenuItem.setDisable(false);
                toolbar.menubar.deleteMenuItem.fire();
            } catch (RuntimeException | IOException | ClassNotFoundException ex) {
                Logger.getLogger(WorkPane.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        
        toolbar.menubar.copyMenuItem.setOnAction((ActionEvent event)-> {
            try {
                //deep clone, so temp= EAPWidget temp=curWidget.clone();
                pasteWidget=cloneTo(curWidget);
                toolbar.menubar.pasteMenuItem.setDisable(false);
            } catch (RuntimeException | IOException | ClassNotFoundException ex) {
                Logger.getLogger(WorkPane.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        
        toolbar.menubar.pasteMenuItem.setOnAction((ActionEvent event)-> {
            pasteWidget.paint(this.drawPane);
            switch (pasteWidget.getWidgetType()) {
                case "Button":
                case "Check Box":
                case "Label":
                case "Line":
                case "Horizontal Line":
                case "Vertical Line":
                    toolbar.menubar.widgets.add(pasteWidget);
                    break;
                case "Pane":
                case "Window":
                case "Scrollable Pane":
                    toolbar.menubar.panes.add(pasteWidget);
            }
        });
    }
    
    /**
     * initialize a VBox for the work tree
     */
    private void initializeVBox() {
        workBox = new VBox();
        
        TreeItem<String> window = new TreeItem<> ("Window");
        TreeItem<String> pane = new TreeItem<> ("Pane");
        TreeItem<String> label = new TreeItem<> ("Label");
        TreeItem<String> button = new TreeItem<> ("Button");
        TreeItem<String> checkBox = new TreeItem<> ("Check Box");
        TreeItem<String> scrollable = new TreeItem<> ("Scrollable Pane");
        TreeItem<String> line = new TreeItem<> ("Line");
        TreeItem<String> vertline = new TreeItem<> ("Vertical Line");
        TreeItem<String> horiline = new TreeItem<> ("Horizontal Line");
        
        TreeItem root = new TreeItem<> ("GUI Drawing Tools");
        root.getChildren().addAll(
                 window, pane, label, button,   
                 checkBox, scrollable, vertline, horiline, line);
        
        workTree = new TreeView<> (root);
        workTree.setShowRoot(false);
        workTree.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        VBox.setVgrow(workTree, Priority.ALWAYS);
        workTree.setMaxHeight(Double.MAX_VALUE);
        
        workBox.getChildren().add(workTree);
        
        this.workTree.getSelectionModel().selectedItemProperty().addListener(
                (ObservableValue<? extends TreeItem<String>> observable, TreeItem<String> oldValue, TreeItem<String> newValue) -> {
            if (newValue != null) {
                curSelected = newValue.getValue();  
                System.out.println(curSelected);
            }
        });
    }
    
    
    /**
     * a constructor
     * @param stage the primary stage of this
     * @param toolbar a related Toolbar class for get color
     */
    public WorkPane(Stage stage, Toolbar toolbar) {
        super();
          
        initializeDrawPane(stage, toolbar);
        initializeVBox();
        
        ColumnConstraints col1 = new ColumnConstraints();
        col1.setMaxWidth(150);
        col1.setMinWidth(150);
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setHgrow(Priority.ALWAYS);
        this.getColumnConstraints().addAll(col1,col2);
        
        RowConstraints row = new RowConstraints();
        row.setVgrow(Priority.ALWAYS);
        this.getRowConstraints().add(row);
        
        StackPane rightPane = new StackPane();
        rightPane.getChildren().add(drawPane);
        rightPane.setStyle("-fx-background-color: white;");
        
        this.add(workBox, 0, 0);
        this.add(rightPane, 1, 0);

    }   
}
