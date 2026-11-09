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


import com.example.demo.model.Producto;
import com.example.demo.model.Image;
import com.example.demo.repo.imagenRepo;
import com.example.demo.repo.productoRepo;

@RestController
public class ImagenController {

@Autowired
imagenRepo repo;
@Autowired
productoRepo repoProducto;
@GetMapping("imagenes")
  public List<Image> findAllimagenes() {
    return this.repo.findAll();
  }

@PostMapping("imagen")
  public Image addimagen(@RequestBody Image imagen) {

    return repo.save(imagen);
  }
  @PostMapping("imagen/{productId}")
  public ResponseEntity<Image> addimagen(@RequestBody Image imagen,@PathVariable String productId) {
    Optional<Producto> product = repoProducto.findById(productId);
    if (product.isEmpty()) {
      return ResponseEntity.notFound().build();
    }
    imagen.setProducto(product.get());
    return ResponseEntity.ok(repo.save(imagen));

  }
  @GetMapping("imagenes/producto/{productId}")
  public ResponseEntity<Set<Image>> getImagensByProduct(@PathVariable String productId) {
    Optional<Producto> product = repoProducto.findById(productId);
    if(product.isPresent()){
      return ResponseEntity.ok(product.get().getImagenes());
    }
    return ResponseEntity.notFound().build();

  }

@GetMapping("imagen/{id}")
public ResponseEntity<Image> getById(@PathVariable String id) {
    Optional<Image> result = repo.findById(id);
    if (result.isPresent()) {
      return ResponseEntity.ok(result.get());
    }
    return ResponseEntity.notFound().build();
}


@PutMapping("imagen/{id}")
public ResponseEntity<Image> putusuari(@PathVariable String id, @RequestBody Image imagen) {
    if (!repo.existsById(id)) {
      return ResponseEntity.notFound().build();
    }
    imagen.setId(id);
    return ResponseEntity.ok(repo.save(imagen));
}

@DeleteMapping("imagen/{id}")
public ResponseEntity<Void> deleteMailbox(@PathVariable String id) {
    if (!repo.existsById(id)) {
      return ResponseEntity.notFound().build();
    }
    repo.deleteById(id);
    return ResponseEntity.noContent().build();
}
}

