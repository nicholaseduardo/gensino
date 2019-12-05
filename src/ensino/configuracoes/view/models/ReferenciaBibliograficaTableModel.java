/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.view.models;

import ensino.configuracoes.model.ReferenciaBibliografica;
import ensino.defaults.DefaultTableModel;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author nicho
 */
public class ReferenciaBibliograficaTableModel extends DefaultTableModel {

    public ReferenciaBibliograficaTableModel() {
        this(new ArrayList<ReferenciaBibliografica>());
    }
    
    public ReferenciaBibliograficaTableModel(List<ReferenciaBibliografica> lista) {
        super(lista, new String[] {
            "Referência bibliográfica"
        });
    }
    
    /**
     * Remoção lógica
     * @param row 
     */
    @Override
    public void removeRow(int row) {
        ReferenciaBibliografica o = (ReferenciaBibliografica) lista.get(row);
        o.delete();
        updateRow(row, o);
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        ReferenciaBibliografica referencia = (ReferenciaBibliografica) getRow(rowIndex);
        switch(columnIndex) {
            case 0: return referencia.getSequencia();
            case 1: return referencia.getTipoDescricao();
            case 2: return referencia.getBibliografia().getTitulo();
            case 3: return referencia.getBibliografia().getAutor();
            default: return null;
        }
    }
    
}
