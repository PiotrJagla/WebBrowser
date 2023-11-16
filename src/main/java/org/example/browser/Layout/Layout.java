package org.example.browser.Layout;


import org.example.browser.HTML.TextNode;
import org.example.browser.Style.StyledNode;

import java.util.List;


public class Layout {

    public LayoutBox layoutTree(StyledNode root, Dimensions containingBlock) {
        containingBlock.getContent().setHeight(0.0f);
        LayoutBox layoutRoot = buildLayoutTree(root);
        layoutRoot.layout(containingBlock);
        return layoutRoot;
    }
    private LayoutBox buildLayoutTree(StyledNode styledNode) {
        LayoutBox node = new LayoutBox();
        BoxType nodeDisplay = BoxType.BlockNode;
        switch(styledNode.getDisplay()) {
            case Block:
                nodeDisplay = BoxType.BlockNode;
                break;
            case Inline:
                nodeDisplay = BoxType.InlineNode;
                break;
            case None:
                nodeDisplay = BoxType.None;
                break;
        }
        node.setBox(new Box(nodeDisplay, styledNode));


        for (StyledNode child : styledNode.getChildren()) {
            switch(child.getDisplay()) {
                case Block:
                    //Wrapping node around anonymous block
//                    LayoutBox anonBox = new LayoutBox();
//                    anonBox.setBox(new Box(BoxType.AnonymousNode, child));
//                    anonBox.setParentBox(node);
//                    anonBox.addChild(buildLayoutTree(child));
                    node.addChild(buildLayoutTree(child));
                    break;
                case Inline:
                    node.getInlineContainer(styledNode.getChildren(), child).addChild(buildLayoutTree(child));
                    break;
                case None:
                    break;
            }
        }

        return node;
    }

}









