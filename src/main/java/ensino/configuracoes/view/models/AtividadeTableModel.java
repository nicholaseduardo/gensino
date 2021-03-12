/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.view.models;

import ensino.configuracoes.model.Atividade;
import ensino.defaults.DefaultTableModel;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author nicho
 */
public class AtividadeTableModel extends DefaultTableModel<Atividade> {

    public AtividadeTableModel() {
        this(new ArrayList());
    }
    
    public AtividadeTableModel(List<Atividade> lista) {
        super(lista, new String[] {
            "Descrição", "Ações"
        });
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Atividade atividade = (Atividade) getRow(rowIndex);
        switch(columnIndex) {
            case 0: return atividade;
            default: return null;
        }
    }
}
