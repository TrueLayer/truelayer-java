package com.truelayer.java.payments.entities.paymentdetail.forminput;

import com.truelayer.java.payments.entities.paymentdetail.forminput.image.Image;
import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = true)
public class TextWithImage extends TextBase {
    Type type = Type.TEXT_WITH_IMAGE;

    Image image;
}
