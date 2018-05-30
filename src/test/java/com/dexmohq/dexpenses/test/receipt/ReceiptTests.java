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
public class ReceiptTests extends AbstractReceiptTests {

    @Test
    void testFigaroSalon() throws IOException {
        final Receipt receipt = read("figaro-salon.png");
        assertEquals("barber", receipt.getCategory().getIdentifier());
        assertEquals(LocalDate.of(2016, 12, 8), receipt.getDate());
        assertEquals(LocalTime.of(11, 56), receipt.getTime());
        assertEquals(PaymentMethod.DEBIT, receipt.getPaymentMethod());
        assertEquals(euros("19.50"), receipt.getTotal());
    }

    @Test
    void testHaargenau() throws IOException {
        final Receipt receipt = read("haargenau.png");
        assertEquals("barber", receipt.getCategory().getIdentifier());
        assertEquals(LocalDate.of(2017, 8, 31), receipt.getDate());
        assertEquals(LocalTime.of(16, 51), receipt.getTime());
        assertEquals(PaymentMethod.DEBIT, receipt.getPaymentMethod());
        assertEquals(euros("15"), receipt.getTotal());
    }

    @Test
    void testBarrique() throws IOException {
        final Receipt receipt = read("barrique.png");
        assertEquals("food", receipt.getCategory().getIdentifier());
        assertEquals(LocalDate.of(2017, 8, 30), receipt.getDate());
        assertEquals(LocalTime.of(17, 53), receipt.getTime());
        assertEquals(PaymentMethod.DEBIT, receipt.getPaymentMethod());
        assertEquals(euros("23.70"), receipt.getTotal());
    }

    @Test
    void testAkiaHandyshop() throws IOException {
        final Receipt receipt = read("akia-handyshop.png");
        assertEquals("tech", receipt.getCategory().getIdentifier());
        assertEquals(LocalDate.of(2017, 9, 1), receipt.getDate());
        assertEquals(LocalTime.of(15, 23), receipt.getTime());
        assertEquals(PaymentMethod.DEBIT, receipt.getPaymentMethod());
        assertEquals(euros("17.99"), receipt.getTotal());
    }

    @Test
    void testFleischereiHass() throws IOException {
        final Receipt receipt = read("fleischerei-hass2.png");
        assertEquals("food", receipt.getCategory().getIdentifier());
        assertEquals(LocalDate.of(2017, 9, 23), receipt.getDate());
        assertEquals(LocalTime.of(12, 22, 59), receipt.getTime());
        assertEquals(PaymentMethod.CASH, receipt.getPaymentMethod());
        assertEquals(euros("7.60"), receipt.getTotal());
    }

    @Test
    void testFleischereiHeine() throws IOException {
        final Receipt receipt = read("fleischerei-heine.png");
        assertEquals("food", receipt.getCategory().getIdentifier());
        assertEquals(LocalDate.of(2017, 9, 28), receipt.getDate());
        assertEquals(LocalTime.of(15, 46, 57), receipt.getTime());
        assertEquals(PaymentMethod.UNKNOWN, receipt.getPaymentMethod());
        assertEquals(euros("8.97"), receipt.getTotal());
    }
}
