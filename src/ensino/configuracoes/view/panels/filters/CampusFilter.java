/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.view.panels.filters;

import ensino.components.GenJRadioButton;
import ensino.configuracoes.controller.CampusController;
import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import ensino.configuracoes.model.Campus;
import ensino.defaults.DefaultFormPanel;
import ensino.helpers.GridLayoutHelper;
import ensino.patterns.factory.ControllerFactory;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

/**
 *
 * @author nicho
 */
public class CampusFilter extends JPanel {

    private ButtonGroup campusGroup;
    private Campus selectedCampus;
    private DefaultFormPanel parentPanel;

    public CampusFilter(DefaultFormPanel parent) {
        this(parent, null);
    }
    
    public CampusFilter(DefaultFormPanel parent, Campus campus) {
        super();
        parentPanel = parent;
        this.selectedCampus = campus;
        initComponents();
    }

    private void initComponents() {
        try {
            campusGroup = new ButtonGroup();
            Integer maxCampusColumn = 3, currColumn = 0, currLine = 0;

            setLayout(new GridBagLayout());
            GridBagConstraints cfilter = new GridBagConstraints();
            CampusController campusCol = ControllerFactory.createCampusController();
            
            List listaCampus = campusCol.listar();
            for (int i = 0; i < listaCampus.size(); i++) {
                Campus c = (Campus) listaCampus.get(i);
                GenJRadioButton rb = new GenJRadioButton(c.getNome());
                rb.setEnabled(selectedCampus == null);
                rb.setObjectValue(c);
                if (c.equals(selectedCampus)) {
                    rb.setSelected(true);
                }
                campusGroup.add(rb);
                GridLayoutHelper.set(cfilter, currColumn++, currLine);
                if (Objects.equals(currColumn, maxCampusColumn)) {
                    currLine++;
                    currColumn = 0;
                }
                add(rb, cfilter);
                rb.addActionListener(e -> {
                    if (e.getSource() instanceof GenJRadioButton) {
                        GenJRadioButton selectedRadio = (GenJRadioButton) e.getSource();
                        selectedCampus = (Campus) selectedRadio.getObjectValue();

                        parentPanel.reloadTableData();
                    }
                });
            };
            
        } catch (IOException | ParserConfigurationException | TransformerException ex) {
            Logger.getLogger(CampusFilter.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public Campus getSelectedCampus() {
        return this.selectedCampus;
    }

    public void setSelectedCampus(Campus campus) {
        this.selectedCampus = campus;
    }

}
