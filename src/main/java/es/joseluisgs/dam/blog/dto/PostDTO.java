package es.joseluisgs.dam.blog.dto;

import es.joseluisgs.dam.blog.model.Category;
import es.joseluisgs.dam.blog.model.Comment;
import es.joseluisgs.dam.blog.model.User;
import lombok.Builder;
import lombok.Data;
import org.bson.types.ObjectId;

import java.sql.Timestamp;
import java.util.Set;

@Data
@Builder
public class PostDTO {

    private ObjectId id;
    private String titulo;
    private String url;
    private String contenido;
    private Timestamp fechaPublicacion;
    // Autor
    private User user;
    // Categoría a la que pertenece
    private Category category;
    // Para mejorar las relaciones y como es un dTO vamos a poner los ids
    // Lista de comentarios asociados
    private Set<Comment> comments;


    @Override
    public String toString() {
        return "PostDTO{" +
                "id=" + id +
                ", titulo='" + titulo + '\'' +
                ", url='" + url + '\'' +
                ", contenido='" + contenido + '\'' +
                ", fechaPublicacion=" + fechaPublicacion +
                ", user=" + user +
                ", category=" + category +
                ", comments=" + comments +
                '}';
    }
}
