package com.ms.app.item.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.ms.app.item.model.Producto;

@FeignClient( name = "microservicio-producto") //, url="localhost:8001")
public interface IProductoClienteRestFeign {
	
	@GetMapping( "/listar") // Hace referencia al mapping del servicio que se está consumiendo
	public List<Producto> listar();
	
	@GetMapping("/ver/{id}")
	public Producto detalle( @PathVariable Long id);
}
