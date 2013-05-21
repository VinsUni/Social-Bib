/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import dao.LivroJpaController;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import modelo.Livro;
import util.EMF;
import util.Filter.*;

/**
 *
 * @author Doomsday
 */
@ManagedBean
@RequestScoped
public class PesquisaMB {

    private LivroJpaController dao = new LivroJpaController(EMF.getEntityManagerFactory());
    private List<Opcao> opcoes = new ArrayList<Opcao>();
    private List<Livro> resultPesquisa;
    private Opcao opcaoTitulo = new Opcao("titulo");
    private Opcao opcaoAno = new Opcao("ano");
    private Opcao opcaoEditora = new Opcao("editora");
    private Opcao opcaoISBN = new Opcao("isbn");
    private Opcao opcaoGenero = new Opcao("genero");
    private Opcao opcaoAutor = new Opcao("autor");
    /**
     * Creates a new instance of PesquisaMB
     */
    public PesquisaMB() {
        new FiltroTitulo();
        new FiltroAno();
        new FiltroEditora();
        new FiltroISBN();
        new FiltroGenero();
        new FiltroAutor();
        opcoes.add(opcaoTitulo);
        opcoes.add(opcaoAno);
        opcoes.add(opcaoEditora);
        opcoes.add(opcaoISBN);
        opcoes.add(opcaoGenero);
        opcoes.add(opcaoAutor);
        
    }
    
    public void pesquisarLivro(){
        EntityManager em = getDao().getEntityManager();
        boolean opAtivo = false;
        String sql = "select l from Livro l where";
        try{
            for(Opcao o: opcoes){
                if(o.isAtivo()){
                    String temp;
                    opAtivo = true;
                    temp = GlobalFilter.getInstance().getFilter(o.getPropriedade()).genSql(sql);
                    if(temp.contains("like")){
                        o.setValor("%"+o.getValor()+"%");
                    }
                    sql += temp;
                }
            }
            if(!opAtivo){
                setResultPesquisa(dao.findLivroEntities());
            }
            else{
                //System.out.println(sql);
                TypedQuery<Livro> query = em.createQuery(sql, Livro.class);
                if(opAtivo){
                    for(Opcao o: opcoes){
                        if(o.isAtivo()){
                            query.setParameter(o.getPropriedade(),o.getValor());
                        }
                    }
                }
                setResultPesquisa(query.getResultList());
                for(Opcao o: opcoes){
                    o.setValor("");
                }
            }
            //return query.getSingleResult();
            //return results;
        }catch(Exception e){
            e.printStackTrace();
            //return null;
        }
    }

    /**
     * @return the dao
     */
    public LivroJpaController getDao() {
        return dao;
    }

    /**
     * @param dao the dao to set
     */
    public void setDao(LivroJpaController dao) {
        this.dao = dao;
    }

    /**
     * @return the opcaoTitulo
     */
    public Opcao getOpcaoTitulo() {
        return opcaoTitulo;
    }

    /**
     * @param opcaoTitulo the opcaoTitulo to set
     */
    public void setOpcaoTitulo(Opcao opcaoTitulo) {
        this.opcaoTitulo = opcaoTitulo;
    }

    /**
     * @return the opcaoAno
     */
    public Opcao getOpcaoAno() {
        return opcaoAno;
    }

    /**
     * @param opcaoAno the opcaoAno to set
     */
    public void setOpcaoAno(Opcao opcaoAno) {
        this.opcaoAno = opcaoAno;
    }

    /**
     * @return the opcaoEditora
     */
    public Opcao getOpcaoEditora() {
        return opcaoEditora;
    }

    /**
     * @param opcaoEditora the opcaoEditora to set
     */
    public void setOpcaoEditora(Opcao opcaoEditora) {
        this.opcaoEditora = opcaoEditora;
    }

    /**
     * @return the opcaoISBN
     */
    public Opcao getOpcaoISBN() {
        return opcaoISBN;
    }

    /**
     * @param opcaoISBN the opcaoISBN to set
     */
    public void setOpcaoISBN(Opcao opcaoISBN) {
        this.opcaoISBN = opcaoISBN;
    }

    /**
     * @return the opcaoGenero
     */
    public Opcao getOpcaoGenero() {
        return opcaoGenero;
    }

    /**
     * @param opcaoGenero the opcaoGenero to set
     */
    public void setOpcaoGenero(Opcao opcaoGenero) {
        this.opcaoGenero = opcaoGenero;
    }

    /**
     * @return the opcaoAutor
     */
    public Opcao getOpcaoAutor() {
        return opcaoAutor;
    }

    /**
     * @param opcaoAutor the opcaoAutor to set
     */
    public void setOpcaoAutor(Opcao opcaoAutor) {
        this.opcaoAutor = opcaoAutor;
    }

    /**
     * @return the resultPesquisa
     */
    public List<Livro> getResultPesquisa() {
        return resultPesquisa;
    }

    /**
     * @param resultPesquisa the resultPesquisa to set
     */
    public void setResultPesquisa(List<Livro> resultPesquisa) {
        this.resultPesquisa = resultPesquisa;
    }
//====================================================================================================    
//Classe interna criada para auxiliar no controle dos dados vindos da pagina
    public class Opcao{
        private boolean ativo;
        private String valor;
        private String propriedade;

        public Opcao(String property){
            this.ativo = false;
            this.propriedade = property;
        }
        /**
         * @return the ativo
         */
        public boolean isAtivo() {
            return ativo;
        }

        /**
         * @param ativo the ativo to set
         */
        public void setAtivo(boolean ativo) {
            this.ativo = ativo;
        }

        /**
         * @return the propriedade
         */
        public String getPropriedade() {
            return propriedade;
        }

        /**
         * @param propriedade the propriedade to set
         */
        public void setPropriedade(String propriedade) {
            this.propriedade = propriedade;
        }

        /**
         * @return the valor
         */
        public String getValor() {
            return valor;
        }

        /**
         * @param valor the valor to set
         */
        public void setValor(String valor) {
            this.valor = valor;
        }
        
    }
}
