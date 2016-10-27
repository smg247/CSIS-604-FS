package filesystems.safs;

import filesystems.safs.storageRepresentations.Node;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Controller {
    public static final Controller CONTROLLER = new Controller();
    private List<Node> nodes;
    private boolean testEnvironment;


    public void initialize(boolean testEnvironment, String... nodeLocations) {
        if (nodes == null) {
            if (nodeLocations != null && nodeLocations.length >= 1) {
                nodes = new ArrayList<>();
                for (String nodeLocation : nodeLocations) {
                    nodes.add(new Node(nodeLocation));
                }

                this.testEnvironment = testEnvironment;
            } else {
                throw new IllegalArgumentException("No Node locations were provided when initializing the Controller.");
            }
        } else {
            throw new IllegalStateException("Attempted to initialize the Controller when it was already initialized.");
        }
    }

    public Node determineNodeToReceiveNewFile() {
        Collections.sort(nodes);
        return nodes.get(0);
    }

    public boolean isTestEnvironment() {
        return testEnvironment;
    }
}
