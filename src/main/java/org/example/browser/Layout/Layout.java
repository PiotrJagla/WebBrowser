package org.example.browser.Layout;


import org.example.browser.Style.StyledNode;


public class Layout {

    public LayoutBox layoutTree(StyledNode root, Dimensions containingBlock) {
        containingBlock.getContent().setHeight(  0.0f);
        LayoutBox layoutRoot = buildLayoutTree(root);
        layoutRoot.layout(containingBlock);
        return layoutRoot;
    }
    private LayoutBox buildLayoutTree(StyledNode styledNode) {
        LayoutBox root = new LayoutBox();
        switch(styledNode.getDisplay()) {
            case Block:
                root.setBox(new Box(BoxType.BlockNode, styledNode));
                break;
            case Inline:
                root.setBox(new Box(BoxType.InlineNode, styledNode));
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









