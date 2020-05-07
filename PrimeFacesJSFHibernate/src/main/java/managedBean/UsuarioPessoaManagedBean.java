package managedBean;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.DatatypeConverter;

import org.apache.tomcat.util.codec.binary.Base64;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.chart.BarChartModel;
import org.primefaces.model.chart.ChartSeries;

import com.google.gson.Gson;
import java.util.Map;
import dao.DaoEmail;
import dao.DaoUsuario;
import datatablelazy.LazyDataTableModelUserPessoa;
import model.EmailUser;
import model.UsuarioPessoa;

@ManagedBean(name = "usuarioPessoaManagedBean")
@ViewScoped
public class UsuarioPessoaManagedBean {

	private UsuarioPessoa usuarioPessoa = new UsuarioPessoa();
	private LazyDataTableModelUserPessoa<UsuarioPessoa> list = new LazyDataTableModelUserPessoa<UsuarioPessoa>();
	private DaoUsuario<UsuarioPessoa> daoGeneric = new DaoUsuario<UsuarioPessoa>();
	private BarChartModel barCharModel = new BarChartModel();
	private EmailUser emailuser= new EmailUser();
	private DaoEmail<EmailUser> daoEmail = new DaoEmail<EmailUser>();
	private String campoPesquisa;


	@PostConstruct
	public void init(){
		
		//list = daoGeneric.listar(UsuarioPessoa.class);
		list.load(0, 5, null, null, null);
		
		montarGrafico();
	}

	private void montarGrafico() {
		ChartSeries userSalario = new ChartSeries();//Grupo de funcionários
		for (UsuarioPessoa usuarioPessoa : list.list) {//add salário p o grupo
			userSalario.set(usuarioPessoa.getNome(), usuarioPessoa.getSalario());//add salários
		}
		barCharModel.addSeries(userSalario);//add o grupo no barModel
		barCharModel.setTitle("Gráfico de salários");
	}
	
	public void pesquisaCep(AjaxBehaviorEvent event) {
		try {
			
			URL url = new URL("https://viacep.com.br/ws/"+usuarioPessoa.getCep()+"/json/");
			URLConnection connection = url.openConnection();
			InputStream is = connection.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
			
			String cep ="";
			StringBuilder jsonCep = new StringBuilder();
			
			while((cep = br.readLine()) != null) {
				jsonCep.append(cep);
			}
			
			UsuarioPessoa userCepPessoa = new Gson().fromJson(jsonCep.toString(), UsuarioPessoa.class);
			
			usuarioPessoa.setCep(userCepPessoa.getCep());
			usuarioPessoa.setLogradouro(userCepPessoa.getLogradouro());
			usuarioPessoa.setComplemento(userCepPessoa.getComplemento());
			usuarioPessoa.setBairro(userCepPessoa.getBairro());
			usuarioPessoa.setLocalidade(userCepPessoa.getLocalidade());
			usuarioPessoa.setUf(userCepPessoa.getUf());
			usuarioPessoa.setUnidade(userCepPessoa.getUnidade());
			usuarioPessoa.setIbge(userCepPessoa.getIbge());
			usuarioPessoa.setGia(userCepPessoa.getGia());
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public BarChartModel getBarCharModel() {
		return barCharModel;
	}

	public UsuarioPessoa getUsuarioPessoa() {
		return usuarioPessoa;
	}

	public void setUsuarioPessoa(UsuarioPessoa usuarioPessoa) {
		this.usuarioPessoa = usuarioPessoa;
	}

	public String salvar() {
		daoGeneric.salvar(usuarioPessoa);
		list.list.add(usuarioPessoa);
		usuarioPessoa = new UsuarioPessoa();
		init();
		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, "Informação: ", "Salvo com sucesso!"));
		return "usuario-salvo";
	}

	public String novo() {
		usuarioPessoa = new UsuarioPessoa();
		return "";
	}

	public LazyDataTableModelUserPessoa<UsuarioPessoa> getList() {
		montarGrafico();
		return list;
	}

	public String remover() {

		try {
			daoGeneric.removerUsario(usuarioPessoa);
			list.list.remove(usuarioPessoa);
			usuarioPessoa = new UsuarioPessoa();
			
			init();
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, "Informação: ", "Removido com sucesso!"));

		} catch (Exception e) {
			if (e.getCause() instanceof org.hibernate.exception.ConstraintViolationException) {
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_INFO,
									"Informação: ", "Existem telefones para o usuario!"));
			}else {
				e.printStackTrace();
			}
		}

		return "";
	}
	
	public EmailUser getEmailuser() {
		return emailuser;
	}

	public void setEmailuser(EmailUser emailuser) {
		this.emailuser = emailuser;
	}
	 
	public void addEmail() {
		emailuser.setUsuarioPessoa(usuarioPessoa);
		emailuser = daoEmail.updateMerge(emailuser);
		usuarioPessoa.getEmails().add(emailuser);
		emailuser = new EmailUser();
		
		FacesContext.getCurrentInstance().addMessage(null, 
				new FacesMessage(FacesMessage.SEVERITY_INFO, "Resulta..", "salvo com sucesso!"));
	}
	
	public void removerEmail() throws Exception {
		String codigoemail = FacesContext.getCurrentInstance().getExternalContext()
				.getRequestParameterMap().get("codigoemail");
		EmailUser remover = new EmailUser();
		remover.setId(Long.parseLong(codigoemail));
		daoEmail.deletarPoId(remover);//remove do banco
		usuarioPessoa.getEmails().remove(remover);//remove da lista em memória
	
		
		FacesContext.getCurrentInstance().addMessage(null, 
				new FacesMessage(FacesMessage.SEVERITY_INFO, "Resulta..", "removido com sucesso!"));
	}
	
	public void pesquisar() {
		list.pesquisar(campoPesquisa);

		montarGrafico();
	}
	
	public void setCampoPesquisa(String campoPesquisa) {
		this.campoPesquisa = campoPesquisa;
	}
	
	public String getCampoPesquisa() {
		return campoPesquisa;
	}
	
	public void upload(FileUploadEvent image) {
		String imagem = "data:image/png;base64,"+ DatatypeConverter.printBase64Binary(image.getFile().getContents());
		usuarioPessoa.setImagem(imagem);
	}
	
	public void download() throws IOException {
		Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext()
				.getRequestParameterMap();
		String fileDownloadId = params.get("fileDownloadId");
		
		UsuarioPessoa pessoa = daoGeneric.pesquisar(Long.parseLong(fileDownloadId), UsuarioPessoa.class);
		
		byte [] imagem = new Base64().decodeBase64(pessoa.getImagem().split("\\.")[1]);
		
		HttpServletResponse response  =(HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
		
		response.addHeader("Content-Disposition", "attachment; filename=download.png");
		response.setContentType("application/octet-stream");
		response.setContentLengthLong(imagem.length);
		response.getOutputStream().write(imagem);
		response.getOutputStream().flush();
		FacesContext.getCurrentInstance().getResponseComplete();
	}

}
