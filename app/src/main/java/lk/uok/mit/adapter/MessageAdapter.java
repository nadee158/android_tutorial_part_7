package lk.uok.mit.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;

import lk.uok.mit.helloworld.R;
import lk.uok.mit.model.Message;

public class MessageAdapter extends ArrayAdapter<Message> {
    //a variable to refer to the passed data set through constructor
    private List<Message> messages;
    //a variable to refer to the passed context through constructor
    private Context context;

    //four variables to refer to four text boxes
    private TextView rowItemContactNumber;
    private TextView rowItemMessageText;
    private TextView rowItemSentStatus;
    private TextView rowItemSentTime;

    //add a constructor to accept context and data set
    public MessageAdapter(@NonNull Context context, List<Message> messages) {
        //call super clase's constructor by passing the context, layout and the data set
        super(context, R.layout.message_row_item, messages);
        this.context = context;
        this.messages = messages;
    }

    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yy hh:mm a");

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        //Using the “position” parameter get the data item for current position from messages
        Message currentMessage = this.messages.get(position);
        //Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            //a view to be reused is not avaiable, therefore inflate new views
            //to inflate views initialize a android.view.LayoutInflaterobject
            LayoutInflater inflater = LayoutInflater.from(this.context);
            //inflate the R.layout.message_row_item, and assign the return value to the convertView
            convertView = inflater.inflate(R.layout.message_row_item, parent, false);
        }

        //using the inflated (initialized) convertView, get its child text views by id, if they are null
        this.rowItemContactNumber = convertView.findViewById(R.id.textViewMessageRowItemContactNumber);
        this.rowItemMessageText = convertView.findViewById(R.id.textViewMessageRowItemMessageText);
        this.rowItemSentStatus = convertView.findViewById(R.id.textViewMessageRowItemSentStatus);
        this.rowItemSentTime = convertView.findViewById(R.id.textViewMessageRowItemSentTime);

        //now set the data to be displyed in each text view
        this.rowItemContactNumber.setText(currentMessage.getContactNumber());
        this.rowItemMessageText.setText(currentMessage.getMessageText());
        this.rowItemSentStatus.setText(currentMessage.isSentStatus() ? "YES" : "NO");
        this.rowItemSentTime.setText(dateFormat.format(currentMessage.getSentTime()));

        return convertView;
    }
}
