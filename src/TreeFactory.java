
import java.util.Random;
import java.util.function.Function;

public class TreeFactory {

    private static final Random random = new Random();

    public static Tree createTree(String type, double spreadability) {

        Function<Double, Tree> factory;

        switch (type.toUpperCase()) {
            case "BIRCH":
                factory = value -> new Birch(value);
                break;

            case "EUCALYPTUS":
                factory = value -> new Eucalyptus(value);
                break;

            case "SPRUCE":
                factory = value -> new Spruce(value);
                break;

            case "JUNGLE":
                factory = value -> new Jungle(value);
                break;

            default:
                factory = value -> new Oak(value);
                break;
        }

        return factory.apply(spreadability);
    }

    public static String randomType() {
        String[] types = {
            "OAK", "BIRCH", "EUCALYPTUS", "SPRUCE", "JUNGLE"
        };

        return types[random.nextInt(types.length)];
    }
}
