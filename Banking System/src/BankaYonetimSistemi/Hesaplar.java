package BankaYonetimSistemi;

import java.sql.*;
import java.util.Scanner;

public class Hesaplar {
    private final Connection con;
    private final Scanner sc;

    public Hesaplar(Connection con, Scanner sc){
        this.con = con;
        this.sc = sc;
    }

    public long hesapAc(String eposta){
        if(hesapVarMi(eposta)) {
            String hesapAcSorgusu = "INSERT INTO Hesaplar(hesap_numarasi, ad_soyad, eposta, bakiye, guvenlik_pini) VALUES(?, ?, ?, ?, ?)";
            sc.nextLine();
            System.out.print("Ad Soyad Girin: ");
            String adSoyad = sc.nextLine();
            System.out.print("İlk Miktar Girin: ");
            double bakiye = sc.nextDouble();
            sc.nextLine();
            System.out.print("Güvenlik Pinini Girin: ");
            String guvenlikPin = String.valueOf(sc.nextLine());
            try {
                long hesapNumarasi = hesapNumarasiOlustur();
                PreparedStatement preparedStatement = con.prepareStatement(hesapAcSorgusu);
                preparedStatement.setLong(1, hesapNumarasi);
                preparedStatement.setString(2, adSoyad);
                preparedStatement.setString(3, eposta);
                preparedStatement.setDouble(4, bakiye);
                preparedStatement.setString(5, guvenlikPin);
                int etkilenenSatirlar = preparedStatement.executeUpdate();
                if (etkilenenSatirlar > 0) {
                    return hesapNumarasi;
                } else {
                    throw new RuntimeException("Hesap Oluşturma Başarısız!");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        throw new RuntimeException("Hesap Zaten Var");
    }

    public long hesapNumarasiniGetir(String eposta) {
        String sorgu = "SELECT hesap_numarasi from Hesaplar WHERE eposta = ?";
        try{
            PreparedStatement preparedStatement = con.prepareStatement(sorgu);
            preparedStatement.setString(1, eposta);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                return resultSet.getLong("hesap_numarasi");
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        throw new RuntimeException("Hesap Numarası Bulunamadı!");
    }

    private long hesapNumarasiOlustur() {
        try {
            Statement statement = con.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT hesap_numarasi from Hesaplar ORDER BY hesap_numarasi DESC LIMIT 1");
            if (resultSet.next()) {
                long sonHesapNumarasi = resultSet.getLong("hesap_numarasi");
                return sonHesapNumarasi+1;
            } else {
                return 10000100;
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return 10000100;
    }

        public String hesapBilgileriniGetir(String eposta) {
            String sorgu = "SELECT hesap_numarasi, ad_soyad FROM Hesaplar WHERE eposta = ?";
            try (PreparedStatement preparedStatement = con.prepareStatement(sorgu)) {
                preparedStatement.setString(1, eposta);
                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    long hesapNumarasi = resultSet.getLong("hesap_numarasi");
                    String adSoyad = resultSet.getString("ad_soyad");
                    return "Hesap Numarası: " + hesapNumarasi + ", Ad Soyad: " + adSoyad;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return "Hesap Bilgisi Bulunamadı!";
        }

    public boolean hesapVarMi(String eposta){
        String sorgu = "SELECT hesap_numarasi from Hesaplar WHERE eposta = ?";
        try{
            PreparedStatement preparedStatement = con.prepareStatement(sorgu);
            preparedStatement.setString(1, eposta);
            ResultSet resultSet = preparedStatement.executeQuery();
            return !resultSet.next();
        }catch (SQLException e){
            e.printStackTrace();
        }
        return true;
    }
}
