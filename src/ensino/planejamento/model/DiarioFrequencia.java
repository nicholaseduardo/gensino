/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.planejamento.model;

import ensino.configuracoes.model.Estudante;
import ensino.defaults.XMLInterface;
import ensino.util.Presenca;
import java.util.HashMap;
import java.util.Objects;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author nicho
 */
public class DiarioFrequencia implements XMLInterface {
    /**
     * Atributo utilizado para identificar de qual diário é o registro
     * desta frequência.
     */
    private Diario diario;
    /**
     * Atributo utilizado para registrar a presença ou a falta do estudante.
     *
     * A presença pode ser:<br/>
     * <ul>
     * <li>PRESENTE</li>
     * <li>FALTA</li>
     * </ul>
     */
    private Presenca presenca;
    /**
     * Atributo utilizado para identificar o estudante cuja frequência está
     * sendo registrada.
     */
    private Estudante estudante;
    
    public DiarioFrequencia() {
        presenca = Presenca.PONTO;
    }
    
    public DiarioFrequencia(Presenca presenca, Estudante estudante) {
        this.presenca = presenca;
        this.estudante = estudante;
    }
    
    public DiarioFrequencia(Element e, Diario diario) {
        this.presenca = Presenca.of(e.getAttribute("presenca"));
        this.diario = diario;
        if (e.hasChildNodes()) {
            NodeList nodeList = e.getChildNodes();
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node child = nodeList.item(i);
                if ("estudante".equals(child.getNodeName())) {
                    estudante = new Estudante((Element) child);
                    if (diario != null) {
                        estudante.setTurma(diario.getPlanoDeEnsino().getTurma());
                    }
                }
            }
        }
    }
    
    public DiarioFrequencia(HashMap<String, Object> params) {
        this((Presenca) params.get("presenca"),
                (Estudante) params.get("estudante"));
        this.diario = (Diario) params.get("diario");
    } 

    @Override
    public Node toXml(Document doc) {
        Element e = doc.createElement("diarioFrequencia");
        e.setAttribute("presenca", presenca == null ? " " :  presenca.getValue());
        e.appendChild(estudante.toXml(doc));
        
        return e;
    }

    @Override
    public HashMap<String, Object> getKey() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 79 * hash + Objects.hashCode(this.diario);
        hash = 79 * hash + Objects.hashCode(this.presenca);
        hash = 79 * hash + Objects.hashCode(this.estudante);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final DiarioFrequencia other = (DiarioFrequencia) obj;
        if (!Objects.equals(this.diario, other.diario)) {
            return false;
        }
        if (this.presenca != other.presenca) {
            return false;
        }
        if (!Objects.equals(this.estudante, other.estudante)) {
            return false;
        }
        return true;
    }

    public Diario getDiario() {
        return diario;
    }

    public void setDiario(Diario diario) {
        this.diario = diario;
    }

    public Presenca getPresenca() {
        return presenca;
    }

    public void setPresenca(Presenca presenca) {
        this.presenca = presenca;
    }

    public Estudante getEstudante() {
        return estudante;
    }

    public void setEstudante(Estudante estudante) {
        this.estudante = estudante;
    }
    
    public String toString() {
        return presenca.getValue();
    }
}
