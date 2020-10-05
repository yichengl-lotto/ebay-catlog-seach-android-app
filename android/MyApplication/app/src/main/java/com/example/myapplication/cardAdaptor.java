package com.example.myapplication;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import androidx.core.text.HtmlCompat;

public class cardAdaptor extends RecyclerView.Adapter<cardAdaptor.cardViewHolder> {

    private Context mcontext;
    private ArrayList<cardInformation> mcardList;
    private  onCardClickL cardListener;

    public interface onCardClickL {
        void onCardClick(int position);
    }

    public void setCardListener(onCardClickL listener) {
        cardListener = listener;
    }

    public cardAdaptor(Context context, ArrayList<cardInformation> cardList) {
        mcontext = context;
        mcardList = cardList;
    }

    @Override
    public cardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mcontext).inflate(R.layout.catlog_card, parent, false);
        return new cardViewHolder(v);
    }

    @Override
    public void onBindViewHolder(cardViewHolder holder, int position) {
        cardInformation currentItem = mcardList.get(position);
        String imageUrl = currentItem.getimageURL();
        String itemTitle = currentItem.getitemTitle();
        String shipPrice = currentItem.getshiPrice();
        String topRated = currentItem.gettopRated();
        String condition = currentItem.getcondition();
        String currentPrice = currentItem.getcurrentPrice();

        holder.mTitleTextView.setText(itemTitle);
        if (shipPrice.equals("0.0")) {
            holder.mShippingFeeTextView.setText(HtmlCompat.fromHtml("<b>FREE </b>Shipping",0));
        }
        else {

            holder.mShippingFeeTextView.setText(HtmlCompat.fromHtml("Ships for <b>$"+shipPrice+"</b>",0));
        }
        if(topRated.equals("true")) {
            holder.mTopRatedTextView.setText("Top Rated Listing");
            holder.mTopRatedTextView.setVisibility(View.VISIBLE);
        }
        else {
            holder.mTopRatedTextView.setVisibility(View.INVISIBLE);
        }
        if(condition.isEmpty()) {
            holder.mConditionTextView.setText("N/A");
        }
        else {
            holder.mConditionTextView.setText(condition);
        }
        holder.mCurrentPriceTextView.setText("$"+currentPrice);

        if(imageUrl.equals("https://thumbs1.ebaystatic.com/pict/04040_0.jpg")|| imageUrl.isEmpty()) {
            Picasso.get().load(R.drawable.ebay_default).fit().centerInside().into(holder.mImageView);
        }
        else {
            Picasso.get().load(imageUrl).fit().centerInside().into(holder.mImageView);
        }
    }

    @Override
    public int getItemCount() {
        return mcardList.size();
    }

    public class cardViewHolder extends RecyclerView.ViewHolder {
        public ImageView mImageView;
        public TextView mTitleTextView;
        public TextView mShippingFeeTextView;
        public TextView mTopRatedTextView;
        public TextView mConditionTextView;
        public TextView mCurrentPriceTextView;

        // public TextView mTextViewLikes;
        public cardViewHolder(View itemView) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.image_view);
            mTitleTextView = itemView.findViewById(R.id.card_title);
            mShippingFeeTextView= itemView.findViewById(R.id.card_shipfee);
            mTopRatedTextView = itemView.findViewById(R.id.card_topRated);
            mConditionTextView = itemView.findViewById(R.id.card_condition);
            mCurrentPriceTextView = itemView.findViewById(R.id.card_currentPrice);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (cardListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            cardListener.onCardClick(position);
                        }
                    }
                }
            });
        }
    }
}
