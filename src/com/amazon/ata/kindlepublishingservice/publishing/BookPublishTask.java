package com.amazon.ata.kindlepublishingservice.publishing;

import com.amazon.ata.kindlepublishingservice.dao.CatalogDao;
import com.amazon.ata.kindlepublishingservice.dao.PublishingStatusDao;
import com.amazon.ata.kindlepublishingservice.dynamodb.models.CatalogItemVersion;
import com.amazon.ata.kindlepublishingservice.enums.PublishingRecordStatus;
import com.amazon.ata.kindlepublishingservice.exceptions.BookNotFoundException;

import javax.inject.Inject;

public class BookPublishTask implements Runnable {

    private BookPublishRequestManager bookPublishRequestManager;
    private PublishingStatusDao publishingStatusDao;
    private CatalogDao catalogDao;

    @Inject
    public BookPublishTask(BookPublishRequestManager bookPublishRequestManager,
                           PublishingStatusDao publishingStatusDao,
                           CatalogDao catalogDao) {
        this.bookPublishRequestManager = bookPublishRequestManager;
        this.publishingStatusDao = publishingStatusDao;
        this.catalogDao = catalogDao;
    }

    @Override
    public void run() {

        while (true) {
            BookPublishRequest bookPublishRequest = bookPublishRequestManager.getBookPublishRequestToProcess();

            if (bookPublishRequest == null) {
                return;
            }

            publishingStatusDao.setPublishingStatus(bookPublishRequest.getPublishingRecordId(),
                    PublishingRecordStatus.IN_PROGRESS,
                    bookPublishRequest.getBookId());

            KindleFormattedBook kindleFormattedBook = KindleFormatConverter.format(bookPublishRequest);

            CatalogItemVersion catalogItemVersion = catalogDao.createOrUpdateBook(kindleFormattedBook);

            publishingStatusDao.setPublishingStatus(bookPublishRequest.getPublishingRecordId(),
                    PublishingRecordStatus.SUCCESSFUL, catalogItemVersion.getBookId());

            return;
        }
    }
}
