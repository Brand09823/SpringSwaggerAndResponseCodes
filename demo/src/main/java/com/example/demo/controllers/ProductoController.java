package com.example.demo.controllers;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.example.demo.model.Producto;
import com.example.demo.repo.productoRepo;

@RestController
@Tag(name = "Productos", description = "Operaciones para consultar y administrar productos")
public class ProductoController {
  @Autowired
  productoRepo repo;

  @GetMapping("productos")
  @Operation(summary = "Listar productos")
  @ApiResponse(responseCode = "200", description = "Lista de productos, que puede estar vacía")
  public ResponseEntity<List<Producto>> findAllProductos() {
    return ResponseEntity.ok(repo.findAll());
  }

  @PostMapping("producto")
  @Operation(summary = "Crear un producto")
  @ApiResponses({
      @ApiResponse(responseCode = "201", description = "Producto creado; incluye el encabezado Location"),
      @ApiResponse(responseCode = "400", description = "Datos del producto no válidos")
  })
  public ResponseEntity<Producto> addProducto(@RequestBody Producto producto) {
    Producto saved = repo.save(producto);
    URI location = URI.create("/producto/" + saved.getId());
    return ResponseEntity.created(location).body(saved);
  }

  @GetMapping("producto/{id}")
  @Operation(summary = "Buscar un producto por ID")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Producto encontrado"),
      @ApiResponse(responseCode = "404", description = "No existe un producto con ese ID")
  })
  public ResponseEntity<Producto> getById(@PathVariable String id) {
    Optional<Producto> result = repo.findById(id);
    if (result.isPresent()) {
      return ResponseEntity.ok(result.get());
    }
    return ResponseEntity.notFound().build();
  }

  @PutMapping("producto/{id}")
  @Operation(summary = "Actualizar un producto existente")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Producto actualizado"),
      @ApiResponse(responseCode = "404", description = "No existe un producto con ese ID")
  })
  public ResponseEntity<Producto> putProduct(@PathVariable String id, @RequestBody Producto product) {
    if (!repo.existsById(id)) {
      return ResponseEntity.notFound().build();
    }
    product.setId(id);
    return ResponseEntity.ok(repo.save(product));
  }

  @DeleteMapping("producto/{id}")
  @Operation(summary = "Eliminar un producto")
  @ApiResponses({
      @ApiResponse(responseCode = "204", description = "Producto eliminado sin cuerpo de respuesta"),
      @ApiResponse(responseCode = "404", description = "No existe un producto con ese ID")
  })
  public ResponseEntity<Void> deleteProducto(@PathVariable String id) {
    if (!repo.existsById(id)) {
      return ResponseEntity.notFound().build();
    }
    repo.deleteById(id);
    return ResponseEntity.noContent().build();
  }
}

