import java.math.BigDecimal;

// a cost the user adds, like "Flour, 2 kg at 45.00 per kg"
// DirectCost and IndirectCost both extend this, they only differ in the category
// (the analyzer uses that to split variable and fixed costs)
public abstract class CostItem {

    private String name;
    private double quantity;
    private String unit;      // unit code, e.g. "kg" (see Units)
    private double unitCost;  // price of ONE unit

    protected CostItem(String name, double quantity, String unit, double unitCost) {
        this.name = name;
        this.quantity = quantity;
        this.unit = unit;
        this.unitCost = unitCost;
    }

    public String getName()      { return name; }
    public double getQuantity()  { return quantity; }
    public String getUnit()      { return unit; }
    public double getUnitCost()  { return unitCost; }

    public void setName(String name)         { this.name = name; }
    public void setQuantity(double quantity) { this.quantity = Math.max(0, quantity); }
    public void setUnit(String unit)         { this.unit = unit; }
    public void setUnitCost(double unitCost) { this.unitCost = unitCost; }

    // quantity x unitCost (BigDecimal so the money math doesnt go weird)
    public BigDecimal getTotal() {
        return BigDecimal.valueOf(quantity).multiply(BigDecimal.valueOf(unitCost));
    }

    // "Direct Cost" or "Indirect Cost"
    public abstract String getCategory();

    // makes a copy of the item, the Duplicate button uses this
    public abstract CostItem copy();
}
