package br.com.treinaweb.repositorios.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import br.com.treinabeb.agenda.repositorios.interfaces.AgendaRepositorio;
import br.com.treinaweb.agenda.entidades.Contato;

public class ContatoRepositorio implements AgendaRepositorio<Contato> {

	private static List<Contato> contatos = new ArrayList<Contato>();

	@Override
	public List<Contato> selecionar() {
		return contatos;
	}

	@Override
	public void inserir(Contato entidade) {
		contatos.add(entidade);
	}

	@Override
	public void atualizar(Contato entidade) {
		Optional<Contato> original = contatos.stream().filter(contato -> contato.getNome().equals(entidade.getNome())).findFirst();
		if (original.isPresent()) {
			original.get().setIdade(entidade.getIdade());
			original.get().setTelefone(entidade.getTelefone());
		}
	}

	@Override
	public void excluir(Contato entidade) {
		contatos.remove(entidade);
	}

}
