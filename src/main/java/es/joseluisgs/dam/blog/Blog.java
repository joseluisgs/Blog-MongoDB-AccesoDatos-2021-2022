package es.joseluisgs.dam.blog;

import es.joseluisgs.dam.blog.controller.CategoryController;
import es.joseluisgs.dam.blog.controller.UserController;
import es.joseluisgs.dam.blog.database.MongoDBController;
import es.joseluisgs.dam.blog.dto.CategoryDTO;
import es.joseluisgs.dam.blog.dto.UserDTO;
import es.joseluisgs.dam.blog.model.Category;
import es.joseluisgs.dam.blog.model.Comment;
import es.joseluisgs.dam.blog.model.Post;
import es.joseluisgs.dam.blog.model.User;
import es.joseluisgs.dam.blog.repository.CategoryRepository;
import es.joseluisgs.dam.blog.repository.CommentRepository;
import es.joseluisgs.dam.blog.repository.PostRepository;
import es.joseluisgs.dam.blog.repository.UserRepository;
import org.bson.types.ObjectId;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.*;

public class Blog {
    private static Blog instance;

    private Blog() {
    }

    public static Blog getInstance() {
        if (instance == null) {
            instance = new Blog();
        }
        return instance;
    }

    public void initDataBase() {
        // Borramos los datos previos
        removeData();

        // Categorías
        System.out.println("Insertando Categorías de Ejemplo");
        CategoryRepository categoryRepository = new CategoryRepository();
        Category c1 = new Category("General");
        Category c2 = new Category("Dudas");
        Category c3 = new Category("Evaluación");
        Category c4 = new Category("Pruebas");
        try {
            categoryRepository.save(c1);
            categoryRepository.save(c2);
            categoryRepository.save(c3);
            categoryRepository.save(c4);
        } catch (SQLException e) {
           System.err.println("Error al insertar datos de ejemplo de Categorías: " + e.getMessage());
        }

        // Usuarios
        System.out.println("Insertando Usuarios de Ejemplo");
        UserRepository userRepository = new UserRepository();
        User u1 = new User("Pepe Perez","pepe@pepe.es","1234");
        User u2 = new User("Ana Anaya","ana@anaya.es","1234");
        User u3 = new User("Paco Perez","paco@perez.es","1234");
        User u4 = new User("Son Goku","goku@dragonball.es","1234");
        User u5 = new User("Chuck Norris","chuck@norris.es","1234");

        try {
            userRepository.save(u1);
            userRepository.save(u2);
            userRepository.save(u3);
            userRepository.save(u4);
            userRepository.save(u5);
        } catch (SQLException e) {
            System.err.println("Error al insertar datos de ejemplo de Usuarios: " + e.getMessage());
        }

        // Post
        System.out.println("Insertando Post de Ejemplo");
        PostRepository postRepository = new PostRepository();
        Post p1 = new Post("Post num 1", "http://post1.com", "Este es el post num 1", u1.getId(), c1.getId());
        Post p2 = new Post("Post num 2", "http://post2.com", "Este es el post num 2", u2.getId(), c2.getId());
        Post p3 = new Post("Post num 3", "http://post3.com", "Este es el post num 3", u3.getId(), c3.getId());
        Post p4 = new Post("Post num 4", "http://post4.com", "Este es el post num 4", u1.getId(), c1.getId());
        Post p5 = new Post("Post num 5", "http://post5.com", "Este es el post num 5", u2.getId(), c3.getId());

        try {
            postRepository.save(p1);
            postRepository.save(p2);
            postRepository.save(p3);
            postRepository.save(p4);
            postRepository.save(p5);

            // Actualizamos los usaurios, bidireccionalidad
            Set<ObjectId> posts = new HashSet<>();
            // usuario 1
            posts.add(p1.getId());
            posts.add(p4.getId());
            u1.setPosts(posts);
            userRepository.update(u1);
            //Usuario 2
            posts = new HashSet<>();
            posts.add(p2.getId());
            posts.add(p5.getId());
            u2.setPosts(posts);
            userRepository.update(u2);
            //Usuario 3
            posts = new HashSet<>();
            posts.add(p3.getId());
            u3.setPosts(posts);
            userRepository.update(u3);

        } catch (SQLException e) {
            System.err.println("Error al insertar datos de ejemplo de Posts: " + e.getMessage());
        }

        // Comentarios
        System.out.println("Insertando Comentarios de Ejemplo");
        CommentRepository commentRepository = new CommentRepository();


        Comment cm1 = new Comment("Comentario 01,", u1.getId(), p1.getId());
        Comment cm2 = new Comment("Comentario 02,", u2.getId(), p2.getId());
        Comment cm3 = new Comment("Comentario 03,", u3.getId(), p2.getId());
        Comment cm4 = new Comment("Comentario 04,", u1.getId(), p3.getId());
        Comment cm5 = new Comment("Comentario 05,", u4.getId(), p4.getId());
        Comment cm6 = new Comment("Comentario 06,", u1.getId(), p3.getId());
        Comment cm7 = new Comment("Comentario 07,", u4.getId(), p4.getId());
        Comment cm8 = new Comment("Comentario 08,", u2.getId(), p3.getId());

        try {
            commentRepository.save(cm1);
            commentRepository.save(cm2);
            commentRepository.save(cm3);
            commentRepository.save(cm4);
            commentRepository.save(cm5);
            commentRepository.save(cm6);
            commentRepository.save(cm7);
            commentRepository.save(cm8);

            // Actualizamos los usaurios, bidireccionalidad
            Set<ObjectId> comments = new HashSet<>();
            // usuario 1
            comments.add(cm1.getId());
            comments.add(cm4.getId());
            comments.add(cm6.getId());
            u1.setComments(comments);
            userRepository.update(u1);
            //Usuario 2
            comments = new HashSet<>();
            comments.add(cm2.getId());
            comments.add(cm8.getId());
            u2.setComments(comments);
            userRepository.update(u2);
            //Usuario 3
            comments = new HashSet<>();
            comments.add(cm3.getId());
            u3.setComments(comments);
            userRepository.update(u3);
            //Usuario 4
            comments = new HashSet<>();
            comments.add(cm5.getId());
            comments.add(cm7.getId());
            u4.setComments(comments);
            userRepository.update(u4);

            // Actualizamos los Post, bidireccionalidad
            // Post 1
            comments = new HashSet<>();
            comments.add(cm1.getId());
            p1.setComments(comments);
            postRepository.update(p1);
            // Post 2
            comments = new HashSet<>();
            comments.add(cm2.getId());
            comments.add(cm3.getId());
            p2.setComments(comments);
            postRepository.update(p2);
            // Post 3
            comments = new HashSet<>();
            comments.add(cm4.getId());
            comments.add(cm6.getId());
            comments.add(cm8.getId());
            p3.setComments(comments);
            postRepository.update(p3);
            // Post 4
            comments = new HashSet<>();
            comments.add(cm5.getId());
            comments.add(cm7.getId());;
            p4.setComments(comments);
            postRepository.update(p4);

        } catch (SQLException e) {
            System.err.println("Error al insertar datos de ejemplo de Comentarios: " + e.getMessage());
        }


    }

    private void removeData() {
        MongoDBController mongoController = MongoDBController.getInstance();
        mongoController.open();
        mongoController.removeDataBase("blog");
        mongoController.close();
    }

  public void Categories() {
        System.out.println("INICIO CATEGORIAS");

        CategoryController categoryController = CategoryController.getInstance();

        // Iniciamos las categorias

        System.out.println("GET Todas las categorías");
        List<CategoryDTO> lista = categoryController.getAllCategories();
        System.out.println(lista);

        System.out.println("GET Categoría con ID: " + lista.get(1).getId()); // Mira en el explorador a ver que categoría hay
        System.out.println(categoryController.getCategoryById(lista.get(1).getId()));


        System.out.println("POST Categoría");
        CategoryDTO categoryDTO1 = CategoryDTO.builder()
                .texto("Insert " + LocalDateTime.now())
                .build();
        categoryDTO1 = categoryController.postCategory(categoryDTO1);
        System.out.println(categoryDTO1);


        CategoryDTO categoryDTO2 = CategoryDTO.builder()
                .texto("Insert Otra " + LocalDateTime.now())
                .build();
        categoryDTO2 = categoryController.postCategory(categoryDTO2);
        System.out.println(categoryDTO2);

        System.out.println("UPDATE Categoría con ID:" + categoryDTO1.getId());
        Optional<CategoryDTO> optionalCategoryDTO = categoryController.getCategoryByIdOptional(categoryDTO1.getId());
        if (optionalCategoryDTO.isPresent()) {
            optionalCategoryDTO.get().setTexto("Update " + LocalDateTime.now());
            System.out.println(categoryController.updateCategory(optionalCategoryDTO.get()));
        }

        System.out.println("DELETE Categoría con ID: " + categoryDTO2.getId());
        optionalCategoryDTO = categoryController.getCategoryByIdOptional(categoryDTO2.getId());
        if (optionalCategoryDTO.isPresent()) {
            System.out.println(categoryController.deleteCategory(optionalCategoryDTO.get()));
        }

        System.out.println("FIN CATEGORIAS");
    }

    public void Users() {
        System.out.println("INICIO USUARIOS");

        UserController userController = UserController.getInstance();

        System.out.println("GET Todos los usuarios");
        List<UserDTO> lista = userController.getAllUsers();
        System.out.println(lista);

        System.out.println("GET Usuario con ID: " + lista.get(1).getId());
        System.out.println(userController.getUserById(lista.get(1).getId()));

        // Lo logico aquí es hacer un findBy algo que no sea el ID, con eso tenemos el objeto
        // Y con ello su id antes de ejecutarlo.

        System.out.println("POST nuevo Usuario 1");
        UserDTO userDTO1 = UserDTO.builder()
                .nombre("Insert " + LocalDateTime.now())
                .email("email" + LocalDateTime.now() + "@mail.com")
                .password("1234")
                .build();
        userDTO1 = userController.postUser(userDTO1);
        System.out.println(userDTO1);

        System.out.println("POST nuevo Usuario 2");
        UserDTO userDTO2 = UserDTO.builder()
                .nombre("Insert Otro" + LocalDateTime.now())
                .email("emailOtro" + LocalDateTime.now() + "@mail.com")
                .password("1234")
                .build();
        userDTO2 = userController.postUser(userDTO2);
        System.out.println(userDTO2);

        System.out.println("UPDATE Usuario con ID: " + userDTO1.getId());
        Optional<UserDTO> optionalUserDTO = userController.getUserByIdOptional(userDTO1.getId());
        if (optionalUserDTO.isPresent()) {
            optionalUserDTO.get().setNombre("Update " + LocalDateTime.now());
            optionalUserDTO.get().setEmail("emailUpdate" + LocalDateTime.now() + "@mail.com");
            System.out.println(userController.updateUser(optionalUserDTO.get()));
        }

        System.out.println("DELETE Usuario con ID: " + userDTO2.getId());
        optionalUserDTO = userController.getUserByIdOptional(userDTO2.getId());
        if (optionalUserDTO.isPresent()) {
            System.out.println(userController.deleteUser(optionalUserDTO.get()));
        }

        System.out.println("FIN USUARIOS");
    }
    /*


    public void Login() {
        System.out.println("INICIO LOGIN");

        LoginController loginController = LoginController.getInstance();

        System.out.println("Login con un usario que SI existe");
        Optional<LoginDTO> login = loginController.login("pepe@pepe.es", "1234");
        System.out.println(login.isPresent() ? "Login OK" : "Usuario o password incorrectos");

        System.out.println("Login con un usario que SI existe Y mal Password datos correctos");
        Optional<LoginDTO> login2 = loginController.login("pepe@pepe.es", "12555");
        System.out.println(login2.isPresent() ? "Login OK" : "Usuario o password incorrectos");

        System.out.println("Login con un usario que NO existe o mal Password datos correctos");
        login2 = loginController.login("pepe@pepe2.es", "12555");
        System.out.println(login2.isPresent() ? "Login OK" : "Usuario o password incorrectos");

        // System.out.println("Logout de usuario que está logueado");
        // System.out.println(loginController.logout(login.get().getId())? "Logout OK" : "Usuarios no logueado en el sistema"); // Mirar su ID

       System.out.println("Logout de usuario que no está logueado");
      System.out.println(loginController.logout(99999999L)? "Logout OK" : "Usuarios no logueado en el sistema");

        System.out.println("FIN LOGIN");
    }

    public void Posts() {
        System.out.println("INICIO POSTS");

        PostController postController = PostController.getInstance();

        System.out.println("GET Todos los Post");
        List<PostDTO> lista = postController.getAllPost();
        System.out.println(lista);

        System.out.println("GET Post con ID: " + lista.get(1).getId());
        System.out.println(postController.getPostById(lista.get(1).getId()));

        System.out.println("POST Insertando Post 1");
        // Lo primero que necesito es un usuario... busco uno ya de la lista
        User user = lista.get(3).getUser();
        // Y una categoría, busco una ya de la lista...
        Category category =lista.get(1).getCategory();

        // Neceistamos mapearlos a objetos y no DTO, no debería ser así y trabajar con DTO completos, pero no es tan crucial para el CRUD
        PostDTO postDTO1 = PostDTO.builder()
                .titulo("Insert 1 " + LocalDateTime.now())
                .contenido("Contenido " + Instant.now().toString())
                .url("http://" + Math.random() + ".dominio.com")
                .user(user)
                .category(category)
                .build();

        postDTO1 = postController.postPost(postDTO1);
        System.out.println(postDTO1);

        System.out.println("POST Insertando Post 2");

        user = lista.get(4).getUser();
        category = lista.get(0).getCategory();

        PostDTO postDTO2 = PostDTO.builder()
                .titulo("Insert Otro 2 " + LocalDateTime.now())
                .contenido("Contenido Otro" + Instant.now().toString())
                .url("http://" + Math.random() + ".dominio.com")
                .user(user)
                .category(category)
                .build();

        postDTO2 = postController.postPost(postDTO2);
        System.out.println(postDTO2);

        System.out.println("UPDATE Post con ID: " + lista.get(4).getId());
        Optional<PostDTO> optionalPostDTO = postController.getPostByIdOptional(lista.get(4).getId());
        if (optionalPostDTO.isPresent()) {
            optionalPostDTO.get().setTitulo("Update " + LocalDateTime.now());
            optionalPostDTO.get().setContenido("emailUpdate" + LocalDateTime.now() + "@mail.com");
            System.out.println(postController.updatePost(optionalPostDTO.get()));
        }

        System.out.println("DELETE Post con ID: " + postDTO2.getId());
        // No lo borra por la bidireccionalidad... Hay que borralo de usuario
        optionalPostDTO = postController.getPostByIdOptional(postDTO2.getId());
        if (optionalPostDTO.isPresent()) {
            System.out.println(postController.deletePost(optionalPostDTO.get()));
        }

        System.out.println("GET Usuario de Post: " + postDTO1.getId() + " usando la Relación Post --> Usuario");
        System.out.println(postDTO1.getUser());

        System.out.println("GET Posts con User ID: " + postDTO1.getUser().getId() + " usando la Relación Post --> Usuario");
        // postController.getPostByUserId(postDTO1.getUser().getId()).forEach(System.out::println);
        // No deja hacerla porque JPA de Mongo no permite hacer las relaciones en la consulta ;)
        // Habria que cambiarlo en el servicio donde se hace esta consulta
        PostDTO finalPostDTO = postDTO1;
        lista.stream().filter(p -> p.getUser().getId() == finalPostDTO.getUser().getId());

        System.out.println("GET By Post con User ID: " + postDTO1.getUser().getId() + "usando la Relación Usuario --> Post");
        // Por cierto, prueba quitando el FetchType.EAGER de getPost de User y mira que pasa. ¿Lo entiendes?
        postDTO1.getUser().getPosts().forEach(System.out::println);

        System.out.println("FIN POSTS");
    }

    public void Comments() {
        System.out.println("INICIO COMENTARIOS");

        CommentController commentController = CommentController.getInstance();

        System.out.println("GET Todos los Comentarios");
        List<CommentDTO> lista = commentController.getAllComments();
        System.out.println(lista);

        System.out.println("GET Comentario con ID: " + lista.get(1).getId());
        System.out.println(commentController.getCommentById(lista.get(1).getId()));

        System.out.println("POST Insertando Comentario 1");

        User user = lista.get(0).getUser(); // Sé que el id existe ...
        // Y un Post
        Post post = lista.get(0).getPost();

        CommentDTO commentDTO1 = CommentDTO.builder()
                .texto("Comentario 1 - " + Instant.now().toString())
                .user(user)
                .post(post)
                .build();
        commentDTO1 = commentController.postComment(commentDTO1);
        System.out.println(commentDTO1);

        System.out.println("POST Insertando Comentario 2");

        user = lista.get(3).getUser();
        // Y un Post
        post = lista.get(3).getPost();

        CommentDTO commentDTO2 = CommentDTO.builder()
                .texto("Comentario 2 - " + Instant.now().toString())
                .user(user)
                .post(post)
                .build();
        commentDTO2 = commentController.postComment(commentDTO2);
        System.out.println(commentDTO2);

        System.out.println("UPDATE Comentario con ID: " + commentDTO1.getId());
        Optional<CommentDTO> optionalCommentDTO = commentController.getCommentByIdOptional(commentDTO1.getId());
        if (optionalCommentDTO.isPresent()) {
            optionalCommentDTO.get().setTexto("Update " + LocalDateTime.now());
            System.out.println(commentController.updateComment(optionalCommentDTO.get()));
        }

        System.out.println("DELETE Comentario con ID: " + commentDTO2.getId());
        optionalCommentDTO = commentController.getCommentByIdOptional(commentDTO2.getId());
        if (optionalCommentDTO.isPresent()) {
            System.out.println(commentController.deleteComment(optionalCommentDTO.get()));
        }

        System.out.println("GET Dado un post ID: " + post.getId() + " Obtener sus Comentarios Post --> Comentarios");
        // No deja hacerlo porque JPA no permite Join con Mongo
        // postController.getPostById(2L).getComments().forEach(System.out::println);
        post.getComments().forEach(System.out::println);

        System.out.println("GET Dado un usuario ID: " + user.getId() + " obtener sus comentarios Usuario --> Comentarios");
        // JPA en Mongo no permite las Queris con Joins
        // userController.getUserById(1L).getComentarios().forEach(System.out::println);
        user.getComments().forEach(System.out::println);

        System.out.println("GET Dado un comentario ID: " + commentDTO1.getId() + " saber su Post Comentario --> Post");
        // System.out.println(commentController.getCommentById(2L).getPost());
        System.out.println(commentDTO1.getPost());

        System.out.println("GET Dado un comentario ID: " + commentDTO1.getId() + " saber su Autor Comentario --> Comentario");
        // System.out.println(commentController.getCommentById(2L).getUser());
        System.out.println(commentDTO1.getUser());

        System.out.println("DELETE Borrrando un post ID: " + post.getId() + " se borran sus comentarios? Post --> Comentario"); // Cascada
        PostController postController = PostController.getInstance();
        PostMapper postMapper = new PostMapper();
        System.out.println(postController.deletePost(postMapper.toDTO(post)));

        System.out.println("DELETE Borrrando un usuario usuario ID: " + user.getId() + "  se borran comentarios User --> Comentarios"); // Cascada
        // Cascada de post y de post comentarios
        UserController userController = UserController.getInstance();
        UserMapper userMapper = new UserMapper();
        System.out.println(userController.deleteUser(userMapper.toDTO(user)));

        System.out.println("FIN COMENTARIOS");
    }*/
}
