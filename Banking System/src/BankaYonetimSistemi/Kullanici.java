package BankaYonetimSistemi;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Kullanici {
    private final Connection con;
    private final Scanner sc;

    public Kullanici(Connection con, Scanner sc){
        this.con = con;
        this.sc = sc;
    }

    public void kayitOl(){
        sc.nextLine();
        System.out.print("Ad Soyad: ");
        String adSoyad = sc.nextLine();
        System.out.print("E-posta: ");
        String eposta = sc.nextLine();
        System.out.print("Şifre: ");
        String sifre = sc.nextLine();
        if(kullaniciVarMi(eposta)) {
            System.out.println("Bu E-posta Adresi Zaten Kullanılıyor!!");
            return;
        }
        String kayitSorgusu = "INSERT INTO Kullanici(ad_soyad, eposta, sifre) VALUES(?, ?, ?)";
        try {
            PreparedStatement preparedStatement = con.prepareStatement(kayitSorgusu);
            preparedStatement.setString(1, adSoyad);
            preparedStatement.setString(2, eposta);
            preparedStatement.setString(3, sifre);
            int etkilenenSatirlar = preparedStatement.executeUpdate();
            if (etkilenenSatirlar > 0) {
                System.out.println("Kayıt Başarılı! Aktivasyon maili gönderiliyor...");
                //String aktivasyonLinki = "http:/********/banka_sistemi/aktivasyon?eposta=" + eposta;
                //String mailIcerigi = "Merhaba " + adSoyad + ",\n\nLütfen aşağıdaki bağlantıya tıklayarak hesabınızı doğrulayın:\n" + aktivasyonLinki;
               //MailGonderici.mailGonder(eposta, "Hesap Aktivasyonu", mailIcerigi);
            } else {
                System.out.println("Kayıt Başarısız!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String girisYap(){
        sc.nextLine();
        System.out.print("E-posta: ");
        String eposta = sc.nextLine();
        System.out.print("Şifre: ");
        String sifre = sc.nextLine();
        String girisSorgusu = "SELECT * FROM Kullanici WHERE eposta = ? AND sifre = ?";
        try{
            PreparedStatement preparedStatement = con.prepareStatement(girisSorgusu);
            preparedStatement.setString(1, eposta);
            preparedStatement.setString(2, sifre);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                return eposta;
            }else{
                return null;
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    public boolean kullaniciVarMi(String eposta){
        String sorgu = "SELECT * FROM kullanici WHERE eposta = ?";
        try{
            PreparedStatement preparedStatement = con.prepareStatement(sorgu);
            preparedStatement.setString(1, eposta);
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();
        }catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }
}
