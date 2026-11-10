public class HealthyState implements TreeState {

    @Override
    public void ignite(Tree tree, double severity) {
        tree.startBurning(severity);
    }

    @Override
    public void advance(Tree tree) {
        // Healthy trees do not take fire damage.
    }

    @Override
    public String getName() {
        return "HEALTHY";
    }
}
