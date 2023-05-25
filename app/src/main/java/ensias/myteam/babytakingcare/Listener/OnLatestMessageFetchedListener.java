package ensias.myteam.babytakingcare.Listener;

import ensias.myteam.babytakingcare.Models.Message;

public interface OnLatestMessageFetchedListener {
    void onLatestMessageFetched(Message latestMessage, String conversationId);
}