package es.joseluisgs.dam.blog.service;

import es.joseluisgs.dam.blog.model.Comment;
import es.joseluisgs.dam.blog.model.Post;
import es.joseluisgs.dam.blog.dto.PostDTO;
import es.joseluisgs.dam.blog.mapper.PostMapper;
import es.joseluisgs.dam.blog.model.User;
import es.joseluisgs.dam.blog.repository.CategoryRepository;
import es.joseluisgs.dam.blog.repository.CommentRepository;
import es.joseluisgs.dam.blog.repository.PostRepository;
import es.joseluisgs.dam.blog.repository.UserRepository;
import org.bson.types.ObjectId;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PostService extends BaseService<Post, ObjectId, PostRepository> {
    PostMapper mapper = new PostMapper();

    // Inyección de dependencias en el constructor. El servicio necesita este repositorio
    public PostService(PostRepository repository) {
        super(repository);
    }

    // Otras operaciones o especificaciones para CRUD
    // O podíamos mapear el nombre
    // O simplemente ocultar las que no queramos usar en niveles superiores
    public List<PostDTO> getAllPosts() throws SQLException {
        UserService userService = new UserService(new UserRepository());
        CategoryService categoryService = new CategoryService(new CategoryRepository());
        CommentService commentService = new CommentService(new CommentRepository());

        // Obtenemos la lista
        List<Post> posts = this.findAll();
        List<PostDTO> lista = new ArrayList<>();
        // Por cada post recuperamos su usuario, sus categoria y sus comentarios
        // Recorremos todos los usuarios
        posts.forEach(p -> {
            PostDTO postDTO = mapper.toDTO(p);
            // Busco sus posts
            try {
                postDTO.setUser(userService.getById(p.getUser()));
            } catch (SQLException e) {
                System.err.println("Error PostService al buscar mi usuario autor con ID: " + p.getUser());
            }
            // Busco sus categoria
            try {
                postDTO.setCategory(categoryService.getById(p.getCategory()));
            } catch (SQLException e) {
                System.err.println("Error PostService al buscar mi categoria con ID: " + p.getCategory());
            }
            // Busco sus comentarios
            postDTO.setComments(commentService.getPostComments(p.getId()));
            lista.add(postDTO);
        });

        return lista;
    }

    public PostDTO getPostById(ObjectId id) throws SQLException {
        UserService userService = new UserService(new UserRepository());
        CategoryService categoryService = new CategoryService(new CategoryRepository());
        CommentService commentService = new CommentService(new CommentRepository());

        Post post = this.getById(id);
        PostDTO postDTO = mapper.toDTO(post);
        try {
            postDTO.setUser(userService.getById(post.getUser()));
        } catch (SQLException e) {
            System.err.println("Error PostService al buscar mi usuario autor con ID: " + post.getUser());
        }
        // Busco sus categoria
        try {
            postDTO.setCategory(categoryService.getById(post.getCategory()));
        } catch (SQLException e) {
            System.err.println("Error PostService al buscar mi categoria con ID: " + post.getCategory());
        }
        // Busco sus comentarios
        postDTO.setComments(commentService.getPostComments(post.getId()));

        return postDTO;
    }

    public PostDTO postPost(PostDTO postDTO) throws SQLException {
        // Le ponemos la fecha
        postDTO.setFechaPublicacion(LocalDateTime.now());
        Post post = mapper.fromDTO(postDTO);
        // Le ponemos el usuario
        post.setUser(postDTO.getUser().getId());
        // Le ponemos la categoria
        post.setCategory(postDTO.getCategory().getId());
        post = this.save(post);
        // No terminamos aqui. A usuario hay que insertarle el post
        postDTO.getUser().getPosts().add(post.getId());
        UserService userService = new UserService(new UserRepository());
        userService.updateUser(postDTO.getUser());

        // Con las categorias no tenemos bidireccionalidad
        PostDTO finalPost  = mapper.toDTO(post);
        finalPost.setUser(postDTO.getUser());
        finalPost.setCategory(postDTO.getCategory());
        return finalPost;
    }

    public PostDTO updatePost(PostDTO postDTO) throws SQLException {
        Post post = this.update(mapper.fromDTO(postDTO));
        return mapper.toDTO(post);
    }

    public PostDTO deletePost(PostDTO postDTO) throws SQLException {
        // Borramos el post
        Post post = this.delete(mapper.fromDTO(postDTO));

        // Lo borramos de la lista de post del usuario
        UserService userService = new UserService(new UserRepository());
        User user = userService.getMyUserById(postDTO.getUser().getId());
        user.getPosts().remove(post.getId());
        userService.update(user);

        // Le borramos los comentarios
        CommentService commentService = new CommentService(new CommentRepository());
        Set<Comment> comentarios = commentService.getPostComments(post.getId());
        // Recorremos cada comentario y lo borramos
        comentarios.forEach(c-> {
            // TODO Refactorizar que al eliminar comentario lo elimine el comentario del autor
            try {
                commentService.deleteCommentByID(c.getId());
            } catch (SQLException e) {
                System.err.println("Error PostService eliminar el comentario con ID: " + c.getId());
            }
            // Ahora debemos quitarselo a usuario de su lista de comentarios... Es un rollo la bidireccionalidad
            user.getComments().remove(c.getId());
            try {
                userService.update(user);
            } catch (SQLException e) {
                System.err.println("Error PostService eliminar al  autor del comentario con ID: " + c.getId());
            }
        });


        return mapper.toDTO(post);
    }

    public List<PostDTO> getPostsByUserId(ObjectId userId) {
        // Obtenemos la lista
        return mapper.toDTO(repository.getByUserId(userId));
    }

    public Set<Post> getUserPosts(ObjectId userId) {
        return new HashSet(repository.getByUserId(userId));
    }
}
