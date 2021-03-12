/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.view.models;

import ensino.configuracoes.model.PeriodoLetivo;
import ensino.defaults.DefaultTableModel;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author nicho
 */
public class PeriodoLetivoTableModel extends DefaultTableModel<PeriodoLetivo> {

    public PeriodoLetivoTableModel() {
        this(new ArrayList());
    }

    public PeriodoLetivoTableModel(List<PeriodoLetivo> lista) {
        super(lista, new String[]{
            "Descrição", "Ações"
        });
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        PeriodoLetivo periodoLetivo = getRow(rowIndex);
        switch (columnIndex) {
            case 0:
                return periodoLetivo;
            default:
                return null;
        }
    }
}
