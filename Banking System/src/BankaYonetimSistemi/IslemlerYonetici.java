package BankaYonetimSistemi;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class IslemlerYonetici {
    private final Connection con;

    public IslemlerYonetici(Connection con) {

        this.con = con;
    }

    public void tumIslemleriGoster() {
        String sorgu = "SELECT * FROM Transactions";
        try (PreparedStatement preparedStatement = con.prepareStatement(sorgu);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            System.out.println("ID | Gönderen Hesap No | Alıcı Hesap No | Miktar | İşlem Tarihi");
            System.out.println("--------------------------------------------------------------------");

            while (resultSet.next()) {
                long islemId = resultSet.getLong("transaction_id");
                long gonderenHesapNo = resultSet.getLong("sender_account_number");
                long aliciHesapNo = resultSet.getLong("receiver_account_number");
                double miktar = resultSet.getDouble("amount");
                String islemTarihi = resultSet.getString("transaction_date");

                System.out.println(islemId + " | " + gonderenHesapNo + " | " + aliciHesapNo + " | " + miktar + " | " + islemTarihi);
            }
            con.setAutoCommit(true);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
