package es.joseluisgs.dam.blog.model;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
public class Comment {
    private ObjectId id;
    private String texto;
    private Timestamp fechaPublicacion;
    private String uuid;
    private User user;
    private Post post;

    public Comment(String texto, User usuario, Post post) {
        this.texto = texto;
        this.user = usuario;
        this.post = post;
        fechaPublicacion = Timestamp.from(Instant.now());
        uuid = UUID.randomUUID().toString();
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public Timestamp getFechaPublicacion() {
        return fechaPublicacion;
    }

    public void setFechaPublicacion(Timestamp fechaPublicacion) {
        this.fechaPublicacion = fechaPublicacion;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Comment comment = (Comment) o;
        return id == comment.id && Objects.equals(texto, comment.texto) && Objects.equals(fechaPublicacion, comment.fechaPublicacion) && Objects.equals(uuid, comment.uuid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, texto, fechaPublicacion, uuid);
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    @Override
    // No es obligatorio, pero al hacerlo podemos tener problemas con la recursividad de las llamadas
    public String toString() {
        return "Comment{" +
                "id=" + id +
                ", texto='" + texto + '\'' +
                ", fechaPublicacion=" + fechaPublicacion +
                ", uuid='" + uuid + '\'' +
                // Cuidado con esto que si no los post que tengan comentarios entra en recursividad
                ", user=" + user +
                ", post= Post{id:" + post.getId() + ", titulo=" + post.getTitulo() + ", " + "url=" + post.getUrl() +
                "}}";
    }
}
