package ch.heig.amt.g4mify.model.view.badgeRule;

import ch.heig.amt.g4mify.model.view.badgeType.BadgeTypeSummary;

import java.util.List;

/**
 * Created by Frederic on 14.12.16.
 */
public class BadgeRuleDetail {

    public long id;

    public String condition;

    public List<String> depends;

    public BadgeTypeSummary grants;

}
