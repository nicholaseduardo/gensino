/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.components;

import javax.swing.tree.DefaultMutableTreeNode;

/**
 *
 * @author nicho
 */
public class ToolTipTreeNode extends DefaultMutableTreeNode {

    private String toolTipText;
    
    public ToolTipTreeNode(Object userObject) {
        super(userObject);
    }
    
    public ToolTipTreeNode(Object userObject, String toolTipText) {
        super(userObject);
        this.toolTipText = toolTipText;
    }

    public String getToolTipText() {
        return toolTipText;
    }
}
