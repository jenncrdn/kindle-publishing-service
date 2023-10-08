package com.amazon.ata.kindlepublishingservice.publishing;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

@Singleton
public class BookPublishRequestManager {
    private Queue<BookPublishRequest> bookPublishRequestQueue = new ConcurrentLinkedQueue<>();

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
