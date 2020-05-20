/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.components;

import ensino.components.listener.GenFocusAdapter;
import ensino.components.listener.GenKeyAdapter;
import java.awt.Font;
import java.awt.Insets;
import java.text.DecimalFormat;
import java.text.ParseException;
import javax.swing.JFormattedTextField;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.MaskFormatter;
import javax.swing.text.NumberFormatter;

/**
 *
 * @author nicho
 */
public class GenJFormattedTextField extends JFormattedTextField {

    public GenJFormattedTextField() {
        super();
        initComponents();
    }
    
    public GenJFormattedTextField(AbstractFormatter formatter) {
        super(formatter);
        initComponents();
    }

    private void initComponents() {
        setMargin(new Insets(6, 6, 6, 6));
        
        super.addFocusListener(new GenFocusAdapter());
        super.addKeyListener(new GenKeyAdapter());
        resetFontSize(16);
    }

    /**
     * Atualiza o tamanho da fonte do TextField
     *
     * @param size
     */
    public void resetFontSize(int size) {
        Font fieldFont = getFont();
        Font font = new Font(fieldFont.getFontName(),
                fieldFont.getStyle(), size);
        setFont(font);
    }

    /**
     * Cria um campo formatado
     *
     * @param format Formato a ser mostrado
     * @param type Type = 0, para números e Type = 1 para outros.
     * @return
     * @throws ParseException
     */
    public static GenJFormattedTextField createFormattedField(String format, Integer type) throws ParseException {
        DefaultFormatterFactory formatter = null;
        if (type == 0) {
//            DecimalFormat df = new DecimalFormat("#0.00", new DecimalFormatSymbols (new Locale ("pt", "BR")));
            DecimalFormat df = new DecimalFormat("#0.00");
            NumberFormatter nf = new NumberFormatter(df);
            formatter = new DefaultFormatterFactory(nf);
        } else {
            formatter = new DefaultFormatterFactory(new MaskFormatter(format));
        }
        GenJFormattedTextField textField = new GenJFormattedTextField();
        textField.setFormatterFactory(formatter);
        return textField;
    }

}
