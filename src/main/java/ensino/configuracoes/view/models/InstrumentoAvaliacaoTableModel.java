/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.view.models;

import ensino.configuracoes.model.InstrumentoAvaliacao;
import ensino.defaults.DefaultTableModel;
import java.util.List;

/**
 *
 * @author nicho
 */
public class InstrumentoAvaliacaoTableModel extends DefaultTableModel {

    public InstrumentoAvaliacaoTableModel(List<?> lista) {
        super(lista, new String[] {
            "Nome"
        });
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        InstrumentoAvaliacao instrumento = (InstrumentoAvaliacao) getRow(rowIndex);
        switch(columnIndex) {
            case 1: return instrumento.getNome();
            default: return null;
        }
    }
}
