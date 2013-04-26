package modelo;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import modelo.Usuario;

@Generated(value="EclipseLink-2.3.2.v20111125-r10461", date="2013-04-25T22:25:06")
@StaticMetamodel(Livro.class)
public class Livro_ { 

    public static volatile SingularAttribute<Livro, Long> id;
    public static volatile SingularAttribute<Livro, String> titulo;
    public static volatile SingularAttribute<Livro, String> genero;
    public static volatile SingularAttribute<Livro, String> autor;
    public static volatile SingularAttribute<Livro, String> ano;
    public static volatile SingularAttribute<Livro, Usuario> dono;
    public static volatile SingularAttribute<Livro, String> editora;

}