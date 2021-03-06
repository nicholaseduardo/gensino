/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.model;

import ensino.patterns.factory.BeanFactory;
import ensino.util.types.Periodo;
import java.util.HashMap;

/**
 *
 * @author nicho
 */
public class AtividadeFactory implements BeanFactory<Atividade> {

    private static AtividadeFactory instance = null;

    private AtividadeFactory() {
    }

    public static AtividadeFactory getInstance() {
        if (instance == null) {
            instance = new AtividadeFactory();
        }
        return instance;
    }

    /**
     * Cria um objeto da classe <code>Atividade</code>
     * 
     * Cria o objeto com base nos atributos informados nos
     * parâmetros
     * 
     * @param args
     * Os parâmetros são:
     * <ul>
     * <li><b><code>Integer</code> id</b> - identificador da atividade. Pode ser nulo</li>
     * <li><b><code>Periodo</code> periodo</b> instância da classe <code>Periodo</code>. Ela contem dados de data de início e fim.</li>
     * <li><b><code>String</code> descricao</b> descrição que identifique a atividade</li>
     * <li><b><code>Legenda</code> legenda</b> instância da classe <code>Legenda</code>. O objeto dessa classe identifica uma legenda visual da atividade no calendário</li>
     * <li><b><code>Calendario</code> calendario</b><i>Opcional</i>. Instância da classe <code>Calendario</code>. O objeto informa a qual calendario deve ser vinculada esta atividade</li>
     * </ul>
     * @return  Um objeto da classe <code>Atividade</code>
     */
    @Override
    public Atividade createObject(Object... args) {
        Atividade o = new Atividade();
        int i = 0;
        o.getId().setId((Long) args[i++]);
        o.setPeriodo((Periodo) args[i++]);
        o.setDescricao((String) args[i++]);
        o.setLegenda((Legenda) args[i++]);
        if (args.length > 4) {
            o.getId().setCalendario((Calendario) args[i++]);
        }
        return o;
    }

    @Override
    public Atividade getObject(HashMap<String, Object> p) {
        Atividade o = createObject(p.get("id"),p.get("periodo"),
                p.get("descricao"),p.get("legenda"));
        o.getId().setCalendario((Calendario) p.get("calendario"));
        return o;
    }

}
