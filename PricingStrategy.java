import java.math.BigDecimal;

// a way to turn the unit cost into a selling price
// CostPlusStrategy and FixedPriceStrategy implement this
public interface PricingStrategy {

    // name for the dropdown
    String getName();

    // what the user typed: markup % (cost plus) or a peso price (fixed price)
    double getTargetValue();

    // selling price per unit, 2 decimals
    BigDecimal recommendedPrice(BigDecimal unitCost);

    // the formula as text
    String formula();

    // the formula with the real numbers
    String explain(BigDecimal unitCost, BigDecimal price);
}
