package ch.epfl.sweng.favors.chat;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ch.epfl.sweng.favors.R;
import ch.epfl.sweng.favors.database.ChatInformations;
import ch.epfl.sweng.favors.database.ChatMessage;
import ch.epfl.sweng.favors.database.ObservableArrayList;
import ch.epfl.sweng.favors.databinding.ChatBubbleBinding;
import ch.epfl.sweng.favors.databinding.ChatListItemBinding;

/**
 * FavorListAdapter
 * Class that represents the graphical list view to display Favors
 * sets the fields that shpould be visible in the ListView
 * favor_item.xml (item of list) and fragment_favors.xml (list)
 */
public class ChatBubbleAdapter extends RecyclerView.Adapter<ChatBubbleAdapter.ChatBubbleViewHolder> {
    private ObservableArrayList<ChatMessage> chatsList;
    private ChatWindow parent;

    private static final String TAG = "CHATS_BUB_ADAPT";

    public ChatBubbleAdapter(ChatWindow fragActivity, ObservableArrayList<ChatMessage> chatsList) {
        this.chatsList = chatsList;
        this.parent = fragActivity;
    }

    class ChatBubbleViewHolder extends RecyclerView.ViewHolder {
        private final ChatBubbleBinding binding;

        public ChatBubbleViewHolder(ChatBubbleBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(ChatMessage item) {
            binding.setChatMessage(item);
        }
    }

    @Override
    public ChatBubbleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ChatBubbleViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.chat_bubble, parent, false));
    }

    @Override
    public void onBindViewHolder(ChatBubbleViewHolder holder, int position) {
        holder.bind(chatsList.get(position));
    }

    @Override
    public int getItemCount() {
        return chatsList.size();
    }



}
