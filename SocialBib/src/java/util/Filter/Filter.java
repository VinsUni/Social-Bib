/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package util.Filter;

/**
 *
 * @author Doomsday
 */
public interface Filter {
    public String genSql(String sqlBase);
    public String getSqlProperty();
}
