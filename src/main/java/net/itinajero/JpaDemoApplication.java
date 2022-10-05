package net.itinajero;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import net.itinajero.model.Categoria;
import net.itinajero.model.Perfil;
import net.itinajero.model.Usuario;
import net.itinajero.model.Vacante;
import net.itinajero.repository.CategoriasRepository;
import net.itinajero.repository.PerfilesRepository;
import net.itinajero.repository.UsuariosRepository;
import net.itinajero.repository.VacantesRepository;

@SpringBootApplication
public class JpaDemoApplication implements CommandLineRunner{
	
	//Inyectando las isntancias de nuestros repositorios
	@Autowired
	private CategoriasRepository repoCategorias;
	
	@Autowired
	private VacantesRepository repoVacantes;
	
	@Autowired
	private UsuariosRepository repoUsuarios;
	
	@Autowired
	private PerfilesRepository repoPerfiles;
	
	
	public static void main(String[] args) {
		SpringApplication.run(JpaDemoApplication.class, args);
	}

	public void run(String... args) throws Exception {
		//buscarUsuario();
		//guardarTodas();
		//buscarVacantes();
		//guardarVacante();
		//crearPerfilesAplicacion();
		//crearUsuarioConDosPerfiles();
		//buscarUsuario();
		//buscarVacantesPorEstatus();
		//buscarVacantesPorDestacadoEstatus();
		//buscarVacantesSalario();
		buscarVacantesVariosEstatus();
	}
	
	//////////////////EJEMPLOS DE QUERY METHOD/////////////////////////////
	
	/**
	 * Query Method: Buscar Vacantes por varios Estatus (In)
	 */
	private void buscarVacantesVariosEstatus() {
		String[] estatus = new String[] {"Eliminada", "Aprobada"};//Arreglo definido con los diferentes estatus que vamos a buscar
		List<Vacante> lista = repoVacantes.findByEstatusIn(estatus);//Pasando el arreglo a nuestro metodo
		System.out.println("Registros encontrados: " + lista.size());//Desplegamos la cantidad de registros que fueron encontrados con esa caracteristica
		for (Vacante v : lista) {
			System.out.println(v.getId() + ": " + v.getNombre() + ": " + v.getEstatus());
		}
	}
	
	
	/**
	 * Query Method: Buscar Vacantes rango de Salario (Between)
	 */
	private void buscarVacantesSalario() {
		List<Vacante> lista = repoVacantes.findBySalarioBetweenOrderBySalarioDesc(7000, 14000);//Buscar salario que este entre el rango de 7000 y 14000
		System.out.println("Registros encontrados: " + lista.size());//Desplegamos la cantidad de registros que fueron encontrados con esa caracteristica
		for (Vacante v : lista) {
			System.out.println(v.getId() + ": " + v.getNombre() + ": $" + v.getSalario());
		}
	}
	
	
	/**
	 * Query Method: Buscar Vacantes por Destacado y Estatus Ordenado por Id Desc
	 */
	private void buscarVacantesPorDestacadoEstatus() {
		List<Vacante> lista = repoVacantes.findByDestacadoAndEstatusOrderByIdDesc(1, "Aprobada");
		System.out.println("Registros encontrados: " + lista.size());//Desplegamos la cantidad de registros que fueron encontrados con esa caracteristica
		for (Vacante v : lista) {
			System.out.println(v.getId() + ": " + v.getNombre() + ": " + v.getEstatus() + ":" + v.getDestacado());
		}
	}
	
	
	/**
	 * Query Method: Buscar Vacantes por Estatus
	 */
	private void buscarVacantesPorEstatus() {
		List<Vacante> lista = repoVacantes.findByEstatus("Eliminada");//Buscamos vacantes con estatus eliminada
		System.out.println("Registros encontrados: " + lista.size());//Desplegamos la cantidad de registros que fueron encontrados con esa caracteristica
		for (Vacante v : lista) { //recorremos la lista
			System.out.println(v.getId() + ": " + v.getNombre() + ": " + v.getEstatus());//imprimimos concatenando informacion
		}
	}
	
	/////////////////////////////////////////////////////////////////////////////////////////	
	
	/**
	 * Metodo para buscar un usuario y desplegar sus perfiles asociados.
	 */
	public void buscarUsuario() {
		Optional<Usuario> optional = repoUsuarios.findById(1);//buscamos el usuario con id
		if (optional.isPresent()) {//Condicional que pregunta si fue encontrado
			Usuario u = optional.get();//si se encontro declaramos variable de tipo usuario, le asignamos lo que nos regrese el metodo get que es el usuario
			System.out.println("Usuario: " + u.getNombre());//nombre del usuario
			System.out.println("Perfiles asignados");//perfiles asignados
			for (Perfil p : u.getPerfiles()) {//recorremos los perfiles que tiene
				System.out.println(p.getPerfil());//desplegamos cada perfil en la consola
			}
		}else {
			System.out.println("Usuario no encontrado");//se ejecuta si el id no fue encontrado
		}
	}
	
	
	/**
	 * Crear un usuario con 2 perfiles ("ADMINISTRADOR", "USUARIO")
	 */
	private void crearUsuarioConDosPerfiles() {
		Usuario user = new Usuario();
		user.setNombre("Ivan Tinajero");
		user.setEmail("ivanetinajero@gmail.com");
		user.setFechaRegistro(new Date());
		user.setUsername("itinajero");
		user.setPassword("12345");
		user.setEstatus(1);
		
		//Creamos los objetos de tipo perfil (dos)
		Perfil per1 = new Perfil();
		per1.setId(2);
		
		Perfil per2 = new Perfil();
		per2.setId(3);
		
		//Lo agregamos con el metodo agregar del modelo
		user.agregar(per1);
		user.agregar(per2);
		
		repoUsuarios.save(user); //Unicamente guardamos el obeto user y automaticamente se inserta los dos perfiles, debido a la configuracion del manytomany
	}
	
	
	/**
	 * Crear un usuario con 1 perfile ("USUARIO")
	 */
	private void crearUsuarioConUnPerfile() {
		//Creamos el objeto
		Usuario user = new Usuario();
		//propiedades de el objeto creado
		user.setNombre("Ivan Tinajero");
		user.setEmail("ivanetinajero@gmail.com");
		user.setFechaRegistro(new Date());
		user.setUsername("itinajero");
		user.setPassword("12345");
		user.setEstatus(1);
		
		//Asignamos el perfil
		Perfil per1 = new Perfil();
		per1.setId(2);//asignando el id
		
		//Lo agregamos con el metodo agregar del modelo
		user.agregar(per1);
		
		repoUsuarios.save(user); //Guardamos
	}
	
	
	/**
	 * Metodo para crear PERFILES / ROLES
	 */
	private void crearPerfilesAplicacion() {
		repoPerfiles.saveAll(getPerfilesAplicacion());//Llamos el repositorio y usamos el metodo saveAll y pasamos como parametro el metodo de getPerfiles.. que es una lista
		//De esta manera insertamos nuestros 3 perfiles a la base de datos 
	}
	
	
	/**
	 * Guardar una Vacante en la base de datos, usando el repositorio
	 */
	//Metodo
	private void guardarVacante() {
		//Crear el objeto de tipo vacante
		Vacante vacante = new Vacante();
		//Asignando los valores de el objeto vacante
		vacante.setNombre("Profesor de Matematicas");
		vacante.setDescripcion("Escuela primaria solicita profesor para curso de Matematicas");
		vacante.setFecha(new Date());
		vacante.setSalario(8500.0);
		vacante.setEstatus("Aprobada");
		vacante.setDestacado(0);
		vacante.setImagen("escuela.png");
		vacante.setDetalles("<h1>Los requisitos para profesor de Matematicas</h1>");
		
		//Asignando a que categoria pertenece
		Categoria cat = new Categoria(); //Creamos el objeto de tipo categoria
		cat.setId(15);//Le asignamos el valor en su id
		vacante.setCategoria(cat);// De esta manera relacionamos la vacante con nuestra categoria
		
		//Mandamos a llamar la repo, usamos save y guardamos el objeto vacante
		repoVacantes.save(vacante);
	}
	
	
	/**
	 * Este metodo permite buscar todas las vacantes en la base de datos
	 */
	private void buscarVacantes() {
		List<Vacante> lista = repoVacantes.findAll();
		//Renderizando la lista en consola
		for(Vacante v : lista) {
			System.out.println(v.getId() + " " + v.getNombre() + "-> " + v.getCategoria().getNombre());//Desplegamos el id, el nombre del vacante y además como realizamos la relacion uno a uno, podemos desplegar su categoria, en este caso el nombre
		}
	}
		
	
	/**
	 * Metodo findAll [Con paginacion y Ordenados] - Interfaz PagingAndSortingRepository
	 */
	private void buscarTodosPaginacionOrdenados() {
		Page<Categoria> page = repoCategorias.findAll(PageRequest.of(0, 5,Sort.by("nombre").descending()));
		System.out.println("Total Registros: " + page.getTotalElements());
		System.out.println("Total Paginas: " + page.getTotalPages());
		for (Categoria c : page.getContent()) {
			System.out.println(c.getId() + " " + c.getNombre());
		}
	}
	
	
	/**
	 * Metodo findAll [Con Paginación] - Interfaz PagingAndSortingRepository
	 */
	private void buscarTodosPaginacion() {
		Page<Categoria> page = repoCategorias.findAll(PageRequest.of(0, 5));
		System.out.println("Total Registros: " + page.getTotalElements());
		System.out.println("Total Paginas: " + page.getTotalPages());
		for (Categoria c : page.getContent()) {
			System.out.println(c.getId() + " " + c.getNombre());
		}
	}
	
	
	/**
	 * Metodo findAll [Ordenados por un campo] - Interfaz PagingAndSortingRepository
	 */
	private void buscarTodosOrdenados() {
		List<Categoria> categorias = repoCategorias.findAll(Sort.by("nombre").descending());
		for (Categoria c : categorias) {
			System.out.println(c.getId() + " " + c.getNombre());
		}
	}
	
	
	/**
	 * Metodo deleteAllInBatch [Usar con precaución] - Interfaz JpaRepository 
	 */
	private void borrarTodoEnBloque() {
		repoCategorias.deleteAllInBatch(); 
	}
	
	
	/**
	 * Metodo findAll - Interfaz JpaRepository
	 */
	private void buscarTodosJpa() {
		List<Categoria> categorias = repoCategorias.findAll();
		for (Categoria c : categorias) {
			System.out.println(c.getId() + " " + c.getNombre());
		}
	}
	
	////////////////////USO DE CRUDREPOSITORY//////////////////////////////////
	/**
	 * Metodo saveAll - Interfaz CrudRepository
	 */
	private void guardarTodas() {
		List<Categoria> categorias = getListaCategorias();
		repoCategorias.saveAll(categorias);
	}
	
	
	/**
	 * Metodo existsById - Interfaz CrudRepository
	 */
	private void existeId() {
		boolean existe = repoCategorias.existsById(50);
		System.out.println("La categoria existe: " + existe);
	}
	
	
	/**
	 * Metodo findAll - Interfaz CrudRepository
	 */
	private void buscarTodos() {
		Iterable<Categoria> categorias = repoCategorias.findAll();
		for (Categoria cat : categorias) {
			System.out.println(cat);
		}
	}
	
	
	/**
	 * Metodo findAllById - Interfaz CrudRepository
	 */
	private void encontrarPorIds() {
		List<Integer> ids = new LinkedList<Integer>();
		ids.add(1);
		ids.add(4);
		ids.add(10);
		Iterable<Categoria> categorias = repoCategorias.findAllById(ids);
		for (Categoria cat : categorias) {
			System.out.println(cat);
		}
	}
	
	
	/**
	 * Metodo deleteAll - Interfaz CrudRepository
	 */
	private void eliminarTodos() {
		repoCategorias.deleteAll();
	}
	
	
	/**
	 * Metodo count - Interfaz CrudRepository
	 */
	private void conteo() {
		long count = repoCategorias.count();
		System.out.println("Total Categorias: " + count);
	}
	
	
	/**
	 * Metodo deleteById - Interfaz CrudRepository
	 */
	private void eliminar() {
		int idCategoria = 1;
		repoCategorias.deleteById(idCategoria);
	}
	
	
	/**
	 * Metodo save(update) - Interfaz CrudRepository
	 */
	private void modificar() {
		Optional<Categoria> optional = repoCategorias.findById(2);
		if (optional.isPresent()) {
			Categoria catTmp = optional.get();
			catTmp.setNombre("Ing. de software");
			catTmp.setDescripcion("Desarrollo de sistemas");
			repoCategorias.save(catTmp);
			System.out.println(optional.get());
		}	
		else
			System.out.println("Categoria no encontrada");
	}
	
	
	/**
	 * Metodo findById - Interfaz CrudRepository
	 */
	private void buscarPorId() {
		Optional<Categoria> optional = repoCategorias.findById(5);
		if (optional.isPresent())
			System.out.println(optional.get());
		else
			System.out.println("Categoria no encontrada");
	}
	
	
	/**
	 * Metodo save - Interfaz CrudRepository
	 */
	private void guardar() {
		Categoria cat = new Categoria();
		cat.setNombre("Finanzas");
		cat.setDescripcion("Trabajos relacionados con finanzas y contabilidad");
		repoCategorias.save(cat);
		System.out.println(cat);		
	}
	
	/////////////METODOS QUE REGRESAN LAS LISTAS//////////////
	
	
	/**
	 * Metodo que regresa una lista de 3 Categorias
	 * @return
	 */
	private List<Categoria> getListaCategorias(){
		List<Categoria> lista = new LinkedList<Categoria>();
		// Categoria 1
		Categoria cat1 = new Categoria();
		cat1.setNombre("Programador de Blockchain");
		cat1.setDescripcion("Trabajos relacionados con Bitcoin y Criptomonedas");
		
		// Categoria 2
		Categoria cat2 = new Categoria();
		cat2.setNombre("Soldador/Pintura");
		cat2.setDescripcion("Trabajos relacionados con soldadura, pintura y enderezado");
						
		// Categoria 3
		Categoria cat3 = new Categoria();
		cat3.setNombre("Ingeniero Industrial");
		cat3.setDescripcion("Trabajos relacionados con Ingenieria industrial.");
		
		lista.add(cat1);
		lista.add(cat2);
		lista.add(cat3);
		return lista;
	}
	
	
	/**
	 * Metodo que regresa una lista de objetos de tipo Perfil que representa los diferentes PERFILES 
	 * O ROLES que tendremos en nuestra aplicación de Empleos
	 * @return
	 */
	private List<Perfil> getPerfilesAplicacion(){	
		//Creando una lista de tipo lista enlazada que contendra objetos de tipo perfil
		List<Perfil> lista = new LinkedList<Perfil>();
		//creamos perfil 1
		Perfil per1 = new Perfil();
		per1.setPerfil("SUPERVISOR");
		//creamos perfil 2
		Perfil per2 = new Perfil();
		per2.setPerfil("ADMINISTRADOR");
		//creamos perfil 3
		Perfil per3 = new Perfil();
		per3.setPerfil("USUARIO");
		
		//Los agregamos a la lista
		lista.add(per1);
		lista.add(per2);
		lista.add(per3);
		
		//Retornar la lista con los 3 objetos
		return lista;
	}
		
}
