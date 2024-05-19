package ordenamientodatos;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class ArbolBinario {

    private Nodo raiz;
    private int criterio;

    public Nodo getRaiz() {
        return raiz;
    }

    public ArbolBinario(Nodo raiz) {
        this.raiz = raiz;
    }

    public ArbolBinario() {
    }

    public int getCriterio() {
        return criterio;
    }

    public void setCriterio(int criterio) {
        this.criterio = criterio;
    }

    private boolean insertar(Nodo nRecorrido, Nodo n) {
        if (n.getDocumento().equals(nRecorrido.getDocumento())) {
            return false;
        } else if (!Documento.esMayor(n.getDocumento(), nRecorrido.getDocumento(), criterio)) {
            if (nRecorrido.izquierdo == null) {
                nRecorrido.izquierdo = n;
                return true;
            } else {
                return insertar(nRecorrido.izquierdo, n);
            }
        } else {
            if (nRecorrido.derecho == null) {
                nRecorrido.derecho = n;
                return true;
            } else {
                return insertar(nRecorrido.derecho, n);
            }
        }
    }

    public boolean insertarNodo(Nodo n) {
        if (raiz == null) {
            raiz = n;
            return true;
        }

        Nodo actual = raiz;
        Nodo padre;

        while (true) {
            padre = actual;

            if (n.getDocumento().equals(actual.getDocumento())) {
                return false;

            } else if (!Documento.esMayor(n.getDocumento(), actual.getDocumento(), criterio)) {
                actual = actual.izquierdo;
                if (actual == null) {
                    padre.izquierdo = n;
                    return true;
                }
            } else {
                actual = actual.derecho;
                if (actual == null) {
                    padre.derecho = n;
                    return true;
                }
            }
        }
    }

    public void recorrerInorden(Nodo n) {
        if (n != null) {
            recorrerInorden(n.izquierdo);
            System.out.println(n.getDocumento().getNombreCompleto() + " " + n.getDocumento().getDocumento());
            recorrerInorden(n.derecho);
        }
    }

    public void mostrar(JTable tbl) {
        String[][] datos = null;
        if (raiz != null) {
            datos = new String[Documento.documentos.size()][Documento.encabezados.length];

            Nodo n = raiz;
            Nodo predecesor;
            int fila = -1;

            while (n != null) {
                if (n.izquierdo == null) {
                    fila++;
                    datos[fila][0] = n.getDocumento().getApellido1();
                    datos[fila][1] = n.getDocumento().getApellido2();
                    datos[fila][2] = n.getDocumento().getNombre();
                    datos[fila][3] = n.getDocumento().getDocumento();

                    n = n.derecho;
                } else {
                    predecesor = n.izquierdo;
                    while (predecesor.derecho != null && predecesor.derecho != n) {
                        predecesor = predecesor.derecho;
                    }

                    if (predecesor.derecho == null) {
                        predecesor.derecho = n;
                        n = n.izquierdo;
                    } else {
                        predecesor.derecho = null;
                        fila++;
                        datos[fila][0] = n.getDocumento().getApellido1();
                        datos[fila][1] = n.getDocumento().getApellido2();
                        datos[fila][2] = n.getDocumento().getNombre();
                        datos[fila][3] = n.getDocumento().getDocumento();
                        n = n.derecho;
                    }
                }
            }

        }
        DefaultTableModel dtm = new DefaultTableModel(datos, Documento.encabezados);
        tbl.setModel(dtm);
    }
}
