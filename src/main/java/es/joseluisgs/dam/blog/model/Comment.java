package es.joseluisgs.dam.blog.model;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
public class Comment {
    private ObjectId id;
    private String texto;
    private LocalDateTime fechaPublicacion;
    private String uuid;
    private ObjectId user;
    private ObjectId post;

    public Comment(String texto, ObjectId usuario, ObjectId post) {
        this.texto = texto;
        this.user = usuario;
        this.post = post;
        fechaPublicacion = LocalDateTime.now();
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

    public LocalDateTime getFechaPublicacion() {
        return fechaPublicacion;
    }

    public void setFechaPublicacion(LocalDateTime fechaPublicacion) {
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

    public ObjectId getUser() {
        return user;
    }

    public void setUser(ObjectId user) {
        this.user = user;
    }

    public ObjectId getPost() {
        return post;
    }

    public void setPost(ObjectId post) {
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
                ", post= Post{id:" + post +
                "}}";
    }
}
