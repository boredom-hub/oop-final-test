import java.util.ArrayList;
import java.util.List;

// everything the user entered about one product
// one object is made at the start and passed from frame to frame
public class ProductPricingModel {

    private String productName = "";
    private PricingStrategy strategy = new CostPlusStrategy(0);
    private int plannedProductionQuantity;
    private int forecastedSalesQuantity;
    private final List<CostItem> directCosts = new ArrayList<CostItem>();
    private final List<CostItem> indirectCosts = new ArrayList<CostItem>();

    public String getProductName()              { return productName; }
    public PricingStrategy getStrategy()        { return strategy; }
    public int getPlannedProductionQuantity()   { return plannedProductionQuantity; }
    public int getForecastedSalesQuantity()     { return forecastedSalesQuantity; }
    public List<CostItem> getDirectCosts()      { return directCosts; }
    public List<CostItem> getIndirectCosts()    { return indirectCosts; }

    public void setProductName(String productName)          { this.productName = productName; }
    public void setStrategy(PricingStrategy strategy)       { this.strategy = strategy; }
    public void setPlannedProductionQuantity(int quantity)  { this.plannedProductionQuantity = quantity; }
    public void setForecastedSalesQuantity(int quantity)    { this.forecastedSalesQuantity = quantity; }

    public boolean hasAnyCosts() {
        return !directCosts.isEmpty() || !indirectCosts.isEmpty();
    }

    // list of problems with the data, empty list = ok
    public List<String> validate() {
        List<String> problems = new ArrayList<String>();
        if (productName == null || productName.trim().isEmpty()) {
            problems.add("Product name is required.");
        }
        if (plannedProductionQuantity <= 0) {
            problems.add("Production quantity must be greater than 0.");
        }
        if (forecastedSalesQuantity < 0) {
            problems.add("Sales quantity cannot be negative.");
        }
        if (forecastedSalesQuantity > plannedProductionQuantity) {
            problems.add("Sales quantity cannot exceed production quantity.");
        }
        return problems;
    }
}
