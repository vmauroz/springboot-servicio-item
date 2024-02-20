package com.ms.app.item.service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.ms.app.item.model.Item;
import com.ms.app.item.model.Producto;

@Service("serviceRestTemplate") //Steretype component de spring
public class ItemServiceImpl implements IItemService {
	
	@Autowired
	private RestTemplate registrarRestTemplate;

	@Override
	public List<Item> findAll() {
		List<Producto> productos = Arrays.asList( registrarRestTemplate.getForObject("http://localhost:8001/listar", Producto[].class));
		
		return productos.stream().map( p -> new Item(p, 1)).collect(Collectors.toList());
	}

	@Override
	public Item findById(Long id, Integer cantidad) {
		// setea los parámetros para pasar por URL
		Map<String, String> pathVariables = new HashMap<String, String>();
		pathVariables.put("id", id.toString());
		
		Producto producto = registrarRestTemplate.getForObject("http://localhost:8001/ver/{id}", Producto.class, pathVariables);
		
		return new Item( producto, cantidad);
	}
}
