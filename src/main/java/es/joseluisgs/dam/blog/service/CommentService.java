package es.joseluisgs.dam.blog.service;

import es.joseluisgs.dam.blog.dto.PostDTO;
import es.joseluisgs.dam.blog.model.Comment;
import es.joseluisgs.dam.blog.dto.CommentDTO;
import es.joseluisgs.dam.blog.mapper.CommentMapper;
import es.joseluisgs.dam.blog.model.Post;
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


public class CommentService extends BaseService<Comment, ObjectId, CommentRepository> {
    CommentMapper mapper = new CommentMapper();

    // Inyección de dependencias en el constructor. El servicio necesita este repositorio
    public CommentService(CommentRepository repository) {
        super(repository);
    }

    // Otras operaciones o especificaciones para CRUD
    // O podíamos mapear el nombre
    // O simplemente ocultar las que no queramos usar en niveles superiores
    public List<CommentDTO> getAllComments() throws SQLException {
        UserService userService = new UserService(new UserRepository());
        PostService postService = new PostService(new PostRepository());

        // Obtenemos la lista
        List<Comment> comments = this.findAll();
        List<CommentDTO> lista = new ArrayList<>();
        // Por cada commentario recuperamos su usuario y su post
        comments.forEach(c -> {
            CommentDTO commentDTO = mapper.toDTO(c);
            // Busco su usuario
            try {
                commentDTO.setUser(userService.getById(c.getUser()));
            } catch (SQLException e) {
                System.err.println("Error CommentService al buscar mi usuario autor con ID: " + c.getUser());
            }
            // Busco su post
            try {
                commentDTO.setPost(postService.getById(c.getPost()));
            } catch (SQLException e) {
                System.err.println("Error CommentService al buscar mi post con ID: " + c.getPost());
            }
            lista.add(commentDTO);
        });

        return lista;
    }

    public CommentDTO getCommentById(ObjectId id) throws SQLException {
        UserService userService = new UserService(new UserRepository());
        PostService postService = new PostService(new PostRepository());
        Comment comment = this.getById(id);
        CommentDTO commentDTO = mapper.toDTO(comment);
        // Busco su usuario
        try {
            commentDTO.setUser(userService.getById(comment.getUser()));
        } catch (SQLException e) {
            System.err.println("Error CommentService al buscar mi usuario autor con ID: " + comment.getUser());
        }
        // Busco su post
        try {
            commentDTO.setPost(postService.getById(comment.getPost()));
        } catch (SQLException e) {
            System.err.println("Error CommentService al buscar mi post con ID: " + comment.getPost());
        }
        return commentDTO;
    }

    public CommentDTO postComment(CommentDTO commentDTO) throws SQLException {
        commentDTO.setFechaPublicacion(LocalDateTime.now());

        Comment comment = mapper.fromDTO(commentDTO);
        // Le ponemos el usuario
        comment.setUser(commentDTO.getUser().getId());
        // Le ponemos el post
        comment.setPost(commentDTO.getPost().getId());
        comment = this.save(comment);

        // No terminamos aqui. A usuario hay que insertarle la categoria
        commentDTO.getUser().getComments().add(comment.getId());
        UserService userService = new UserService(new UserRepository());
        userService.updateUser(commentDTO.getUser());

        // Ademas debemos insertárselo a POST
        commentDTO.getPost().getComments().add(comment.getId());
        PostService postService = new PostService(new PostRepository());
        postService.updatePost(commentDTO.getPost());

        CommentDTO finalComment  = mapper.toDTO(comment);
        finalComment.setUser(commentDTO.getUser());
        finalComment.setPost(commentDTO.getPost());
        return finalComment;
    }

    public CommentDTO updateComment(CommentDTO commentDTO) throws SQLException {
        Comment comment = this.update(mapper.fromDTO(commentDTO));
        CommentDTO res = mapper.toDTO(comment);
        return res;
    }

    public CommentDTO deleteComment(CommentDTO commentDTO) throws SQLException {
        Comment comment = this.delete(mapper.fromDTO(commentDTO));
        CommentDTO res = mapper.toDTO(comment);
        return res;
    }

    public Set<Comment> getUserComments(ObjectId userId) {
        return new HashSet(repository.getByUserId(userId));
    }

    public Set<Comment> getPostComments(ObjectId userId) {
        return new HashSet(repository.getByPostId(userId));
    }

    public Comment deleteCommentByID(ObjectId commentId) throws SQLException {
        return repository.deleteById(commentId);
    }

//    private User getUserById(Long id) throws SQLException {
//        UserService service = new UserService(new UserRepository());
//        return service.getById(id);
//    }
//
//    private Post getPostById(Long id) throws SQLException {
//        PostService service = new PostService(new PostRepository());
//        return service.getById(id);
//    }
//
//    public List<Comment> getCommentsByPost(Long id) throws SQLException {
//        return repository.getByPost(id);
//    }
}
