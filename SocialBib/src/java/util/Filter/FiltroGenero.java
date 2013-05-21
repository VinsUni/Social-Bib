/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package util.Filter;

/**
 *
 * @author Doomsday
 */
public class FiltroGenero implements Filtro{

    public FiltroGenero(){
		GlobalFilter.getInstance().addFilter(this);
    }
	
    @Override
    public String genSql(String sqlBase) {
        String sqlPart = "";
        if(!sqlBase.endsWith("where")){
            sqlPart += " and";
        }
        sqlPart += " l.genero =:genero";
        return sqlPart;
    }

    @Override
    public String getSqlProperty() {
        return "genero";
    }
    
}
