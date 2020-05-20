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
        super("<html><h1>Orientações para importação de estudantes</h1></html>",
                "<html>Organize os dados dos alunos em colunas: Id, Nome, Número do R.A., Data de Ingresso e Situação.<br/>"
                + "<ol><li>Id = Id do estudante. Esse campo é utilizado para identificar a sequência de cadastro do estudante.</li>"
                + "<li>Nome = Nome do estudante. Esse campo pode ter caracteres alfanuméricos.</li>"
                + "<li>R.A. = Registro Acadêmico. Esse campo pode ter caracteres alfanuméricos.</li>"
                + "<li>Ingresso = Data do Ingresso no curso. Esse campo pode ter data no formato dd/MM/yyyy.</li>"
                + "<li>Situacao = Situação do Acadêmico. Esse campo deve ser preenchido com: 'EC', 'RN', 'RF', 'DE'.<br/>"
                + "<b>Legenda:</b><br/>"
                + "    <b>EC:</b> Em curso<br/>"
                + "    <b>RN:</b> Reprovado por Nota<br/>"
                + "    <b>RF:</b> Reprovado por Falta<br/>"
                + "    <b>DE:</b> Desligado do curso</li></ol>"
                + "Importante: A primeira linha é utilizada para colocar os nomes dos campos e sua sequência deve ser respeitada.<br/>"
                + "Salve o arquivo no formato *.CSV. Você pode escolhar o separador ',' ou ';'.</html>");
        String campos[] = {"id", "nome", "registro", "ingresso", "situacao"};
        super.setFileFields(campos);
        super.setVisible(true);
    }
}
