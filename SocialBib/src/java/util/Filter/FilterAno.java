/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package util.Filter;

/**
 *
 * @author Doomsday
 */
public class FilterAno implements Filter {
    
    @Override
    public String genSql(String sqlBase) {
        String sqlPart = "";
        if(!sqlBase.endsWith("where")){
            sqlPart += " and";
        }
        //VERIFICAR COMO FAZER O SQL DA CONSULTA POR DATA
        sqlPart += " l.titulo like :titulo";
        return sqlPart;
    }

    @Override
    public String getSqlProperty() {
        return "ano";
    }
}
