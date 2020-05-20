/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.model;

import ensino.patterns.BaseObject;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

/**
 *
 * @author nicho
 */
@Entity
@Table(name = "nivelEnsino")
public class NivelEnsino extends BaseObject {
    
    @OneToMany(mappedBy = "id.nivelEnsino", cascade = CascadeType.ALL, 
            fetch = FetchType.LAZY, orphanRemoval = true)
    @OrderBy(value = "id.id")
    private List<EtapaEnsino> etapas;
    
    public NivelEnsino() {
        super();
        etapas = new ArrayList<>();
    }

    public List<EtapaEnsino> getEtapas() {
        return etapas;
    }

    public void setEtapas(List<EtapaEnsino> etapas) {
        this.etapas = etapas;
    }
    
    public void addEtapaEnsino(EtapaEnsino o) {
        o.getId().setNivelEnsino(this);
        etapas.add(o);
    }
    
    public void removeEtapaEnsino(EtapaEnsino o) {
        etapas.remove(o);
    }
}
