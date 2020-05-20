/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.planejamento.view.renderer;

import ensino.components.GenJLabel;
import ensino.configuracoes.model.Campus;
import ensino.configuracoes.model.Curso;
import ensino.configuracoes.model.UnidadeCurricular;
import ensino.planejamento.model.PlanoDeEnsino;
import ensino.util.types.TabsPlano;
import java.awt.Component;
import java.net.URL;
import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeCellRenderer;

/**
 *
 * @author santos
 */
public class ConfigTreeCellRenderer implements TreeCellRenderer {

    private GenJLabel label;

    public ConfigTreeCellRenderer() {
        label = new GenJLabel();
        label.resetFontSize(16);
    }

    @Override
    public Component getTreeCellRendererComponent(JTree jtree, Object value,
            boolean selected, boolean expanded, boolean leaf, int row,
            boolean hasFocus) {
        String pathFormat = "/img/%s.png", imgPath = "", nome = "";
        Object o = ((DefaultMutableTreeNode) value).getUserObject();
        if (o instanceof Campus) {
            imgPath = "university-icon-25px";
            nome = ((Campus) o).getNome();
        } else if (o instanceof Curso) {
            imgPath = "courses-icon-25px";
            nome = ((Curso) o).getNome();
        } else if (o instanceof UnidadeCurricular) {
            imgPath = "school-icon-25px";
            nome = ((UnidadeCurricular) o).getNome();
        } else if (o instanceof PlanoDeEnsino) {
            imgPath = "plano-icon-25px";
            PlanoDeEnsino p = (PlanoDeEnsino) o;
            nome = String.format("[%d] Plano de Ensino [%s | %s]",
                    p.getId(), p.getTurma().getNome(),
                    p.getPeriodoLetivo().getDescricao());
        } else if (o instanceof TabsPlano) {
            TabsPlano tp = (TabsPlano) o;
            switch (tp) {
                case IDEN:
                    imgPath = "Info-icon-25px";
                    break;
                case ESP:
                    imgPath = "target-icon-25px";
                    break;
                case DET:
                    imgPath = "Logos-Details-icon-25px";
                    break;
                case PAVA:
                    imgPath = "project-plan-icon-25px";
                    break;
                case HOR:
                    imgPath = "Apps-preferences-system-time-icon-25px";
                    break;
                case CON:
                    imgPath = "content-icon-25px";
                    break;
                case FREQ:
                    imgPath = "document-frequency-icon-25px";
                    break;
                case AVA:
                    imgPath = "Status-mail-task-icon-25px";
                    break;
                default:
                    imgPath = "plan-icon-25px";
            }
            nome = tp.toString();
        } else if (leaf) {
            imgPath = "view-button-25px";
            nome = "" + value;
        } else {
            imgPath = "directory-icon-25px";
            nome = "" + value;
        }

        if (!"".equals(imgPath)) {
            String path = String.format(pathFormat, imgPath);
            URL imageUrl = getClass().getResource(path);
            if (imageUrl != null) {
                label.setIcon(new ImageIcon(imageUrl));
            }
            label.setText(nome);
            return label;
        } else {
            label.setIcon(null);
            label.setText("" + value);
        }

        return label;
    }

}
