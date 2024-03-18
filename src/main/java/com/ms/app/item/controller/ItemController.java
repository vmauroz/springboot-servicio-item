package com.ms.app.item.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.ms.app.item.model.Item;
import com.ms.app.item.service.IItemService;

import cl.ms.app.commons.model.entity.Producto;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;

@RefreshScope
@RestController
public class ItemController {
	
	private final Logger  logger =  LoggerFactory.getLogger( "ItemController");
	
	@Autowired
	private Environment env;
	
	@Value( "${configuracion.texto}")
	private String texto;
	
	@Autowired
	private CircuitBreakerFactory cbFactory;
	
	@Autowired
	@Qualifier( "serviceRestTemplate")
	private IItemService itemService;
	
	@Autowired
	@Qualifier( "serviceRestFeign")
	private IItemService itemServiceFeign;

	@GetMapping( "/listar")
	public List<Item> listar() 
	{
		return itemService.findAll();
	}
	
	@GetMapping( "/ver/{id}/cantidad/{cantidad}")
	public Item detalle( @PathVariable Long id, @PathVariable Integer cantidad) {
		return itemService.findById(id, cantidad);
	}
	
	@GetMapping( "/listarFeign")
	public List<Item> listarFeign(
			@RequestParam( name="nombre", required=false) String nombre,
			@RequestHeader( name="token_request", required=false) String token) {
		
		System.out.println( "Token: " + token );
		System.out.println( "Nombre: " + nombre );
		
		return itemServiceFeign.findAll();
	}
	
//	@HystrixCommand( fallbackMethod = "metodoAlternativo")
	@GetMapping( "/verFeign/{id}/cantidad/{cantidad}")
	public Item detalleFeign( @PathVariable Long id, @PathVariable Integer cantidad) {
		return cbFactory.create( "items")
				.run(() -> itemServiceFeign.findById(id, cantidad), 
					  e -> metodoAlternativo(id, cantidad, e)); 
//		return itemServiceFeign.findById(id, cantidad);
	}	
	
	@CircuitBreaker( name = "items", fallbackMethod = "metodoAlternativo")
	@GetMapping( "/verFeign2/{id}/cantidad/{cantidad}")
	public Item detalleFeign2( @PathVariable Long id, @PathVariable Integer cantidad) {
		return itemServiceFeign.findById(id, cantidad); 
	}
	
	@CircuitBreaker( name="items", fallbackMethod = "metodoAlternativo3")
	@TimeLimiter ( name = "items")
	@GetMapping( "/verFeign3/{id}/cantidad/{cantidad}")
	public CompletableFuture<Item> detalleFeign3( @PathVariable Long id, @PathVariable Integer cantidad) {
		return CompletableFuture.supplyAsync(() -> itemServiceFeign.findById(id, cantidad)); 
	}
	
	@GetMapping( "/obtener-config")
	public ResponseEntity<?> obtenerConfig( @Value("${server.port}") String puerto) {
		logger.info(texto);
		Map<String, String> json = new HashMap<String, String>();
		
		json.put( "texto", texto);
		json.put( "puerto", puerto);
		
		if ( env.getActiveProfiles().length > 0 && env.getActiveProfiles()[0].equals("dev")) {
			json.put( "autor.nombre", env.getProperty("configuracion.autor.name"));
			json.put( "autor.email",  env.getProperty("configuracion.autor.email"));
		}
		
		return new ResponseEntity<Map<String, String>>(json, HttpStatus.OK );
	}
	
	@PostMapping( "/crear")
	@ResponseStatus( HttpStatus.CREATED)
	public Producto crear( @RequestBody Producto producto) {
		return itemService.save(producto);
	}
	
	@PutMapping( "/editar/{id}")
	@ResponseStatus( HttpStatus.CREATED)
	public Producto editar( @RequestBody Producto producto, @PathVariable Long id) {
		return itemService.editar(producto, id);
	}
	
	@DeleteMapping( "/eliminar/{id}")
	@ResponseStatus( HttpStatus.NO_CONTENT)
	public void eliminar( @PathVariable Long id) {
		itemService.eliminar( id);
	}
	
	@PostMapping( "/feignCrear")
	@ResponseStatus( HttpStatus.CREATED)
	public Producto feignCrear( @RequestBody Producto producto) {
		return itemServiceFeign.save(producto);
	}
	
	@PutMapping( "/feignEditar/{id}")
	@ResponseStatus( HttpStatus.CREATED)
	public Producto feignEditar( @RequestBody Producto producto, @PathVariable Long id) {
		return itemServiceFeign.editar(producto, id);
	}
	
	@DeleteMapping( "/feignEliminar/{id}")
	@ResponseStatus( HttpStatus.NO_CONTENT)
	public void feignEliminar( @PathVariable Long id) {
		itemServiceFeign.eliminar( id);
	}
	
	public Item metodoAlternativo( Long id, Integer cantidad, Throwable e) {
		logger.info( e.getMessage());

		Item item = new Item();
		Producto producto = new Producto();
		
		item.setCantidad( cantidad);
		producto.setId( id);
		producto.setName("camara sony manual");
		producto.setPrecio(500.00);
		item.setProducto(producto);
		
		return item;
		
	}
	
	public CompletableFuture<Item> metodoAlternativo3( Long id, Integer cantidad, Throwable e) {
		logger.info( e.getMessage());

		Item item = new Item();
		Producto producto = new Producto();
		
		item.setCantidad( cantidad);
		producto.setId( id);
		producto.setName("camara sony manual");
		producto.setPrecio(500.00);
		item.setProducto(producto);
		
		return CompletableFuture.supplyAsync(() ->item);
		
	}
}
