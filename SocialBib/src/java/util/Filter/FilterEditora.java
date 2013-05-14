/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package util.Filter;

/**
 *
 * @author Doomsday
 */
public class FilterEditora implements Filter {
    
    @Override
    public String genSql(String sqlBase) {
        String sqlPart = "";
        if(!sqlBase.endsWith("where")){
            sqlPart += " and";
        }
        sqlPart += " l.editora like :editora";
        return sqlPart;
    }

    @Override
    public String getSqlProperty() {
        return "editora";
    }
}
