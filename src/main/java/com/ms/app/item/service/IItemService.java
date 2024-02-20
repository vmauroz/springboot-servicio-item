package com.ms.app.item.service;

import java.util.List;

import com.ms.app.item.model.Item;

public interface IItemService {
	public List<Item> findAll();
	public Item findById( Long id, Integer cantidad);

}
