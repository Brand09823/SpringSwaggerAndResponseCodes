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

import com.example.demo.model.Department;
import com.example.demo.model.Producto;
import com.example.demo.repo.categoriaRepo;
import com.example.demo.repo.productoRepo;

@RestController
public class CategoriaController {

@Autowired
categoriaRepo repo;
@Autowired
productoRepo repoProducto;
@GetMapping("categorias")
  public List<Department> findAllcategoria() {
    return this.repo.findAll();
  }

@PostMapping("categoria")
  public Department addcategoria(@RequestBody Department categoria) {

    return repo.save(categoria);
  }
  @PostMapping("addProductoCategoria/{productId}/{categoriaId}")
  public ResponseEntity<Department> addcategoria(@PathVariable String productId ,@PathVariable String categoriaId) {
    Optional<Producto> product = repoProducto.findById(productId);
    Optional<Department> category = repo.findById(categoriaId);
    if (product.isEmpty() || category.isEmpty()) {
      return ResponseEntity.notFound().build();
    }
    category.get().getProductos().add(product.get());
    return ResponseEntity.ok(repo.save(category.get()));

  }
  @GetMapping("categoria/producto/{productId}")
  public ResponseEntity<Set<Department>> getDepartmentsByProduct(@PathVariable String productId) {
    Optional<Producto> product = repoProducto.findById(productId);
    if(product.isPresent()){
      return ResponseEntity.ok(product.get().getDeparments());
    }
    return ResponseEntity.notFound().build();

  }

@GetMapping("categoria/{id}")
public ResponseEntity<Department> getById(@PathVariable String id) {
    Optional<Department> result = repo.findById(id);
    if (result.isPresent()) {
      return ResponseEntity.ok(result.get());
    }
    return ResponseEntity.notFound().build();
}


@PutMapping("categoria/{id}")
public ResponseEntity<Department> putDepartment(@PathVariable String id, @RequestBody Department categoria) {
    if (!repo.existsById(id)) {
      return ResponseEntity.notFound().build();
    }
    categoria.setId(id);
    return ResponseEntity.ok(repo.save(categoria));
}

@DeleteMapping("categoria/{id}")
public ResponseEntity<Void> deleteDeparment(@PathVariable String id) {
    if (!repo.existsById(id)) {
      return ResponseEntity.notFound().build();
    }
    repo.deleteById(id);
    return ResponseEntity.noContent().build();
}
}