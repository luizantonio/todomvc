package br.com.todo.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.constraints.Size;

import org.primefaces.PrimeFaces;

import br.com.todo.model.Todo;
import br.com.todo.repository.TodoRepository;




//@Named
//@RequestScoped
//@ManagedBean(name="todoController")
public class BKP  implements java.io.Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected String message = null;
	@Size(min=2,max=20)
	protected String title = null;
	protected String title_update = null;
	protected boolean completed = false;
	protected boolean completedUpdate = false;
	protected int id = 0;
	private FacesMessage msg = null;
	protected List<Todo> todos = new ArrayList<Todo>();
	protected static Map<String, String> todosMap = null;
	protected String RETURN_VIEW = "index.xhtml";
	protected String BAC_VIEW = "index.xhtml";
	protected int registros = 0;
	protected boolean running = true;
	protected Todo todo = null;
	
	@Inject
    private TodoRepository service = new TodoRepository();
	
	@PostConstruct
	public void init() {

		System.out.println("TodoController bean executing!.. init\n");		
		
		/*
		 * FacesContext context = FacesContext.getCurrentInstance();
		 * context.getExternalContext() .getSessionMap().remove("todoController");
		 */
		
//		if(!FacesContext.getCurrentInstance().isPostback()) {
//            PrimeFaces.current().executeScript("alert('This onload script is added from backing bean.')");
//        }
		FacesContext.getCurrentInstance().getExternalContext()
		.getSessionMap().put("loginController", this);
		
		FacesContext.getCurrentInstance().getPartialViewContext().getRenderIds().add("formId:title");

		PrimeFaces.current().ajax().update("formId:title");
		this.LimparHttpSession();
		
		if(this.todos == null) {
			this.todos = new ArrayList<Todo>();
		}
		try {
			this.todos = this.service.findAll();
			//Verifica se todos eh null, se sim seta para  Lista vazia
			if(this.todo == null) {
				this.todos = new ArrayList<Todo>();
			}
		}catch(Exception ex){
			
		}
		
		
	}
	
	//@PostConstruct
    //public void post()
    //{   
		
    //}
	
	public String welcome() {
		msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
				"0 Todos cadastrados", null);									
		FacesContext.getCurrentInstance().addMessage(null, msg);
		
		HttpSession session = this.CriarHttpSession();
		if (session != null) {
			session.setAttribute("view", this.RETURN_VIEW);
		}
		
		FacesContext.getCurrentInstance().getExternalContext()
		.getSessionMap().put("todoController", this);
		
		return this.RETURN_VIEW;
	}
	public String show() {
		//this.todos = this.service.findAll();
		return "index.xhtml";
	}
	
	/**
	 * Adiciona um novo objeto todo no banco de dados
	 * Utiliza a class TodoRepository
	 * Method: addTodo - Salva novo Todo no Banco - Ação 
	 * para botão "Salvar" em index page
	 * @return String - path view
	 */
	public String addTodo() {	
		if(this.getTitle().trim().length() <= 0) {
			PrimeFaces.current().executeScript("alert('Operação realizada com sucesso!')");
			this.setRETURN_VIEW("index.xhtml?faces-redirect=true");		
			return this.RETURN_VIEW;	
		}
		HttpSession session = this.CriarHttpSession();
		// Limpar session com mensagens
		this.LimparHttpSession();
		// Verifica se todos list eh null e cria novo objeto
		if(this.todos == null) {
			this.todos = new ArrayList<Todo>();
		}
		// Novo objeto todo
		Todo todo = new Todo();
		todo.setTitle(this.getTitle() );
		todo.setCompleted(this.getCompleted() );			
		try {
			/*
			 * Operação realizada com o Repositorey
			 */
			this.service.save(todo);
		}
		catch (Exception e) {
			this.message = "Falha: O cadastro falhou!";
		}finally {
			if(!FacesContext.getCurrentInstance().isPostback()) {
		         PrimeFaces.current().executeScript("alert('"+ this.message +"')");
		    }
		}
		this.message = "Cadastro realizado com sucesso!";
		FacesContext.getCurrentInstance().addMessage(null, 
				new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso!", message));
		if(!FacesContext.getCurrentInstance().isPostback()) {
         PrimeFaces.current().executeScript("alert('Operação realizada com sucesso!')");
        }
		
		this.setRETURN_VIEW("index.xhtml?faces-redirect=true");		
		return this.RETURN_VIEW;
	}
	
	/**
	 * Edita o todo objeto - ação do notão para
	 * redirecionar para apágina de atualização 
	 * update.xhtml
	 * @param todo
	 * @return an string to redirect page
	 */
	public String editTodo(Todo todo) {	
		FacesContext.getCurrentInstance().getPartialViewContext().getRenderIds().add("foo:bar");

		PrimeFaces.current().ajax().update("foo:bar");
		
		HttpSession session = this.CriarHttpSession();
		// Limpar session com mensagens
		this.LimparHttpSession();
		if(todo == null) {
			this.setRETURN_VIEW("index.xhtml?faces-redirect=true");		
			return this.RETURN_VIEW;
		}
		if (session != null) {
			session.setAttribute("todo", todo);
			session.setAttribute("todoTipo", todo.getClass()
					.getName());
			session.setAttribute("update_todo_id", todo.getId());
			session.setAttribute("update_todo_title", todo.getTitle());
			session.setAttribute("voltarView", this.BAC_VIEW);
		}		
		ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();    
	    //String id_h = ec.getRequestParameterMap().get("id_h");
	    //System.out.println("\n id 137name=id_h id=next:  " + id_h);
		if(todo != null) {
			
			//System.out.println("id todo:" + todo.getTitle());
			this.setTodo(todo);
		}		
		FacesContext.getCurrentInstance().renderResponse(); 
		this.setRETURN_VIEW("update.xhtml?faces-redirect=true");		
		return this.RETURN_VIEW;
	}
	
	
	/**
	 * Remover o objeto todo utilizando o 
	 * Utiliza a class TodoRepository
	 * @param int - id do todo
	 * @return an string to redirect page
	 * index.xhtml
	 */
	public String removeTodo(int id) {
		HttpSession session = this.CriarHttpSession();
		// Limpar session com mensagens
		this.LimparHttpSession();
		//JOptionPane.showMessageDialog(null, id, "",JOptionPane.INFORMATION_MESSAGE);	
		boolean result = false;
		if(id > 0) {
			Todo todo = this.service.findOne(id);
			if(todo != null) {	
				/*
				 * Operação realizada com o Repositorey
				 */
				try {
					result = this.service.remove(todo);
				} catch (Exception e) {					
					e.printStackTrace();
				}
				if(result != true) {					
					String message = "Exclusão realizada com sucesso!";
					FacesMessage facesMessage =
							new FacesMessage(FacesMessage.SEVERITY_INFO, message, null);
					
					PrimeFaces.current()
					.executeScript("alert('TODO removido com sucesso!')");
					this.setRETURN_VIEW("index.xhtml?faces-redirect=true");		
					return this.RETURN_VIEW;
				}
			}
		}
		FacesContext.getCurrentInstance().renderResponse(); 
		this.setRETURN_VIEW("index.xhtml?faces-redirect=true");		
		return this.RETURN_VIEW;
	}

	/**
	 * Atualizar o objeto todo utilizando o 
	 * repository entidade
	 * @param 
	 * @return uma string para redirecionar
	 * para a página index.xhtml
	 */
	public String updateTodo() {
		//JOptionPane.showMessageDialog(null, this.getTitle(), "",JOptionPane.INFORMATION_MESSAGE);
		HttpSession session = this.CriarHttpSession();
		// Limpar session com mensagens
		this.LimparHttpSession();
		
		Todo todo = null;
		int result = 0;
		
		if(this.getTitle() == null) {
			this.setRETURN_VIEW("update.xhtml?faces-redirect=true");		
			return this.RETURN_VIEW;
		}
		
		if(this.getTitle().trim().length() <= 0) {
			if (session != null) {
				todo = (Todo) session.getAttribute("todo");
				this.setTitle(todo.getTitle());
			}
			this.setRETURN_VIEW("update.xhtml?faces-redirect=true");		
			return this.RETURN_VIEW;
			
		}
		if(this.getTitle() != null && this.getTitle().trim().length() > 0) {
			if (session != null) {
				todo = (Todo) session.getAttribute("todo");
				this.setId(todo.getId());
			}
			
			/*
			 * Operação realizada com o Repositorey
			 */
			try {
				result = this.service.update(this.getId(), this.getTitle());
			} catch (Exception e) {				
				e.printStackTrace();
			}
			if(result > 0) {
				PrimeFaces.current()
				.executeScript("alert('TODO atualizado com sucesso!')");
			}
		}
	
		if (session != null) {
			session.setAttribute("todo", null);
			session.setAttribute("todoTipo", null);
			session.setAttribute("update_todo_id", null);
		}
		FacesContext.getCurrentInstance().renderResponse();
		this.setRETURN_VIEW("index.xhtml?faces-redirect=true");		
		return this.RETURN_VIEW;
	}

	/**
	 * Cria uma nova instancia de HTTPSession
	 * @return HttpSession
	 */
	public HttpSession CriarHttpSession() {
		return (HttpSession) FacesContext
				.getCurrentInstance().getExternalContext()
				.getSession(true);
	}
	/**
	 * Limpa os objetos acessados por sessionScope
	 */
	public void LimparHttpSession() {
		HttpSession session = this.CriarHttpSession();
		if (session != null) {
			session.setAttribute("message_todo", null);
		}
	}
	

	/**
	 * Função para o Botão voltar
	 * @return uma string para redirecionar
	 * para a página index.xhtml
	 */
	public String voltar() {
		return BAC_VIEW+"?faces-redirect=true";	
    }
	
	/**
	 * Recupera o título via ajax
	 */
	public void handleKeyEvent() {
		title = title.toUpperCase();
    }
	
	public void addMessage() {
        String summary = completed ? "Checked" : "Unchecked";
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(summary));
    }
	
	public void processChecked() {
		
	}
	/**
	 * GETs e SETs
	 * @return
	 */
	public String getRETURN_VIEW() {
		return RETURN_VIEW;
	}
	
	public void setRETURN_VIEW(String rETURN_VIEW) {
		this.RETURN_VIEW = rETURN_VIEW;
	}
	
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public FacesMessage getMsg() {
		return msg;
	}

	public void setMsg(FacesMessage msg) {
		this.msg = msg;
	}

	public List<Todo> getTodos() {
		return todos;
	}

	public void setTodos(List<Todo> todos) {
		this.todos = todos;
	}

	public int getRegistros() {
		return registros;
	}

	public void setRegistros(int registros) {
		this.registros = registros;
	}

	public boolean isRunning() {
		return running;
	}

	public void setRunning(boolean running) {
		this.running = running;
	}

	public String getTitle() {
		HttpServletRequest  request = (HttpServletRequest) FacesContext.getCurrentInstance()
				.getExternalContext().getRequest();
		
		title = request.getParameter("formId:title");
		
		
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	
	public String getTitle_update() {
		return title_update;
	}

	public void setTitle_update(String title_update) {
		this.title_update = title_update;
	}

	public boolean getCompleted() {
		return completed;
	}

	public void setCompleted(boolean completed) {
		this.completed = completed;
	}
	

	public boolean isCompletedUpdate() {
		return completedUpdate;
	}

	public void setCompletedUpdate(boolean completedUpdate) {
		this.completedUpdate = completedUpdate;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public static Map<String, String> getTodosMap() {
		return todosMap;
	}

	public static void setTodosMap(Map<String, String> todosMap) {
		TodoController.todosMap = todosMap;
	}

	public Todo getTodo() {
		return todo;
	}

	public void setTodo(Todo todo) {
		this.todo = todo;
	}

	public String getBAC_VIEW() {
		return BAC_VIEW;
	}

	public void setBAC_VIEW(String bAC_VIEW) {
		BAC_VIEW = bAC_VIEW;
	}
	
}
