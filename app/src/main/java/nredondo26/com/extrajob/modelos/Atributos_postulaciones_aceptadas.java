package nredondo26.com.extrajob.modelos;

public class Atributos_postulaciones_aceptadas {

    private String nombre_empresa;
    private String nombre_postulante;
    private String titulo_oferta;
    private String valor;
    private String direccion;

    public Atributos_postulaciones_aceptadas(String titulo_oferta,String nombre_empresa,String valor,String direccion/*, String nombre_postulante, , */) {
        this.nombre_empresa = nombre_empresa;
        this.nombre_postulante = nombre_postulante;
        this.titulo_oferta = titulo_oferta;
        this.valor = valor;
        this.direccion = direccion;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getNombre_empresa() {
        return nombre_empresa;
    }

    public void setNombre_empresa(String nombre_empresa) {
        this.nombre_empresa = nombre_empresa;
    }

    public String getNombre_postulante() {
        return nombre_postulante;
    }

    public void setNombre_postulante(String nombre_postulante) {
        this.nombre_postulante = nombre_postulante;
    }

    public String getTitulo_oferta() {
        return titulo_oferta;
    }

    public void setTitulo_oferta(String titulo_oferta) {
        this.titulo_oferta = titulo_oferta;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

}
