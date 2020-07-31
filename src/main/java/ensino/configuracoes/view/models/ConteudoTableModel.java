/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.view.models;

import ensino.configuracoes.model.Conteudo;
import ensino.defaults.DefaultTableModel;
import java.util.List;

/**
 *
 * @author nicho
 */
public class ConteudoTableModel extends DefaultTableModel<Conteudo> {
    
    public ConteudoTableModel(List<Conteudo> lista) {
        super(lista, new String[] {
            "Nome", "Ações"
        });
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Conteudo conteudo = (Conteudo) getRow(rowIndex);
        switch(columnIndex) {
            case 0: return conteudo;
            default: return null;
        }
    }
    
}
