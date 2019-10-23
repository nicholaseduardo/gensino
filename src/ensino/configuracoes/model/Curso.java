/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.model;

import ensino.patterns.BaseObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import javax.swing.ImageIcon;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author nicho
 */
public class Curso extends BaseObject {

    private ImageIcon imagem;
    private Campus campus;
    
    private List<UnidadeCurricular> unidadesCurriculares;
    private List<Turma> turmas;

    public Curso() {
        this(null, "", null);
    }
    
    public Curso(Integer id, String nome) {
        this(id, nome, null);
    }

    public Curso(Integer id, String nome, Campus campus) {
        super(id, nome);
        this.imagem = null;
        this.campus = campus;
        unidadesCurriculares = new ArrayList<>();
        turmas = new ArrayList<>();
    }

    public Curso(Element element) {
        super(element);
        unidadesCurriculares = new ArrayList<>();
        turmas = new ArrayList<>();
        
        if (element.hasChildNodes()) {
            NodeList nodeList = element.getChildNodes();
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node child = nodeList.item(i);
                if ("unidadeCurricular".equals(child.getNodeName())) {
                    addUnidadeCurricular(new UnidadeCurricular((Element) child));
                } else if ("turma".equals(child.getNodeName())) {
                    addTurma(new Turma((Element) child));
                }
            }
        }
    }

    public Curso(HashMap<String, Object> params) {
        super(params);
        unidadesCurriculares = new ArrayList<>();
        this.imagem = (ImageIcon) params.get("imagem");
        this.campus = (Campus) params.get("campus");
        this.unidadesCurriculares = (List<UnidadeCurricular>) params.get("unidadesCurriculares");
        this.turmas = (List<Turma>) params.get("turmas");
    }

    public ImageIcon getImagem() {
        return imagem;
    }

    public void setImagem(ImageIcon imagem) {
        this.imagem = imagem;
    }

    public Campus getCampus() {
        return campus;
    }

    public void setCampus(Campus campus) {
        this.campus = campus;
    }
    
    public void addUnidadeCurricular(UnidadeCurricular unidadeCurricular) {
        unidadeCurricular.setCurso(this);
        unidadesCurriculares.add(unidadeCurricular);
    }
    
    public void upddateUnidadeCurricular(UnidadeCurricular unidadeCurricular) {
        for(UnidadeCurricular und : unidadesCurriculares) {
            if (und.getId().equals(unidadeCurricular.getId())) {
                und = unidadeCurricular;
                break;
            }
        }
    }
    
    public void removeUnidadeCurricular(UnidadeCurricular unidadeCurricular) {
        unidadesCurriculares.remove(unidadeCurricular);
    }

    public List<UnidadeCurricular> getUnidadesCurriculares() {
        return unidadesCurriculares;
    }

    public void setUnidadesCurriculares(List<UnidadeCurricular> unidadesCurriculares) {
        this.unidadesCurriculares = unidadesCurriculares;
    }
    
    public void addTurma(Turma turma) {
        turma.setCurso(this);
        turmas.add(turma);
    }
    
    public void removeTurma(Turma turma) {
        turmas.remove(turma);
    }

    public List<Turma> getTurmas() {
        return turmas;
    }

    public void setTurmas(List<Turma> turmas) {
        this.turmas = turmas;
    }

    @Override
    public Node toXml(Document doc) {
        Element elem = doc.createElement("curso");
        elem.setAttribute("id", id.toString());
        elem.setAttribute("nome", nome);
        
        if (!unidadesCurriculares.isEmpty()) {
            unidadesCurriculares.forEach((und) -> {
                elem.appendChild(und.toXml(doc));
            });
        }
        
        if (!turmas.isEmpty()) {
            turmas.forEach((turma) -> {
                elem.appendChild(turma.toXml(doc));
            });
        }

        return elem;
    }

    @Override
    public int hashCode() {
        int hash = 7;
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
        if (!super.equals(obj)) {
            return false;
        }
        final Curso other = (Curso) obj;
        return Objects.equals(this.campus, other.campus);
    }
    
    @Override
    public String toString() {
        return nome;
    }

}
