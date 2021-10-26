package es.joseluisgs.dam.blog.dto;

import es.joseluisgs.dam.blog.model.Post;
import es.joseluisgs.dam.blog.model.User;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;

import java.time.LocalDateTime;


@Builder
@Getter
@Setter
public class CommentDTO {
    private ObjectId id;
    private String texto;
    private LocalDateTime fechaPublicacion;
    // Autor que la realiza
    private User user;
    // Post al que pertenece
    private Post post;

    @Override
    // DE comentario nos interesa saber su autor, porque el foro sabemos cual si accedemos por foro
    // Asi que en vez d eimprimirlo todo, solo haremos cosas que nos interese
    public String toString() {
        return "CommentDTO{" +
                "id=" + id +
                ", texto='" + texto + '\'' +
                ", fechaPublicacion=" + fechaPublicacion +
                ", mi_autor=" + user +
                ", mi_post=" + post +
                "}";
    }
}
