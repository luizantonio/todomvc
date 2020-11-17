package br.com.todo.repository;

import java.util.List;

import br.com.todo.model.Todo;


public interface TodoRepositoryInterface {

	public boolean objExists(Todo todo);	

	public List<Todo> findAll();
		
	public Todo findOne(int i);
		
	public boolean save(Todo todo) throws Exception;
	
	public boolean remove(Todo todo) throws Exception;
	
	public int update(int id, String title)throws Exception ;
}
