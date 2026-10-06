import java.math.BigDecimal;
import java.math.RoundingMode;

// selling price is just the price the user typed in
//
// NOTE: the web app (appweb) did unit cost + typed value for "fixed price", even though its help
// text said "enter the fixed price for your product". here the typed value IS the price.
// to get the web app behavior back, change the return line of recommendedPrice() to
// unitCost.add(BigDecimal.valueOf(price)).setScale(2, RoundingMode.HALF_UP)
public class FixedPriceStrategy implements PricingStrategy {

    private final double price;

    public FixedPriceStrategy(double price) {
        this.price = price;
    }

    public String getName()        { return "Fixed Price"; }
    public double getTargetValue() { return price; }

    public BigDecimal recommendedPrice(BigDecimal unitCost) {
        return BigDecimal.valueOf(price).setScale(2, RoundingMode.HALF_UP);
    }

    public String formula() {
        return "Selling Price = the fixed price you entered";
    }

    public String explain(BigDecimal unitCost, BigDecimal price) {
        return "Fixed price = " + Fmt.money(price.doubleValue());
    }
}
