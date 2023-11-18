package org.example.browser.Painting;


import io.github.humbleui.skija.Font;
import org.example.browser.CSS.Values.CSSColor;
import org.example.browser.CSS.Values.Value;
import org.example.browser.HTML.Node;
import org.example.browser.HTML.TextNode;
import org.example.browser.Layout.Dimensions;
import org.example.browser.Layout.LayoutBox;
import org.example.browser.Layout.Rectangle;
import org.example.graphicslibrary.Text;

import java.util.ArrayList;
import java.util.List;

public class RenderingPaint {

    public List<DisplayCommand> buildDisplayList(LayoutBox layoutRoot) {
        List<DisplayCommand> list = new ArrayList<>();
        renderLayoutBox(list, layoutRoot);
        return list;
    }

    private void renderLayoutBox(List<DisplayCommand> list, LayoutBox layoutRoot) {
        renderBackground(list, layoutRoot);
        renderText(list,layoutRoot);
        renderBorders(list, layoutRoot);

        for (LayoutBox child : layoutRoot.getChildren()) {
            renderLayoutBox(list,child);
        }
    }

    private void renderText(List<DisplayCommand> list, LayoutBox layoutBox) {
        if(layoutBox.getBox().getStyledNode().getNode() instanceof TextNode textNode) {
            Text text = new Text(textNode.getText(), layoutBox.getDimensions().borderBox().x(), layoutBox.getDimensions().borderBox().y(), new Font());
            list.add(new SolidText().setText(text));
        }
    }


    private void renderBackground(List<DisplayCommand> list, LayoutBox layoutBox) {
        CSSColor color = getColor(layoutBox, "background");
        SolidColor sc;
        if(color != null) {
            sc = new SolidColor();
            sc.setColor(color);
        }
        else {
            sc = new SolidColor();
            sc.setColor(new CSSColor());
        }
        sc.setRect(layoutBox.getDimensions().borderBox());
        list.add(sc);
    }

    private void renderBorders(List<DisplayCommand> list, LayoutBox layoutRoot) {
        CSSColor color = getColor(layoutRoot, "border-color");
        if(color == null) {
            color = new CSSColor();
        }

        Dimensions d = layoutRoot.getDimensions();
        Rectangle border_box = d.borderBox();

        //Left border
        Rectangle rect = new Rectangle();
        rect.setX(border_box.x());
        rect.setY(border_box.y());
        rect.setWidth( d.getBorder().getLeft());
        rect.setHeight( border_box.height());
        SolidColor sc = new SolidColor();
        sc.setRect(rect);
        sc.setColor(color);



        //Right border
        rect = new Rectangle();
        rect.setX( border_box.x() + border_box.width() - d.getBorder().getRight());
        rect.setY( border_box.y());
        rect.setWidth( d.getBorder().getRight());
        rect.setHeight( border_box.height());
        sc = new SolidColor();
        sc.setRect(rect);
        sc.setColor(color);

        //Top border
        rect = new Rectangle();
        rect.setX( border_box.x());
        rect.setY(border_box.y());
        rect.setWidth( border_box.width());
        rect.setHeight( d.getBorder().getTop());
        sc = new SolidColor();
        sc.setRect(rect);
        sc.setColor(color);

        //Bottom border
        rect = new Rectangle();
        rect.setX(border_box.x());
        rect.setY( border_box.y() + border_box.height() - d.getBorder().getBottom());
        rect.setWidth( border_box.width());
        rect.setHeight( d.getBorder().getBottom());
        sc = new SolidColor();
        sc.setRect(rect);
        sc.setColor(color);
    }

    private CSSColor getColor(LayoutBox layoutBox, String name) {
        switch(layoutBox.getBox().getBoxType()) {
            case BlockNode:
            case InlineNode:
                Value v = layoutBox.getBox().getStyledNode().value(name);
                if(v instanceof CSSColor) {
                    return (CSSColor) v;
                }
            default:
                return null;
        }
    }
}


