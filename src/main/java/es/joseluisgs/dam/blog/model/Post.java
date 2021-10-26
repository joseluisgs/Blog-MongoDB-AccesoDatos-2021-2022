package es.joseluisgs.dam.blog.model;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;


@NoArgsConstructor
@AllArgsConstructor
public class Post {
    private ObjectId id;
    private String titulo;
    private String url;
    private String contenido;
    private LocalDateTime fechaPublicacion;
    private ObjectId user;
    private ObjectId category;
    private Set<ObjectId> comments;

    public Post(String titulo, String url, String contenido, ObjectId user, ObjectId category) {
        this.titulo = titulo;
        this.url = url;
        this.contenido = contenido;
        this.user = user;
        this.category = category;
        fechaPublicacion = LocalDateTime.now();
        comments = new HashSet<ObjectId>();
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public LocalDateTime getFechaPublicacion() {
        return fechaPublicacion;
    }

    public void setFechaPublicacion(LocalDateTime fechaPublicacion) {
        this.fechaPublicacion = fechaPublicacion;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Post post = (Post) o;
        return id == post.id && Objects.equals(titulo, post.titulo) && Objects.equals(url, post.url) && Objects.equals(contenido, post.contenido) && Objects.equals(fechaPublicacion, post.fechaPublicacion);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, titulo, url, contenido, fechaPublicacion);
    }

    public ObjectId getUser() {
        return user;
    }

    public void setUser(ObjectId user) {
        this.user = user;
    }

    public ObjectId getCategory() {
        return category;
    }

    public void setCategory(ObjectId category) {
        this.category = category;
    }

    public Set<ObjectId> getComments() {
        return comments;
    }

    public void setComments(Set<ObjectId> comments) {
        this.comments = comments;
    }

    @Override
    // No es obligatorio, pero al hacerlo podemos tener problemas con la recursividad de las llamadas
    // Post es mi entidad fuerte, voy a sacra todo de ella porque si la debo procesar en una interfaz lo veo todo de ella
    public String toString() {
        return "Post{" +
                "id=" + id +
                ", titulo='" + titulo + '\'' +
                ", url='" + url + '\'' +
                ", contenido='" + contenido + '\'' +
                ", fechaPublicacion=" + fechaPublicacion +
                // Cuidado con la recursividad
                ", user=" + user +
                ", category=" + category +
                ", comments=" + comments +
                '}';
    }
}
