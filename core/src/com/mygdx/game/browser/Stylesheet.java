package com.mygdx.game.browser;


import java.util.ArrayList;
import java.util.List;

public class Stylesheet {
    private List<Rule> rules = new ArrayList<>();

    public List<Rule> getRules() {
        return rules;
    }

    public void setRules(List<Rule> rules) {
        this.rules = rules;
    }
}
