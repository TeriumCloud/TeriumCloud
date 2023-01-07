package cloud.terium.teriumapi.node;

import java.util.List;
import java.util.Optional;

public interface INodeProvider {

    /**
     * @param name String
     * @return INode
     */
    Optional<INode> getNodeByName(String name);

    /**
     * Get the name of the node
     *
     * @return String
     */
    List<INode> getAllNodes();
}