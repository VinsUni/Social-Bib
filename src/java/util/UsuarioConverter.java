/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import dao.UsuarioJpaController;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import modelo.Usuario;

@FacesConverter(value = "usuarioConverter")
public class UsuarioConverter implements Converter {

    @Override
    public Object getAsObject(FacesContext fc, UIComponent uic, String id) {
        UsuarioJpaController dao = new UsuarioJpaController(EMF.getEntityManagerFactory());
        Usuario usuario = dao.findUsuario(Long.valueOf(id));
        return usuario;
    }

    @Override
    public String getAsString(FacesContext fc, UIComponent uic, Object o) {
        Usuario usuario = new Usuario();
        usuario = (Usuario) o;
        return String.valueOf(usuario.getId());
    }
}