package es.joseluisgs.dam.blog.model;


import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;

import java.util.Objects;

@NoArgsConstructor
public class Category {
    private ObjectId id;
    private String texto;

    public Category(String texto) {
        this.texto = texto;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Category category = (Category) o;
        return Objects.equals(id, category.id) && Objects.equals(texto, category.texto);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, texto);
    }

    @Override
    public String toString() {
        return "Category{" +
                "id=" + id +
                ", texto='" + texto + '\'' +
                '}';
    }
}
