/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Dao.AlunoDAO;
import Dao.AssuntoDAO;
import Dao.BancaDAO;
import Dao.BancaStatusDAO;
import Dao.ConvidadoDAO;
import Dao.CursoDAO;
import Dao.DefesaDAO;
import Dao.EmpresaDAO;
import Dao.EstagioDAO;
import Dao.ModalidadeDAO;
import Dao.PerfilDAO;
import Dao.ProfessorDAO;
import Dao.TrabalhoDAO;
import Dao.TrabalhoStatusDAO;
import Model.Aluno;
import Model.Assunto;
import Model.Banca;
import Model.Bancastatus;
import Model.Convidado;
import Model.Curso;
import Model.Defesa;
import Model.Empresa;
import Model.Estagio;
import Model.Modalidade;
import Model.Perfil;
import Model.Professor;
import Model.Trabalho;
import Model.Trabalhostatus;
import Util.CalendarFormat;
import Util.Functions;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author Alessandro
 */
@ManagedBean(name = "ImportarBean")
@SessionScoped
public class importarController {

    private String importacaoErrosAluno = "";
    private String importacaoErrosProfessor = "";
    private String importacaoErrosTrabalho = "";
    private String nomeArquivo = "";
    private UploadedFile arquivo;

    /**
     * função que direciona para pagina de visualizar trabalho
     *
     * @return
     */
    public String direcionaVisualizarImportacao() {
        return "importaDados.xhtml?facesRedirect=true";
    }

    /**
     * função para fazer upload do arquivo de Alunos
     *
     * @param event
     */
    public void uploadDadosAluno(FileUploadEvent event) {
        arquivo = event.getFile();
        nomeArquivo = event.getFile().getFileName();

        if (!nomeArquivo.equalsIgnoreCase("")) {
            List dataHolder = null;
            try {
                dataHolder = ReadFile(nomeArquivo, (FileInputStream) arquivo.getInputstream());
            } catch (IOException ex) {
                FacesContext.getCurrentInstance().addMessage("growlImportaAluno",
                        new FacesMessage(FacesMessage.SEVERITY_WARN, "Erro ao ler arquivo.", ""));
            }
            importarAluno(dataHolder);
            RequestContext rc = RequestContext.getCurrentInstance();
            rc.execute("PF('tabImportacao').select(0)");

        } else {
            FacesContext.getCurrentInstance().addMessage("growlImportaAluno",
                    new FacesMessage(FacesMessage.SEVERITY_WARN, "Selecione o arquivo em Excel.", ""));
        }

    }

    /**
     * função para fazer upload do arquivo de Professores
     *
     * @param event
     */
    public void uploadDadosProfessor(FileUploadEvent event) {
        arquivo = event.getFile();
        nomeArquivo = event.getFile().getFileName();

        if (!nomeArquivo.equalsIgnoreCase("")) {
            List dataHolder = null;
            try {
                dataHolder = ReadFile(nomeArquivo, (FileInputStream) arquivo.getInputstream());
            } catch (IOException ex) {
                FacesContext.getCurrentInstance().addMessage("growlImportacaoProfessor",
                        new FacesMessage(FacesMessage.SEVERITY_WARN, "Erro ao ler arquivo.", ""));
            }
            importarProfessor(dataHolder);
            RequestContext rc = RequestContext.getCurrentInstance();
            rc.execute("PF('tabImportacao').select(1)");

        } else {
            FacesContext.getCurrentInstance().addMessage("growlImportacaoProfessor",
                    new FacesMessage(FacesMessage.SEVERITY_WARN, "Selecione o arquivo em Excel.", ""));
        }

    }

    /**
     * função para fazer upload do arquivo de Trabalho
     *
     * @param event
     */
    public void uploadDadosTrabalho(FileUploadEvent event) {
        arquivo = event.getFile();
        nomeArquivo = event.getFile().getFileName();

        if (!nomeArquivo.equalsIgnoreCase("")) {
            List dataHolder = null;
            try {
                dataHolder = ReadFile(nomeArquivo, (FileInputStream) arquivo.getInputstream());
            } catch (IOException ex) {
                FacesContext.getCurrentInstance().addMessage("growlImportacaoTrabalho",
                        new FacesMessage(FacesMessage.SEVERITY_WARN, "Erro ao ler arquivo.", ""));
            }
            importarTrabalho(dataHolder);
            RequestContext rc = RequestContext.getCurrentInstance();
            rc.execute("PF('tabImportacao').select(2)");

        } else {
            FacesContext.getCurrentInstance().addMessage("growlImportacaoTrabalho",
                    new FacesMessage(FacesMessage.SEVERITY_WARN, "Selecione o arquivo em Excel.", ""));
        }

    }

    /**
     * função para ler um arquivo em Excel
     *
     * @param fileName
     * @param myInput
     * @return
     */
    @SuppressWarnings("unchecked")
    public List ReadFile(String fileName, FileInputStream myInput) {
        List cellVectorHolder = new ArrayList();
        try {
            //FileInputStream myInput = new FileInputStream(fileName);
            POIFSFileSystem myFileSystem = new POIFSFileSystem(myInput);
            HSSFWorkbook myWorkBook = new HSSFWorkbook(myFileSystem);
            HSSFSheet mySheet = myWorkBook.getSheetAt(0);
            Iterator rowIter = mySheet.rowIterator();
            while (rowIter.hasNext()) {
                HSSFRow myRow = (HSSFRow) rowIter.next();
                Iterator cellIter = myRow.cellIterator();
                List cellStoreVector = new ArrayList();
                while (cellIter.hasNext()) {
                    HSSFCell myCell = (HSSFCell) cellIter.next();
                    cellStoreVector.add(myCell);
                }
                cellVectorHolder.add(cellStoreVector);
            }
        } catch (Exception e) {
        }
        return cellVectorHolder;
    }

    /**
     * função para importar o arquivo de Alunos
     *
     */
    @SuppressWarnings("unchecked")
    private void importarAluno(List dataHolder) {

        String erros = "";
        //cameça na linha 1 porque a linha 0 é cabeçalho
        int qtdImportado = 0;
        String totalDeLinhas = String.valueOf(dataHolder.size() - 1);

        AlunoDAO aDAO = new AlunoDAO();
        for (int i = 1; i < dataHolder.size(); i++) {
            List linha = (List) dataHolder.get(i);
            Aluno aluno = new Aluno();
            Curso curso = new Curso();
            for (int col = 0; col < linha.size(); col++) {
                HSSFCell myCell = (HSSFCell) linha.get(col);
                myCell.setCellType(Cell.CELL_TYPE_STRING);
                String stringCellValue = myCell.toString().trim();

                switch (col) {
                    case 0:
                        aluno.setMatricula(stringCellValue.trim());
                        break;
                    case 1:
                        aluno.setNome(stringCellValue.trim());
                        break;
                    case 2:
                        if (!stringCellValue.trim().equalsIgnoreCase("") && stringCellValue.trim() != null) {
                            aluno.setEmail(stringCellValue.trim());
                        }
                        break;
                    case 3:
                        if (!stringCellValue.trim().equalsIgnoreCase("") && stringCellValue.trim() != null) {
                            String tel = Functions.formatString(stringCellValue.trim(), "(##) ####-####");
                            aluno.setTelefone(tel);
                        }
                        break;
                    case 4:
                        /*saber o curso*/
                        if (stringCellValue.equalsIgnoreCase("BCC")
                                || stringCellValue.equalsIgnoreCase("Ciência da Computação - Curriculo Antigo")) {
                            stringCellValue = "Ciência da Computação - Curriculo Antigo";
                        } else if (stringCellValue.equalsIgnoreCase("BCC")
                                || stringCellValue.equalsIgnoreCase("Ciência da Computação - Curriculo Novo")) {
                            stringCellValue = "Ciência da Computação - Curriculo Novo";
                        } else if (stringCellValue.equalsIgnoreCase("BCC")
                                || stringCellValue.equalsIgnoreCase("Ciência da Computação - Curriculo Antigo")) {
                            stringCellValue = "Ciência da Computação";
                        } else if (stringCellValue.equalsIgnoreCase("BSI")
                                || stringCellValue.equalsIgnoreCase("Sistemas de Informação")) {
                            stringCellValue = "Sistemas de Informação";
                        }
                        CursoDAO cDAO = new CursoDAO();
                        try {
                            curso = cDAO.getCursoByNomeCurso(stringCellValue);
                        } catch (Exception ex) {
                            FacesContext.getCurrentInstance().addMessage("growlImportaAluno",
                                    new FacesMessage(FacesMessage.SEVERITY_WARN, "Curso não encontrado!", ""));
                        }
                        cDAO.closeSession();

                        if (null != curso.getIdcurso()) {
                            aluno.setCursoidcurso(curso);

                            try {
                                Aluno alunoByMatricula = aDAO.getAlunoByMatricula(aluno.getMatricula());
                                if (alunoByMatricula == null) {
                                    //insere porque não existe na base
                                    boolean inserido = aDAO.insert(aluno);
                                    qtdImportado++;

                                } else {
                                    String novoErro = "Aluno " + aluno.getNome() + " já existe.";
                                    erros = erros + "\n" + novoErro;
                                }
                            } catch (Exception ex) {
                                FacesContext.getCurrentInstance().addMessage("growlImportaAluno",
                                        new FacesMessage(FacesMessage.SEVERITY_WARN, "Erro ao importar aluno" + aluno.getNome(), ""));
                            }
                        }
                        break;
                }
            }
        }

        aDAO.closeSession();
        this.setImportacaoErrosAluno(erros);
        if (qtdImportado > 0) {
            FacesContext.getCurrentInstance().addMessage("growlImportaAluno",
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Importação Realizada!\r\n Importado " + String.valueOf(qtdImportado) + " de " + totalDeLinhas, ""));
        } else {
            FacesContext.getCurrentInstance().addMessage("growlImportaAluno",
                    new FacesMessage(FacesMessage.SEVERITY_WARN, "Importação não realizada!", ""));
        }

    }

    /**
     * função para importar o arquivo de Professores
     *
     */
    @SuppressWarnings("unchecked")
    private void importarProfessor(List dataHolder) {
        String erros = "";
        //cameça na linha 1 porque a linha 0 é cabeçalho
        int qtdImportado = 0;
        String totalDeLinhas = String.valueOf(dataHolder.size() - 1);

        ProfessorDAO pDAO = new ProfessorDAO();
        for (int i = 1; i < dataHolder.size(); i++) {
            List linha = (List) dataHolder.get(i);
            Professor professor = new Professor();
            Perfil perfil = new Perfil();
            for (int col = 0; col < linha.size(); col++) {
                HSSFCell myCell = (HSSFCell) linha.get(col);
                myCell.setCellType(Cell.CELL_TYPE_STRING);
                String stringCellValue = myCell.toString().trim();

                switch (col) {
                    case 0:
                        professor.setSiape(Integer.parseInt(stringCellValue.trim()));
                        break;
                    case 1:
                        professor.setNome(stringCellValue.trim());
                        break;
                    case 2:
                        professor.setEmail(stringCellValue.trim());
                        break;
                    case 3:
                        if (!stringCellValue.trim().equalsIgnoreCase("") && stringCellValue.trim() != null) {
                            String tel = Functions.formatString(stringCellValue.trim(), "(##) ####-####");
                            professor.setTelefone(tel);
                        }
                        break;
                    case 4:
                        if (stringCellValue.equalsIgnoreCase("sim")) {
                            professor.setAtivo(true);
                        } else if (stringCellValue.equalsIgnoreCase("não")) {
                            professor.setAtivo(false);
                        }
                        break;
                    case 5:
                        PerfilDAO peDAO = new PerfilDAO();
                        try {
                            perfil = peDAO.getPerfilByNome(stringCellValue.trim());
                        } catch (Exception ex) {
                            FacesContext.getCurrentInstance().addMessage("growlImportacaoProfessor",
                                    new FacesMessage(FacesMessage.SEVERITY_WARN, "Perfil não encontrado!", ""));
                        }
                        peDAO.closeSession();

                        if (null != perfil.getNomeperfil()) {
                            professor.setPerfilidperfil(perfil);
                            try {
                                Professor professorBySiape = pDAO.getProfessorBySiape(professor.getSiape());
                                if (professorBySiape == null) {
                                    //insere porque não existe na base
                                    boolean inserido = pDAO.insert(professor);
                                    qtdImportado++;
                                } else {
                                    String novoErro = "Professor " + professor.getNome() + " já existe.";
                                    erros = erros + "\n" + novoErro;
                                }
                            } catch (Exception ex) {
                                FacesContext.getCurrentInstance().addMessage("growlImportacaoProfessor",
                                        new FacesMessage(FacesMessage.SEVERITY_WARN, "Erro ao importar professor " + professor.getNome(), ""));
                            }
                        }
                        break;
                }
            }
        }

        pDAO.closeSession();
        this.setImportacaoErrosProfessor(erros);

        if (qtdImportado > 0) {
            FacesContext.getCurrentInstance().addMessage("growlImportacaoProfessor",
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Importação Realizada!\r\n Importado " + String.valueOf(qtdImportado) + " de " + totalDeLinhas, ""));
        } else {
            FacesContext.getCurrentInstance().addMessage("growlImportacaoProfessor",
                    new FacesMessage(FacesMessage.SEVERITY_WARN, "Importação não realizada!", ""));
        }

    }

    public String getImportacaoErrosAluno() {
        return importacaoErrosAluno;
    }

    public void setImportacaoErrosAluno(String importacaoErrosAluno) {
        this.importacaoErrosAluno = importacaoErrosAluno;
    }

    public String getImportacaoErrosProfessor() {
        return importacaoErrosProfessor;
    }

    public void setImportacaoErrosProfessor(String importacaoErrosProfessor) {
        this.importacaoErrosProfessor = importacaoErrosProfessor;
    }

    public String getImportacaoErrosTrabalho() {
        return importacaoErrosTrabalho;
    }

    public void setImportacaoErrosTrabalho(String importacaoErrosTrabalho) {
        this.importacaoErrosTrabalho = importacaoErrosTrabalho;
    }

    /**
     * função para importar o arquivo de Trabalho
     *
     */
    @SuppressWarnings("unchecked")
    private void importarTrabalho(List dataHolder) {

        String erros = "";
        //cameça na linha 1 porque a linha 0 é cabeçalho
        int qtdImportado = 0;
        String totalDeLinhas = String.valueOf(dataHolder.size() - 1);

        for (int i = 1; i < dataHolder.size(); i++) {
            List linha = (List) dataHolder.get(i);
            Aluno aluno = new Aluno();
            Curso curso = new Curso();
            Trabalho trab = new Trabalho();
            Defesa defesa = new Defesa();
            Assunto assunto = new Assunto();
            Perfil perfil = new Perfil();
            Professor orientador = new Professor();
            Empresa empresa = new Empresa();
            Banca banca = new Banca();
            Bancastatus bancaStatus = new Bancastatus();
            Convidado conv1 = new Convidado();
            Convidado conv2 = new Convidado();
            Trabalhostatus trabStatus = new Trabalhostatus();
            Modalidade modalidade = new Modalidade();
            Estagio estagio = new Estagio();

            //----------------------------------------------------
            //tipo de estágio
            HSSFCell myCellEstagioTipo = (HSSFCell) linha.get(18);
            myCellEstagioTipo.setCellType(Cell.CELL_TYPE_STRING);
            String stringCellEstagioTipo = myCellEstagioTipo.toString().trim();
            //----------------------------------------------------

            //----------------------------------------------------
            //data defesa
            HSSFCell myCellDataDefesa = (HSSFCell) linha.get(12);
            myCellDataDefesa.setCellType(Cell.CELL_TYPE_STRING);
            String stringCellDataDefesa = myCellDataDefesa.toString().trim();
            //----------------------------------------------------

            for (int col = 0; col < linha.size(); col++) {
                HSSFCell myCell = (HSSFCell) linha.get(col);
                myCell.setCellType(Cell.CELL_TYPE_STRING);
                String stringCellValue = myCell.toString().trim();

                switch (col) {
                    case 0:
                        /*matricula aluno*/
                        aluno.setMatricula(stringCellValue);
                        break;
                    case 1:
                        /*nome aluno*/
                        aluno.setNome(stringCellValue);
                        break;
                    case 2:
                        /*saber o curso*/
                        if (stringCellValue.equalsIgnoreCase("BCC")
                                || stringCellValue.equalsIgnoreCase("Ciência da Computação - Curriculo Antigo")) {
                            stringCellValue = "Ciência da Computação - Curriculo Antigo";
                        } else if (stringCellValue.equalsIgnoreCase("BCC")
                                || stringCellValue.equalsIgnoreCase("Ciência da Computação - Curriculo Novo")) {
                            stringCellValue = "Ciência da Computação - Curriculo Novo";
                        } else if (stringCellValue.equalsIgnoreCase("BCC")
                                || stringCellValue.equalsIgnoreCase("Ciência da Computação - Curriculo Antigo")) {
                            stringCellValue = "Ciência da Computação";
                        } else if (stringCellValue.equalsIgnoreCase("BSI")
                                || stringCellValue.equalsIgnoreCase("Sistemas de Informação")) {
                            stringCellValue = "Sistemas de Informação";
                        }

                        CursoDAO cDAO = new CursoDAO();
                        try {
                            curso = cDAO.getCursoByNomeCurso(stringCellValue);
                        } catch (Exception ex) {
                            String novoErro = "Linha: " + i + " . Curso não encontrado.";
                            erros = erros + "\n" + novoErro;
                        }
                        cDAO.closeSession();
                        AlunoDAO aDAO = new AlunoDAO();
                        if (null != curso.getIdcurso()) {
                            aluno.setCursoidcurso(curso);

                            try {
                                Aluno alunoByMatricula = aDAO.getAlunoByMatricula(aluno.getMatricula());
                                if (alunoByMatricula == null) {
                                    //insere porque não existe na base
                                    boolean inserido = aDAO.insert(aluno);
                                }
                            } catch (Exception ex) {
                                String novoErro = "Linha: " + i + " . Erro ao encontrar aluno.";
                                erros = erros + "\n" + novoErro;

                            }
                        }
                        trab.setAlunomatricula(aluno);
                        aDAO.closeSession();
                        break;
                    case 3:
                        /*titulo trabalho*/
                        trab.setTitulo(stringCellValue);
                        break;
                    case 4:
                        /*data matricula do trabalho*/
                        trab.setDatamatricula(CalendarFormat.getDataISO(stringCellValue));
                        /*horas por dia*/
                        trab.setQtdhorasdia(CalendarFormat.parseTime("06:00:00"));
                        break;
                    case 5:
                        if (!stringCellEstagioTipo.equalsIgnoreCase("estágio não obrigatório")
                                && !stringCellEstagioTipo.equalsIgnoreCase("ESTAGIO_NAO_OBRIGATORIO")
                                && !stringCellEstagioTipo.equalsIgnoreCase("ESTAGIO NAO OBRIGATORIO")
                                && !stringCellDataDefesa.equalsIgnoreCase("\\0")
                                && !stringCellDataDefesa.equalsIgnoreCase("")) {
                            /*data minima do trabalho*/
                            defesa.setStatusdefesa(true);
                            defesa.setMatriculadatadefesa(trab.getDatamatricula());
                            defesa.setMindatadefesa(CalendarFormat.getDataISO(stringCellValue));
                        }
                        break;
                    case 6:
                        if (!stringCellEstagioTipo.equalsIgnoreCase("estágio não obrigatório")
                                && !stringCellEstagioTipo.equalsIgnoreCase("ESTAGIO_NAO_OBRIGATORIO")
                                && !stringCellEstagioTipo.equalsIgnoreCase("ESTAGIO NAO OBRIGATORIO")
                                && !stringCellDataDefesa.equalsIgnoreCase("\\0")
                                && !stringCellDataDefesa.equalsIgnoreCase("")) {
                            /*data maxima do trabalho*/
                            defesa.setMaxdatadefesa(CalendarFormat.getDataISO(stringCellValue));
                        } else {
                            /*insere a data maxima como data de finalização
                             para estagios não obrigatorios*/
                            trab.setDatafinalizacao(CalendarFormat.getDataISO(stringCellValue));
                        }
                        break;
                    case 7:
                        /*assunto/area do trabalho*/
                        assunto.setNome(stringCellValue);
                        AssuntoDAO asDAO = new AssuntoDAO();
                        Assunto assuntoByNome = null;
                        try {
                            assuntoByNome = asDAO.getAssuntoByNome(stringCellValue);
                        } catch (Exception ex) {
                            String novoErro = "Linha: " + i + " . Erro ao encontrar assunto.";
                            erros = erros + "\n" + novoErro;
                        }
                        if (assuntoByNome == null) {
                            try {
                                //insere porque não existe na base
                                boolean inserido = asDAO.insert(assunto);
                            } catch (Exception ex) {
                                String novoErro = "Linha: " + i + " . Erro salvar assunto.";
                                erros = erros + "\n" + novoErro;
                            }
                        }
                         {
                            try {
                                assunto = asDAO.getAssuntoByNome(stringCellValue);
                            } catch (Exception ex) {
                                String novoErro = "Linha: " + i + " . Erro ao encontrar assunto.";
                                erros = erros + "\n" + novoErro;
                            }
                        }
                        trab.setAssuntoidassunto(assunto);
                        asDAO.closeSession();
                        break;
                    case 8:
                        if (!stringCellValue.equalsIgnoreCase("")) {
                            orientador.setSiape(Integer.parseInt(stringCellValue));
                        }
                        break;
                    case 9:
                        PerfilDAO peDAO = new PerfilDAO();
                        ProfessorDAO pDAO = new ProfessorDAO();
                        Professor professorBySiape = null;

                        orientador.setNome(stringCellValue);

                        /*1º tentativa de achar o professor*/
                        if (orientador.getNome() != null) {
                            try {
                                professorBySiape = pDAO.getProfessorByNome(orientador.getNome());
                            } catch (Exception ex) {
                                String novoErro = "Linha: " + i + " . Erro ao encontrar professor.";
                                erros = erros + "\n" + novoErro;
                            }
                        }

                        /*2º tentativa de achar o professor*/
                        if (null == professorBySiape && orientador.getSiape() != null) {
                            try {
                                professorBySiape = pDAO.getProfessorBySiape(orientador.getSiape());
                            } catch (Exception ex) {
                                String novoErro = "Linha: " + i + " . Erro ao encontrar professor.";
                                erros = erros + "\n" + novoErro;
                            }
                        }

                        if (null == professorBySiape && !orientador.getNome().equalsIgnoreCase("") && !orientador.getNome().equalsIgnoreCase("\\0")) {
                            try {
                                perfil = peDAO.getPerfilByNome("Professor");
                            } catch (Exception ex) {
                                String novoErro = "Linha: " + i + " . Erro ao encontrar perfil.";
                                erros = erros + "\n" + novoErro;
                            }

                            orientador.setPerfilidperfil(perfil);

                            /*dar o maior siape ao novo professor*/
                            int siape = 0;
                            try {
                                siape = pDAO.getMaiorSiape();
                                siape++;
                            } catch (Exception ex) {
                                String novoErro = "Linha: " + i + " . Erro ao retornar siape.";
                                erros = erros + "\n" + novoErro;
                            }
                            orientador.setSiape(siape);
                            orientador.setAtivo(true);
                            try {
                                boolean inserido = pDAO.insert(orientador);
                            } catch (Exception ex) {
                                String novoErro = "Linha: " + i + " . Erro ao inserir orientador.";
                                erros = erros + "\n" + novoErro;
                            }
                        } else {
                            orientador = professorBySiape;
                        }

                        trab.setProfessorsiape(orientador);
                        peDAO.closeSession();
                        pDAO.closeSession();
                        break;
                    case 10:
                        EmpresaDAO eDAO = new EmpresaDAO();
                        empresa.setNome(stringCellValue);
                        Empresa empresaByNome = null;
                        try {
                            empresaByNome = eDAO.getEmpresaByNome(empresa.getNome());
                        } catch (Exception ex) {
                            Logger.getLogger(importarController.class.getName()).log(Level.SEVERE, null, ex);
                        }

                        if (empresaByNome == null) {
                            try {
                                eDAO.insert(empresa);
                            } catch (Exception ex) {
                                String novoErro = "Linha: " + i + " . Erro ao inserir empresa.";
                                erros = erros + "\n" + novoErro;
                            }
                            try {
                                empresa = eDAO.getEmpresaByNome(empresa.getNome());
                            } catch (Exception ex) {
                                String novoErro = "Linha: " + i + " . Erro ao encontrar empresa.";
                                erros = erros + "\n" + novoErro;
                            }
                        } else {
                            empresa = empresaByNome;
                        }

                        trab.setEmpresaidempresa(empresa);
                        eDAO.closeSession();
                        break;
                    case 11:
                        if (!stringCellEstagioTipo.equalsIgnoreCase("estágio não obrigatório")
                                && !stringCellEstagioTipo.equalsIgnoreCase("ESTAGIO_NAO_OBRIGATORIO")
                                && !stringCellEstagioTipo.equalsIgnoreCase("ESTAGIO NAO OBRIGATORIO")
                                && !stringCellDataDefesa.equalsIgnoreCase("\\0")
                                && !stringCellDataDefesa.equalsIgnoreCase("")) {
                            banca.setLocalbanca(stringCellValue);
                        }
                        break;
                    case 12:
                        if (!stringCellEstagioTipo.equalsIgnoreCase("estágio não obrigatório")
                                && !stringCellEstagioTipo.equalsIgnoreCase("ESTAGIO_NAO_OBRIGATORIO")
                                && !stringCellEstagioTipo.equalsIgnoreCase("ESTAGIO NAO OBRIGATORIO")
                                && !stringCellDataDefesa.equalsIgnoreCase("\\0")
                                && !stringCellDataDefesa.equalsIgnoreCase("")) {
                            banca.setDatabanca(CalendarFormat.getDataISO(stringCellValue));
                            banca.setDatafinalizacao(CalendarFormat.getDataISO(stringCellValue));
                            trab.setDatafinalizacao(CalendarFormat.getDataISO(stringCellValue));
                        }
                        break;
                    case 13:
                        if (!stringCellEstagioTipo.equalsIgnoreCase("estágio não obrigatório")
                                && !stringCellEstagioTipo.equalsIgnoreCase("ESTAGIO_NAO_OBRIGATORIO")
                                && !stringCellEstagioTipo.equalsIgnoreCase("ESTAGIO NAO OBRIGATORIO")
                                && !stringCellDataDefesa.equalsIgnoreCase("\\0")
                                && !stringCellDataDefesa.equalsIgnoreCase("")) {
                            banca.setHorario(CalendarFormat.parseTime(stringCellValue));
                        }
                        break;
                    case 14:
                        /*status da banca */
                        if (!stringCellEstagioTipo.equalsIgnoreCase("estágio não obrigatório")
                                && !stringCellEstagioTipo.equalsIgnoreCase("ESTAGIO_NAO_OBRIGATORIO")
                                && !stringCellEstagioTipo.equalsIgnoreCase("ESTAGIO NAO OBRIGATORIO")
                                && !stringCellDataDefesa.equalsIgnoreCase("\\0")
                                && !stringCellDataDefesa.equalsIgnoreCase("")) {

                            BancaStatusDAO bsDAO = new BancaStatusDAO();
                            if (stringCellValue.equalsIgnoreCase("Finalizado")
                                    || stringCellValue.equalsIgnoreCase("Reprovado")) {
                                try {
                                    bancaStatus = bsDAO.getStatusByTipo("Finalizada");
                                } catch (Exception ex) {
                                    String novoErro = "Linha: " + i + " . Erro ao encontrar o status da banca";
                                    erros = erros + "\n" + novoErro;
                                }
                            } else if (stringCellValue.equalsIgnoreCase("A")
                                    || stringCellValue.equalsIgnoreCase("")) {
                                try {
                                    bancaStatus = bsDAO.getStatusByTipo("Em andamento");
                                } catch (Exception ex) {
                                    String novoErro = "Linha: " + i + " . Erro ao encontrar o status da banca";
                                    erros = erros + "\n" + novoErro;
                                }
                            }
                            banca.setStatusidstatus(bancaStatus);
                            bsDAO.closeSession();
                        }
                        break;
                    case 15:
                        if (!stringCellEstagioTipo.equalsIgnoreCase("estágio não obrigatório")
                                && !stringCellEstagioTipo.equalsIgnoreCase("ESTAGIO_NAO_OBRIGATORIO")
                                && !stringCellEstagioTipo.equalsIgnoreCase("ESTAGIO NAO OBRIGATORIO")
                                && !stringCellDataDefesa.equalsIgnoreCase("\\0")
                                && !stringCellDataDefesa.equalsIgnoreCase("")) {
                            /*primeiro convidado */
                            PerfilDAO pe1DAO = new PerfilDAO();
                            ProfessorDAO p1DAO = new ProfessorDAO();

                            Professor professorBySiape1 = null;
                            Professor pConvidado1 = new Professor();
                            String nomeConv1 = stringCellValue;

                            /*procura o professor*/
                            try {
                                professorBySiape1 = p1DAO.getProfessorByNome(nomeConv1);
                            } catch (Exception ex) {
                                String novoErro = "Linha: " + i + " . Erro ao encontrar professor convidado 1.";
                                erros = erros + "\n" + novoErro;
                            }


                            /*se não encontrar insere */
                            if (null == professorBySiape1 && !nomeConv1.equalsIgnoreCase("") && !nomeConv1.equalsIgnoreCase("\\0")) {
                                try {
                                    perfil = pe1DAO.getPerfilByNome("Professor");
                                } catch (Exception ex) {
                                    String novoErro = "Linha: " + i + " . Erro ao encontrar perfil.";
                                    erros = erros + "\n" + novoErro;
                                }

                                pConvidado1.setPerfilidperfil(perfil);

                                /*dar o maior siape ao novo professor*/
                                int siape = 0;
                                try {
                                    siape = p1DAO.getMaiorSiape();
                                    siape++;
                                } catch (Exception ex) {
                                    String novoErro = "Linha: " + i + " . Erro ao retornar siape.";
                                    erros = erros + "\n" + novoErro;
                                }
                                pConvidado1.setSiape(siape);
                                pConvidado1.setNome(nomeConv1);
                                pConvidado1.setAtivo(true);
                                try {
                                    boolean inserido = p1DAO.insert(pConvidado1);
                                } catch (Exception ex) {
                                    String novoErro = "Linha: " + i + " . Erro ao inserir orientador.";
                                    erros = erros + "\n" + novoErro;
                                }
                            } else {
                                pConvidado1 = professorBySiape1;
                            }

                            conv1.setProfessorsiape(pConvidado1);
                            conv1.setConfirmado(true);
                            conv1.setEnviadoemail(true);
                            conv1.setDataconfirmacao(defesa.getMatriculadatadefesa());
                            conv1.setDataconvite(defesa.getMatriculadatadefesa());

                            pe1DAO.closeSession();
                            p1DAO.closeSession();
                        }
                        break;
                    case 16:
                        if (!stringCellEstagioTipo.equalsIgnoreCase("estágio não obrigatório")
                                && !stringCellEstagioTipo.equalsIgnoreCase("ESTAGIO_NAO_OBRIGATORIO")
                                && !stringCellEstagioTipo.equalsIgnoreCase("ESTAGIO NAO OBRIGATORIO")
                                && !stringCellDataDefesa.equalsIgnoreCase("\\0")
                                && !stringCellDataDefesa.equalsIgnoreCase("")) {
                            /*segundo convidado */
                            PerfilDAO pe2DAO = new PerfilDAO();
                            ProfessorDAO p2DAO = new ProfessorDAO();
                            Professor professorBySiape2 = null;
                            Professor pConvidado2 = new Professor();
                            String nomeConv2 = stringCellValue;

                            /*procura o professor*/
                            try {
                                professorBySiape2 = p2DAO.getProfessorByNome(nomeConv2);
                            } catch (Exception ex) {
                                String novoErro = "Linha: " + i + " . Erro ao encontrar professor convidado 1.";
                                erros = erros + "\n" + novoErro;
                            }

                            /*se não encontrar insere */
                            if (null == professorBySiape2 && !nomeConv2.equalsIgnoreCase("") && !nomeConv2.equalsIgnoreCase("\\0")) {
                                try {
                                    perfil = pe2DAO.getPerfilByNome("Professor");
                                } catch (Exception ex) {
                                    String novoErro = "Linha: " + i + " . Erro ao encontrar perfil.";
                                    erros = erros + "\n" + novoErro;
                                }

                                pConvidado2.setPerfilidperfil(perfil);

                                /*dar o maior siape ao novo professor*/
                                int siape = 0;
                                try {
                                    siape = p2DAO.getMaiorSiape();
                                    siape++;
                                } catch (Exception ex) {
                                    String novoErro = "Linha: " + i + " . Erro ao retornar siape.";
                                    erros = erros + "\n" + novoErro;
                                }
                                pConvidado2.setSiape(siape);
                                pConvidado2.setNome(nomeConv2);
                                pConvidado2.setAtivo(true);
                                try {
                                    boolean inserido = p2DAO.insert(pConvidado2);
                                } catch (Exception ex) {
                                    String novoErro = "Linha: " + i + " . Erro ao inserir orientador.";
                                    erros = erros + "\n" + novoErro;
                                }
                            } else {
                                pConvidado2 = professorBySiape2;
                            }

                            conv2.setProfessorsiape(pConvidado2);
                            conv2.setConfirmado(true);
                            conv2.setEnviadoemail(true);
                            conv2.setDataconfirmacao(defesa.getMatriculadatadefesa());
                            conv2.setDataconvite(defesa.getMatriculadatadefesa());

                            pe2DAO.closeSession();
                            p2DAO.closeSession();

                        }
                        break;
                    case 17:
                        /*status do trabalho*/
                        TrabalhoStatusDAO tsDAO = new TrabalhoStatusDAO();

                        if (stringCellValue.equalsIgnoreCase("") || stringCellValue.equalsIgnoreCase("A")) {
                            try {
                                trabStatus = tsDAO.getStatusByTipo("Em andamento");
                            } catch (Exception ex) {
                                String novoErro = "Linha: " + i + " . Erro ao encontrar o status do trabalho.";
                                erros = erros + "\n" + novoErro;
                            }
                        } else if (stringCellValue.equalsIgnoreCase("REPROVADO")) {
                            try {
                                trabStatus = tsDAO.getStatusByTipo("Reprovado");
                            } catch (Exception ex) {
                                String novoErro = "Linha: " + i + " . Erro ao encontrar o status do trabalho.";
                                erros = erros + "\n" + novoErro;
                            }
                        } else if (stringCellValue.equalsIgnoreCase("Finalizado")) {
                            try {
                                trabStatus = tsDAO.getStatusByTipo("Finalizado");
                            } catch (Exception ex) {
                                String novoErro = "Linha: " + i + " . Erro ao encontrar o status do trabalho.";
                                erros = erros + "\n" + novoErro;
                            }
                        }
                        trab.setStatusidstatus(trabStatus);
                        tsDAO.closeSession();
                        break;
                    case 18:
                        /*tipo de estágio*/
                        EstagioDAO estDAO = new EstagioDAO();
                        ModalidadeDAO mDAO = new ModalidadeDAO();

                        if (stringCellValue.equalsIgnoreCase("ESTAGIO_OBRIGATORIO")
                                || stringCellValue.equalsIgnoreCase("ESTAGIO OBRIGATORIO")) {
                            try {
                                modalidade = mDAO.getModalidadeByNome("Estágio");
                            } catch (Exception ex) {
                                String novoErro = "Linha: " + i + " . Erro ao encontrar a modalidade.";
                                erros = erros + "\n" + novoErro;
                            }

                            try {
                                estagio = estDAO.getEstagioByTipo("Estágio Obrigatório");
                            } catch (Exception ex) {
                                String novoErro = "Linha: " + i + " . Erro ao encontrar a modalidade.";
                                erros = erros + "\n" + novoErro;
                            }
                        } else if (stringCellValue.equalsIgnoreCase("estágio não obrigatório")
                                || stringCellValue.equalsIgnoreCase("ESTAGIO_NAO_OBRIGATORIO")
                                || stringCellValue.equalsIgnoreCase("ESTAGIO NAO OBRIGATORIO")) {
                            try {
                                modalidade = mDAO.getModalidadeByNome("Estágio");
                            } catch (Exception ex) {
                                String novoErro = "Linha: " + i + " . Erro ao encontrar o estágio.";
                                erros = erros + "\n" + novoErro;
                            }

                            try {
                                estagio = estDAO.getEstagioByTipo("Estágio Não Obrigatório");
                            } catch (Exception ex) {
                                String novoErro = "Linha: " + i + " . Erro ao encontrar o estágio.";
                                erros = erros + "\n" + novoErro;
                            }
                        }
                        trab.setEstagioidestagio(estagio);
                        trab.setModalidadeidmodalidade(modalidade);

                        estDAO.closeSession();
                        mDAO.closeSession();
                        break;
                }
            }

            //---------------------------------------
            TrabalhoDAO tDAO = new TrabalhoDAO();
            List<Trabalho> trabTemp = new ArrayList<>();
            //procurar trabalho se encontrar não importa novamente
            try {
                if (banca.getLocalbanca() != null
                        && !banca.getLocalbanca().equals("")
                        && !banca.getLocalbanca().equals("\\0")) {
                    trabTemp = tDAO.getTrabalhosByConvidado(trab.getAlunomatricula(), trab.getEmpresaidempresa(), trab.getTitulo(), trab.getEstagioidestagio().getTipoestagio(), trab.getProfessorsiape().getSiape(), trab.getProfessorsiape().getNome(), trab.getDatamatricula(), banca.getLocalbanca());
                } else {
                    trabTemp = tDAO.getTrabalhos(trab.getAlunomatricula(), trab.getEmpresaidempresa(), trab.getTitulo(), trab.getEstagioidestagio().getTipoestagio(), trab.getProfessorsiape().getSiape(), trab.getProfessorsiape().getNome(), trab.getDatamatricula());
                }
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
                String novoErro = "Linha: " + i + " . Erro ao encontrar trabalho.";
                erros = erros + "\n" + novoErro;
            }
            if (trabTemp.isEmpty()) {

                //salvar trabalho
                try {
                    boolean inserido = tDAO.insert(trab);
                    qtdImportado++;
                } catch (Exception ex) {
                    String novoErro = "Linha: " + i + " . Erro ao inserir trabalho.";
                    erros = erros + "\n" + novoErro;
                }
                tDAO.closeSession();
                //---------------------------------------

                if (!stringCellEstagioTipo.equalsIgnoreCase("estágio não obrigatório")
                        && !stringCellEstagioTipo.equalsIgnoreCase("ESTAGIO_NAO_OBRIGATORIO")
                        && !stringCellEstagioTipo.equalsIgnoreCase("ESTAGIO NAO OBRIGATORIO")
                        && !stringCellDataDefesa.equalsIgnoreCase("\\0")
                        && !stringCellDataDefesa.equalsIgnoreCase("")) {
                    //---------------------------------------
                    //salvar banca
                    BancaDAO bDAO = new BancaDAO();
                    banca.setTrabalhoidtrabalho(trab);
                    try {
                        boolean inserido = bDAO.insert(banca);
                    } catch (Exception ex) {
                        String novoErro = "Linha: " + i + " . Erro ao inserir banca.";
                        erros = erros + "\n" + novoErro;
                    }
                    bDAO.closeSession();
                    //---------------------------------------
                    //salvar o convidado 1
                    ConvidadoDAO c1DAO = new ConvidadoDAO();
                    conv1.setBancaidbanca(banca);
                    try {
                        boolean inserido = c1DAO.insert(conv1);
                    } catch (Exception ex) {
                        String novoErro = "Linha: " + i + " . Erro ao inserir convidado 1.";
                        erros = erros + "\n" + novoErro;
                    }
                    c1DAO.closeSession();
                    //---------------------------------------
                    //salvar o convidado 2
                    ConvidadoDAO c2DAO = new ConvidadoDAO();
                    conv2.setBancaidbanca(banca);
                    try {
                        boolean inserido = c2DAO.insert(conv2);
                    } catch (Exception ex) {
                        String novoErro = "Linha: " + i + " . Erro ao inserir convidado 2.";
                        erros = erros + "\n" + novoErro;
                    }
                    c2DAO.closeSession();
                    //---------------------------------------
                    //salvar defesa
                    DefesaDAO dDAO = new DefesaDAO();
                    defesa.setTrabalhoidtrabalho(trab);
                    try {
                        boolean inserido = dDAO.insert(defesa);
                    } catch (Exception ex) {
                        String novoErro = "Linha: " + i + " . Erro ao inserir defesa.";
                        erros = erros + "\n" + novoErro;
                    }
                    dDAO.closeSession();
                    //---------------------------------------
                }
            } else {
                int line = i + 1;
                String novoErro = "Linha: " + line + " . Erro ao salvar. Trabalho já existe na base.";
                erros = erros + "\n" + novoErro;
            }
        }

        this.setImportacaoErrosTrabalho(erros);
        if (qtdImportado > 0) {
            FacesContext.getCurrentInstance().addMessage("growlImportacaoTrabalho",
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Importação Realizada!\r\n Importado " + String.valueOf(qtdImportado) + " de " + totalDeLinhas, ""));
        } else {
            FacesContext.getCurrentInstance().addMessage("growlImportacaoTrabalho",
                    new FacesMessage(FacesMessage.SEVERITY_WARN, "Importação não realizada!", ""));
        }
    }

}
