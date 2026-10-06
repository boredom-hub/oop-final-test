// overhead cost (rent, utilities), counts as a fixed cost
public class IndirectCost extends CostItem {

    public IndirectCost(String name, double quantity, String unit, double unitCost) {
        super(name, quantity, unit, unitCost);
    }

    public String getCategory() {
        return "Indirect Cost";
    }

    public CostItem copy() {
        return new IndirectCost(getName(), getQuantity(), getUnit(), getUnitCost());
    }
}
