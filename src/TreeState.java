public interface TreeState {
    void ignite(Tree tree, double severity);
    void advance(Tree tree);
    String getName();
}
