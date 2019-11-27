/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.util.helper;

import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author nicho
 */
public final class FileHelper {
    public static final int FILE_CHOOSER_OPEN = 1;
    public static final int FILE_CHOOSER_SAVE = 2;

    public static final int FILE_TYPE_CSV = 0;
    public static final int FILE_TYPE_PNG = 1;
    public static final int FILE_TYPE_SPX = 2;
    
    public static File defaultPath = new File(System.getProperty("user.dir"));
    
    /**
     * Cria a classe <code>JFileChooser</code> para seleção de um arquivo 
     * (<code>File</code>)do sistema operacional.
     * 
     * @param type      Tipo de tela de escolha de arquivo (Abrir/Salvar)
     * @param fileType  Definição da extensão do arquivo (csv,png,spx)
     * @return 
     */
    public static JFileChooser loadFileChooser(int type, int fileType) {
        JFileChooser fileChooser = new JFileChooser();
        File workingDirectory = defaultPath;
        fileChooser.setCurrentDirectory(workingDirectory);
        String titleFileChooser = "Salvar como ...";
        if (type == FILE_CHOOSER_OPEN) {
            titleFileChooser = "Abrir";
        }
        fileChooser.setDialogTitle(titleFileChooser);
        // Selecionar apenas arquivos
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        // Se não for imagem, filtra pelo arquivo do programa
        FileNameExtensionFilter filter;
        switch (fileType) {
            case FILE_TYPE_CSV:
                // Criando filtro para selecionar somente arquivos deste programa SPX.
                filter = new FileNameExtensionFilter("Arquivos CSV", "csv", "Formato CSV");
                break;
            case FILE_TYPE_PNG:
                // Criando filtro para selecionar somente arquivos deste programa SPX.
                filter = new FileNameExtensionFilter("Arquivos PNG", "png", "Imagem PNG");
                break;
            default:
                // Criando filtro para selecionar somente arquivos deste programa SPX.
                filter = new FileNameExtensionFilter("Arquivos SPX", "spx", "Shape XML");
                break;
        }

        fileChooser.setFileFilter(filter);

        return fileChooser;
    }
}
