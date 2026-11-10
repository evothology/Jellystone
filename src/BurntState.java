public class BurntState implements TreeState {

    @Override
    public void ignite(Tree tree, double severity) {
        // Burnt trees cannot ignite again.
    }

    @Override
    public void advance(Tree tree) {
        // Burnt trees no longer take damage.
    }

    @Override
    public String getName() {
        return "BURNT";
    }
}
