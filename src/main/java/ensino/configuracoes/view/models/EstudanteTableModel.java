/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.view.models;

import ensino.configuracoes.model.Estudante;
import ensino.defaults.DefaultTableModel;
import ensino.helpers.DateHelper;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author nicho
 */
public class EstudanteTableModel extends DefaultTableModel<Estudante> {

    public EstudanteTableModel() {
        this(new ArrayList<Estudante>());
    }
    
    public EstudanteTableModel(List<Estudante> lista) {
        super(lista, new String[] {
            "Estudante"
        });
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Estudante estudante = (Estudante) getRow(rowIndex);
        switch(columnIndex) {
            case 0: return estudante.getId().getId();
            case 1: return estudante.getNome();
            case 2: return estudante.getRegistro();
            case 3: return DateHelper.dateToString(estudante.getIngresso(), "dd/MM/yyyy");
            case 4: return estudante.getSituacaoEstudante() != null ? estudante.getSituacaoEstudante().toString() : "-";
            default: return null;
        }
    }
    
}
