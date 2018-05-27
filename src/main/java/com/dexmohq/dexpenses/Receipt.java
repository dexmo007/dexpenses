package com.dexmohq.dexpenses;

import com.dexmohq.dexpenses.categorize.Category;
import lombok.Data;

import javax.money.MonetaryAmount;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * @author Henrik Drefs
 */
@Data
public class Receipt {

    private String[] header;
    private Category category;
    private LocalDate date;
    private LocalTime time;
    private MonetaryAmount total;
    private PaymentMethod paymentMethod;

    // todo receipt items

}
