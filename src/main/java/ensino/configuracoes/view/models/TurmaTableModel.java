/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.view.models;

import ensino.configuracoes.model.Turma;
import ensino.defaults.DefaultTableModel;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author nicho
 */
public class TurmaTableModel extends DefaultTableModel {

    public TurmaTableModel() {
        this(new ArrayList<Turma>());
    }
    
    public TurmaTableModel(List<Turma> lista) {
        super(lista, new String[] {
            "Nome"
        });
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Turma turma = (Turma) getRow(rowIndex);
        switch(columnIndex) {
            case 0: return turma.getNome();
            default: return null;
        }
    }
    
}
