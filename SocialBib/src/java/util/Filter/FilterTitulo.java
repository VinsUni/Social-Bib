/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package util.Filter;

import util.Filter.Filter;

/**
 *
 * @author Doomsday
 */
public class FilterTitulo implements Filter {

    @Override
    public String genSql(String sqlBase) {
        String sqlPart = "";
        if(!sqlBase.endsWith("where")){
            sqlPart += " and";
        }
        sqlPart += " l.titulo like :titulo";
        return sqlPart;
    }

    @Override
    public String getSqlProperty() {
        return "titulo";
    }
}
