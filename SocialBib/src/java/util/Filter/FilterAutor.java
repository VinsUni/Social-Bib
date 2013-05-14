/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package util.Filter;

/**
 *
 * @author Doomsday
 */
public class FilterAutor implements Filter {
    
    @Override
    public String genSql(String sqlBase) {
        String sqlPart = "";
        if(!sqlBase.endsWith("where")){
            sqlPart += " and";
        }
        sqlPart += " l.autor like :autor";
        return sqlPart;
    }

    @Override
    public String getSqlProperty() {
        return "autor";
    }
}
