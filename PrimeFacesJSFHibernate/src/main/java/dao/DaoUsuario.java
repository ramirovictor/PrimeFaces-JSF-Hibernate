package dao;

import java.util.List;

import javax.persistence.Query;

import model.UsuarioPessoa;

public class DaoUsuario<E> extends DaoGeneric<UsuarioPessoa> {
	
	
	public void removerUsario(UsuarioPessoa pessoa) throws Exception{
		getEntityManager().getTransaction().begin();
		
		getEntityManager().remove(pessoa);
		
		getEntityManager().getTransaction().commit();
		
		super.deletarPoId(pessoa);
	}

	public List<UsuarioPessoa> pesquisar(String campoPesquisa) {
		// TODO Auto-generated method stub
		Query query = super.getEntityManager().createQuery("from UsuarioPessoa where nome like '%"+campoPesquisa+"%'");
		
		return query.getResultList();
	}

}
