package com.truelayer.java.payments.entities.paymentmethod.provider;

import static org.junit.jupiter.api.Assertions.*;

import com.truelayer.java.TrueLayerException;
import com.truelayer.java.entities.providerselection.ProviderSelection;
import com.truelayer.java.entities.providerselection.UserSelectedProviderSelection;
import com.truelayer.java.payments.entities.providerselection.PreselectedProviderSelection;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ProviderSelectionTests {

    @Test
    @DisplayName("It should yield true if instance is of type UserSelectedProviderSelection")
    public void shouldYieldTrueIfUserSelectedProviderSelection() {
        ProviderSelection sut = UserSelectedProviderSelection.userSelected().build();

        assertTrue(sut.isUserSelected());
    }

    @Test
    @DisplayName("It should convert to an instance of class UserSelectedProviderSelection")
    public void shouldConvertToProviderSelection() {
        ProviderSelection sut = UserSelectedProviderSelection.userSelected().build();

        assertDoesNotThrow(sut::asUserSelected);
    }

    @Test
    @DisplayName("It should throw an error when converting to UserSelectedProviderSelection")
    public void shouldNotConvertToUserSelectedProviderSelection() {
        ProviderSelection sut = PreselectedProviderSelection.builder().build();

        Throwable thrown = assertThrows(TrueLayerException.class, sut::asUserSelected);

        assertEquals(
                String.format(
                        "Provider selection is of type %s.", sut.getClass().getSimpleName()),
                thrown.getMessage());
    }

    @Test
    @DisplayName("It should yield true if instance is of type PreselectedProviderSelection")
    public void shouldYieldTrueIfPreselectedProviderSelection() {
        ProviderSelection sut = PreselectedProviderSelection.preselected().build();

        assertTrue(sut.isPreselected());
    }

    @Test
    @DisplayName("It should convert to an instance of class PreselectedProviderSelection")
    public void shouldConvertToPreselectedProviderSelection() {
        ProviderSelection sut = PreselectedProviderSelection.preselected().build();

        assertDoesNotThrow(sut::asPreselected);
    }

    @Test
    @DisplayName("It should throw an error when converting to PreselectedProviderSelection")
    public void shouldNotConvertToPreselectedProviderSelection() {
        ProviderSelection sut = UserSelectedProviderSelection.builder().build();

        Throwable thrown = assertThrows(TrueLayerException.class, sut::asPreselected);

        assertEquals(
                String.format(
                        "Provider selection is of type %s.", sut.getClass().getSimpleName()),
                thrown.getMessage());
    }
}
