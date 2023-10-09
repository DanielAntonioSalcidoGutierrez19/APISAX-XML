import org.xml.sax.*;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Catalogo {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            CrearJframe();
        });
    }
    private static void CrearJframe() {
        //Aqui estamos creando el JFrame
        JFrame frm = new JFrame("API SAX COMO AYUDA EN XML");
        frm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frm.setPreferredSize(new Dimension(500, 450));
        
        //Contenido de la tabla
        DefaultTableModel contenido = new DefaultTableModel(new Object[]{"Title", "Artist", "Country", "Company", "Price", "Year"}, 0);
        JTable tablaContenido = new JTable(contenido);
        tablaContenido.setFillsViewportHeight(true);
        JScrollPane TablaScroll = new JScrollPane(tablaContenido);
        frm.getContentPane().add(TablaScroll, BorderLayout.CENTER);
        //Boton que me permite obtener todos los valos del archivo xml a un jTable
        JButton btn1 = new JButton("Cargar XML");
        btn1.addActionListener(e -> {
            contenido.setRowCount(0);
            //Lectura y declaracion de mi xml y ArrayList
            List<Double> precios = new ArrayList<>();
            try {
                XMLReader Lectura = XMLReaderFactory.createXMLReader();
                DefaultHandler hnd = new DefaultHandler() {
                    private String Elemento;
                    private List<String> Datos = new ArrayList<>();

                    public void startElement(String uri, String NombreXml, String AlmacenaNombre, Attributes atributo) {
                        Elemento = AlmacenaNombre;
                    }
                    public void characters(char[] caracteres, int comienzo, int tamaño) {
                        if (Elemento != null) {
                            String dato = new String(caracteres, comienzo, tamaño).trim();
                            if (!dato.isEmpty()) {
                                Datos.add(dato);
                                if (Elemento.equals("PRICE")) {
                                    double precio = Double.parseDouble(dato);
                                    precios.add(precio); }}}}
                    public void endElement(String uri, String localName, String qName) {
                        if (qName.equalsIgnoreCase("CD")) {
                            contenido.addRow(Datos.toArray());
                            Datos.clear(); }}};
                Lectura.setContentHandler(hnd);
                Lectura.parse("C:\\Users\\salci\\IdeaProjects\\APISAW-XML\\src\\cdcatalog.xml");
            } catch (SAXException | IOException exe) {
                exe.printStackTrace();
            }});
        JButton Calcular = new JButton("Calcular Media y Desviación Estándar");
        Calcular.addActionListener(e -> {
            List<Double> precios = new ArrayList<>();
            for (int row = 0; row < contenido.getRowCount(); row++) {
                String precio = (String) contenido.getValueAt(row, 4);
                double precio1 = Double.parseDouble(precio);
                precios.add(precio1); }
            double media = 0.0;
            double suma = 0.0;
            for (Double precio1 : precios) {
                suma += precio1;}
            if (!precios.isEmpty()) {
                media = suma / precios.size();}
            double DesviacionEs = 0.0;
            for (Double precio : precios) {
                double resta = precio - media;
                DesviacionEs += resta * resta; }
            double DesviacionEstandar= Math.sqrt(DesviacionEs / precios.size());
                    JOptionPane.showMessageDialog(frm, "Media de precios: " + media +
                    "\nDesviación estándar de precios: " +
                    DesviacionEstandar, "Resultados",
                    JOptionPane.INFORMATION_MESSAGE);
        });
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(btn1);
        buttonPanel.add(Calcular);
        frm.add(buttonPanel, BorderLayout.NORTH);
        frm.pack();
        frm.setLocationRelativeTo(null);
        frm.setVisible(true);
    }
}