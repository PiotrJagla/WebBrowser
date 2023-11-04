package org.example.browser.Layout;


import org.example.browser.CSS.Values.Keyword;
import org.example.browser.CSS.Values.Length;
import org.example.browser.CSS.Values.Value;
import org.example.browser.Style.StyledNode;
import org.example.browser.Layout.Display;

import java.util.ArrayList;
import java.util.List;

import static org.example.browser.CSS.Values.Unit.Px;


public class Layout {

    public LayoutBox layoutTree(StyledNode root, Dimensions containingBlock) {
        containingBlock.getContent().setHeight(  0.0f);
        LayoutBox layoutRoot = buildLayoutTree(root);
        layoutRoot.layout(containingBlock);
        return layoutRoot;
    }
    private LayoutBox buildLayoutTree(StyledNode styledNode) {
        LayoutBox root = new LayoutBox();
        BoxType boxType = new BoxType();
        switch(styledNode.getDisplay()) {
            case Block:
                boxType.setBoxTypeName(BoxTypeName.BlockNode);
                boxType.setStyledNode(styledNode);
                root.setBoxType(boxType);
                break;
            case Inline:
                boxType.setBoxTypeName(BoxTypeName.InlineNode);
                boxType.setStyledNode(styledNode);
                root.setBoxType(boxType);
                break;
        }

        for (StyledNode child : styledNode.getChildren()) {
            switch(child.getDisplay()) {
                case Block:
                    root.getChildren().add(buildLayoutTree(child));
                    break;
                case Inline:
                    root.getInlineContainer().getChildren().add(buildLayoutTree(child));
                    break;
                case None:
                    break;
            }
        }

        return root;
    }

}









