package org.example.browser.Style;

import org.example.browser.CSS.*;
import org.example.browser.CSS.Values.Value;
import org.example.browser.HTML.ElementNode;
import org.example.browser.HTML.Node;
import org.example.browser.HTML.TextNode;

import java.util.*;
import java.util.stream.Collectors;

public class Style {

    public StyledNode styleTree(Node root, Stylesheet stylesheet) {
        StyledNode styledNode = new StyledNode();
        styledNode.setNode(root);
        if(root instanceof TextNode) {
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
            int x = 0;
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

    private HashMap<String, Value> specifiedValues(ElementNode elem, Stylesheet stylesheet) {
        HashMap<String, Value> values = new HashMap<>();
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

        return values;
    }

}


