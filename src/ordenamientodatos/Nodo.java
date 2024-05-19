package ordenamientodatos;

public class Nodo {
    
    private Documento documento;
    public Nodo izquierdo;
    public Nodo derecho;

    public Nodo(Documento documento) {
        this.documento = documento;
        this.izquierdo = null;
        this.derecho = null;
    }
    public Documento getDocumento() {
        return documento;
    }

    public void setDocumento(Documento documento) {
        this.documento = documento;
    }
    
    
}
