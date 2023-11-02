package com.mygdx.game.browser;

import java.util.*;
import java.util.stream.Collectors;

public class Style {

    public StyledNode styleTree(Node root, Stylesheet stylesheet) {
        StyledNode styledNode = new StyledNode();
        styledNode.setNode(root);
        if(root instanceof TextNode) {
            styledNode.setSpecifiedValues(new PropertyMap());
        } else if (root instanceof ElementNode) {
            styledNode.setSpecifiedValues(specifiedValues((ElementNode) root, stylesheet));
            for (Node child : ((ElementNode) root).getChildren()) {
                styledNode.getChildren().add(styleTree(child, stylesheet));
            }
        }

        return styledNode;
    }

    private boolean matches(ElementNode elem, SimpleSelector selector) {
        //Matches simple selector

        if(elem.getTagName().equals("div")) {
            System.out.println("here");
        }
        //TODO: change this matched function when whole engine is ready
        if(!selector.getTagName().equals("") && !selector.getTagName().equals(elem.getTagName())) {
            return false;
        }

        if(!selector.getId().equals("") && !selector.getId().equals(elem.id())) {
            return false;
        }

        HashSet<String> elemClasses = elem.classes();
        if(selector.getClasses().stream().anyMatch(c -> !elemClasses.contains(c))) {
            return false;
        }

        return true;
    }

    private MatchedRule matchRule(ElementNode elem, Rule rule) {
        CSSSelector r = rule.getSelectors().stream().filter(s -> matches(elem, (SimpleSelector) s)).findFirst().orElse(null);
        if(r == null) {
            return null;
        }
        MatchedRule mr = new MatchedRule();
        mr.setRule(rule);
        mr.setSpecifity(r.specifity());
        return mr;
    }

    private List<MatchedRule> matchRules(ElementNode elem, Stylesheet stylesheet) {
        return stylesheet.getRules().stream().map(r -> matchRule(elem,r)).filter(mr -> mr != null).collect(Collectors.toList());
    }

    private PropertyMap specifiedValues(ElementNode elem, Stylesheet stylesheet) {
        HashMap<String,Value> values = new HashMap<>();
        List<MatchedRule> rules = matchRules(elem,stylesheet);

        Comparator<MatchedRule> cmp = (a, b) -> {
            Specifity sA = a.getSpecifity();
            Specifity sB = b.getSpecifity();

            int comp = Integer.compare(sB.getA(), sA.getA());
            if(comp == 0) {
                comp = Integer.compare(sB.getB(), sA.getB());
                if(comp == 0) {
                    comp = Integer.compare(sB.getC(), sA.getC());
                }
            }
            return comp;
        };

        Collections.sort(rules, cmp);
        for (MatchedRule rule : rules) {
            for (Declaration dec : rule.getRule().getDeclarations()) {
                values.put(dec.getName(), dec.getValue());
            }
        }

        PropertyMap pm = new PropertyMap();
        pm.setMap(values);
        return pm;
    }

}

class PropertyMap{
    private Map<String, Value> map = new HashMap<>();

    public Map<String, Value> getMap() {
        return map;
    }

    public void setMap(Map<String, Value> map) {
        this.map = map;
    }
}

class MatchedRule{
    private Specifity specifity;
    private Rule rule;

    public Specifity getSpecifity() {
        return specifity;
    }

    public void setSpecifity(Specifity specifity) {
        this.specifity = specifity;
    }

    public Rule getRule() {
        return rule;
    }

    public void setRule(Rule rule) {
        this.rule = rule;
    }
}
class StyledNode {
    private Node node;
    private PropertyMap specifiedValues = new PropertyMap();
    private List<StyledNode> children = new ArrayList<>();

    public Node getNode() {
        return node;
    }

    public void setNode(Node node) {
        this.node = node;
    }

    public PropertyMap getSpecifiedValues() {
        return specifiedValues;
    }

    public void setSpecifiedValues(PropertyMap specifiedValues) {
        this.specifiedValues = specifiedValues;
    }

    public List<StyledNode> getChildren() {
        return children;
    }

    public void setChildren(List<StyledNode> children) {
        this.children = children;
    }

    public Value value(String str) {
        return specifiedValues.getMap().getOrDefault(str,null);
    }

    public Display getDisplay() {
        if(!(value("display") instanceof Keyword)){
            return Display.Inline;
        }
        Keyword k = (Keyword) value("display");
        if(k.getK().equals("block")) {
            return Display.Block;
        } else if (k.getK().equals("none")) {
            return Display.None;
        }
        return Display.Inline;
    }
}
