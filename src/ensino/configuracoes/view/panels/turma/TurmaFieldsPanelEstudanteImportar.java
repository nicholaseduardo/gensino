/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.view.panels.turma;

import ensino.components.GenImportDialog;

/**
 *
 * @author nicho
 */
public class TurmaFieldsPanelEstudanteImportar extends GenImportDialog {

    public TurmaFieldsPanelEstudanteImportar() {
        super("Orientações para importação de estudantes",
                "Organize os dados dos alunos em colunas: Nome e Número do R.A.\n"
                + "Nome = Nome do estudante. Esse campo pode ter caracteres alfanuméricos.\n"
                + "R.A. = Registro Acadêmico. Esse campo pode ter caracteres alfanuméricos.\n\n"
                + "Importante: A primeira linha é utilizada para colocar os nomes dos campos.\n\n"
                + "Salve o arquivo no formato *.CSV. Você pode escolhar o separador ',' ou ';'.");
        String campos[] = {"nome", "registro"};
        super.setFileFields(campos);
        super.setVisible(true);
    }

    public static void main(String args[]) {
        new TurmaFieldsPanelEstudanteImportar();
    }
}
