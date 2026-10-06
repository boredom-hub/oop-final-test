// cost that goes straight into making the product (materials, ingredients), changes with output
public class DirectCost extends CostItem {

    public DirectCost(String name, double quantity, String unit, double unitCost) {
        super(name, quantity, unit, unitCost);
    }

    public String getCategory() {
        return "Direct Cost";
    }

    public CostItem copy() {
        return new DirectCost(getName(), getQuantity(), getUnit(), getUnitCost());
    }
}
