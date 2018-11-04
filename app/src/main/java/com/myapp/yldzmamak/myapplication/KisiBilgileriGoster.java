package com.myapp.yldzmamak.myapplication;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

import de.hdodenhof.circleimageview.CircleImageView;

public class KisiBilgileriGoster extends AppCompatActivity {
    ImageButton duzenle,btnBack;
    ImageButton btnAra,btnMail;
    CircleImageView emojiImage;
    TextView adSoyad, numara, eMail, dogumGunu,tvCinsiyet,tvAdres;
    private AdView mAdView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kisi_bilgileri_goster);

        MobileAds.initialize(this, "ca-app-pub-5507053716111047~7567517224");

        mAdView = (AdView) findViewById(R.id.adView_kisiBilgileri); //Reklamın layoutda tanımladığımız idsini alıyoruz ve load ediyoruz.
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        emojiImage = (CircleImageView) findViewById(R.id.profile_image);
        adSoyad = (TextView) findViewById(R.id.tvAdSoyad);
        numara = (TextView) findViewById(R.id.tvNumara);
        eMail = (TextView) findViewById(R.id.tvMail);
        dogumGunu = (TextView) findViewById(R.id.tvDgunu);
        duzenle = (ImageButton) findViewById(R.id.btnDuzenle);
        btnAra = (ImageButton) findViewById(R.id.btnAra);
        btnMail = (ImageButton) findViewById(R.id.btnMail);
        tvCinsiyet = (TextView)findViewById(R.id.tvCinsiyet);
        btnBack = (ImageButton)findViewById(R.id.btnBack);
        tvAdres = (TextView) findViewById(R.id.tvAdres);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        final Intent i = getIntent();
        final kisi kisi = (kisi) i.getSerializableExtra("kisiNesne");
        final String id = i.getStringExtra("id");

        if(kisi.getEmoji().equals("")){
            emojiImage.setImageResource(R.drawable.bos);
        }else{
            emojiImage.setImageResource(Integer.parseInt(kisi.getEmoji()));
        }

        adSoyad.setText(kisi.getAd() + " " + kisi.getSoyad());
        numara.setText(kisi.getTelNo());
        eMail.setText(kisi.getMailAdresi());
        dogumGunu.setText(kisi.getDogumTarihi());
        tvAdres.setText(kisi.getAdres());
        switch (kisi.getCinsiyet()) {
            case "1":
               tvCinsiyet.setText(getResources().getString(R.string.rbKadin));
                break;
            case "2":
                tvCinsiyet.setText(getResources().getString(R.string.rbErkek));
                break;
            case "":
                tvCinsiyet.setText("-");
                break;
        }

        btnAra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int REQUEST_PHONE_CALL = 1;
                Intent a = new Intent(Intent.ACTION_CALL);
                a.setData(Uri.parse("tel:" + kisi.getTelNo()));

                if (ActivityCompat.checkSelfPermission(KisiBilgileriGoster.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(KisiBilgileriGoster.this, new String[]{Manifest.permission.CALL_PHONE},REQUEST_PHONE_CALL);

                }else
                {
                    startActivity(a);
                }
            }
        });

        btnMail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/html");
                intent.putExtra(Intent.EXTRA_EMAIL, new String[] { kisi.getMailAdresi()});
                intent.putExtra(Intent.EXTRA_SUBJECT, "");
                intent.putExtra(Intent.EXTRA_TEXT, "");
                startActivity(Intent.createChooser(intent, getResources().getString(R.string.mesaj_gonder)));
            }
        });

        duzenle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(KisiBilgileriGoster.this,KisiekleActivity.class);
                intent.putExtra("Kisi", kisi);
                intent.putExtra("kisiId",id);
                finish();
                KisiBilgileriGoster.this.startActivity(intent);
            }
        });

    }

}
