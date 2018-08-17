import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.apache.commons.csv.CSVRecord;

import java.lang.*;
import java.io.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import static lombok.AccessLevel.PRIVATE;

@Getter
@Setter
@FieldDefaults(level = PRIVATE)
class TableRecord {

    String seller_inn;
    String seller_kpp;
    String customer_inn;
    String customer_kpp;
    double total;
    RecordType recordType;

    TableRecord(CSVRecord rec, RecordType rt) {
        this.recordType = rt;
        switch (recordType) {
            case SELLER:
                this.setSeller_inn(rec.get(0));
                this.setSeller_kpp(rec.get(1));
                this.setCustomer_inn(rec.get(2));
                this.setCustomer_kpp(rec.get(3));
                this.setTotal(Double.parseDouble(rec.get(4)));
                break;
            case CUSTOMER:
                this.setCustomer_inn(rec.get(0));
                this.setCustomer_kpp(rec.get(1));
                this.setSeller_inn(rec.get(2));
                this.setSeller_kpp(rec.get(3));
                this.setTotal(Double.parseDouble(rec.get(4)));
                break;
        }
    }

    @Override
    public String toString() {
        String res;
        switch (recordType) {
            case SELLER:
                return getSeller_inn() + "," + getSeller_kpp() + "," + getCustomer_inn() + "," + getCustomer_kpp() + "," + getTotal() + ',' + getTotal();
				
            case CUSTOMER:
                return getCustomer_inn() + "," + getCustomer_kpp() + "," + getSeller_inn() + "," + getSeller_kpp() + "," + getTotal() + ',' + getTotal();
            default:
                return "Error in TableRecord initialization! Only SELLER or CUSTOMER.";
        }
    }

    public void insertIntoTable(Connection con) {
        
        try {
	    Statement stmt = con.createStatement();
	    switch (recordType) {
                case SELLER:
                    System.out.println("insert into table default." + recordType
                            + " VALUES("+getSeller_inn()+"," + getSeller_kpp() + ","+getCustomer_inn() + ","+getCustomer_kpp()
                            + ","+getTotal() + "," + getTotal() + ")");


		    stmt.execute("insert into table default." + recordType
                            + " VALUES("+getSeller_inn()+"," + getSeller_kpp() + ","+getCustomer_inn() + ","+getCustomer_kpp()
                            + ","+getTotal() + "," + getTotal() + ")");
  
                    break;
                case CUSTOMER:
                   System.out.println("insert into table default." + recordType
                            + " VALUES("+getSeller_inn()+"," + getSeller_kpp() + ","+getCustomer_inn() + ","+getCustomer_kpp()
                            + ","+getTotal() + "," + getTotal() + ")");
		   
                   stmt.execute("insert into table default." + recordType
                            + " VALUES("+getCustomer_inn()+"," + getCustomer_kpp() + ","+getSeller_inn() + ","+getSeller_kpp()
                            + "," + getTotal() + ","+getTotal() + ")");
//		    stmt.execute("insert into table default.CUSTOMER VALUES(1,2,3,4,5,6)");

		    break;
            }  
        } catch (SQLException e) {
            e.printStackTrace();
	}
    }
}
