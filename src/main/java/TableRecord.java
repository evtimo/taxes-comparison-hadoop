import org.apache.commons.csv.CSVRecord;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

class TableRecord {

    private String seller_inn;
    private String seller_kpp;
    private String customer_inn;
    private String customer_kpp;
    private double total;
    private RecordType recordType;

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

        switch (recordType) {
            case SELLER:
                return getSeller_inn() + "," + getSeller_kpp() + "," + getCustomer_inn() + "," + getCustomer_kpp() + "," + getTotal() + ',' + getTotal();

            case CUSTOMER:
                return getCustomer_inn() + "," + getCustomer_kpp() + "," + getSeller_inn() + "," + getSeller_kpp() + "," + getTotal() + ',' + getTotal();
        }
        return "";
    }

    public void insertIntoTable(Connection con) {

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

    public String getSeller_inn() {
        return seller_inn;
    }

    public String getSeller_kpp() {
        return seller_kpp;
    }

    public String getCustomer_inn() {
        return customer_inn;
    }

    public String getCustomer_kpp() {
        return customer_kpp;
    }

    public double getTotal() {
        return total;
    }

    public void setSeller_inn(String seller_inn) {
        this.seller_inn = seller_inn;
    }

    public void setSeller_kpp(String seller_kpp) {
        this.seller_kpp = seller_kpp;
    }

    public void setCustomer_kpp(String customer_kpp) {
        this.customer_kpp = customer_kpp;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public void setCustomer_inn(String customer_inn) {
        this.customer_inn = customer_inn;
    }
}
