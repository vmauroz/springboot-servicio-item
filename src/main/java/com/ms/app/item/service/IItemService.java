package com.ms.app.item.service;

import java.util.List;

import com.ms.app.item.model.Item;
import com.ms.app.item.model.Producto;

public interface IItemService {
	public List<Item> findAll();
	public Item findById( Long id, Integer cantidad);
	
	public Producto save( Producto producto);
	public Producto editar( Producto producto, Long id);
	public void eliminar( Long id);
	

}
