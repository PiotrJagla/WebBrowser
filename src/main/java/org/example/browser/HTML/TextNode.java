package org.example.browser.HTML;


import io.github.humbleui.skija.Font;
import org.example.browser.Layout.Rectangle;
import org.example.graphicslibrary.Text;

public class TextNode extends Node{
    private Text text;

    public TextNode(String text) {

        this.text = new Text(text,0,0, new Font());
    }

    public String getText() {
        return text.getText();
    }

    public Rectangle getBounds() {
        return text.getBounds();
    }

}
