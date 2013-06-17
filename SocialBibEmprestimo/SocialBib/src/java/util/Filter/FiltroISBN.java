/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package util.Filter;

/**
 *
 * @author Doomsday
 */
public class FiltroISBN implements Filtro{

    public FiltroISBN(){
        GlobalFilter.getInstance().addFilter(this);
    }
    @Override
    public String genSql(String sqlBase) {
        String sqlPart = "";
        if(!sqlBase.endsWith("where")){
            sqlPart += " and";
        }
        sqlPart += " l.isbn =:isbn";
        return sqlPart;
    }

    @Override
    public String getSqlProperty() {
        return "isbn";
    }
    
}
