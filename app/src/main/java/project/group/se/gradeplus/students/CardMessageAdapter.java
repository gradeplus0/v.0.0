package project.group.se.gradeplus.students;

import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Random;

import project.group.se.gradeplus.R;

public class CardMessageAdapter extends RecyclerView.Adapter<CardMessageAdapter.ViewHolder>{
    private String[] messages;
    private int[] colors = {Color.CYAN,Color.RED,Color.BLUE,Color.GREEN,Color.MAGENTA,Color.LTGRAY,Color.BLACK};
    public CardMessageAdapter(String[] messages){
        this.messages = messages;

    }
    public static class ViewHolder extends  RecyclerView.ViewHolder {
        private CardView cardView;

        public ViewHolder(CardView v){
            super(v);
            cardView = v;
        }
    }

    @Override
    public CardMessageAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        CardView cv = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.card_message,parent,false);
        return new ViewHolder(cv);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position){
        CardView card = holder.cardView;
        TextView message = card.findViewById(R.id.card_body);
        message.setText(messages[position]);
        message.setTextColor(Color.WHITE);
        int random = (int)( (Math.random()*colors.length));
        card.setCardBackgroundColor(getRandomColor());
        System.out.println(random);
    }
    public int getRandomColor(){
        Random rnd = new Random();
        return Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
    }

    @Override
    public int getItemCount(){
        return this.messages.length;
    }
}
