package br.com.todo.repository;

import org.springframework.transaction.annotation.Transactional;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ApplicationScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.servlet.http.HttpSession;

import br.com.todo.model.Todo;
import br.com.todo.persistence.JPAUtil;

/***
 * TodoRepository class.  Classe para persistir os dados
 * @author luiz
 *
 */
@Named
@ApplicationScoped
public class TodoRepository implements TodoRepositoryInterface{
	
	public HttpSession criarHttpSession() {
		HttpSession session = (HttpSession) FacesContext
				.getCurrentInstance().getExternalContext()
				.getSession(true);
		return session;
	}
	/*
	* Verify if Todo exists by id
	*
	* @param Todo
	* @return boolean
	*/
	public boolean objExists(Todo todo) {
		
		EntityManager em = JPAUtil.getEntityManager();
	    String query = "select count(t) from Todo as t where t.id =:id";
	    // you will always get a single result
	    Long count = (Long) em.createQuery( query )
	    		.setParameter("id", todo.getId() )
	    		.getSingleResult();	    
	    System.out.println("count: ");
	    System.out.println(count);	    
	    em = null;
	    return ( ( count.equals( 0L ) ) ? false : true );
	}
	
	/*
	* Get All Todos - Recupera todos os todos
	*
	* @param
	* @return Collection
	*/
	public List<Todo> findAll() {
		EntityManager em = JPAUtil.getEntityManager();
		EntityTransaction tx = em.getTransaction();
    	CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<Todo> criteria = builder.createQuery(Todo.class);
        criteria.from(Todo.class);        
        return em.createQuery(criteria).getResultList();
		
	}
	/*
	* Get todo by id
	*
	* @param Int
	* @return Todo
	*/
	public Todo findOne(int i) {
		EntityManager em = JPAUtil.getEntityManager();		
		try {
			Todo todo = (Todo) em
					.createQuery(
							"SELECT todo from Todo as todo where todo.id =:id")
	                .setParameter("id", i).getSingleResult();			
			em.close();
			em= null;			
	        return todo;	        
	      } catch (NoResultException e) {
	            return null;
	      }finally {
	    	  //em.flush();
			  //em.close();
	      }
	}
	
	/**
	* Persiste objeto todo em DB
	*
	* @param Todo
	* @return boolean
	 * @throws Exception 
	*/
	public boolean save(Todo todo) throws Exception {
		HttpSession session = this.criarHttpSession();
		
		EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
		try {			
	        tx.begin();
	        em.persist(todo);
	        tx.commit();	        
	        em.close();
	        em = null;
		}catch( javax.persistence.PersistenceException  ex)
		{
			System.err.println("\n save todo:\n" + ex );
			if(tx == null) {
				tx.rollback();
				if (session != null) {
					session.setAttribute("message_todo",
							"O cadastro falhou! Não foi possível realizar o cadastro!");
				}
			}
			ex.printStackTrace();
			throw new Exception("Error ao criar todo!");
		}finally {
			//em.flush();
			//em.close();
		}
		String message = "Cadastro realizado com sucesso!";
		FacesMessage facesMessage =
				new FacesMessage(FacesMessage.SEVERITY_INFO, message, null);
		
		if (session != null) {
			session.setAttribute("message_todo", message);
		}
	    return true;
	}

	
	
	/**
	* Exclui objeto todo em DB
	*
	* @param Todo
	* @return boolean
	 * @throws Exception 
	*/
	public boolean remove(Todo todo) throws Exception {
		HttpSession session = this.criarHttpSession();
		
		EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = null;
	    if( this.objExists(todo)) {
			try {				
		        tx = em.getTransaction();
		        tx.begin();	      
		        //em.remove(todo);
		        em.remove(em.contains(todo) ? todo : em.merge(todo));
		        tx.commit();	        
		        em.close();
		        em = null;
			}catch( javax.persistence.PersistenceException  ex)
			{
				System.err.println("\n remove todo:\n" + ex );
				if (session != null) {
					session.setAttribute("message_todo",
							"Não foi possível realizar a operação!");
				}
				ex.printStackTrace();
				throw new Exception("Error ao excluir todo!");
				
			}finally {
		        //em.flush();
				//em.close();
			}
			String message = "Exclusão realizado com sucesso!";
			if (session != null) {
				session.setAttribute("message_todo", message);
				session.setAttribute("alert-info", "bg-success");
			}
		    return true;
	    }else {
	    	if (session != null) {
				session.setAttribute("message_todo",
						"O objeto não foi encontrado! Não foi possível realizar a operação!");
			}
	    }
	    return false;
		
	}
	
	/**
	* Update - Atualiza objeto todo em DB
	*
	* @param id - int, title - String
	* @return int
	 * @throws Exception 
	*/
	@Transactional
	public int update(int id, String title) throws Exception {
		System.out.println("update repo: \n");
		System.out.println(id);
		System.out.println(title);
		
		HttpSession session = (HttpSession) FacesContext
				.getCurrentInstance().getExternalContext()
				.getSession(true);
		if (id <= 0) {
			session.setAttribute("message_todo",
					"Erro: A ftualização falhou! Não foi possível realizar a atualização!");
			return 0;
		}
		if( this.findOne(id) == null) {
			session.setAttribute("message_todo",
					"Erro: A ftualização falhou! Não foi possível realizar a atualização!");
			return 0;
		}
		EntityManager em = JPAUtil.getEntityManager();	
		EntityTransaction tx = null;
		
		int result = 0;
		try {			
			tx = em.getTransaction();
			tx.begin();	
			result = em
			       .createQuery(
			           "UPDATE Todo SET title=:title WHERE id =:id")
			       .setParameter("id", id)
			       .setParameter("title", title)
			       .executeUpdate();
			
			
			tx.commit();	        
	        em.close();
	        em = null;		
	        System.out.println("Sucessful!");
	        if(result > 0) {
				if (session != null) {
					session.setAttribute("message_todo",
								"Atualização realizada com Sucessful!");
				}
			}
		} catch (NoResultException e) {
	    	System.err.println(e );
	    	if(tx == null) {
				tx.rollback();
				if (session != null) {
					session.setAttribute("message_todo",
							"O cadastro falhou! Não foi possível realizar o cadastro!");
				}
			}
			e.printStackTrace();
			throw new Exception("Error ao criar todo!");	    	
	    } catch (javax.persistence.PersistenceException pe) {
	    	System.err.println( pe );
		}finally {
	    	  //em.flush();
			  //em.close();
	    }
		if(result <= 0) {
			if (session != null) {
				session.setAttribute("message_todo",
							"A atualização falhou!");
			}
		}
		return result;
	}
}
