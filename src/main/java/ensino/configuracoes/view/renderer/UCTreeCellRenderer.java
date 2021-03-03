/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.view.renderer;

import ensino.components.GenJLabel;
import ensino.configuracoes.model.Conteudo;
import ensino.configuracoes.model.ObjetivoUC;
import ensino.configuracoes.model.ObjetivoUCConteudo;
import ensino.configuracoes.model.SemanaLetiva;
import java.awt.Color;
import java.awt.Component;
import java.net.URL;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeCellRenderer;

/**
 *
 * @author santos
 */
public class UCTreeCellRenderer implements TreeCellRenderer {

    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value,
            boolean selected, boolean expanded, boolean leaf, int row,
            boolean hasFocus) {
        String pathFormat = "/img/%s.png", imgPath = "", descricao = "";
        String html = "<html>%s</html>";

        Object o = ((DefaultMutableTreeNode) value).getUserObject();
        if (o instanceof Conteudo) {
            descricao = ((Conteudo) o).getDescricao();
        } else if (o instanceof ObjetivoUCConteudo) {
            descricao = ((ObjetivoUCConteudo) o).getConteudo().getDescricao();
        } else if (o instanceof ObjetivoUC) {
            imgPath = "target-icon-25px";
            descricao = ((ObjetivoUC) o).getDescricao();
        } else if (o instanceof SemanaLetiva) {
            imgPath = "calendar-image-png-25px";
            descricao = ((SemanaLetiva) o).toString();
        } else {
            descricao = (String) o;
        }

        if ("".equals(imgPath)) {
            if (leaf) {
                imgPath = "view-button-25px";
            } else {
                imgPath = "directory-icon-25px";
            }
        }

        GenJLabel label = new GenJLabel();
        label.resetFontSize(16);

        String path = String.format(pathFormat, imgPath);
        URL imageUrl = getClass().getResource(path);
        if (imageUrl != null) {
            label.setIcon(new ImageIcon(imageUrl));
        }
        label.setText(String.format(html, descricao.replaceAll("\\n", "<br/>")));

        JPanel panel = new JPanel();
        panel.add(label);
        panel.setOpaque(true);
        if (selected) {
            panel.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1, true));
            panel.setBackground(new Color(150, 150, 150, 70));
        } else {
            panel.setBackground(Color.WHITE);
        }

        return panel;
    }

}
