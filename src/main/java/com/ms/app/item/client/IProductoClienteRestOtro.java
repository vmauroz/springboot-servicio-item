package com.ms.app.item.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import cl.ms.app.commons.model.entity.Producto;

//@FeignClient( name = "microservicio-producto")
public interface IProductoClienteRestOtro {
	
	@GetMapping( "/listar")
	public List<Producto> listar();
	
	@GetMapping("/ver/{id}")
	public Producto detalle( @PathVariable Long id);
}
