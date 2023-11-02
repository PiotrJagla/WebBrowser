package com.mygdx.game.browser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

public class CSSParser {
    private String input;
    private int pos;

    public CSSParser(String input) {
        this.input = input;
        this.pos = 0;
    }

    public Stylesheet parse() {
        Stylesheet s = new Stylesheet();
        s.setRules(parseRules());
        return s;
    }

    private List<Rule> parseRules() {
        List<Rule> rules = new ArrayList<>();
        while(true) {
            consumeWhitespace();
            if(eof()) {
                break;
            }
            rules.add(parseRule());
        }
        return rules;
    }

    private Rule parseRule() {
        Rule r = new Rule();
        r.setSelectors(parseSelectors());
        r.setDeclarations(parseDeclarations());
        return r;
    }

    private List<CSSSelector> parseSelectors() {
        List<CSSSelector> selectors = new ArrayList<>();
        while(true) {
            selectors.add(parseSimpleSelector());
            consumeWhitespace();
            if(peek() == ',') {
                consume();
                consumeWhitespace();
            }
            else if(peek() == '{') {
                break;
            }
            else{
                throw new RuntimeException("Unexpected character {} in selector list" + peek());
            }
        }
        Comparator<CSSSelector> cmp = (a, b) -> {
            Specifity sA = a.specifity();
            Specifity sB = b.specifity();

            int comp = Integer.compare(sB.getA(), sA.getA());
            if(comp == 0) {
                comp = Integer.compare(sB.getB(), sA.getB());
                if(comp == 0) {
                    comp = Integer.compare(sB.getC(), sA.getC());
                }
            }
            return comp;
        };
        Collections.sort(selectors,cmp);
        return selectors;
    }

    private SimpleSelector parseSimpleSelector() {
        SimpleSelector selector = new SimpleSelector();

        while(!eof()) {

            boolean didMatch = true;
            switch(peek()) {
                case '#':
                    consume();
                    selector.setId(parseIdentifier());
                    break;
                case '.':
                    consume();
                    selector.getClasses().add(parseIdentifier());
                    break;
                case '*':
                    consume();
                    break;
                default:
                    if(validIdentifierChar(peek())) {
                        selector.setTagName(parseIdentifier());
                    }
                    else {
                        didMatch = false;
                    }
            }
            if(!didMatch) {
               break;
            }
        }
        return selector;
    }

    private List<Declaration> parseDeclarations() {
        if(consume() != '{'){
            throw new RuntimeException("This is not right, in parseDeclarations the character consumed shoud be {");
        }
        List<Declaration> declarations = new ArrayList<>();

        while(true) {
            consumeWhitespace();
            if(peek() == '}') {
                consume();
                break;
            }
            declarations.add(parseDeclaration());
        }
        return declarations;
    }

    private Declaration parseDeclaration() {
        String propName = parseIdentifier();
        consumeWhitespace();
        if(consume() != ':') {
            throw new RuntimeException("in parse declaration consumed shoud ne ':'");
        }
        consumeWhitespace();
        Value v = parseValue();
        consumeWhitespace();
        if(consume() != ';') {
            throw new RuntimeException("in parse declaration consumed shoud ne ';'");
        }
        Declaration d = new Declaration();
        d.setValue(v);
        d.setName(propName);
        return d;

    }

    private Value parseValue() {
        Value res = null;

        if(Character.isDigit(peek())) {
            res = parseLength();
        }
        else if(peek() == '#') {
            res = parseColor();
        }
        else {
            Keyword k = new Keyword();
            k.setK(parseIdentifier());
            res = k;
        }

        return res;
    }

    private Length parseLength() {
        Length res = new Length();
        res.setLength(parseFloat());
        res.setUnit(parseUnit());
        return res;
    }

    private float parseFloat() {
        String s = consumeUntil(c -> Character.isDigit(c) || c == '.');
        return Float.parseFloat(s);
    }

    private Unit parseUnit() {
        String i = parseIdentifier();
        if(i.toLowerCase().equals("px")) {
            return Unit.Px;
        }
        throw new RuntimeException("unrecognized unit in parseUnit()");
    }

    private Color parseColor() {
        if(consume() != '#') {
            throw new RuntimeException("Bad identifier for color, should be #");
        }

        Color c = new Color();
        c.setR(parseHexPair());
        c.setG(parseHexPair());
        c.setB(parseHexPair());
        c.setA(255);
        return c;
    }

    private int parseHexPair() {
        String s = input.substring(pos, pos+2);
        pos += 2;
        return Integer.parseInt(s, 16);
    }

    private String parseIdentifier() {
        return consumeUntil(c -> (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || (c >= '0' && c <= '9'));
    }


    private boolean eof() {
        return pos >= input.length();
    }

    private char consume() {
        return input.charAt(pos++);
    }

    private char peek() {
        return input.charAt(pos);
    }

    private void consumeWhitespace() {
        consumeUntil(c -> c == ' ' || c == '\n');
    }

    private boolean validIdentifierChar(char c) {
        if((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || (c >= '0' && c <= '9')) {
            return true;
        }
        return false;
    }

    private String consumeUntil(Predicate<Character> test) {
        StringBuilder res = new StringBuilder();
        while(!eof() && test.test(peek())) {
            res.append(consume());
        }
        return res.toString();
    }
}
