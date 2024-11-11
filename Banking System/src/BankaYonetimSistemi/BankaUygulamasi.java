package BankaYonetimSistemi;
import java.lang.String;
import java.sql.*;
import java.util.Scanner;

import static java.sql.DriverManager.getConnection;

public class BankaUygulamasi {
    private static final String kullaniciadi = "root";
    private static final String sifre = "Eren.2144-";
    private static final String URL = "jdbc:mysql://127.0.0.1:3306/banka_sistemi";


    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }
        try {
            Connection con = getConnection(URL, kullaniciadi, sifre);
            Scanner sc = new Scanner(System.in);
            Kullanici kullanici = new Kullanici(con, sc);
            BankaYonetimSistemi.Hesaplar hesaplar = new BankaYonetimSistemi.Hesaplar(con, sc);
            BankaYonetimSistemi.HesapYonetici hesapYonetici = new BankaYonetimSistemi.HesapYonetici(con, sc);
            IslemlerYonetici islemlerYonetici = new IslemlerYonetici(con);

            String eposta;
            long hesapNumarasi;

            while (true) {
                System.out.println("*** BANKA SİSTEMİNE HOŞGELDİNİZ ***");
                System.out.println();
                System.out.println("1. Kayıt Ol");
                System.out.println("2. Giriş Yap");
                System.out.println("3. Çıkış");
                System.out.println("Seçiminizi Girin: ");
                int secim1 = sc.nextInt();
                switch (secim1) {
                    case 1:
                        kullanici.kayitOl();
                        break;
                    case 2:
                        eposta = kullanici.girisYap();
                        if (eposta != null) {
                            System.out.println();
                            System.out.println("Kullanıcı Giriş Yaptı!");
                            System.out.println(hesaplar.hesapBilgileriniGetir(eposta));
                            if (hesaplar.hesapVarMi(eposta)) {
                                System.out.println();
                                System.out.println("1. Yeni Bir Banka Hesabı Aç");
                                System.out.println("2. Çıkış");
                                if (sc.nextInt() == 1) {
                                    hesapNumarasi = hesaplar.hesapAc(eposta);
                                    System.out.println("Hesap Başarıyla Açıldı.");
                                    System.out.println("Hesap Numaranız: " + hesapNumarasi);
                                } else {
                                    break;
                                }
                            }
                            hesapNumarasi = hesaplar.hesapNumarasiniGetir(eposta);
                            int secim2 = 0;
                            while (secim2 != 5) {
                                System.out.println();
                                System.out.println("1. Para Çek");
                                System.out.println("2. Para Yatır");
                                System.out.println("3. Para Transferi");
                                System.out.println("4. Bakiye Görüntüle");
                                System.out.println("5. Tüm İşlemleri Göster");
                                System.out.println("6. Çıkış Yap");
                                System.out.println("Seçiminizi Girin: ");
                                secim2 = sc.nextInt();
                                switch (secim2) {
                                    case 1:
                                        hesapYonetici.paraCek(hesapNumarasi);
                                        break;
                                    case 2:
                                        hesapYonetici.paraYatir(hesapNumarasi);
                                        break;
                                    case 3:
                                        hesapYonetici.paraTransferi(hesapNumarasi);
                                        break;
                                    case 4:
                                        hesapYonetici.bakiyeGoruntule(hesapNumarasi);
                                        break;
                                    case 5:
                                        islemlerYonetici.tumIslemleriGoster();
                                        break;
                                    default:
                                        System.out.println("Geçerli Bir Seçim Girin!");
                                        break;
                                }
                            }
                        } else {
                            System.out.println("Yanlış E-posta veya Şifre!");
                        }
                        break;
                    case 3:
                        System.out.println("BANKA SİSTEMİNİ KULLANDIĞINIZ İÇİN TEŞEKKÜRLER!!!");
                        System.out.println("Sistemden Çıkılıyor!");
                        return;
                    default:
                        System.out.println("Geçerli Bir Seçim Girin");
                        break;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
