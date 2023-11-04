package org.example.browser.Style;


import org.example.browser.CSS.Rule;
import org.example.browser.CSS.Specifity;

public class MatchedRule{
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
