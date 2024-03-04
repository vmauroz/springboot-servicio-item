package com.ms.app.item.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ms.app.item.client.IProductoClienteRestFeign;
import com.ms.app.item.model.Item;
import com.ms.app.item.model.Producto;

@Service("serviceRestFeign")
//@Primary //deja este servicio como primarios, si hay 2 iguales deja este, reemplaza al qualifier
public class ItemServiceFeignImpl implements IItemService {

	@Autowired
	private IProductoClienteRestFeign clienteFeign;
	
	@Override
	public List<Item> findAll() {	
		return clienteFeign.listar().stream().map( p -> new Item(p, 1)).collect(Collectors.toList());
	}

	@Override
	public Item findById(Long id, Integer cantidad) {
		return new Item( clienteFeign.detalle(id), cantidad);
	}

	@Override
	public Producto save(Producto producto) {
		return clienteFeign.crear(producto);
	}

	@Override
	public Producto editar(Producto producto, Long id) {
		return clienteFeign.editar(producto, id);
	}

	@Override
	public void eliminar(Long id) {
		clienteFeign.eliminar(0);
		
	}
}
