package lk.uok.mit.helloworld;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import lk.uok.mit.adapter.MessageAdapter;
import lk.uok.mit.database.MyDataBaseOperations;
import lk.uok.mit.model.Message;

public class HelloWorldActivity extends AppCompatActivity {

    //a variable to hold context
    private Context context;
    //a variable to hold the message list retrieved from database
    private List<Message> messages;
    // a variable to hold the list view view
    private ListView listView;
    // a variable to hold the adapter
    private MessageAdapter adapter;
    // a variable to hold the database operations
    private MyDataBaseOperations dataBaseOperations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hello_world);
        this.context = getApplicationContext();
        //initialize the List View
        listView = (ListView) findViewById(R.id.listViewMessageList);
        //initialize the database operations
        this.dataBaseOperations = new MyDataBaseOperations(context);
        //retrieve all the saved messages
        this.messages = this.dataBaseOperations.getAllMessages();
        //call the sortMessages() method to sort message list
        this.sortMessages();
        //initialize the adapter using the retrieved and sorted messages and context
        this.adapter = new MessageAdapter(this.context, this.messages);
        //set the initialized adapter to the list view
        listView.setAdapter(adapter);
        //vcall “notifyDataSetChanged” method of the adapter to reflect the
        // latest data set available in the “messages”
        adapter.notifyDataSetChanged();
    }

    private void sortMessages() {
        //check if the class variable “messages” is null or empty – no point to sort a null or empty list
        if (!(this.messages == null || this.messages.isEmpty())) {
            Collections.sort(this.messages, new Comparator<Message>() {
                //of the anonymous inner class, override the compare method
                @Override
                public int compare(Message o1, Message o2) {
                    //since we want the most recently sent message at the top, take second object at front
                    //and use the compare method of java.util.Date
                    if (!(o1.getSentTime() == null || o2.getSentTime() == null)) {
                        return o2.getSentTime().compareTo(o1.getSentTime());
                    }
                    return o2.getId() - o1.getId();
                }
            });
        }
    }
}
