package me.someonelove.nmsadapter;

import java.util.LinkedHashMap;
import java.util.Map;

public class VersionMatcher {

    /**
     * Kinda inspired from Rust's `match` expression.
     * Rule - The rule required to pass for the corresponding String to be used.
     * <p>
     * Entries closest to the head have priority.
     * e.g.
     * Version of bukkit running is 1.12
     * A field in SomeClass is 'a' in every version, but 'c' in 1.14
     * rules.put(new Rule(Comparative.EQUALS, 14), "c");
     * defaultRule = "a";
     * <p>
     * or
     * <p>
     * rules.put(new Rule(Comparative.LESS_THAN, 14), "a");
     * defaultRule = "c";
     */
    public final LinkedHashMap<Rule, String> rules = new LinkedHashMap<>();
    public final String defaultRule;

    public VersionMatcher(/* @NotNull */ String defaultName) {
        this.defaultRule = defaultName;
    }

    /**
     * Append an entry to the rules map and re-return this VersionMatcher, for chained `then()` calls.
     * @param rule The version rule
     * @param name The name to use if this rule matches
     */
    public VersionMatcher then(Rule rule, String name) {
        rules.put(rule, name);
        return this;
    }

    protected String match(int thisVersion) {
        for (Map.Entry<Rule, String> rulePair : rules.entrySet()) {
            if (rulePair.getKey().compare(thisVersion)) {
                return rulePair.getValue();
            }
        }
        return defaultRule;
    }

    public static class Rule {
        private Comparative comparative;
        private int base;

        public Rule(Comparative comparative, int base) {
            this.base = base;
            this.comparative = comparative;
        }

        private boolean compare(int v) {
            switch (this.comparative) {
                case EQUAL:
                    return v == base;
                case LESS_OR:
                    return v <= base;
                case LESS_THAN:
                    return v < base;
                case GREATER_OR:
                    return v >= base;
                case GREATER_THAN:
                    return v > base;
            }
            return false;
        }
    }

    public enum Comparative {
        EQUAL, // ==
        LESS_THAN, // <
        GREATER_THAN, // >
        GREATER_OR, // >=
        LESS_OR; // <=
    }

}
