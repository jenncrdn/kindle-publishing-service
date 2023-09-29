package com.amazon.ata.kindlepublishingservice.publishing;

import javax.inject.Inject;
import java.util.LinkedList;
import java.util.Queue;

public class BookPublishRequestManager {
    private Queue<BookPublishRequest> bookPublishRequestQueue = new LinkedList<>();

    @Inject
    public BookPublishRequestManager() {

    }

    public BookPublishRequest addBookPublishRequest(BookPublishRequest request) {
        bookPublishRequestQueue.offer(request);
        return request;
    }

    public BookPublishRequest getBookPublishRequestToProcess() {
        if (bookPublishRequestQueue.isEmpty()) {
            return null;
        }
        return bookPublishRequestQueue.poll();
    }
}
