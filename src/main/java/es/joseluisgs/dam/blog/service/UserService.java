package es.joseluisgs.dam.blog.service;

import es.joseluisgs.dam.blog.model.User;
import es.joseluisgs.dam.blog.dto.UserDTO;
import es.joseluisgs.dam.blog.mapper.UserMapper;
import es.joseluisgs.dam.blog.repository.CommentRepository;
import es.joseluisgs.dam.blog.repository.PostRepository;
import es.joseluisgs.dam.blog.repository.UserRepository;
import es.joseluisgs.dam.blog.utils.Cifrador;
import org.bson.types.ObjectId;

import java.time.LocalDate;
import java.util.*;
import java.sql.SQLException;

public class UserService extends BaseService<User, ObjectId, UserRepository> {
    private UserMapper mapper = new UserMapper();


    // Inyección de dependencias en el constructor. El servicio necesita este repositorio
    public UserService(UserRepository repository) {
        super(repository);
    }


    // Otras operaciones o especificaciones para CRUD
    // O podíamos mapear el nombre
    // O simplemente ocultar las que no queramos usar en niveles superiores
    public List<UserDTO> getAllUsers() throws SQLException {
        PostService postService = new PostService(new PostRepository());
        CommentService commentService = new CommentService(new CommentRepository());

        List<User> usuarios = this.findAll();
        // Ahora debemos recuperar sus posts y sus comentarios
        List<UserDTO> lista = mapper.toDTO(usuarios);
        // Recorremos todos los usuarios
        lista.forEach(u-> {
            // Busco sus posts
            u.setPosts(postService.getUserPosts(u.getId()));
            // Busco sus comentarios
            u.setComentarios(commentService.getUserComments(u.getId()));
        });
        return lista;
    }

    public UserDTO getUserById(ObjectId id) throws SQLException {
        PostService postService = new PostService(new PostRepository());
        CommentService commentService = new CommentService(new CommentRepository());

        User usuario = this.getById(id);
        UserDTO usuarioDTO = mapper.toDTO(usuario);
        // Busco sus posts
        usuarioDTO.setPosts(postService.getUserPosts(usuario.getId()));
        // Busco sus comentarios
        usuarioDTO.setComentarios(commentService.getUserComments(usuario.getId()));
        return usuarioDTO;
    }

    public UserDTO postUser(UserDTO userDTO) throws SQLException {
        // Ciframos antes el password
        userDTO.setPassword(Cifrador.getInstance().SHA256(userDTO.getPassword()));
        // Le ponemos la fecha de registro
        userDTO.setFechaRegistro(LocalDate.now());
        User res = this.save(mapper.fromDTO(userDTO));
        return mapper.toDTO(res);
    }

    public UserDTO updateUser(UserDTO userDTO) throws SQLException {
        User res = this.update(mapper.fromDTO(userDTO));
        return mapper.toDTO(res);
    }

    public UserDTO deleteUser(UserDTO userDTO) throws SQLException {
        PostService postService = new PostService(new PostRepository());
        CommentService commentService = new CommentService(new CommentRepository());

        User res = this.delete(mapper.fromDTO(userDTO));
        // Borramos todos los post asociados
        postService.getUserPosts(userDTO.getId()).forEach(post -> {
            try {
                postService.delete(post);
            } catch (SQLException e) {
                System.err.println("Error UserService al borrar mi post con id: " + post.getId());
            }
        });
        // Borramos todos los comentarios asociados
        commentService.getUserComments(userDTO.getId()).forEach(comment -> {
            try {
                commentService.delete(comment);
            } catch (SQLException e) {
                System.err.println("Error UserService al borrar mi comentario con id: " + comment.getId());
            }
        });
        return mapper.toDTO(res);
    }

    public User getUserByMail(String userMail) throws SQLException {
        return repository.getByEmail(userMail);
    }
}
