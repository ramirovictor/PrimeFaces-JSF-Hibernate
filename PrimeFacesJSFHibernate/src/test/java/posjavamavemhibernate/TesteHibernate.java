package posjavamavemhibernate;
import java.util.List;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.junit.Test;

import dao.DaoGeneric;
import model.TelefoneUser;
import model.UsuarioPessoa;

public class TesteHibernate {

	@Test
	public void testeHibernateUtil() {
		
		DaoGeneric<UsuarioPessoa> daoGeneric =
				new DaoGeneric<UsuarioPessoa>();// Instância o DAO genérico
		UsuarioPessoa pessoa = new UsuarioPessoa(); // Cria o obejto para ser salvo

		// Seta todas as propriedades do objeto
		pessoa.setIdade(45);
		pessoa.setLogin("teste");
		pessoa.setNome("Paulo");
		pessoa.setSenha("123");
		pessoa.setSobrenome("Egidio");

		daoGeneric.salvar(pessoa);// Chama o salvar para gravar no banco de dados.

	}

	@Test
	public void testeBuscar() {
		DaoGeneric<UsuarioPessoa> daoGeneric = 
				new DaoGeneric<UsuarioPessoa>();// Inicia o DAO
		UsuarioPessoa pessoa = new UsuarioPessoa();// Inicia o Objeto 
		pessoa.setId(2L);// Precisamos apenas do ID

		pessoa = daoGeneric.pesquisar(pessoa); // Envia para pesquisa

		System.out.println(pessoa); // Imprime objeto para conferir

	}

	@Test
	public void testeBuscar2() {
		DaoGeneric<UsuarioPessoa> daoGeneric = new DaoGeneric<UsuarioPessoa>();

		UsuarioPessoa pessoa = daoGeneric.pesquisar(1L, UsuarioPessoa.class);

		System.out.println(pessoa);

	}

	@Test
	public void testeUpdate() {
		DaoGeneric<UsuarioPessoa> daoGeneric = new DaoGeneric<UsuarioPessoa>();

		UsuarioPessoa pessoa = daoGeneric.
				pesquisar(1L, UsuarioPessoa.class);// Carrega Objeto para editar

		pessoa.setIdade(99);// Atualiza os atributos
		pessoa.setNome("Nome atualizado Hibernate");
		pessoa.setSenha("sd4s5d4s4d");

		pessoa = daoGeneric.updateMerge(pessoa);// Atualiza no banco de dados.

		System.out.println(pessoa);

	}

	@Test
	public void testeDelete() {
		DaoGeneric<UsuarioPessoa> daoGeneric = new 
				DaoGeneric<UsuarioPessoa>();// Instancia o DAO

		UsuarioPessoa pessoa = daoGeneric.
				pesquisar(3L, UsuarioPessoa.class);// Consulta o objeto antes de remover

		try {
			daoGeneric.deletarPoId(pessoa);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}// Invoca o método de remoção do banco de dados

	}

	@Test
	public void testeConsultar() {
		DaoGeneric<UsuarioPessoa> daoGeneric = 
				new DaoGeneric<UsuarioPessoa>(); // Instanciar DAO

		List<UsuarioPessoa> list = daoGeneric.
				listar(UsuarioPessoa.class);// Invocar o método de lista passando a classe

		for (UsuarioPessoa usuarioPessoa : list) {// Percorrer a lista verificando se está correto
			System.out.println(usuarioPessoa);
			System.out.println("--------------------------------------------------");
		}

	}

	@Test
	public void testeQueryList() {
		DaoGeneric<UsuarioPessoa> daoGeneric = new DaoGeneric<UsuarioPessoa>();
		List<UsuarioPessoa> list = daoGeneric.getEntityManager()
				.createQuery(" from UsuarioPessoa where nome = 'Egidio'").getResultList();

		for (UsuarioPessoa usuarioPessoa : list) {
			System.out.println(usuarioPessoa);
		}

	}

	@SuppressWarnings("unchecked")
	
	@Test
	public void testeQueryListMaxResult() {
		DaoGeneric<UsuarioPessoa> daoGeneric = new DaoGeneric<UsuarioPessoa>();
		List<UsuarioPessoa> list = daoGeneric.getEntityManager().
				createQuery(" from UsuarioPessoa order by id")
				.setMaxResults(5).getResultList();

		for (UsuarioPessoa usuarioPessoa : list) {
			System.out.println(usuarioPessoa);
		}

	}

	@SuppressWarnings("unchecked")
	
	@Test
	public void testeQueryListParameter() {
		DaoGeneric<UsuarioPessoa> daoGeneric = new DaoGeneric<UsuarioPessoa>();

		List<UsuarioPessoa> list = daoGeneric.getEntityManager()
				.createQuery("from UsuarioPessoa where nome = :nome or sobrenome = :sobrenome")
				.setParameter("nome", "sdsd").
				 setParameter("sobrenome", "Alex")
				.getResultList();

		for (UsuarioPessoa usuarioPessoa : list) {
			System.out.println(usuarioPessoa);
		}
	}

	@Test
	public void testeQuerySomaIdade() {

		DaoGeneric<UsuarioPessoa> daoGeneric = new DaoGeneric<UsuarioPessoa>();

		Double somaIdade = (Double) daoGeneric.getEntityManager()
				.createQuery("select avg(u.idade) from UsuarioPessoa u ")
				.getSingleResult();

		System.out.println("Soma de todas as idades é --> " + somaIdade);

	}

	@SuppressWarnings("unchecked")
	
	
	@Test
	public void testeNameQUery1() {
		DaoGeneric<UsuarioPessoa> daoGeneric = new DaoGeneric<UsuarioPessoa>();

		List<UsuarioPessoa> list = daoGeneric.getEntityManager().
				createNamedQuery("UsuarioPessoa.todos")// Query escrita na entidade
				.getResultList();

		for (UsuarioPessoa usuarioPessoa : list) {
			System.out.println(usuarioPessoa);
		}

	}

	@SuppressWarnings("unchecked")
	
	@Test
	public void testeNameQUery2() {
		DaoGeneric<UsuarioPessoa> daoGeneric = new DaoGeneric<UsuarioPessoa>();

		List<UsuarioPessoa> list = daoGeneric.getEntityManager().
				createNamedQuery("UsuarioPessoa.buscaPorNome")
				.setParameter("nome", "Paulo")
				.getResultList();

		for (UsuarioPessoa usuarioPessoa : list) {
			System.out.println(usuarioPessoa);
		}

	}
	
	@Test
	public void testeGravaTelefone(){
		DaoGeneric daoGeneric = new DaoGeneric();
		
		UsuarioPessoa pessoa =  (UsuarioPessoa) daoGeneric.pesquisar(28L, UsuarioPessoa.class);
		
		TelefoneUser telefoneUser = new TelefoneUser();
		
		telefoneUser.setTipo("Casa");
		telefoneUser.setNumero("8778987987");
		telefoneUser.setUsuarioPessoa(pessoa);
		
		daoGeneric.salvar(telefoneUser);
		
	}
	
	
	
	@Test
	public void testeConsultaTelefones(){
		DaoGeneric daoGeneric = new DaoGeneric();
		
		UsuarioPessoa pessoa =  (UsuarioPessoa) daoGeneric.pesquisar(24L, UsuarioPessoa.class);
		
		for (TelefoneUser fone : pessoa.getTelefoneUsers()) {
			System.out.println(fone.getNumero());
			System.out.println(fone.getTipo());
			System.out.println(fone.getUsuarioPessoa().getNome());
			System.out.println("----------------------------------");
		}
		
	}
	
	@SuppressWarnings({ "rawtypes", "unused" })
	
	@Test
	public void testeCriteria1(){
		DaoGeneric daoGeneric = new DaoGeneric();
		
		CriteriaBuilder criteriaBuilder = daoGeneric.
								getEntityManager().getCriteriaBuilder();
		
		CriteriaQuery<UsuarioPessoa> criteriaQuery = criteriaBuilder.
									createQuery(UsuarioPessoa.class);
		
		Root<UsuarioPessoa> root = criteriaQuery.from(UsuarioPessoa.class);
		
		criteriaQuery.select(criteriaQuery.from(UsuarioPessoa.class));
		
		TypedQuery<UsuarioPessoa> query = daoGeneric.getEntityManager()
				       .createQuery(criteriaQuery);
		
		List<UsuarioPessoa> list = query.getResultList();
		
		for (UsuarioPessoa usuarioPessoa : list) {
			System.out.println(usuarioPessoa);
		}
		
	}
}


