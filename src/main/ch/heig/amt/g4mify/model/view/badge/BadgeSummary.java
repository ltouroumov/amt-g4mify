package ch.heig.amt.g4mify.model.view.badge;

import ch.heig.amt.g4mify.model.view.badgeType.BadgeTypeDetail;
import ch.heig.amt.g4mify.model.view.badgeType.BadgeTypeSummary;

import java.sql.Timestamp;

/**
 * @author ldavid
 * @created 1/6/17
 */
public class BadgeSummary {

    public long level;

    public Timestamp awarded;

    public BadgeTypeSummary type;

}
