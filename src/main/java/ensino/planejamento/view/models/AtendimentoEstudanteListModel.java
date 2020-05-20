/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.planejamento.view.models;

import ensino.planejamento.model.AtendimentoEstudante;
import java.util.ArrayList;
import java.util.List;
import javax.swing.AbstractListModel;

/**
 *
 * @author nicho
 */
public class AtendimentoEstudanteListModel extends AbstractListModel<AtendimentoEstudante> {

    private List<AtendimentoEstudante> list;
    
    public AtendimentoEstudanteListModel() {
        this(new ArrayList<AtendimentoEstudante>());
    }
    
    public AtendimentoEstudanteListModel(List<AtendimentoEstudante> lista) {
        super();
        this.list = lista;
    }

    @Override
    public int getSize() {
        return list.size();
    }

    @Override
    public AtendimentoEstudante getElementAt(int index) {
        return list.get(index);
    }
    
    public List<AtendimentoEstudante> getData() {
        return list;
    }
    
}
