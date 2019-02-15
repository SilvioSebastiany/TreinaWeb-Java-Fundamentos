package br.com.treinaweb.agenda.fx;

import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import br.com.treinabeb.agenda.repositorios.interfaces.AgendaRepositorio;
import br.com.treinaweb.agenda.entidades.Contato;
import br.com.treinaweb.repositorios.impl.ContatoRepositorio;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class MainController implements Initializable {

	@FXML
	private TableView<Contato> tabelaContatos;
	@FXML
	private Button botaoInserir;
	@FXML
	private Button botaoAlterar;
	@FXML
	private Button botaoExcluir;
	@FXML
	private TextField txfNome;
	@FXML
	private TextField txfIdade;
	@FXML
	private TextField txfTelefone;
	@FXML
	private Button botaoSalvar;
	@FXML
	private Button botaoCancelar;
	
	private boolean ehInserir;
	
	private Contato contatoSelecionado;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		this.tabelaContatos.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		habilitarEdicaoAgenda(false);
//		this.tabelaContatos.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Contato>() {
//
//			@Override
//			public void changed(ObservableValue<? extends Contato> observable, Contato oldValue, Contato newValue) {
//				if (newValue != null) {
//					txfNome.setText(newValue.getNome());
//					txfIdade.setText(String.valueOf(newValue.getIdade()));
//					txfTelefone.setText(newValue.getTelefone());
//				}
//			}
//		});

		this.tabelaContatos.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {

			if (newValue != null) {
				txfNome.setText(newValue.getNome());
				txfIdade.setText(String.valueOf(newValue.getIdade()));
				txfTelefone.setText(newValue.getTelefone());
				this.contatoSelecionado = newValue;
			}

		});

		CarregarTabelaContatos();
	}
	
	public void botaoInserir_Action() {
		this.ehInserir = true;
		this.txfIdade.setText("");
		this.txfNome.setText("");
		this.txfTelefone.setText("");
		habilitarEdicaoAgenda(true);
	}
	
	public void botaoAlterar_Action() {
		this.ehInserir = false;
		habilitarEdicaoAgenda(true);
		this.txfIdade.setText(Integer.toString(this.contatoSelecionado.getIdade()));
		this.txfNome.setText(this.contatoSelecionado.getNome());
		this.txfTelefone.setText(this.contatoSelecionado.getTelefone());
	}
	
	public void botaoExcluir_Action() {
		Alert confimacao = new Alert(AlertType.CONFIRMATION);
		confimacao.setTitle("Confirmação");
		confimacao.setHeaderText("Confirmação da exclusão do contato");
		confimacao.setContentText("Tem certeza de que deseja excluir esse contato?");
		Optional<ButtonType> resultadoConfirmacao = confimacao.showAndWait();
		
		if (resultadoConfirmacao.isPresent() && resultadoConfirmacao.get() == ButtonType.OK) {
			AgendaRepositorio<Contato> repositorioContatos = new ContatoRepositorio();
			repositorioContatos.excluir(contatoSelecionado);
			CarregarTabelaContatos();
			this.tabelaContatos.getSelectionModel().selectFirst();
		}
	}
	
	public void botaoCancelar_Action() {
		habilitarEdicaoAgenda(false);
		this.tabelaContatos.getSelectionModel().selectFirst();
	}
	
	public void botaoSalavar_Action() {
		AgendaRepositorio<Contato> repositorioContatos = new ContatoRepositorio();
		Contato contato = new Contato();
		contato.setNome(txfNome.getText());
		contato.setIdade(Integer.parseInt(txfIdade.getText()));
		contato.setTelefone(txfTelefone.getText());
		if (this.ehInserir) {
			repositorioContatos.inserir(contato);
		} else {
			repositorioContatos.atualizar(contato);
		}
		habilitarEdicaoAgenda(false);
		CarregarTabelaContatos();
		this.tabelaContatos.getSelectionModel().selectFirst();
	}
	
	private void CarregarTabelaContatos() {
		AgendaRepositorio<Contato> repositorioContatos = new ContatoRepositorio();
		List<Contato> contatos = repositorioContatos.selecionar();
		if (contatos.isEmpty()) {
			Contato contato = new Contato();
			contato.setNome("Silvio");
			contato.setIdade(25);
			contato.setTelefone("980417650");
			contatos.add(contato);
		}
		ObservableList<Contato> contatosObservableList = FXCollections.observableArrayList(contatos);
		this.tabelaContatos.getItems().setAll(contatosObservableList);
	}

	private void habilitarEdicaoAgenda(Boolean edicaoHabilitar) {
		this.txfNome.setDisable(!edicaoHabilitar);
		this.txfIdade.setDisable(!edicaoHabilitar);
		this.txfTelefone.setDisable(!edicaoHabilitar);
		this.botaoSalvar.setDisable(!edicaoHabilitar);
		this.botaoCancelar.setDisable(!edicaoHabilitar);
		this.botaoAlterar.setDisable(edicaoHabilitar);
		this.botaoExcluir.setDisable(edicaoHabilitar);
		this.botaoInserir.setDisable(edicaoHabilitar);
		this.tabelaContatos.setDisable(edicaoHabilitar);
	}
}
