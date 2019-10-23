/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.planejamento.view.models;

import ensino.defaults.DefaultTableModel;
import ensino.planejamento.model.Avaliacao;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author nicho
 */
public class AvaliacaoTableModel extends DefaultTableModel {

    public AvaliacaoTableModel() {
        this(new ArrayList<Avaliacao>());
    }

    public AvaliacaoTableModel(List<Avaliacao> lista) {
        super(lista, new String[]{
            "Estudante", "Data", "Horário", "Registro"
        });
    }
    
    public AvaliacaoTableModel(List<Object> lista, String[] columns) {
        super(lista, columns);
        
    }

    @Override
    public List<Object> getData() {
        return (List<Object>) super.getData();
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Object obj = getRow(rowIndex);
        if (obj instanceof List) {
            List l = (List) obj;
            if (l.size() > columnIndex) {
                Object inObj = l.get(columnIndex);
                return inObj.toString();
            }
        }
        return null;
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        if (aValue instanceof String) {
            String sValue = (String)aValue;
            Double value = Double.parseDouble(sValue);
            /**
             * Recupera o objeto na linha e coluna informada para atualizar
             * o valor da nota
             */
            Object oRow = lista.get(rowIndex);
            if (oRow instanceof List) {
                List lRow = (List) oRow;
                /**
                 * Cada coluna da linha é um objeto da classe Avaliacao
                 */
                Object oCol = lRow.get(columnIndex);
                if (oCol instanceof Avaliacao) {
                    Avaliacao avaliacao = (Avaliacao) oCol;
                    /**
                     * Atualiza o valor da nota
                     */
                    avaliacao.setNota(value);
                }
            }
        }
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return columnIndex > 0;
    }

}
