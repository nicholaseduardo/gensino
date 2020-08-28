/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.view.models;

import ensino.configuracoes.model.Curso;
import ensino.defaults.DefaultTableModel;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author nicho
 */
public class CursoTableModel extends DefaultTableModel {

    public CursoTableModel() {
        this(new ArrayList<Curso>());
    }
    
    public CursoTableModel(List<Curso> lista) {
        super(lista, new String[] {
            "Nome", "Ações"
        });
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Curso curso = (Curso) getRow(rowIndex);
        switch(columnIndex) {
            case 0: return curso;
            default: return null;
        }
    }
    
}
