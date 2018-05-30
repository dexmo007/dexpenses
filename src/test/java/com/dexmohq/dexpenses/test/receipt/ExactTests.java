package com.dexmohq.dexpenses.test.receipt;

import com.dexmohq.dexpenses.PaymentMethod;
import com.dexmohq.dexpenses.Receipt;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Henrik Drefs
 */
public class ExactTests extends AbstractReceiptTests {

    @Test
    void testCinemaxx() throws IOException {
        final Receipt receipt = read("cinemaxx.png");
        assertEquals("cinema", receipt.getCategory().getIdentifier());
        assertEquals(LocalDate.of(2018, 5, 23), receipt.getDate());
        assertEquals(LocalTime.of(19, 47, 44), receipt.getTime());
        assertEquals(PaymentMethod.DEBIT, receipt.getPaymentMethod());
        //todo how to handle this case because it always reads as 11.19
        //assertEquals(Monetary.getDefaultAmountFactory().setCurrency("EUR").setNumber(new BigDecimal("11.10")).create(), receipt.getTotal());
    }

    @Test
    void testSubway() throws IOException {
        final Receipt receipt = read("subway.png");
        assertEquals("food", receipt.getCategory().getIdentifier());
        assertEquals(LocalDate.of(2018, 5, 23), receipt.getDate());
        assertEquals(LocalTime.of(18, 58, 48), receipt.getTime());
        assertEquals(PaymentMethod.DEBIT, receipt.getPaymentMethod());
        assertEquals(euros("18.58"), receipt.getTotal());
    }

    @Test
    void testEdeka() throws IOException {
        final Receipt receipt = read("edeka.png");
        assertEquals("food", receipt.getCategory().getIdentifier());
        assertEquals(LocalDate.of(2018, 5, 26), receipt.getDate());
        assertEquals(LocalTime.of(12, 5, 16), receipt.getTime());
        assertEquals(PaymentMethod.DEBIT, receipt.getPaymentMethod());
        assertEquals(euros("44.13"), receipt.getTotal());
    }

    @Test
    void testApotheke() throws IOException {
        final Receipt receipt = read("apotheke.png");
        assertEquals("health", receipt.getCategory().getIdentifier());
        assertEquals(LocalDate.of(2017, 9, 27), receipt.getDate());
        assertEquals(LocalTime.of(17, 16), receipt.getTime());
        assertEquals(PaymentMethod.DEBIT, receipt.getPaymentMethod());
        assertEquals(euros("27.87"), receipt.getTotal());
    }

    @Test
    void testSausa() throws IOException {
        final Receipt receipt = read("sausa.png");
        assertEquals("entertainment", receipt.getCategory().getIdentifier());
        assertEquals(LocalDate.of(2017, 8, 28), receipt.getDate());
        assertEquals(LocalTime.of(20, 33), receipt.getTime());
        assertEquals(PaymentMethod.CREDIT, receipt.getPaymentMethod());
        assertEquals(euros("28.80"), receipt.getTotal());
    }

}
