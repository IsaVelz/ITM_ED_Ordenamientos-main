package ordenamientodatos;

import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import java.util.List;
import java.util.ArrayList;
import java.io.BufferedReader;

public class Documento {

    private String apellido1;
    private String apellido2;
    private String nombre;
    private String documento;

    public Documento(String apellido1, String apellido2, String nombre, String documento) {
        this.apellido1 = apellido1;
        this.apellido2 = apellido2;
        this.nombre = nombre;
        this.documento = documento;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellido1() {
        return apellido1;
    }

    public String getApellido2() {
        return apellido2;
    }

    public String getDocumento() {
        return documento;
    }

    public String getNombreCompleto() {
        return apellido1 + " " + apellido2 + " " + nombre;
    }

    public boolean equals(Documento d) {
        return getDocumento().equals(d.getDocumento()) && getNombreCompleto().equals(d.getNombreCompleto());
    }

    //********** Atributos y Metodos estaticos ********** 
    //Almacena la lista de documentos
    public static List<Documento> documentos = new ArrayList<>();
    public static String[] encabezados;

    //Metodo que obtiene los datos desde el archivo CSV
    public static void obtenerDatosDesdeArchivo(String nombreArchivo) {
        documentos.clear();
        BufferedReader br = Archivo.abrirArchivo(nombreArchivo);
        if (br != null) {
            try {
                String linea = br.readLine();
                encabezados = linea.split(";");
                linea = br.readLine();
                while (linea != null) {
                    String[] textos = linea.split(";");
                    if (textos.length >= 4) {
                        Documento d = new Documento(textos[0], textos[1], textos[2], textos[3]);
                        documentos.add(d);
                    }
                    linea = br.readLine();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    //Metodo para mostrar los datos en una tabla
    public static void mostrarDatos(JTable tbl) {
        String[][] datos = null;
        if (documentos.size() > 0) {
            datos = new String[documentos.size()][encabezados.length];
            for (int i = 0; i < documentos.size(); i++) {
                datos[i][0] = documentos.get(i).apellido1;
                datos[i][1] = documentos.get(i).apellido2;
                datos[i][2] = documentos.get(i).nombre;
                datos[i][3] = documentos.get(i).documento;
            }
        }
        DefaultTableModel dtm = new DefaultTableModel(datos, encabezados);
        tbl.setModel(dtm);
    }

    //metodo para intercambiar elementos
    private static void intercambiar(int origen, int destino) {
        Documento temporal = documentos.get(origen);
        documentos.set(origen, documentos.get(destino));
        documentos.set(destino, temporal);
    }

    //metodo para verificar si un documento es mayor que otro
    public static boolean esMayor(Documento d1, Documento d2, int criterio) {
        if (criterio == 0) {
            return ((d1.getNombreCompleto().compareTo(d2.getNombreCompleto()) > 0)
                    || (d1.getNombreCompleto().equals(d2.getNombreCompleto())
                    && d1.getDocumento().compareTo(d2.getDocumento()) > 0));
        } else {
            return ((d1.getDocumento().compareTo(d2.getDocumento()) > 0)
                    || (d1.getDocumento().equals(d2.getDocumento())
                    && d1.getNombreCompleto().compareTo(d2.getNombreCompleto()) > 0));
        }
    }

    public static void ordenarBurbuja(int criterio) {
        for (int i = 0; i < documentos.size() - 1; i++) {
            for (int j = i + 1; j < documentos.size(); j++) {
                if (esMayor(documentos.get(i), documentos.get(j), criterio)) {
                    intercambiar(i, j);
                }
            }
        }
    }

    private static int localizarPivote(int inicio, int fin, int criterio) {
        int pivote = inicio;
        Documento dP = documentos.get(pivote);

        for (int i = inicio + 1; i <= fin; i++) {
            if (esMayor(dP, documentos.get(i), criterio)) {
                pivote++;
                if (i != pivote) {
                    intercambiar(i, pivote);
                }
            }
        }
        if (inicio != pivote) {
            intercambiar(inicio, pivote);
        }
        return pivote;
    }

    public static void ordenarRapido(int inicio, int fin, int criterio) {
        if (inicio >= fin) {
            return;
        }
        int pivote = localizarPivote(inicio, fin, criterio);
        ordenarRapido(inicio, pivote - 1, criterio);
        ordenarRapido(pivote + 1, fin, criterio);
    }
    
    public static ArbolBinario obtenerArbolBinario(int criterio) {
        ArbolBinario ab = new ArbolBinario();
        ab.setCriterio(criterio);
        for (Documento documento : documentos) {
            Nodo n = new Nodo(documento);
            ab.insertarNodo(n);
        }
        return ab;
    }

    public static void ordenarRapido(int criterio) {
        if (documentos.size() > 1) {
            ordenarRapido(0, documentos.size() - 1, criterio);
        }
    }

    public static int busquedaBinaria(String clave, int criterio) {
        int inicio = 0;
        int fin = documentos.size() - 1;

        while (inicio <= fin) {
            int medio = (inicio + fin) / 2;
            Documento docMedio = documentos.get(medio);
            int comparacion;

            if (criterio == 0) {
                comparacion = docMedio.getNombreCompleto().compareTo(clave);
            } else {
                comparacion = docMedio.getDocumento().compareTo(clave);
            }

            if (comparacion == 0) {
                return medio; 
            } else if (comparacion < 0) {
                inicio = medio + 1;
            } else {
                fin = medio - 1;
            }
        }

        return -1;
    }
}
