/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package util.Filter;

/**
 *
 * @author Doomsday
 */
public interface Filtro {
    public String genSql(String sqlBase);
    public String getSqlProperty();
}
