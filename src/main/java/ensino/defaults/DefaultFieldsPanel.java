/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.defaults;

import ensino.components.GenJPanel;
import java.awt.Color;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.HashMap;
import javax.swing.BorderFactory;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

/**
 *
 * @author nicho
 */
public abstract class DefaultFieldsPanel extends GenJPanel implements
        ComponentListener {
    
    public static final int INSERT_STATUS_PANEL = 0;
    public static final int UPDATE_STATUS_PANEL = 1;
    public static final int VIEW_STATUS_PANEL = 2;
    /**
     * Atributo utilizado para identificar o status do formulário.
     * Este atributo identifica se o painel está com status de 
     * inclusão ou de alteração/atualização
     */
    private int statusPanel;
    private String imageSourceAdd;
    private String imageSourceUpdate;
    private String imageSourceNew;
    private String imageSourceDel;
    private String imageSourceClear;
    private String imageSourceGenerator;
    private String imageSourceImport;
    
    public DefaultFieldsPanel() {
        this("Ficha de cadastro/alteração");
    }
    
    public DefaultFieldsPanel(String titleBorder) {
        super();
        super.addComponentListener(this);
        super.setBorder(createTitleBorder(titleBorder));
        
        imageSourceAdd = String.format("/img/%s", "add-icon-png-25px.png");
        imageSourceUpdate = String.format("/img/%s", "update-button-25px.png");
        imageSourceNew = String.format("/img/%s", "view-button-25px.png");
        imageSourceDel = String.format("/img/%s", "del-button-png-25px.png");
        imageSourceClear = String.format("/img/%s", "clear-icon-25px.png");
        imageSourceGenerator = String.format("/img/%s", "gear-icon-25px.png");
        imageSourceImport = String.format("/img/%s", "import-button-25px.png");
    }

    /**
     * Recupera o status do painel.
     * Identifica qual o status atual do painel.<br/>
     * <ul>
     *      <li><code>INSERT_STATUS_PANEL</li> indica que o formulário está
     *          com o status de inclusão</li>
     *      <li><code>UPDATE_STATUS_PANEL</li> indica que o formulário está
     *          com o status de atualização/alteração</li>
     *      <li><code>VIEW_STATUS_PANEL</li> indica que o formulário está
     *          com o status de visualização</li>
     * </ul>
     * 
     * @return 
     */
    public int getStatusPanel() {
        return statusPanel;
    }

    public String getImageSourceAdd() {
        return imageSourceAdd;
    }

    public String getImageSourceUpdate() {
        return imageSourceUpdate;
    }

    public String getImageSourceNew() {
        return imageSourceNew;
    }

    public String getImageSourceDel() {
        return imageSourceDel;
    }

    public String getImageSourceClear() {
        return imageSourceClear;
    }

    public String getImageSourceGenerator() {
        return imageSourceGenerator;
    }

    public String getImageSourceImport() {
        return imageSourceImport;
    }
    
    /**
     * Atualiza o status do painel.
     * @param statusPanel   DefaultFieldsPanel.INSERT_STATUS_PANEL ou
     *                      UPDATE_STATUS_PANEL ou VIEW_STATUS_PANEL
     */
    public void setStatusPanel(int statusPanel) {
        this.statusPanel = statusPanel;
    }
    
    
    /**
     * Recupera os valores informados nos campos do painel
     * @return 
     */
    public abstract HashMap<String, Object> getFieldValues();
    /**
     * Atribui valores aos campos adicionados ao painel
     * @param mapValues 
     */
    public abstract void setFieldValues(HashMap<String, Object> mapValues);
    public abstract void setFieldValues(Object object);
    /**
     * Verifica se os campos estão corretamente preenchidos
     * @return 
     */
    public abstract boolean isValidated();
    /**
     * Limpa os campos do painel
     */
    public abstract void clearFields();
    /**
     * Habilita/desabilita os campos do painel
     * @param active 
     */
    public abstract void enableFields(boolean active);
    /**
     * Indica qual será o componente que receberá o foco quando
     * a janela for mostrada no evento componentShown()
     */
    public abstract void initFocus();
    
    /**
     * Cria um objeto da classe <code>Border</code>
     * @param title     Texto que será colocado como título da borda
     * @return 
     */
    protected Border createTitleBorder(String title) {
        return BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.BLACK), title, 
                TitledBorder.LEFT, TitledBorder.TOP);
    }
    
    @Override
    public void componentResized(ComponentEvent e) {

    }

    @Override
    public void componentMoved(ComponentEvent e) {

    }

    @Override
    public void componentShown(ComponentEvent e) {
        initFocus();
    }

    @Override
    public void componentHidden(ComponentEvent e) {

    }
}
