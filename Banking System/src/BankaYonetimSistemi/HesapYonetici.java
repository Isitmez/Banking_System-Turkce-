package BankaYonetimSistemi;

import java.sql.*;
import java.util.Scanner;

public class HesapYonetici {
    private final Connection con;
    private final Scanner sc;

    HesapYonetici(Connection con, Scanner sc) {
        this.con = con;
        this.sc = sc;
    }

    public void paraYatir(long hesapNumarasi) throws SQLException {
        sc.nextLine();
        System.out.print("Miktarı Girin: ");
        double miktar = sc.nextDouble();
        sc.nextLine();
        System.out.print("Güvenlik Pinini Girin: ");
        String guvenlikPin = sc.nextLine();

        try {
            con.setAutoCommit(false);
            if (hesapNumarasi != 0) {
                PreparedStatement preparedStatement = con.prepareStatement("SELECT * FROM Hesaplar WHERE hesap_numarasi = ? and guvenlik_pini = ? ");
                preparedStatement.setLong(1, hesapNumarasi);
                preparedStatement.setString(2, guvenlikPin);
                ResultSet resultSet = preparedStatement.executeQuery();

                if (resultSet.next()) {
                    String krediSorgusu = "UPDATE Hesaplar SET bakiye = bakiye + ? WHERE hesap_numarasi = ?";
                    PreparedStatement preparedStatement1 = con.prepareStatement(krediSorgusu);
                    preparedStatement1.setDouble(1, miktar);
                    preparedStatement1.setLong(2, hesapNumarasi);
                    int etkilenenSatirlar = preparedStatement1.executeUpdate();
                    if (etkilenenSatirlar > 0) {
                        System.out.println(miktar + " TL başarıyla yatırıldı.");
                        con.commit();
                        con.setAutoCommit(true);
                        return;
                    } else {
                        System.out.println("İşlem Başarısız!");
                        con.rollback();
                        con.setAutoCommit(true);
                    }
                } else {
                    System.out.println("Geçersiz Güvenlik Pini!");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        con.setAutoCommit(true);
    }

    public void paraCek(long hesapNumarasi) throws SQLException {
        sc.nextLine();
        System.out.print("Miktarı Girin: ");
        double miktar = sc.nextDouble();
        sc.nextLine();
        System.out.print("Güvenlik Pinini Girin: ");
        String guvenlikPin = sc.nextLine();
        try {
            con.setAutoCommit(false);
            if (hesapNumarasi != 0) {
                PreparedStatement preparedStatement = con.prepareStatement("SELECT * FROM Hesaplar WHERE hesap_numarasi = ? and guvenlik_pini = ? ");
                preparedStatement.setLong(1, hesapNumarasi);
                preparedStatement.setString(2, guvenlikPin);
                ResultSet resultSet = preparedStatement.executeQuery();

                if (resultSet.next()) {
                    double mevcutBakiye = resultSet.getDouble("bakiye");
                    if (miktar <= mevcutBakiye) {
                        String cekSorgusu = "UPDATE Hesaplar SET bakiye = bakiye - ? WHERE hesap_numarasi = ?";
                        PreparedStatement preparedStatement1 = con.prepareStatement(cekSorgusu);
                        preparedStatement1.setDouble(1, miktar);
                        preparedStatement1.setLong(2, hesapNumarasi);
                        int etkilenenSatirlar = preparedStatement1.executeUpdate();
                        if (etkilenenSatirlar > 0) {
                            System.out.println(miktar + " TL başarıyla çekildi.");
                            con.commit();
                            con.setAutoCommit(true);
                            return;
                        } else {
                            System.out.println("İşlem Başarısız!");
                            con.rollback();
                            con.setAutoCommit(true);
                        }
                    } else {
                        System.out.println("Yetersiz Bakiye!");
                    }
                } else {
                    System.out.println("Geçersiz Güvenlik Pini!");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        con.setAutoCommit(true);
    }

    public void paraTransferi(long gonderenHesapNumarasi) throws SQLException {
        sc.nextLine();
        System.out.print("Alıcı Hesap Numarasını Girin: ");
        long aliciHesapNumarasi = sc.nextLong();
        System.out.print("Miktarı Girin: ");
        double miktar = sc.nextDouble();
        sc.nextLine();
        System.out.print("Güvenlik Pinini Girin: ");
        String guvenlikPin = sc.nextLine();
        try {
            con.setAutoCommit(false);
            if (gonderenHesapNumarasi != 0 && aliciHesapNumarasi != 0) {
                PreparedStatement preparedStatement = con.prepareStatement("SELECT * FROM Hesaplar WHERE hesap_numarasi = ? AND guvenlik_pini = ? ");
                preparedStatement.setLong(1, gonderenHesapNumarasi);
                preparedStatement.setString(2, guvenlikPin);
                ResultSet resultSet = preparedStatement.executeQuery();

                if (resultSet.next()) {
                    double mevcutBakiye = resultSet.getDouble("bakiye");
                    if (miktar <= mevcutBakiye) {

                        String cekSorgusu = "UPDATE Hesaplar SET bakiye = bakiye - ? WHERE hesap_numarasi = ?";
                        String krediSorgusu = "UPDATE Hesaplar SET bakiye = bakiye + ? WHERE hesap_numarasi = ?";

                        PreparedStatement krediPreparedStatement = con.prepareStatement(krediSorgusu);
                        PreparedStatement cekPreparedStatement = con.prepareStatement(cekSorgusu);

                        krediPreparedStatement.setDouble(1, miktar);
                        krediPreparedStatement.setLong(2, aliciHesapNumarasi);
                        cekPreparedStatement.setDouble(1, miktar);
                        cekPreparedStatement.setLong(2, gonderenHesapNumarasi);
                        int etkilenenSatirlar1 = cekPreparedStatement.executeUpdate();
                        int etkilenenSatirlar2 = krediPreparedStatement.executeUpdate();
                        if (etkilenenSatirlar1 > 0 && etkilenenSatirlar2 > 0) {
                            System.out.println("İşlem Başarılı!");
                            System.out.println(miktar + " TL başarıyla transfer edildi.");
                            con.commit();
                            con.setAutoCommit(true);
                            return;
                        } else {
                            System.out.println("İşlem Başarısız!");
                            con.rollback();
                            con.setAutoCommit(true);
                        }
                    } else {
                        System.out.println("Yetersiz Bakiye!");
                    }
                } else {
                    System.out.println("Geçersiz Güvenlik Pini!");
                }
            } else {
                System.out.println("Geçersiz Hesap Numarası!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        con.setAutoCommit(true);
    }

    public void bakiyeGoruntule(long hesapNumarasi) {
        sc.nextLine();
        System.out.print("Güvenlik Pinini Girin: ");
        String guvenlikPin = sc.nextLine();
        try {
            PreparedStatement preparedStatement = con.prepareStatement("SELECT bakiye FROM Hesaplar WHERE hesap_numarasi = ? AND guvenlik_pini = ?");
            preparedStatement.setLong(1, hesapNumarasi);
            preparedStatement.setString(2, guvenlikPin);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                double bakiye = resultSet.getDouble("bakiye");
                System.out.println("Bakiye: " + bakiye);
            } else {
                System.out.println("Geçersiz Güvenlik Pini!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
