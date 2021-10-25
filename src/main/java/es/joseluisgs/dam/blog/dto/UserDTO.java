package es.joseluisgs.dam.blog.dto;

import es.joseluisgs.dam.blog.model.Comment;
import es.joseluisgs.dam.blog.model.Post;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;


@Builder
@Getter
@Setter
public class UserDTO {
    // @Expose
    private ObjectId id;
    // @Expose
    private String nombre;
    // @Expose
    private String email;
    // @Expose
    private Date fechaRegistro;

    // TODO Bidireccionalidad
    // Lista de Comentarios
    private Set<Comment> comentarios = new HashSet<>();
    // Lista de Posts
    private Set<Post> posts = new HashSet<>();
    // Su login activo si lo tiene
    //private Login login;

    // Eliminar campos de las serializaciónno ponemos expose
    // https://www.baeldung.com/gson-exclude-fields-serialization
    private String password;

    @Override
    public String toString() {
        return "UserDTO{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", email='" + email + '\'' +
                ", fechaRegistro=" + fechaRegistro +
                // ", posts=" + posts + // No voy a imprimir los posts poprque son muy largos
                //", password='" + password + '\'' + // Así no sale el password
                '}';
    }
}