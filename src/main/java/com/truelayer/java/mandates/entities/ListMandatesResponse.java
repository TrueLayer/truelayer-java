package com.truelayer.java.mandates.entities;

import com.truelayer.java.entities.PaginationMetadata;
import com.truelayer.java.mandates.entities.mandatedetail.MandateDetail;
import java.util.List;
import lombok.Value;

@Value
public class ListMandatesResponse {
    List<MandateDetail> items;

    PaginationMetadata pagination;
}
