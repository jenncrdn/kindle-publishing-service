package com.amazon.ata.kindlepublishingservice.activity;

import com.amazon.ata.kindlepublishingservice.converters.BookPublishRequestConverter;
import com.amazon.ata.kindlepublishingservice.dao.PublishingStatusDao;
import com.amazon.ata.kindlepublishingservice.dynamodb.models.PublishingStatusItem;
import com.amazon.ata.kindlepublishingservice.models.PublishingStatusRecord;
import com.amazon.ata.kindlepublishingservice.models.requests.GetPublishingStatusRequest;
import com.amazon.ata.kindlepublishingservice.models.response.GetPublishingStatusResponse;
import com.amazonaws.services.lambda.runtime.Context;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class GetPublishingStatusActivity {
    private PublishingStatusDao publishingStatusDao;
    @Inject
    public GetPublishingStatusActivity(PublishingStatusDao publishingStatusDao) {
        this.publishingStatusDao = publishingStatusDao;
    }

    public GetPublishingStatusResponse execute(GetPublishingStatusRequest publishingStatusRequest) {
        List<PublishingStatusItem> publishingStatus = publishingStatusDao.GetPublishingStatus(publishingStatusRequest.getPublishingRecordId());
        List<PublishingStatusRecord> records = new ArrayList<>();

        for (PublishingStatusItem i : publishingStatus) {
            records.add(PublishingStatusRecord.builder()
                    .withBookId(i.getBookId())
                    .withStatus(i.getStatus().toString())
                    .withStatusMessage(i.getStatusMessage())
                    .build());
        }

        return GetPublishingStatusResponse.builder()
                .withPublishingStatusHistory(records)
                .build();
    }
}
