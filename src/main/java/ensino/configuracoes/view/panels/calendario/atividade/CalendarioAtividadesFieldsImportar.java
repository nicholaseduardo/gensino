/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.view.panels.calendario.atividade;

import ensino.components.GenImportDialog;

/**
 *
 * @author nicho
 */
public class CalendarioAtividadesFieldsImportar extends GenImportDialog {

    public CalendarioAtividadesFieldsImportar() {
        super("<html><h1>Orientações para importação de atividades de calendário</h1></html>",
                "<html>Organize os dados das atividades em colunas: Mês, Dias, Atividade.<br/>"
                + "<ol><li>Mês = Esse campo é utilizado para identificar o mês de realização da atividade. São as 3 primeiras letras do mês em MAIÚSCULO.</li>"
                + "<li>Dias = Dia da atividade. Esse campo pode ter caracteres alfanuméricos. Por exemplo: 3, 3 a 10</li>"
                + "<li>Atividade = Descrição da atividade. Esse campo pode ter caracteres alfanuméricos.</li>"
                + "<li>Legenda = Código da legenda. Este código pode ser obtido no cadastro de legendas no sistema e deve ser informado o número de identificação da legenda.</li></ol>"
                + "Importante: A primeira linha é utilizada para colocar os nomes dos campos e sua sequência deve ser respeitada.<br/>"
                + "Salve o arquivo no formato *.CSV. Você pode escolhar o separador ',' ou ';'.</html>");
        String campos[] = {"mes", "dias", "atividades", "legenda"};
        super.setFileFields(campos);
        super.setVisible(true);
    }
}
