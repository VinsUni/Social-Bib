/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

/**
 *
 * @author CASSIO
 */
import java.util.List;  
import javax.faces.model.ListDataModel;  
import modelo.Mensagem;
import org.primefaces.model.SelectableDataModel;  
  
public class MensagemDataModel extends ListDataModel<Mensagem> implements SelectableDataModel<Mensagem> {    
  
    public MensagemDataModel() {  
    }  
  
    public MensagemDataModel(List<Mensagem> data) {  
        super(data);  
    }  
      
    @Override  
    public Mensagem getRowData(String rowKey) {  
        //In a real app, a more efficient way like a query by rowKey should be implemented to deal with huge data  
          
        List<Mensagem> cars = (List<Mensagem>) getWrappedData();  
          
        for(Mensagem car : cars) {  
            if(car.getId().equals(rowKey))  
                return car;  
        }  
          
        return null;  
    }  
  
    @Override  
    public Object getRowKey(Mensagem car) {  
        return car.getId();  
    }  
}
