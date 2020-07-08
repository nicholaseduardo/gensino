/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.defaults;

import ensino.configuracoes.model.Conteudo;
import ensino.configuracoes.model.ObjetivoUC;
import ensino.configuracoes.model.SemanaLetiva;
import ensino.patterns.BaseObject;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

/**
 *
 * @author santos
 */
public class GensinoTransferable implements Transferable {

    public static DataFlavor CONTEUDO_FLAVOR = new DataFlavor(Conteudo[].class, "Conteudo");
    public static DataFlavor OBJETIVOUC_FLAVOR = new DataFlavor(ObjetivoUC[].class, "ObjetivoUC");
    public static DataFlavor SEMANALETIVA_FLAVOR = new DataFlavor(SemanaLetiva[].class, "SemanaLetiva");
    public static DataFlavor BASEOBJECT_FLAVOR = new DataFlavor(BaseObject[].class, "BaseObject");

    private DataFlavor flavors[] = {CONTEUDO_FLAVOR, OBJETIVOUC_FLAVOR,
        SEMANALETIVA_FLAVOR, BASEOBJECT_FLAVOR};

    private Object[] aData;

    public GensinoTransferable(Object[] a) {
        this.aData = a;
    }

    @Override
    public DataFlavor[] getTransferDataFlavors() {
        return flavors;
    }

    @Override
    public boolean isDataFlavorSupported(DataFlavor df) {
        for(int i = 0; i < flavors.length; i++) {
            if (flavors[i].equals(df)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Object getTransferData(DataFlavor df) throws UnsupportedFlavorException, IOException {
        if (isDataFlavorSupported(df)) {
            return (Object) aData;
        } else {
            throw new UnsupportedFlavorException(df);
        }
    }

}
