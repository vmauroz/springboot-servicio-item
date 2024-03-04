package com.ms.app.item.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.ms.app.item.model.Producto;

@FeignClient( name = "microservicio-producto") //, url="localhost:8001")
public interface IProductoClienteRestFeign {
	
	@GetMapping( "/listar") // Hace referencia al mapping del servicio que se está consumiendo
	public List<Producto> listar();
	
	@GetMapping("/ver/{id}")
	public Producto detalle( @PathVariable Long id);
	
	@PostMapping( "/crear")
	public Producto crear( @RequestBody Producto producto);
	
	@PutMapping( "/editar/{id}")
	public Producto editar( @RequestBody Producto producto, @PathVariable Long id);

	@DeleteMapping( "/eliminar/{id}")
	public void eliminar( @PathVariable long id);
	
}
