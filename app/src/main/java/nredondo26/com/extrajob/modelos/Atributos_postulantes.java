package nredondo26.com.extrajob.modelos;

public class Atributos_postulantes {

    private String nombre;
    private String ocupacion;
    private String foto;
    private String informacion;
    private String id;
    private String idofertas;

    public Atributos_postulantes(String nombre, String ocupacion, String foto , String informacion, String id, String idofertas) {
        this.nombre = nombre;
        this.ocupacion = ocupacion;
        this.foto = foto;
        this.informacion = informacion;
        this.id = id;
        this.idofertas = idofertas;
    }



    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getOcupacion() {
        return ocupacion;
    }

    public void setOcupacion(String ocupacion) {
        this.ocupacion = ocupacion;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getInformacion() {
        return informacion;
    }

    public void setInformacion(String informacion) {
        this.informacion = informacion;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdofertas() {
        return idofertas;
    }

    public void setIdofertas(String idofertas) {
        this.idofertas = idofertas;
    }
}
