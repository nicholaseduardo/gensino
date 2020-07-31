/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.view.models;

import ensino.configuracoes.controller.ConteudoController;
import ensino.configuracoes.model.Conteudo;
import ensino.configuracoes.model.ConteudoFactory;
import ensino.configuracoes.model.ConteudoId;
import ensino.configuracoes.model.UnidadeCurricular;
import ensino.patterns.factory.ControllerFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractListModel;
import javax.swing.ComboBoxModel;

/**
 *
 * @author nicho
 */
public class ConteudoComboBoxModel extends AbstractListModel implements ComboBoxModel {
    private ConteudoController col;
    private List<Conteudo> list;
    private UnidadeCurricular unidadeCurricular;
    
    private Conteudo selection;
    
    public ConteudoComboBoxModel(UnidadeCurricular uc) {
        this.unidadeCurricular = uc;
        
        initComponents();
    }
    
    private void initComponents() {
        try {
            col = ControllerFactory.createConteudoController();
            refresh();
        } catch (Exception ex) {
            Logger.getLogger(ConteudoComboBoxModel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void refresh() {
        refresh(null);
    }
    
    /**
     * Atualiza a lista dos conteúdos removendo o item informado por
     * parâmetro
     * 
     * @param except 
     */
    public void refresh(Conteudo except) {
        int index = 0;
        Conteudo c = ConteudoFactory.getInstance()
                .createObject(new ConteudoId(null, this.unidadeCurricular),
                        -1, " <-- Raiz Principal -->", null, 0);
        /**
         * Captura dos conteúdos sem conteudo superior vinculado
         */
        List<Conteudo> l = col.listar(this.unidadeCurricular);
        for(Conteudo o : l) {
            if (!o.hasParent()) {
                c.addChild(o);
            }
        }
        
        list = new ArrayList();
        list.add(c);
        list.addAll(1, l);
        if (except != null) {
            list.remove(except);
        }
        
        index = list.size() - 1;
        setSelectedItem(null);
        fireIntervalAdded(this, 0, index);
    }

    @Override
    public int getSize() {
        return list.size();
    }

    @Override
    public Object getElementAt(int index) {
        return list.get(index);
    }

    @Override
    public void setSelectedItem(Object anItem) {
        selection = (Conteudo)anItem;
    }

    @Override
    public Object getSelectedItem() {
        return selection;
    }
}
