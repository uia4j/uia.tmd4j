package uia.tmd.ui.navi;

import java.util.LinkedHashMap;

import javax.swing.Icon;
import javax.swing.ImageIcon;

import uia.tmd.model.xml.ExecutorSpaceType;
import uia.tmd.ui.NaviPanel;

public class ExecutorSpaceNodeValue implements NodeValue {

    private ExecutorSpaceType es;

    public ExecutorSpaceNodeValue(ExecutorSpaceType es) {
        this.es = es;
    }

    @Override
    public String getName() {
        return "Executor";
    }

    @Override
    public Icon getIcon(boolean nodeSelected) {
        return new ImageIcon(NodeValue.class.getResource("/resources/images/executorSpace.png"));
    }

    @Override
    public boolean appendable(String taskName) {
        return false;
    }

    @Override
    public void appendNode(NaviPanel naviPanel) {

    }

    @Override
    public void select(NaviPanel naviPanel) {
        LinkedHashMap<String, String> props = new LinkedHashMap<String, String>();
        props.put("Node Type", "Executor Space");
        naviPanel.updateProperties(props);

        naviPanel.nodeSelected(this.es);
    }

    @Override
    public void delete(NaviPanel naviPanel) {

    }

    @Override
    public void expand(NaviPanel naviPanel) {

    }

    @Override
    public void execute(NaviPanel naviPanel) {

    }

    @Override
    public boolean edit(NaviPanel naviPanel) {
        return false;
    }

    @Override
    public String toString() {
        return "Exeuctor List";
    }

}
