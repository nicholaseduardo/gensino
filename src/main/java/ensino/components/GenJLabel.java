/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.components;

import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.StringJoiner;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.border.Border;

/**
 *
 * @author nicho
 */
public class GenJLabel extends JLabel {

    private String actionCommand;
    private Integer columns;

    public GenJLabel() {
        actionCommand = "";
    }

    public GenJLabel(String text) {
        super(text);
        initComponents();
    }

    public GenJLabel(String text, int horizontalAlignment) {
        super(text, horizontalAlignment);
        initComponents();
    }

    public GenJLabel(String text, Icon image, int horizontalAlignment) {
        super(text, image, horizontalAlignment);
        initComponents();
    }

    public GenJLabel(Icon image) {
        super(image);
        initComponents();
    }

    public GenJLabel(Icon image, int horizontalAlignment) {
        super(image, horizontalAlignment);
        initComponents();
    }

    private void initComponents() {
        resetFontSize(16);
    }

    public String getActionCommand() {
        return actionCommand;
    }

    public void setActionCommand(String actionCommand) {
        this.actionCommand = actionCommand;
    }

    private void formatFont(String fontname, int style, int size) {
        Font font = new Font(fontname, style, size);
        setFont(font);
    }

    /**
     * Atualiza o tamanho da fonte do TextField
     *
     * @param size
     */
    public GenJLabel resetFontSize(int size) {
        Font fieldFont = getFont();
        formatFont(fieldFont.getFontName(),
                fieldFont.getStyle(), size);
        return this;
    }

    public void toItalic() {
        Font fieldFont = getFont();
        formatFont(fieldFont.getFontName(),
                Font.ITALIC,
                fieldFont.getSize());
    }

    public void toBold() {
        Font fieldFont = getFont();
        formatFont(fieldFont.getFontName(),
                Font.BOLD,
                fieldFont.getSize());
    }

    public void activateMouseEvents() {
        LabelMouseEvents lme = new LabelMouseEvents();
        addMouseListener(lme);
        addMouseMotionListener(lme);
        addMouseWheelListener(lme);
    }

    /**
     * Atribui um TEXT ao componente e realiza uma quebre de linha a cada
     * nColumns usando TAGS HTML
     *
     * @param value
     * @param nColumns
     */
    public void setText(String value, Integer nColumns) {
        String html = "<html><body>%s</body></html>";
        Integer length = value.length();
        if (length <= nColumns) {
            super.setText(String.format(html, value));
        } else {
            StringJoiner sj = new StringJoiner("<br/>");
            String format = String.format("(?<=\\G.{%d})", nColumns);
            String v[] = value.split(format);
            for (int i = 0; i < v.length; i++) {
                sj.add(v[i]);
            }
            super.setText(String.format(html, sj.toString()));
        }
    }

    private class LabelMouseEvents extends MouseAdapter {

        private Border outsideBorder;
        private Border insideBorder;
        private Font originalFont;

        public LabelMouseEvents() {
            originalFont = getFont();
            toOriginal();
        }

        private void toOriginal() {
            outsideBorder = BorderFactory.createLineBorder(
                    GenJLabel.this.getForeground(), 1, true);
            insideBorder = BorderFactory.createEmptyBorder(5, 5, 5, 5);
            setBorder(BorderFactory.createCompoundBorder(outsideBorder, insideBorder));
        }

        @Override
        public void mousePressed(MouseEvent e) {
//            outsideBorder = BorderFactory.createBevelBorder(BevelBorder.LOWERED);
            resetFontSize(originalFont.getSize() - 1);
            setBorder(BorderFactory.createCompoundBorder(outsideBorder, insideBorder));
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            resetFontSize(originalFont.getSize());
            setBorder(BorderFactory.createCompoundBorder(outsideBorder, insideBorder));
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            outsideBorder = BorderFactory.createLineBorder(
                    GenJLabel.this.getBackground(), 1, true);
            setBorder(BorderFactory.createCompoundBorder(outsideBorder, insideBorder));
        }

        @Override
        public void mouseExited(MouseEvent e) {
            setCursor(Cursor.getDefaultCursor());
            toOriginal();
        }
    }
}
