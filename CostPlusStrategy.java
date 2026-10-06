import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

// price = unit cost + markup %
public class CostPlusStrategy implements PricingStrategy {

    private static final MathContext MC = new MathContext(20, RoundingMode.HALF_UP);
    private final double markupPercent;

    public CostPlusStrategy(double markupPercent) {
        this.markupPercent = markupPercent;
    }

    public String getName()        { return "Cost Plus"; }
    public double getTargetValue() { return markupPercent; }

    public BigDecimal recommendedPrice(BigDecimal unitCost) {
        BigDecimal factor = BigDecimal.ONE.add(
                BigDecimal.valueOf(markupPercent).divide(BigDecimal.valueOf(100), MC), MC);
        return unitCost.multiply(factor, MC).setScale(2, RoundingMode.HALF_UP);
    }

    public String formula() {
        return "Unit Cost x (1 + Markup % / 100)";
    }

    public String explain(BigDecimal unitCost, BigDecimal price) {
        return Fmt.money(unitCost.doubleValue()) + " x (1 + " + Fmt.num(markupPercent) + " / 100) = "
                + Fmt.money(price.doubleValue());
    }
}
