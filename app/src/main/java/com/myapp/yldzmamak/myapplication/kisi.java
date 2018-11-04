package com.myapp.yldzmamak.myapplication;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

/**
 * Created by yldzmamak on 18.07.2018.
 */
@Table(name = "kisiler")
public class kisi extends Model implements Serializable {

    @Column(name = "Ad")
    private String Ad;

    @Column(name = "Soyad")
    private String Soyad;

    @Column(name = "TelNo")
    private String TelNo;

    @Column(name = "MailAdresi")
    private String MailAdresi;

    @Column(name = "DogumTarihi")
    private String DogumTarihi;

    @Column(name = "Emoji")
    private String Emoji;

    @Column(name = "Cinsiyet")
    private String Cinsiyet;

    @Column(name = "Adres")
    private String Adres;

    public String getAd() {
        return Ad;
    }

    public void setAd(String ad) {
        Ad = ad;
    }

    public String getSoyad() {
        return Soyad;
    }

    public void setSoyad(String soyad) {
        Soyad = soyad;
    }

    public String getTelNo() {
        return TelNo;
    }

    public void setTelNo(String telNo) {
        TelNo = telNo;
    }

    public String getMailAdresi() {
        return MailAdresi;
    }

    public void setMailAdresi(String mailAdresi) {
        MailAdresi = mailAdresi;
    }

    public String  getDogumTarihi() {
        return DogumTarihi;
    }

    public void setDogumTarihi(String dogumTarihi) {
        DogumTarihi = dogumTarihi;
    }

    public String  getEmoji() {
        return Emoji;
    }

    public void setEmoji(String emoji) {
        Emoji = emoji;
    }

    public String getCinsiyet() {
        return Cinsiyet;
    }

    public void setCinsiyet(String cinsiyet) {
        Cinsiyet = cinsiyet;
    }

    public String getAdres() {
        return Adres;
    }

    public void setAdres(String adres) {
        Adres = adres;
    }

    public void addKisi(kisi mKisi){
        mKisi.save();
    }

    public List<kisi> getKisiList() {
        return new Select()
                .from(kisi.class)
                .execute();
    }

    public kisi getKisi(UUID id_) {
        return new Select()
                .from(kisi.class)
                .where("mId = ?", id_)
                .executeSingle();
    }
}

