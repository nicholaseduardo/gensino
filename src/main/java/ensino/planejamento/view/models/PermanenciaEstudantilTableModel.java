/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.planejamento.view.models;

import ensino.defaults.DefaultTableModel;
import ensino.planejamento.model.PermanenciaEstudantil;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author nicho
 */
public class PermanenciaEstudantilTableModel extends DefaultTableModel {

    public PermanenciaEstudantilTableModel() {
        this(new ArrayList<PermanenciaEstudantil>());
    }
    
    public PermanenciaEstudantilTableModel(List<PermanenciaEstudantil> lista) {
        super(lista, new String[] {
            "Atendimento"
        });
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        PermanenciaEstudantil permanenciaEstudantil = (PermanenciaEstudantil) getRow(rowIndex);
        switch(columnIndex) {
            case 0: return permanenciaEstudantil.getDescricao();
            default: return null;
        }
    }
    
}
