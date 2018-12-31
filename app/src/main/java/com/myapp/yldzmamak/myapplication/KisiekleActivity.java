package com.myapp.yldzmamak.myapplication;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import com.activeandroid.query.Delete;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

import java.util.ArrayList;
import java.util.Calendar;
import de.hdodenhof.circleimageview.CircleImageView;

public class KisiekleActivity extends AppCompatActivity {

    RadioButton radioButtonErkek, radioButtonKadin;
    private int mYear, mMonth, mDay;
    Button btnkaydet;
    ImageButton btnBack;
    EditText etAd, etSoyad, etTelNo, etEmail,etAdres;
    TextView etDogumTarihi,emoji;
    RecyclerView recyclerView;
    CircleImageView emojiimage;
    public String emojiDegeri ="";
    ArrayList<emojiItem> arrayList;
    String ad="",soyad="",telno="",email="",dogumtarihi="",cinsiyet="", adres="";
    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kisiekle);


        MobileAds.initialize(this, "ca-app-pub-5507053716111047~7567517224");

        mAdView = (AdView) findViewById(R.id.adView_kisiEkle); //Reklamın layoutda tanımladığımız idsini alıyoruz ve load ediyoruz.
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        btnkaydet = (Button) findViewById(R.id.btnkaydet);
        etAd = (EditText) findViewById(R.id.ad);
        etSoyad = (EditText) findViewById(R.id.soyad);
        etTelNo = (EditText) findViewById(R.id.telno);
        etEmail = (EditText) findViewById(R.id.email);
        etDogumTarihi = (TextView) findViewById(R.id.dogumtarihi);
        emojiimage = (CircleImageView) findViewById(R.id.profile_image);
        radioButtonErkek = (RadioButton) findViewById(R.id.rbErkek);
        radioButtonKadin = (RadioButton) findViewById(R.id.rbKadin);
        btnBack = (ImageButton)findViewById(R.id.btnBack);
        etAdres = (EditText) findViewById(R.id.adres);

        Intent intent = getIntent();
        final kisi kisi = (kisi) intent.getSerializableExtra("Kisi");

        if(kisi != null){
            emojiimage.setImageResource(Integer.parseInt(kisi.getEmoji()));
            etAd.setText(kisi.getAd());
            etSoyad.setText(kisi.getSoyad());
            etTelNo.setText(kisi.getTelNo());
            etEmail.setText(kisi.getMailAdresi());
            etAdres.setText(kisi.getAdres());
            etDogumTarihi.setText(kisi.getDogumTarihi());
            switch (kisi.getCinsiyet()) {
                case "1":
                    radioButtonKadin.setChecked(true);
                    break;
                case "2":
                    radioButtonErkek.setChecked(true);
                    break;
                case "":
                    break;
            }
            emojiDegeri = kisi.getEmoji();
        }

        emojiimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showMyCustomAlertDialog();
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    onBackPressed();
            }
        });

        etDogumTarihi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(KisiekleActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {

                        etDogumTarihi.setText(i2 + "-" + (i1 + 1) + "-" + i);

                    }
                },mYear,mMonth,mDay);
                datePickerDialog.show();
            }
        });

        btnkaydet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (kisi == null) {
                    kisi yenikisi = new kisi();

                    ad = ilkHarfBuyuk(etAd.getText().toString());
                    soyad = ilkHarfBuyuk(etSoyad.getText().toString());
                    telno = etTelNo.getText().toString();
                    email = etEmail.getText().toString();
                    dogumtarihi = etDogumTarihi.getText().toString();
                    adres = etAdres.getText().toString();
                    cinsiyet = "";
                    if (radioButtonKadin.isChecked()) {
                        cinsiyet = "1";
                    } else if (radioButtonErkek.isChecked()) {
                        cinsiyet = "2";
                    }

                    if (ad.equals("") && soyad.equals("") && telno.equals("") && email.equals("") && dogumtarihi.equals("") && adres.equals("")) {
                        Toast.makeText(KisiekleActivity.this, getResources().getString(R.string.gereklialan), Toast.LENGTH_SHORT).show();
                    } else {
                        yenikisi.setAd(ad);
                        yenikisi.setSoyad(soyad);
                        yenikisi.setTelNo(telno);
                        yenikisi.setMailAdresi(email);
                        yenikisi.setDogumTarihi(dogumtarihi);
                        yenikisi.setAdres(adres);
                        if (emojiDegeri.equals("2131165276") || emojiDegeri.equals("")) {
                            if (radioButtonKadin.isChecked()) {
                                emojiDegeri = "2131165394";
                            } else if (radioButtonErkek.isChecked()) {
                                emojiDegeri = "2131165391";
                            }
                        }

                        if(emojiDegeri.equals("")){
                            emojiDegeri = "2131165276";
                        }

                        yenikisi.setEmoji(emojiDegeri);
                        yenikisi.setCinsiyet(cinsiyet);
                        yenikisi.save();

                        KisiekleActivity.this.finish();
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.eklendi), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Intent intent = getIntent();
                    Integer id = Integer.parseInt(intent.getStringExtra("kisiId"));

                    new Delete().from(kisi.class).where("Id = ?", id).execute();
                    kisi yenikisi = new kisi();

                    ad = ilkHarfBuyuk(etAd.getText().toString());
                    soyad = ilkHarfBuyuk(etSoyad.getText().toString());
                    telno = etTelNo.getText().toString();
                    email = etEmail.getText().toString();
                    dogumtarihi = etDogumTarihi.getText().toString();
                    adres = etAdres.getText().toString();
                    cinsiyet = "";
                    if (radioButtonKadin.isChecked()) {
                        cinsiyet = "1";
                    } else if (radioButtonErkek.isChecked()) {
                        cinsiyet = "2";
                    }

                    if (ad.equals("") && soyad.equals("") && telno.equals("") && email.equals("") && dogumtarihi.equals("") && adres.equals("")) {
                        Toast.makeText(KisiekleActivity.this, getResources().getString(R.string.gereklialan), Toast.LENGTH_SHORT).show();
                    } else {
                        yenikisi.setAd(ad);
                        yenikisi.setSoyad(soyad);
                        yenikisi.setTelNo(telno);
                        yenikisi.setMailAdresi(email);
                        yenikisi.setDogumTarihi(dogumtarihi);
                        if (emojiDegeri.equals("2131165276") || emojiDegeri.equals("")) {
                            if (radioButtonKadin.isChecked()) {
                                emojiDegeri = "2131165394";
                            } else if (radioButtonErkek.isChecked()) {
                                emojiDegeri = "2131165391";
                            }
                        }
                        if(emojiDegeri.equals("")){
                            emojiDegeri = "2131165276";
                        }
                        if (emojiDegeri.equals("2131165394") || emojiDegeri.equals("2131165391")){
                            if(cinsiyet =="1"){
                                emojiDegeri = "2131165394";
                            }else{ emojiDegeri = "2131165391";}
                        }

                        yenikisi.setEmoji(emojiDegeri);
                        yenikisi.setCinsiyet(cinsiyet);
                        yenikisi.setAdres(adres);
                        yenikisi.save();

                       KisiekleActivity.this.finish();
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.guncellendi), Toast.LENGTH_SHORT).show();
                    }
                }
                }

        });
    }

    String trimed="";
    String ilkHarfBuyuk(String str)
    {
        if(str.equals("") ){
            return str;
        }else {
            trimed = str.trim();
            // str Stringinin içindeki kelimelerin ilk harfleri büyük diğerleri küçük yapılır.
            char c = Character.toUpperCase(trimed.charAt(0));
            //ilk harfini buyuttuk
            trimed = c + trimed.substring(1);
            //buyutulen ilk harften sonra kelimenin diger harflerini ekledik.
            String bosluk = " ";
            for (int i = 1; i < trimed.length(); i++) {
                if (trimed.charAt(i) == ' ') {
                    c = Character.toUpperCase(trimed.charAt(i + 1));
                    trimed = trimed.substring(0, i) + bosluk + c + trimed.substring(i + 2);
                }

            }

        }
        return trimed;
    }

    public void showMyCustomAlertDialog() {

        // dialog nesnesi oluştur ve layout dosyasına bağlan
        final Dialog dialog = new Dialog(KisiekleActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.emoji_window);

        // custom dialog elemanlarını tanımla - text, image ve button
        recyclerView = (RecyclerView) dialog.findViewById(R.id.recyclerView);

        arrayList = new ArrayList<>();
        arrayList.add(new emojiItem( R.drawable.bos, "#ffffff"));
        arrayList.add(new emojiItem( R.drawable.emoji2, "#F778A1"));
        arrayList.add(new emojiItem( R.drawable.emoji3, "#99CC00"));
        arrayList.add(new emojiItem( R.drawable.emoji4, "#4BAA50"));
        arrayList.add(new emojiItem( R.drawable.emoji5, "#33CC33"));
        arrayList.add(new emojiItem( R.drawable.emoji6, "#3E51B1"));
        arrayList.add(new emojiItem( R.drawable.emoji7, "#66CC99"));
        arrayList.add(new emojiItem( R.drawable.emoji8, "#9999CC"));
        arrayList.add(new emojiItem( R.drawable.emoji9, "#CC6666"));
        arrayList.add(new emojiItem( R.drawable.emoji10, "#FF9966"));
        arrayList.add(new emojiItem( R.drawable.emoji11, "#09A9FF"));
        arrayList.add(new emojiItem( R.drawable.emoji12, "#99CC00"));
        arrayList.add(new emojiItem( R.drawable.emoji13, "#66CCFF"));
        arrayList.add(new emojiItem( R.drawable.emoji14, "#FF9933"));
        arrayList.add(new emojiItem( R.drawable.emoji15, "#9966CC"));
        arrayList.add(new emojiItem( R.drawable.emoji16, "#FF66FF"));
        arrayList.add(new emojiItem( R.drawable.emoji19, "#09A9FF"));
        arrayList.add(new emojiItem( R.drawable.emoji20, "#CC66CC"));
        arrayList.add(new emojiItem( R.drawable.emoji22, "#FF6666"));
        arrayList.add(new emojiItem( R.drawable.emoji23, "#6699CC"));
        arrayList.add(new emojiItem( R.drawable.emoji24, "#50EBEC"));
        arrayList.add(new emojiItem( R.drawable.emoji25, "#FF6600"));
        arrayList.add(new emojiItem( R.drawable.emoji26, "#09A9FF"));
        arrayList.add(new emojiItem( R.drawable.emoji27, "#CC3399"));
        arrayList.add(new emojiItem( R.drawable.emoji28, "#FF0033"));
        arrayList.add(new emojiItem( R.drawable.emoji29, "#999900"));
        arrayList.add(new emojiItem( R.drawable.emoji30, "#46C7C7"));
        arrayList.add(new emojiItem( R.drawable.emoji31, "#CCCC33"));
        arrayList.add(new emojiItem( R.drawable.emoji32, "#09A9FF"));
        arrayList.add(new emojiItem( R.drawable.emoji33, "#CC3366"));
        arrayList.add(new emojiItem( R.drawable.emoji35, "#999933"));
        arrayList.add(new emojiItem( R.drawable.emoji36, "#FFCCCC"));
        arrayList.add(new emojiItem( R.drawable.emoji37, "#F88017"));
        arrayList.add(new emojiItem( R.drawable.emoji39, "#00CCCC"));
        arrayList.add(new emojiItem( R.drawable.emoji40, "#F9966B"));
        arrayList.add(new emojiItem( R.drawable.emoji41, "#0A9B88"));
        arrayList.add(new emojiItem( R.drawable.emoji42, "#09A9FF"));
        arrayList.add(new emojiItem( R.drawable.emoji43, "#F778A1"));
        arrayList.add(new emojiItem( R.drawable.emoji44, "#99CC00"));
        arrayList.add(new emojiItem( R.drawable.emoji45, "#4BAA50"));
        arrayList.add(new emojiItem( R.drawable.emoji46, "#33CC33"));
        arrayList.add(new emojiItem( R.drawable.emoji47, "#3E51B1"));
        arrayList.add(new emojiItem( R.drawable.emoji48, "#66CC99"));
        arrayList.add(new emojiItem( R.drawable.emoji49, "#9999CC"));
        arrayList.add(new emojiItem( R.drawable.emoji52, "#CC6666"));
        arrayList.add(new emojiItem( R.drawable.emoji53, "#FF9966"));
        arrayList.add(new emojiItem( R.drawable.emoji54, "#09A9FF"));
        arrayList.add(new emojiItem( R.drawable.emoji55, "#99CC00"));
        arrayList.add(new emojiItem( R.drawable.emoji56, "#66CCFF"));
        arrayList.add(new emojiItem( R.drawable.emoji57, "#FF9933"));
        arrayList.add(new emojiItem( R.drawable.emoji58, "#9966CC"));
        arrayList.add(new emojiItem( R.drawable.emoji59, "#FF66FF"));
        arrayList.add(new emojiItem( R.drawable.emoji60, "#09A9FF"));
        arrayList.add(new emojiItem( R.drawable.emoji61, "#CC66CC"));
        arrayList.add(new emojiItem( R.drawable.emoji62, "#FF6666"));
        arrayList.add(new emojiItem( R.drawable.emoji65, "#6699CC"));
        arrayList.add(new emojiItem( R.drawable.emoji67, "#50EBEC"));
        arrayList.add(new emojiItem( R.drawable.emoji68, "#FF6600"));
        arrayList.add(new emojiItem( R.drawable.emoji70, "#09A9FF"));
        arrayList.add(new emojiItem( R.drawable.emoji71, "#CC3399"));
        arrayList.add(new emojiItem( R.drawable.emoji72, "#FF0033"));
        arrayList.add(new emojiItem( R.drawable.emoji73, "#999900"));
        arrayList.add(new emojiItem( R.drawable.emoji74, "#46C7C7"));
        arrayList.add(new emojiItem( R.drawable.emoji75, "#CCCC33"));
        arrayList.add(new emojiItem( R.drawable.emoji76, "#09A9FF"));
        arrayList.add(new emojiItem( R.drawable.emoji77, "#CC3366"));
        arrayList.add(new emojiItem( R.drawable.emoji78, "#999933"));
        arrayList.add(new emojiItem( R.drawable.emoji79, "#FFCCCC"));
        arrayList.add(new emojiItem( R.drawable.emoji80, "#F88017"));
        arrayList.add(new emojiItem( R.drawable.emoji81, "#00CCCC"));
        arrayList.add(new emojiItem( R.drawable.emoji82, "#F9966B"));
        arrayList.add(new emojiItem( R.drawable.emoji83, "#0A9B88"));
        arrayList.add(new emojiItem( R.drawable.emoji84, "#09A9FF"));
        arrayList.add(new emojiItem( R.drawable.emoji85, "#F778A1"));
        arrayList.add(new emojiItem( R.drawable.emoji86, "#99CC00"));
        arrayList.add(new emojiItem( R.drawable.emoji87, "#4BAA50"));
        arrayList.add(new emojiItem( R.drawable.emoji88, "#33CC33"));
        arrayList.add(new emojiItem( R.drawable.emoji89, "#3E51B1"));
        arrayList.add(new emojiItem( R.drawable.emoji90, "#66CC99"));
        arrayList.add(new emojiItem( R.drawable.emoji91, "#9999CC"));
        arrayList.add(new emojiItem( R.drawable.emoji92, "#CC6666"));
        arrayList.add(new emojiItem( R.drawable.emoji93, "#FF9966"));
        arrayList.add(new emojiItem( R.drawable.emoji94, "#09A9FF"));
        arrayList.add(new emojiItem( R.drawable.emoji95, "#99CC00"));
        arrayList.add(new emojiItem( R.drawable.emoji96, "#66CCFF"));
        arrayList.add(new emojiItem( R.drawable.emoji97, "#FF9933"));
        arrayList.add(new emojiItem( R.drawable.emoji98, "#9966CC"));
        arrayList.add(new emojiItem( R.drawable.emoji99, "#FF66FF"));
        arrayList.add(new emojiItem( R.drawable.emoji100, "#09A9FF"));
        arrayList.add(new emojiItem( R.drawable.emoji101, "#CC66CC"));
        arrayList.add(new emojiItem( R.drawable.emoji102, "#FF6666"));
        arrayList.add(new emojiItem( R.drawable.emoji103, "#6699CC"));
        arrayList.add(new emojiItem( R.drawable.emoji104, "#50EBEC"));
        arrayList.add(new emojiItem( R.drawable.emoji105, "#FF6600"));
        arrayList.add(new emojiItem( R.drawable.emoji106, "#09A9FF"));
        arrayList.add(new emojiItem( R.drawable.emoji107, "#CC3399"));
        arrayList.add(new emojiItem( R.drawable.emoji108, "#FF0033"));
        arrayList.add(new emojiItem( R.drawable.emoji109, "#999900"));
        arrayList.add(new emojiItem( R.drawable.emoji110, "#46C7C7"));
        arrayList.add(new emojiItem( R.drawable.emoji111, "#CCCC33"));
        arrayList.add(new emojiItem( R.drawable.emoji112, "#09A9FF"));
        arrayList.add(new emojiItem( R.drawable.emoji113, "#CC3366"));
        arrayList.add(new emojiItem( R.drawable.emoji114, "#999933"));
        arrayList.add(new emojiItem( R.drawable.emoji115, "#FFCCCC"));

        emojiAdapter adapter = new emojiAdapter(this, arrayList, new emojiAdapter.ItemListener() {
            @Override
            public void onItemClick(emojiItem item) {
                dialog.dismiss();
                emojiimage.setImageResource(item.drawable);
                emojiDegeri = Integer.toString(item.drawable) ;
            }
        });

        recyclerView.setAdapter(adapter);
        GridLayoutManager manager = new GridLayoutManager(this, 4, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);

        dialog.show();

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

}
