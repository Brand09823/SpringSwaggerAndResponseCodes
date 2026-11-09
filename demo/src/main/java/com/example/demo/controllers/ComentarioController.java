package com.example.demo.controllers;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.Comment;
import com.example.demo.model.Producto;
import com.example.demo.repo.comentarioRepo;
import com.example.demo.repo.productoRepo;

@RestController
public class ComentarioController {

@Autowired
comentarioRepo repo;
@Autowired
productoRepo repoProducto;
@GetMapping("comentarios")
  public List<Comment> findAllcomentarios() {
    return this.repo.findAll();
  }

@PostMapping("comentario")
  public Comment addcomentario(@RequestBody Comment comentario) {

    return repo.save(comentario);
  }
  @PostMapping("comentario/{productId}")
  public ResponseEntity<Comment> addcomentario(@RequestBody Comment comentario,@PathVariable String productId) {
    Optional<Producto> product = repoProducto.findById(productId);
    if (product.isEmpty()) {
      return ResponseEntity.notFound().build();
    }
    comentario.setProducto(product.get());
    return ResponseEntity.ok(repo.save(comentario));

  }
  @GetMapping("comentarios/{productId}")
  public ResponseEntity<Set<Comment>> getCommentsByProduct(@PathVariable String productId) {
    Optional<Producto> product = repoProducto.findById(productId);
    if(product.isPresent()){
      return ResponseEntity.ok(product.get().getComentarios());
    }
    return ResponseEntity.notFound().build();

  }

@GetMapping("comentario/{id}")
public ResponseEntity<Comment> getById(@PathVariable String id) {
    Optional<Comment> result = repo.findById(id);
    if (result.isPresent()) {
      return ResponseEntity.ok(result.get());
    }
    return ResponseEntity.notFound().build();
}


@PutMapping("comentario/{id}")
public ResponseEntity<Comment> putusuari(@PathVariable String id, @RequestBody Comment comentario) {
    if (!repo.existsById(id)) {
      return ResponseEntity.notFound().build();
    }
    comentario.setId(id);
    return ResponseEntity.ok(repo.save(comentario));
}

@DeleteMapping("comentario/{id}")
public ResponseEntity<Void> deleteMailbox(@PathVariable String id) {
    if (!repo.existsById(id)) {
      return ResponseEntity.notFound().build();
    }
    repo.deleteById(id);
    return ResponseEntity.noContent().build();
}
}

