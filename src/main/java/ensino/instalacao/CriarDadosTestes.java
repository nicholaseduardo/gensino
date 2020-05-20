/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.instalacao;

import ensino.configuracoes.controller.EstudanteController;
import ensino.configuracoes.model.Estudante;
import ensino.patterns.factory.ControllerFactory;
import ensino.planejamento.controller.AvaliacaoController;
import ensino.planejamento.controller.DiarioFrequenciaController;
import ensino.planejamento.model.Avaliacao;
import ensino.planejamento.model.DiarioFrequencia;
import ensino.util.types.Presenca;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author santos
 */
public class CriarDadosTestes {

    public static int getRandomNumberInRange(int min, int max) {

        if (min >= max) {
            throw new IllegalArgumentException("max must be greater than min");
        }

        Random r = new Random();
        return r.nextInt((max - min) + 1) + min;
    }

    public static void main(String args[]) {
        try {
            EstudanteController col = ControllerFactory.createEstudanteController();
            AvaliacaoController colAval = ControllerFactory.createAvaliacaoController();
            DiarioFrequenciaController colDf = ControllerFactory.createDiarioFrequenciaController();

            Random random = new Random();
            /**
             * Atualiza as notas de todos os estudantes que já tem avaliações
             */
            List<Estudante> lista = col.listar();
            Integer ne = lista.size();
            for (int i = 0; i < ne; i++) {
                Estudante e = lista.get(i);
                /**
                 * Recupera as avaliações
                 */
                List<Avaliacao> la = e.getAvaliacoes();
                Integer na = la.size();
                for (int j = 0; j < na; j++) {
                    Avaliacao a = la.get(j);
                    Double nota = 0.0;
                    if (!a.getId().getPlanoAvaliacao().getEtapaEnsino().isRecuperacao()) {
                        nota = random.nextDouble() * 10;
                    }
                    a.setNota(nota);
                    colAval.salvar(a);

                    System.out.printf("Estudantes: %.2f%% \t Avaliacoes: %.2f %%\r\n",
                            (i + 1) / ne.doubleValue() * 100, (j + 1) / na.doubleValue() * 100);
                }

                /**
                 * Recupera as frequências
                 */
                List<DiarioFrequencia> ldf = e.getFrequencias();
                Integer ndf = ldf.size();
                for (int j = 0; j < ndf; j++) {
                    DiarioFrequencia df = ldf.get(j);
                    df.setPresenca(Presenca.of(getRandomNumberInRange(0, 2)));

                    colDf.salvar(df);

                    System.out.printf("Estudantes: %.2f%% \t Presenças: %.2f %%\r\n",
                            (i + 1) / ne.doubleValue() * 100, (j + 1) / ndf.doubleValue() * 100);
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(CriarDadosTestes.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
