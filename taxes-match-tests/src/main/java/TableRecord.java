import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.apache.commons.csv.CSVRecord;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

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

    void insertIntoTable(Connection con) {

        PreparedStatement preparedStmt = null;
        try {

            switch (recordType) {
                case SELLER:
                    preparedStmt = con.prepareStatement("insert into table default.seller VALUES(?,?,?,?,?,?)");
                    preparedStmt.setString(1, getSeller_inn());
                    preparedStmt.setString(2, getSeller_kpp());
                    preparedStmt.setString(3, getCustomer_inn());
                    preparedStmt.setString(4, getCustomer_kpp());
                    break;
                case CUSTOMER:
                    preparedStmt = con.prepareStatement("insert into table default.customer VALUES(?,?,?,?,?,?)");
                    preparedStmt.setString(1, getCustomer_inn());
                    preparedStmt.setString(2, getCustomer_kpp());
                    preparedStmt.setString(3, getSeller_inn());
                    preparedStmt.setString(4, getSeller_kpp());
                    break;
            }

            preparedStmt.setDouble(5, getTotal());
            preparedStmt.setDouble(6, getTotal());
            preparedStmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

