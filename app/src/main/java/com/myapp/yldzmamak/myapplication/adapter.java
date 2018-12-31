package com.myapp.yldzmamak.myapplication;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.activeandroid.query.Delete;
import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.chauthai.swipereveallayout.ViewBinderHelper;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.List;


public class adapter extends RecyclerView.Adapter<adapter.MyHolder> {

    private Context mContext;
    private List<kisi> mKisiList = new ArrayList<>();
    private ItemListener mListener;

    private final ViewBinderHelper viewBinderHelper = new ViewBinderHelper();

    public adapter(Context context,List<kisi> _ProdList, ItemListener itemListener) {
        mKisiList =_ProdList;
        mContext = context;
        mListener=itemListener;
    }

    public adapter(List<kisi> kisiler) {
        this.mKisiList = kisiler;
    }

    @Override
    public adapter.MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.kisi_item,parent,false);
        MyHolder holder = new MyHolder(v);

        return holder;
    }

    @Override
    public void onBindViewHolder(adapter.MyHolder holder, int position) {
        kisi kisi = mKisiList.get(position);
        holder.doldur(kisi,mContext);
        viewBinderHelper.bind(holder.swipe ,String.valueOf(kisi.getId()));
    }

    @Override
    public int getItemCount() {
        return mKisiList.size();
    }

    public void removeItem(int position){
        mKisiList.remove(position);
        notifyItemRemoved(position);
    }

    public class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView adSoyad, TelNo;
        SwipeRevealLayout swipe;
        Button btnDelete;
        ImageView kisiEmoji;
        public kisi item;
        LinearLayout linearLayout;
        private AVLoadingIndicatorView avi;


        public MyHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            adSoyad = (TextView) itemView.findViewById(R.id.AdSoyad);
            TelNo = (TextView) itemView.findViewById(R.id.TelNo);
            swipe = (SwipeRevealLayout) itemView.findViewById(R.id.swipe);
            btnDelete = (Button) itemView.findViewById(R.id.btnDelete);
            kisiEmoji = (ImageView) itemView.findViewById(R.id.kisiEmoji);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.linearr);

        }


        public void doldur(final kisi kisi, final Context context)  {

            this.item = kisi;
            if(kisi.getEmoji().equals(null) || kisi.getEmoji().equals("") ){
                kisiEmoji.setImageResource(R.drawable.bos);
            }
            else {
                kisiEmoji.setImageResource(Integer.parseInt(kisi.getEmoji()));
            }
            adSoyad.setText(kisi.getAd() + " " + kisi.getSoyad());
            TelNo.setText(kisi.getTelNo());
//2131165276

            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View view) {

                    // dialog nesnesi oluştur ve layout dosyasına bağlan
                    final Dialog dialog = new Dialog(view.getContext());
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.custom_dialog);

                    // custom dialog elemanlarını tanımla - text, image ve button
                    Button btnKaydet = (Button) dialog.findViewById(R.id.button_kaydet);
                    Button btnIptal = (Button) dialog.findViewById(R.id.button_iptal);
                    // custom dialog elemanlarına değer ataması yap - text, image
                    //tvBaslik.setText("Custom Dialog Örneği");

                    // tamam butonunun tıklanma olayları
                    btnKaydet.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            new Delete().from(kisi.class).where("Id = ?", kisi.getId()).execute();
                            removeItem(getAdapterPosition());
                            dialog.dismiss();
                            Toast.makeText(context, R.string.silindi, Toast.LENGTH_SHORT).show();
                        }
                    });
                    // iptal butonunun tıklanma olayları
                    btnIptal.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });

                    dialog.show();

                }
            });

            linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        mListener.onItemClick(item);
                        //Toast.makeText(mContext, kisi_item.text + " is clicked", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }

        @Override
        public void onClick(View v) {

        }
    }
    public interface ItemListener {
        void onItemClick(kisi kisi);
    }

    public void setfilter(List<kisi> listitem)
    {
        mKisiList=new ArrayList<>();
        mKisiList.addAll(listitem);
        notifyDataSetChanged();
    }
}
