package cloud.terium.teriumapi.node;

import java.util.List;

public interface INodeProvider {

    /**
     * Get the name of the node
     *
     * @return String
     */
    List<INode> getAllNodes();

    /**
     *
     * @param name String
     * @return INode
     */
    INode getNodeByName(String name);
}