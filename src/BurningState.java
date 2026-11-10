
public class BurningState implements TreeState {

    @Override
    public void ignite(Tree tree, double severity) {
        // Already burning.
    }

    @Override
    public void advance(Tree tree) {
        tree.damage(30.0 * tree.getBurnIntensity());

    }

    @Override
    public String getName() {
        return "BURNING";
    }
}
