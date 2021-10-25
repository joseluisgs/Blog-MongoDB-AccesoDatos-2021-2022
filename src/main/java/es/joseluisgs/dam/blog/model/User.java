package es.joseluisgs.dam.blog.model;

import es.joseluisgs.dam.blog.utils.Cifrador;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;

import java.util.*;

@NoArgsConstructor
@AllArgsConstructor
public class User {
    private ObjectId id;
    private String nombre;
    private String email;
    private String password;
    private Date fechaRegistro;
    private Set<Post> posts;
    private Set<Comment> comments;

    public User(String nombre, String email, String password) {
        this.nombre = nombre;
        this.email = email;
        this.password = Cifrador.getInstance().SHA256(password);
        this.fechaRegistro = new Date(System.currentTimeMillis());
        this.posts = new HashSet<Post>();
        this.comments = new HashSet<Comment>();
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(Date fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id && Objects.equals(nombre, user.nombre) && Objects.equals(email, user.email) && Objects.equals(password, user.password) && Objects.equals(fechaRegistro, user.fechaRegistro);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nombre, email, password, fechaRegistro);
    }

    public Set<Post> getPosts() {
        return posts;
    }

    public void setPosts(Set<Post> posts) {
        this.posts = posts;
    }

    public Set<Comment> getComments() {
        return comments;
    }

    public void setComments(Set<Comment> comments) {
        this.comments = comments;
    }

    @Override
    // No es obligatorio, pero al hacerlo podemos tener problemas con la recursividad de las llamadas
    public String toString() {
        return "User{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", email='" + email + '\'' +
                // ", password='" + password + '\'' + Evitamos
                ", fechaRegistro=" + fechaRegistro +
                // Cuidado aqui con las llamadas recursivas No me interesa imprimir los post del usuario, pueden ser muchos
                 // ", posts=" + posts + // Podriamos quitarlos para no verlos
                // Tampoco saco los comentarios
                '}';
    }
}
