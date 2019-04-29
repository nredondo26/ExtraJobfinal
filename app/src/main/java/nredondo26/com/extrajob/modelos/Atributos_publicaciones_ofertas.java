package nredondo26.com.extrajob.modelos;

public class Atributos_publicaciones_ofertas {

    private String titulo;
    private String descipcion;
    private String direccion;
    private String fecha;
    private String horario;
    private String remuneracion;
    private String id;

    public Atributos_publicaciones_ofertas(String titulo, String descipcion, String direccion, String fecha, String horario, String remuneracion, String id) {
        this.titulo = titulo;
        this.descipcion = descipcion;
        this.direccion = direccion;
        this.fecha = fecha;
        this.horario = horario;
        this.remuneracion = remuneracion;
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescipcion() {
        return descipcion;
    }

    public void setDescipcion(String descipcion) {
        this.descipcion = descipcion;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getHorario() {
        return horario;
    }

    public void setHorario(String horario) {
        this.horario = horario;
    }

    public String getRemuneracion() {
        return remuneracion;
    }

    public void setRemuneracion(String remuneracion) {
        this.remuneracion = remuneracion;
    }
}


