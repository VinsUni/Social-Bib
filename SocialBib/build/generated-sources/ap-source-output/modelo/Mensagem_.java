package modelo;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import modelo.Usuario;

@Generated(value="EclipseLink-2.3.2.v20111125-r10461", date="2013-06-14T13:52:50")
@StaticMetamodel(Mensagem.class)
public class Mensagem_ { 

    public static volatile SingularAttribute<Mensagem, Long> id;
    public static volatile SingularAttribute<Mensagem, String> mensagem;
    public static volatile SingularAttribute<Mensagem, Date> data;
    public static volatile SingularAttribute<Mensagem, String> assunto;
    public static volatile SingularAttribute<Mensagem, Usuario> destinatario;
    public static volatile SingularAttribute<Mensagem, Usuario> remetente;
    public static volatile SingularAttribute<Mensagem, Boolean> isLida;

}