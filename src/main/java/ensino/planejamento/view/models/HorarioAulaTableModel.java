/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.planejamento.view.models;

import ensino.defaults.DefaultTableModel;
import ensino.patterns.factory.DaoFactory;
import ensino.planejamento.model.HorarioAula;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author nicho
 */
public class HorarioAulaTableModel extends DefaultTableModel<HorarioAula> {

    public HorarioAulaTableModel() {
        this(new ArrayList<HorarioAula>());
    }

    public HorarioAulaTableModel(List<HorarioAula> lista) {
        super(lista, new String[]{
            "Horários das aulas"
        });
    }

    /**
     * Remoção lógica
     *
     * @param row
     */
    @Override
    public void removeRow(int row) {
        if (DaoFactory.isXML()) {
            HorarioAula o = (HorarioAula) lista.get(row);
            o.delete();
            updateRow(row, o);
        } else {
            super.removeRow(row);
        }
    }

    @Override
    public List<HorarioAula> getData() {
        return (List<HorarioAula>) super.getData();
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        HorarioAula obj = (HorarioAula) getRow(rowIndex);
        switch (columnIndex) {
            case 0:
                return obj.getHorario();
            default:
                return null;
        }
    }

}
