/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.view.models;

import ensino.configuracoes.model.Bibliografia;
import ensino.defaults.DefaultTableModel;
import java.util.List;

/**
 *
 * @author nicho
 */
public class BibliografiaTableModel extends DefaultTableModel {
     public BibliografiaTableModel(List<?> lista) {
        super(lista, new String[] {
            "Código", "Título", "Autor"
        });
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Bibliografia bibliografia = (Bibliografia) getRow(rowIndex);
        
        switch(columnIndex) {
            case 0: return bibliografia.getId();
            case 1: return bibliografia.getTitulo();
            case 2: return bibliografia.getAutor();
            default: return null;
        }
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        switch (columnIndex) {
            case 0: return Integer.class;
            case 1: return String.class;
            case 2: return String.class;
            default:
                throw new IndexOutOfBoundsException("columnIndex out of bounds");
        }
        
    }
}
