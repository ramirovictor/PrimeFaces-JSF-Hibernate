package dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import posjavamavemhibernate.HibernateUtil;

@SuppressWarnings("unchecked")
public class DaoGeneric<E> {

	private EntityManager entityManager = HibernateUtil.geEntityManager();

	public void salvar(E entidade) {
		EntityTransaction transaction = entityManager.getTransaction(); // Obejto de transação
		transaction.begin();// Inicia uma transação
		entityManager.persist(entidade);// Salva no banco de dados
		transaction.commit();// Grava no banco de dados
	}

	public E updateMerge(E entidade) { // Salva ou Atualiza
		EntityTransaction transaction = entityManager.getTransaction();
		transaction.begin();
		E entidadeSalva = entityManager.merge(entidade);// Salva, atualiza e retorna o objeto
		transaction.commit();

		return entidadeSalva;
	}

	public E pesquisar(E entidade) {
		Object id = HibernateUtil.getPrimaryKey(entidade);
		E e = (E) entityManager.find(entidade.getClass(), id);
		return e;

	}

	public E pesquisar(Long id, Class<E> entidade) {
		entityManager.clear();
		E e = (E) entityManager.createQuery("from " + entidade.getSimpleName() + " where id = " + id).getSingleResult();
		return e;

	}

	public void deletarPoId(E entidade) throws Exception {
		Object id = HibernateUtil.getPrimaryKey(entidade); // Obtem o ID do objeto PK
		EntityTransaction transaction = entityManager.getTransaction();// Obejeto de transação
		transaction.begin();// Começa uma Transação no banco de dados

		entityManager.createNativeQuery("delete from " + entidade.getClass(). // Monta a Query para delete
				getSimpleName().toLowerCase() + " where id =" + id).executeUpdate(); // Executa o delete no banco de
																						// dados
		transaction.commit();// Grava alteração no banco

	}

	public List<E> listar(Class<E> entidade) {
		EntityTransaction transaction = entityManager.getTransaction();
		transaction.begin();
		List<E> lista = entityManager.createQuery("from " + entidade.getName())// Cria a queru de consulta
				.getResultList();// Retorna a lista de objetos consultados
		transaction.commit();

		return lista;
	}

	public EntityManager getEntityManager() {
		return entityManager;
	}

}