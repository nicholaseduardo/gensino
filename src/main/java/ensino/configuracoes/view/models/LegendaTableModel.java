/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.view.models;

import ensino.configuracoes.model.Legenda;
import ensino.defaults.DefaultTableModel;
import java.awt.Color;
import java.util.List;

/**
 *
 * @author nicho
 */
public class LegendaTableModel extends DefaultTableModel {
     public LegendaTableModel(List<?> lista) {
        super(lista, new String[] {
            "Código", "Nome", "Letivo", "Informativo", "Cor"
        });
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Legenda legenda = (Legenda) getRow(rowIndex);
        
        switch(columnIndex) {
            case 0: return legenda.getId();
            case 1: return legenda.getNome();
            case 2: return legenda.isLetivo()?"Sim":"Não";
            case 3: return legenda.isInformativo()?"Sim":"Não";
            case 4: return legenda.getCor();
            default: return null;
        }
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        switch (columnIndex) {
            case 0: return Integer.class;
            case 1: return String.class;
            case 2: return String.class;
            case 3: return String.class;
            case 4: return Color.class;
            default:
                throw new IndexOutOfBoundsException("columnIndex out of bounds");
        }
        
    }
}
