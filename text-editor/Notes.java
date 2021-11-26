/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package notes;

/**
 *
 * @author Academia
 */
// Edit Combobox (x) |  Width Combobox (x)  |  Guardar estilos (x)  |  Vista previa (~)  |  JToolBar newLine (v)
import java.util.*;
import javax.swing.*;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledEditorKit;

import java.awt.*;
import java.awt.event.*;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

class Notes extends JFrame implements ActionListener{

    private JMenuBar menuBar;
    private JToolBar toolBar;
    private JPopupMenu popMenu;

    private JScrollPane sPanel;
    //StyledEditorKit doesn't work on textAreas
    private JTextPane text;

    // ====================================================
    // CONSTRUCTOR
    // ====================================================
    Notes(){
        super("Notes");

        toolBar = new JToolBar();
        popMenu = new JPopupMenu();        

        JMenu file = new JMenu("Archivo");
        createComponent("Guardar", file, "Guardar documento");
        createComponent("Nuevo", file, "Abrir documento en blanco");
        createComponent("Abrir", file, "Abrir documento especifico");
        createComponent("Imprimir", file, "Imprimir documento");
        toolBar.addSeparator();

        JMenu edit = new JMenu("Editar");
        createComponent("Cortar", edit, "Cortar texto seleccionado");
        createComponent("Copiar", edit, "Copiar texto seleccionado");
        createComponent("Pegar", edit, "Pegar texto seleccionado");
        toolBar.addSeparator();
        popMenu.addSeparator();
        
        JMenu format = new JMenu("Formato");
        createComponent("Alinear Izquierda", format, "Alinear parrafo izquierda");
        createComponent("Alinear Derecha", format, "Alinear parrafo derecha");
        createComponent("Centrar", format, "Centrar parrafo");
        createComponent("Justificar", format, "Justificar parrafo");
        toolBar.addSeparator();
        format.addSeparator();
        createComponent("Negrita", format, "Poner en negrita texto seleccionado");
        createComponent("Cursiva", format, "Poner en cursiva texto seleccionado");
        createComponent("Subrayado", format, "Subrayar texto seleccionado");
        toolBar.addSeparator();
        
        // FONTS & SIZE ------------------------------------------
        JComboBox<String> fonts= new JComboBox<>();
        createComboBox(fonts, "font");        
        JComboBox<String> fontSize = new JComboBox<>();
        createComboBox(fontSize, "size");
        // -------------------------------------------------------

        JMenu others = new JMenu("Otros");
        createComponent("Cerrar", others, "Cierra el programa");
        createComponent("Ayuda", others, "Informacion del programa");
        toolBar.addSeparator();

        menuBar = new JMenuBar();
        menuBar.add(file);
        menuBar.add(edit);
        menuBar.add(format);
        menuBar.add(others);

        // SCROLLING TEXT AREA ------------------------------------
        text = new JTextPane();
        text.setComponentPopupMenu(popMenu);
        sPanel = new JScrollPane(text);

        // STRUCTURING THE FRAME ----------------------------------
        setLayout(new BorderLayout());
        setJMenuBar(menuBar);
        add(toolBar, BorderLayout.NORTH);
        add(sPanel, BorderLayout.CENTER);
        
        setSize(500, 500);
        setExtendedState(MAXIMIZED_BOTH);
        setVisible(true);

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                close();
            }
        });
    }

    // GENERATE COMPONENTS =============================================================
    private void createComponent ( String s, JMenu m, String tooltip){

        ImageIcon icon = new ImageIcon(getClass().getResource("img/"+s+".png"));
        ActionListener action = this;

        // Special cases --------------------------------------------------------------
        if(s.equals("Negrita"))        {action = new StyledEditorKit.BoldAction();}
        else if(s.equals("Cursiva"))   {action = new StyledEditorKit.ItalicAction();}
        else if(s.equals("Subrayado")) {action = new StyledEditorKit.UnderlineAction();}
        else if(s.equals("Alinear Izquierda")) {action = new StyledEditorKit.AlignmentAction("Alinear Izquierda", StyleConstants.ALIGN_LEFT);}
        else if(s.equals("Alinear Derecha"))   {action = new StyledEditorKit.AlignmentAction("Alinear Derecha", StyleConstants.ALIGN_RIGHT);}
        else if(s.equals("Centrar"))           {action = new StyledEditorKit.AlignmentAction("Centrar", StyleConstants.ALIGN_CENTER);}
        else if(s.equals("Justificar"))        {action = new StyledEditorKit.AlignmentAction("Justificar", StyleConstants.ALIGN_JUSTIFIED);}

        // Menu Item -----------------------------------------------------------------
        JMenuItem mItem = new JMenuItem(s);
        mItem.setIcon(icon);
        mItem.addActionListener(action);
        mItem.setToolTipText(tooltip);
        m.add(mItem);

        // Tool Bar Button -----------------------------------------------------------
        JButton btn = new JButton(icon);
        btn.addActionListener(action);
        btn.setToolTipText(tooltip);
        toolBar.add(btn);

        // Popup Menu ---------------------------------------------------------------
        if (s.equals("Negrita") || s.equals("Cursiva") || s.equals("Subrayado")|| 
            s.equals("Cortar")  || s.equals("Copiar")  || s.equals("Pegar")) {
            JMenuItem popItem = new JMenuItem(s, icon);
            popItem.addActionListener(action);
            popItem.setToolTipText(tooltip);
            popMenu.add(popItem);
        }
    }

    // COMBOBOX ========================================================================
    private void createComboBox (JComboBox<String> fPropertie, String s){
        
        if (s.equals("font")){ 
            fPropertie.setCursor(new Cursor(Cursor.WAIT_CURSOR));

            String [] sisFonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
            
            for(String f:sisFonts) {
                fPropertie.addItem(f);
            }
            fPropertie.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));

            fPropertie.addActionListener(e -> {        
                Action fontAction = new StyledEditorKit.FontFamilyAction("",(String) fPropertie.getSelectedItem());
                fontAction.actionPerformed(e);
            });
        } else {
            for(int i = 12; i < 72; i += 2) {fPropertie.addItem(String.valueOf (i));}

            fPropertie.addActionListener(e -> {  
                int size = Integer.parseInt(fPropertie.getSelectedItem().toString());
                Action fontAction = new StyledEditorKit.FontSizeAction("", size);
                fontAction.actionPerformed(e);
            });
        }
        toolBar.add(fPropertie);
        toolBar.addSeparator();
    }

    // =================================================================================
    // MAIN
    // =================================================================================
    public static void main(String[] args) {
        new Notes();
    }

    // =================================================================================
    // EVENTS 
    // =================================================================================
    public void actionPerformed(ActionEvent e) {
        String ac = e.getActionCommand();
        String s = e.getSource().toString();

        // File --------------------------------------------------------------------------
        if (ac.equals("Guardar") || s.contains("Guardar"))  {save("");}
        else if (ac.equals("Nuevo") || s.contains("Nuevo")) {newDoc();}
        else if (ac.equals("Abrir") || s.contains("Abrir")) {open();}
        else if (ac.equals("Imprimir") || s.contains("Imprimir")) {
            try {
                JOptionPane.showMessageDialog(this, text.getText());
                text.print();

            } catch (Exception evt) {
                JOptionPane.showMessageDialog(this, evt.getMessage());
            }
        }

        // Edit --------------------------------------------------------------------------
        else if (ac.equals("Cortar") || s.contains("Cortar")) {text.cut();}
        else if (ac.equals("Copiar") || s.contains("Copiar")) {text.copy();}
        else if (ac.equals("Pegar")  ||  s.contains("Pegar")) {text.paste();}

        // Others --------------------------------------------------------------------------
        else if(ac.equals("Cerrar") || s.contains("Cerrar")) {close();}
        else if (ac.equals("Ayuda") || s.contains("Ayuda"))  {
            String pgrInfo = "Programa realizado por: Javier Palacios\n" +
                             "Fecha:                  15/10/2021\n" + 
                             "Version:                1.0.0";
            JOptionPane.showMessageDialog(this, pgrInfo, "Informacion del Programa", 1);
        }
    }

    // OPEN ============================================================================
    private void open() {
        int openDoc = JOptionPane.showConfirmDialog(this, "Guardar los cambios?", "Nuevo Documento", JOptionPane.YES_NO_CANCEL_OPTION);
        if(openDoc == 0) {save("");} //yes

        else if (openDoc != 2){
            JFileChooser j = new JFileChooser("f:");
            
            // Save file dialog
            int r = j.showOpenDialog(this);
            
            if (r == JFileChooser.APPROVE_OPTION) {               
                File fi = new File(j.getSelectedFile().getAbsolutePath());
                try {
                    // File reader
                    String sReader = "", sText = "";
                    FileReader fr = new FileReader(fi);
                    BufferedReader br = new BufferedReader(fr);
                    
                    while ((sReader = br.readLine()) != null) {   
                        sText += sReader + "\n";
                    }
    
                    text.setText(sText);
                    br.close();

                } catch (Exception evt) {
                    JOptionPane.showMessageDialog(this, evt.getMessage());
                }    
            } else {//User cancels
                JOptionPane.showMessageDialog(this, "Operacion cancelada");
            }
        }
    }

    // SAVE =============================================================================
    private void save(String s) {
            JFileChooser j = new JFileChooser("f:");
            
            // Save file dialog
            int r = j.showSaveDialog(this);
            
            if (r == JFileChooser.APPROVE_OPTION) {
                File fi = new File(j.getSelectedFile().getAbsolutePath());
                
                try {
                    // File writer 
                    FileWriter wr = new FileWriter(fi, false);
                    BufferedWriter w = new BufferedWriter(wr);
                    w.write(text.getText());
                    w.flush();
                    w.close(); 

                    //If the method making the call is close(). Exit when saved.
                    if (s.equals("close")) System.exit(0);

                } catch (Exception evt) {
                    JOptionPane.showMessageDialog(this, evt.getMessage());
                }
            } else {//User cancels
                JOptionPane.showMessageDialog(this, "Operacion cancelada");
                // Call calling methods to allow other option to be selected.
                if (s.equals("close")) close();
                else if ( s.equals("newDoc")) newDoc();
            }
    }

    // NEW DOCUMENT ======================================================================
    private void newDoc (){
        int newD = JOptionPane.showConfirmDialog(this, "Guardar los cambios?", "Nuevo Documento", JOptionPane.YES_NO_CANCEL_OPTION);
        if(newD == 1) {text.setText("");} //no
        else if(newD == 0) {//yes
            save("newDoc");
            text.setText("");
        } 
    }

    // CLOSE =============================================================================
    private void close (){
        int exit = JOptionPane.showConfirmDialog(this, "Guardar los cambios?", "Salir", JOptionPane.YES_NO_CANCEL_OPTION);
        if(exit == 1) {System.exit(0);} //no
        else if(exit == 0) {//yes
            save("close");
        } 
    }
}
