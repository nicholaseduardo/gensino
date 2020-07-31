/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.components;

import ensino.components.listener.GenFocusAdapter;
import ensino.components.listener.GenKeyAdapter;
import java.awt.Color;
import java.awt.Font;
import java.awt.Insets;
import javax.swing.BorderFactory;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 *
 * @author nicho
 */
public class GenJTextField extends JTextField {

    /**
     * Atributo utilizado para informar que o campo é de preenchimento
     * obrigatório
     */
    private boolean required = false;

    private Font originalFont;
    private Color originalForeground;
    /**
     * Grey by default*
     */
    private Color placeholderForeground = new Color(160, 160, 160);
    private boolean textWrittenIn;
    private String placeholderText;

    public GenJTextField(boolean required) {
        super();
        this.required = required;
        this.placeholderText = "";
    }

    public GenJTextField(int columns, boolean required) {
        super(columns);
        this.required = required;
        this.placeholderText = "";
        initComponents();
    }

    private void initComponents() {
        if (required) {
            Border lineBorder = BorderFactory.createLineBorder(Color.RED);
            Border outsider = BorderFactory.createEmptyBorder(6, 6, 6, 6);
            super.setBorder(BorderFactory.createCompoundBorder(lineBorder, outsider));
        } else {
            super.setMargin(new Insets(6, 6, 6, 6));
        }
        resetFontSize(16);
        super.addFocusListener(new GenFocusAdapter());
        super.addKeyListener(new GenKeyAdapter());
    }

    /**
     * Cria um objeto da classe <code>Border</code>
     *
     * @param label Texto que será colocado como título do campo
     */
    public void setLabelFor(String label) {
        Border lineBorder = BorderFactory.createLineBorder(required ? Color.RED : Color.BLACK);
        setBorder(BorderFactory.createTitledBorder(
                lineBorder, label,
                TitledBorder.LEFT,
                TitledBorder.TOP));
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

    @Override
    public void setFont(Font f) {
        super.setFont(f);
        if (!isTextWrittenIn()) {
            originalFont = f;
        }
    }

    @Override
    public void setForeground(Color fg) {
        super.setForeground(fg);
        if (!isTextWrittenIn()) {
            originalForeground = fg;
        }
    }

    public Color getPlaceholderForeground() {
        return placeholderForeground;
    }
    
    public Boolean isSetPlaceholderText() {
        return !"".equals(placeholderText);
    }

    public void setPlaceholderForeground(Color placeholderForeground) {
        this.placeholderForeground = placeholderForeground;
    }

    public boolean isTextWrittenIn() {
        return textWrittenIn;
    }

    public void setTextWrittenIn(boolean textWrittenIn) {
        this.textWrittenIn = textWrittenIn;
    }
    
    @Override
    public String getText() {
        if (isSetPlaceholderText() && !isTextWrittenIn()) {
            return "";
        }
        return super.getText();
    }

    public void setPlaceholder(final String text) {
        placeholderText = text;
        this.customizeText();

        this.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                warn();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                warn();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                warn();
            }

            public void warn() {
                if (isSetPlaceholderText() &&
                        getText().trim().length() != 0) {
                    setFont(originalFont);
                    setForeground(originalForeground);
                    setTextWrittenIn(true);
                }

            }
        });

    }

    public void customizeText() {
        if (isSetPlaceholderText()) {
            setText(placeholderText);
            /**
             * If you change font, family and size will follow changes, while
             * style will always be italic*
             */
            setFont(new Font(getFont().getFamily(), Font.ITALIC, getFont().getSize()));
            setForeground(getPlaceholderForeground());
            setTextWrittenIn(false);
        }
    }

}
