/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.components;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

/**
 *
 * @author santos
 */
public class GenTableModelListener implements TableModelListener {

    @Override
    public void tableChanged(TableModelEvent e) {
        int primeiraLinha = e.getFirstRow();
        int ultimaLinha = e.getLastRow();
        int coluna = e.getColumn();

        String acao = "";
        if (e.getType() == TableModelEvent.INSERT) {
            acao = "inseriu";
        } else if (e.getType() == TableModelEvent.UPDATE) {
            acao = "alterou";
        } else {
            acao = "deletou";
        }

        if (primeiraLinha == TableModelEvent.HEADER_ROW) {
            System.out.println("Você " + acao + " a META-DATA da tabela");
        } else {
            System.out.println("Você " + acao + " da linha " + primeiraLinha + " até " + ultimaLinha);
        }

        if (coluna == TableModelEvent.ALL_COLUMNS) {
            System.out.println("Você " + acao + " todas as colunas");
        } else {
            System.out.println("Você " + acao + " a coluna " + coluna);
        }
    }

}
