package com.mygdx.game.browser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

public class HTMLParser {
    private String input;
    private int pos;

    public HTMLParser(String input) {
        this.input = input;
        pos = 0;
    }

    public Node parse() {
        Node root = parseNode();
        return root;
    }

    private char peek() {
        return input.charAt(pos);
    }

    private boolean startsWith(String str) {
        return input.substring(pos).startsWith(str);
    }

    private boolean eof() {
        return pos >= input.length();
    }

    private char consume() {
        return input.charAt(pos++);
    }

    private Node parseNode() {
        switch(peek()){
            case '<':
                return parseElement();
            default:
                return parseText();
        }
    }

    private Node parseElement() {
        consume();
        String tagName = parseTagName();
        Map<String, String> attrs = parseAttrs();
        consume();

        List<Node> children = parseNodes();
        consume();
        consume();
        parseTagName();
        consume();
        return new ElementNode(tagName, attrs, children);
    }

    private List<Node> parseNodes() {
        List<Node> nodes = new ArrayList<>();
        while(true) {
            consumeWhitespace();
            if(eof() || startsWith("</")) {
                break;
            }
            Node n = parseNode();
            nodes.add(n);
        }
        return nodes;
    }

    private Map<String,String> parseAttrs() {
        Map<String,String> attrs = new HashMap<>();
        while(true) {
            consumeWhitespace();
            if(eof() || peek() == '>') {
                break;
            }
            String oneAttr = parseAttr();
            String[] ggg = oneAttr.split("#");
            attrs.put(ggg[0], ggg[1]);
        }
        return attrs;
    }

    private String parseAttr() {
        String name = parseTagName();
        consume();
        String value = parseAttrValue();
        return name + "#" + value;
    }

    private String parseAttrValue() {
        char openQ = consume();
        System.out.println(openQ);
        String value = consumeUntil(c -> c != openQ);
        consume();
        return value;
    }

    private Node parseText() {
        return new TextNode(consumeUntil(c -> c != '<'));
    }

    private String parseTagName() {
        return consumeUntil(c -> (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || (c >= '0' && c <= '9'));
    }

    private void consumeWhitespace() {
        consumeUntil((character -> character == ' ' || character == '\n'));
//        if(peek() == ' ' ||)

    }

    private String consumeUntil(Predicate<Character> test) {
        StringBuilder res = new StringBuilder();
        while(!eof() && test.test(peek())) {
            res.append(consume());
        }
        return res.toString();
    }

}
