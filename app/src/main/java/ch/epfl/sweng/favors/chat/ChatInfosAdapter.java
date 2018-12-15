package ch.epfl.sweng.favors.chat;

import android.databinding.DataBindingUtil;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ch.epfl.sweng.favors.R;
import ch.epfl.sweng.favors.database.ChatInformations;
import ch.epfl.sweng.favors.database.ObservableArrayList;
import ch.epfl.sweng.favors.databinding.ChatListItemBinding;


public class ChatInfosAdapter extends RecyclerView.Adapter<ChatInfosAdapter.ChatItemViewHolder> {
    private ObservableArrayList<ChatInformations> chatsList;
    private ChatsList parent;

    private static final String TAG = "CHATS_IT_ADAPT";

    public ChatInfosAdapter(ChatsList fragActivity, ObservableArrayList<ChatInformations> chatsList) {
        this.chatsList = chatsList;
        this.parent = fragActivity;
    }

    class ChatItemViewHolder extends RecyclerView.ViewHolder {
        private final ChatListItemBinding binding;

        public ChatItemViewHolder(ChatListItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(ChatInformations item) {
            binding.setChatItem(item);
            if(!item.isRead())
                binding.chatTitle.setTypeface(null, Typeface.BOLD);
            binding.deleteChat.setOnClickListener(v -> parent.delete(item));
            binding.chatListItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    parent.open(item);
                }
            });
        }
    }

    @Override
    public ChatItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ChatItemViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.chat_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(ChatItemViewHolder holder, int position) {
        holder.bind(chatsList.get(position));
    }

    @Override
    public int getItemCount() {
        return chatsList.size();
    }

}
