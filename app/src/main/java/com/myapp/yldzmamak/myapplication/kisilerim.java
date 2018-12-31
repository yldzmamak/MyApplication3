package com.myapp.yldzmamak.myapplication;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.activeandroid.query.Select;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class kisilerim extends AppCompatActivity implements Comparator<kisi> {
    Toolbar toolbar;
    RecyclerView recyclerView;
    private adapter mAdapter;
    FloatingActionButton floatingActionButton;
    List<kisi> prodList = new ArrayList<kisi>();
    TextView textView;
    SearchView searchView;
    private AdView mAdView;
    AVLoadingIndicatorView avi;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kisilerim);

        MobileAds.initialize(this, "ca-app-pub-5507053716111047~7567517224");

        mAdView = (AdView) findViewById(R.id.adView_kisilerim); //Reklamın layoutda tanımladığımız idsini alıyoruz ve load ediyoruz.
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);


        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //avi = (AVLoadingIndicatorView) findViewById(R.id.avi);
        progressBar = (ProgressBar) findViewById(R.id.progress);

        floatingActionButton = (FloatingActionButton)findViewById(R.id.fab);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent kisi_bilgileri_gecis=new Intent(kisilerim.this, KisiekleActivity.class);
                startActivity(kisi_bilgileri_gecis);
            }
        });
    }

    void startAnim(){
   //     avi.show();
        // or avi.smoothToShow();
    }

    void stopAnim(){
      //  avi.hide();
        // or avi.smoothToHide();
    }

    @Override
    protected void onStart() {
        super.onStart();
        prodList.clear();
        prodList =getAll();
        textView = (TextView) findViewById(R.id.tvk);

        recyclerView = (RecyclerView) findViewById(R.id.rcView);

        if(prodList.size()!= 0) {
            progressBar.setVisibility(View.INVISIBLE);
            textView.setVisibility(View.INVISIBLE);
            recyclerView.setVisibility(View.VISIBLE);
            Collections.sort(prodList, new kisilerim());
            recyclerView.setHasFixedSize(false);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(linearLayoutManager);
            mAdapter = new adapter(kisilerim.this, prodList, new adapter.ItemListener() {
                @Override
                public void onItemClick(kisi kisi) {
                    Intent i = new Intent(kisilerim.this, KisiBilgileriGoster.class);
                    i.putExtra("kisiNesne", kisi);

                    String id = kisi.getId().toString();
                    i.putExtra("id", id);

                    kisilerim.this.startActivity(i);
                }
            });
            //List<kisi> filtermodelist=filter(prodList,"");
            //mAdapter.setfilter(filtermodelist);
            recyclerView.setAdapter(mAdapter);

        }else {
            showContacts();
        }
    }
    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 100;

    private void showContacts() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, PERMISSIONS_REQUEST_READ_CONTACTS);
            } else {
            progressBar.setVisibility(View.VISIBLE);
            List<kisi> contacts = getContactList();
            prodList = contacts;
            if (contacts.size() == 0) {
                textView.setVisibility(View.VISIBLE);
            } else {
                textView.setVisibility(View.INVISIBLE);
                Collections.sort(prodList, new kisilerim());
                recyclerView.setHasFixedSize(false);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
                recyclerView.setLayoutManager(linearLayoutManager);
                mAdapter = new adapter(kisilerim.this, prodList, new adapter.ItemListener() {
                    @Override
                    public void onItemClick(kisi kisi) {
                        Intent i = new Intent(kisilerim.this, KisiBilgileriGoster.class);
                        i.putExtra("kisiNesne", kisi);

                        String id = kisi.getId().toString();
                        i.putExtra("id", id);

                        kisilerim.this.startActivity(i);
                    }
                });
                progressBar.setVisibility(View.INVISIBLE);
                recyclerView.setAdapter(mAdapter);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission is granted
                showContacts();
            } else {
                progressBar.setVisibility(View.INVISIBLE);
                textView.setVisibility(View.VISIBLE);
            }
        }
    }

    private List<kisi> getContactList() {

        List<kisi> contacts = new ArrayList<>();

        ContentResolver cr = getContentResolver();

        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);

        if ((cur != null ? cur.getCount() : 0) > 0) {

            while (cur != null && cur.moveToNext()) {
                String c="";
                String[] phone_No = new String[6];
                int i =0;
                String[] e_mail = new String[6]; ;
                int a =0;
                e_mail[0] ="";
                phone_No[0]="";
                String name ="";
                String surname = "";
                String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));

                name = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

                String[] newName = name.split(" ");

                if(newName.length == 1){
                    name = newName[0];
                    surname = "";
                }else {
                    for (int k = 0; k == newName.length - 2; k++) {
                        name = newName[k] + " ";
                        surname = newName[newName.length - 1 ];
                    }
                }
                name = ilkHarfBuyuk(name);
                surname = ilkHarfBuyuk(surname);

                if (cur.getInt(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {

                    Cursor pCur = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            new String[]{id}, null);

                    while (pCur.moveToNext()) {
                        String phoneNo = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        phone_No[i] = phoneNo;
                        i++;
                    }
                    i=0;
                    c = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                    if(phone_No[0].equals(c)){
                        name = "";
                        surname = "";
                    }
                    pCur.close();
                }
                if (cur.getInt(cur.getColumnIndex(ContactsContract.Contacts.Data._ID)) > 0) {
                    Cursor cur1 = cr.query(ContactsContract.CommonDataKinds.Email.CONTENT_URI, null,
                            ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?",
                            new String[]{id}, null);

                    while (cur1.moveToNext()) {
                        String email = cur1.getString(cur1.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
                        e_mail[a] = email;
                        a++;
                    }
                    a=0;
                    String d = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                    if(e_mail[0].equals(d)){
                        name = "";
                        surname = "";
                    }
                    cur1.close();
                }

                kisi yenikisi = new kisi();
                yenikisi.setAd(name);
                yenikisi.setSoyad(surname);
                yenikisi.setCinsiyet("");
                yenikisi.setDogumTarihi("");
                yenikisi.setEmoji("2131165276");
                yenikisi.setMailAdresi(e_mail[0]);
                yenikisi.setTelNo(phone_No[0]);
                yenikisi.setAdres("");
                yenikisi.save();
            }
        }
        contacts = getAll();

        if(cur!=null){
            cur.close();
        }
        return contacts;
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



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.ana_menu, menu);
        final MenuItem myActionMenuItem = menu.findItem(R.id.search);
        searchView = (SearchView) myActionMenuItem.getActionView();
       // changeSearchViewTextColor(searchView);
        ((EditText) searchView.findViewById(
                android.support.v7.appcompat.R.id.search_src_text)).
                setHintTextColor(getResources().getColor(R.color.white));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (!searchView.isIconified()) {
                    searchView.setIconified(true);
                }
                myActionMenuItem.collapseActionView();
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                if(newText.equals("")){
                    List<kisi> filtermodelist=filter(prodList,newText);
                    mAdapter.setfilter(filtermodelist);
                    return true;
                }else{
                    List<kisi> filtermodelist=filter(prodList,newText);
                    mAdapter.setfilter(filtermodelist);
                    return true;
                }
            }
        });
        return true;
    }

    public List<kisi> filter(List<kisi> pl,String query)
    {
        query=query.toLowerCase();
        final List<kisi> filteredModeList=new ArrayList<>();
        for (kisi model:pl)
        {

            final String text=model.getAd().toLowerCase();
            if (text.startsWith(query))
            {
                filteredModeList.add(model);
            }
        }
        return filteredModeList;
    }
    private void changeSearchViewTextColor(View view) {
        if (view != null) {
            if (view instanceof TextView) {
                ((TextView) view).setTextColor(Color.WHITE);
                return;
            } else if (view instanceof ViewGroup) {
                ViewGroup viewGroup = (ViewGroup) view;
                for (int i = 0; i < viewGroup.getChildCount(); i++) {
                    changeSearchViewTextColor(viewGroup.getChildAt(i));
                }
            }
        }
    }
    public static List<kisi> getAll(){
        return new Select()
                .from(kisi.class)
                .orderBy("id ASC")
                .execute();
    }

    @Override
    public int compare(kisi kisi, kisi t1) {
        return kisi.getAd().compareTo(t1.getAd());
    }
}
