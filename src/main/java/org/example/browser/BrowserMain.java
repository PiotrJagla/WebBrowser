package org.example.browser;

import java.util.List;
import java.util.Map;

public class BrowserMain {

    public static void main(String[] args) {
        //HTML parser
        HTMLParser parser = new HTMLParser("<html>" +
                "<body>" +
                "<h1>Title</h1>" +
                "<div id=\"main\" class=\"ltest\">" +
                "<p>Hello <em>world</em>!</p>" +
                "</div>" +
                "</body>" +
                "</html>");
        HTMLParser parser1 = new HTMLParser("<div name=\"this is name\" class=\"firstClass\" > Hello </div>");
        Node root = parser.parse();
        printTree(root);

//        CSSParser
        CSSParser cssParser = new CSSParser(
            "h1 { padding: auto; }" +
                    ".ltest {color:#cc0011; margin: 20px; }" +
                "p{display:block; padding: 20px; }"
        );
        Stylesheet stylesheet = cssParser.parse();

        //Styling DOM tree
        Style style = new Style();
        StyledNode styledTreeRoot = style.styleTree(root,stylesheet);
        System.out.println("Styling done");

        //Layout
        Layout layout = new Layout();
        Dimensions viewport = new Dimensions();
        viewport.getContent().width = 800;
        viewport.getContent().height = 600;
        LayoutBox layoutRoot = layout.layoutTree(styledTreeRoot, viewport);
        System.out.println("Layout done");
        Paint paint = new Paint();
        List<DisplayCommand> list = paint.buildDisplayList(layoutRoot);
        System.out.print("Display commands done");
    }

    public static void printTree(Node root) {
        System.out.print("(");

        if(root instanceof TextNode) {
            TextNode textNode = (TextNode)root;
            System.out.print("T: " + textNode.getText());
            return;
        }
        else if(root instanceof ElementNode) {
            ElementNode elementNode = (ElementNode) root;
            System.out.print("N: " + elementNode.getTagName());
            for (Map.Entry<String, String> en : elementNode.getAttrMap().entrySet()) {
                System.out.print( " " + en);
            }
            if(elementNode.getChildren().size() == 0) {
                return;
            }

            for (Node child : elementNode.getChildren()) {
                printTree(child);
            }

        }

        System.out.print(")");
    }
}
