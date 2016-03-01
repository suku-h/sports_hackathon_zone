package in.jiyofit.the_app;

import java.io.Serializable;
import java.util.ArrayList;

//Serializable class should be in a separate java class.
//If create in a activity, the same code give NPE
public class DataHelper implements Serializable {
    private ArrayList<ArrayList<String>> list;
    private static final long serialVersionUID = 27311958;

    public void setList(ArrayList<ArrayList<String>> list) {
        this.list = list;
    }

    public ArrayList<ArrayList<String>> getList() {
        return list;
    }
}
