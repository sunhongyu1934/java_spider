package Id3;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "Node")
@XmlType(name = "Node")
public class Node
{
    /**
     * 到达此节点的特征值
     */
    public String lastFeatureValue;
    /**
     * 此节点的特征名称或答案
     */
    public String featureName;
    /**
     * 此节点的分类子节点
     */
    public List<Node> childrenNodeList = new ArrayList<Node>();

    @Override
    public String toString()
    {
        return "Node [lastFeatureValue=" + lastFeatureValue + ", featureName=" + featureName + ", childrenNodeList=" + childrenNodeList + "]";
    }

}
