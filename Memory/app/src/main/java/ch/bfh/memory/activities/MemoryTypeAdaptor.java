package ch.bfh.memory.activities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.List;

import ch.bfh.memory.R;
import ch.bfh.memory.interfaces.ClickListener;
import ch.bfh.memory.models.MemoryCard;
import ch.bfh.memory.models.MemoryPair;

public class MemoryTypeAdaptor extends RecyclerView.Adapter<MemoryTypeAdaptor.MemoryViewHolder> {

    List<MemoryPair> memoryPairs;
    private final ClickListener listener;

    public MemoryTypeAdaptor(List<MemoryPair> memoryPairs, ClickListener listener) {
        this.memoryPairs = memoryPairs;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MemoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_layout, parent, false);

        MemoryViewHolder viewHolder = new MemoryViewHolder(view, listener);


        return viewHolder;
    }

    /**
     * Set the content of the cards
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(@NonNull MemoryViewHolder holder, int position) {

        MemoryCard mOne = memoryPairs.get(position).cardOne;
        MemoryCard mTwo = memoryPairs.get(position).cardTwo;

        //Set the content of the first card
        if (mOne != null) {
            holder.getTextOne().setText(memoryPairs.get(position).cardOne.getWord());

            holder.getImgOne().setImageBitmap(getBitmapFromPath(memoryPairs.get(position).cardOne.getPath()));
        }

        //Set the content of the second card
        if (mTwo != null) {
            holder.getTextTwo().setText(memoryPairs.get(position).cardTwo.getWord());

            holder.getImgTwo().setImageBitmap(getBitmapFromPath(memoryPairs.get(position).cardTwo.getPath()));

            holder.setButtonVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return memoryPairs.size();
    }

    public Bitmap getBitmapFromPath(String path) {
        try {
            File img = new File(path);
            if (img.exists()) {
                Bitmap bitmap = BitmapFactory.decodeFile(img.getAbsolutePath());
                return bitmap;
            }
        } catch (Exception exception) {
            return null;
        }
        return null;
    }

    /**
     * This class is for the layout
     */
    public static class MemoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        /**
         * Textviews
         */
        private TextView card_text_one;
        private TextView card_text_two;

        /**
         * Imageviews
         */
        private ImageView card_image_one;
        private ImageView card_image_two;



        /**
         * Buttons
         */
        private Button btn_add_second;
        private Button btn_delete;
        private Button btn_split;

        private WeakReference<ClickListener> listenerRef;

        public MemoryViewHolder(View viewItem, ClickListener listener) {
            super(viewItem);

            this.card_text_one = (TextView) viewItem.findViewById(R.id.card_text_one);
            this.card_text_two = viewItem.findViewById(R.id.card_text_two);

            this.card_image_one = viewItem.findViewById(R.id.card_image_one);
            this.card_image_two = viewItem.findViewById(R.id.card_image_two);

            this.btn_add_second = viewItem.findViewById(R.id.btn_add_second);
            this.btn_delete = viewItem.findViewById(R.id.btn_delete);
            this.btn_split = viewItem.findViewById(R.id.btn_split);

            listenerRef = new WeakReference<>(listener);

            btn_add_second.setOnClickListener(this);
            btn_delete.setOnClickListener(this);
            btn_split.setOnClickListener(this);

        }

        public void setButtonVisibility(int visibility) {
            this.btn_add_second.setVisibility(visibility);
        }

        public TextView getTextOne() {
            return card_text_one;
        }

        public TextView getTextTwo() {
            return card_text_two;
        }

        public ImageView getImgOne() {
            return card_image_one;
        }

        public ImageView getImgTwo() {
            return card_image_two;
        }

        @Override
        public void onClick(View view) {
            if (view.getId() == btn_add_second.getId()) {
                listenerRef.get().onAddSecondClicked(getAdapterPosition());
            } else if (view.getId() == btn_delete.getId()) {
                listenerRef.get().onDeleteClick(getAdapterPosition());
            } else if (view.getId() == btn_split.getId()) {
                listenerRef.get().onSplitClick(getAdapterPosition());
            } else {
                Toast.makeText(view.getContext(), "ROW PRESSED = " + String.valueOf(getAdapterPosition()), Toast.LENGTH_SHORT).show();
            }


        }
    }
}
