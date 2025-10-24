package com.truelayer.java.payouts.entities.beneficiary.providerselection;

import static org.junit.jupiter.api.Assertions.*;

import com.truelayer.java.TrueLayerException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ProviderSelectionTests {

    @Test
    @DisplayName("It should yield true if instance is of type UserSelectedProviderSelection")
    public void shouldYieldTrueIfUserSelectedProviderSelection() {
        ProviderSelection sut = ProviderSelection.userSelected().build();

        assertTrue(sut.isUserSelected());
    }

    @Test
    @DisplayName("It should convert to an instance of class UserSelectedProviderSelection")
    public void shouldConvertToUserSelectedProviderSelection() {
        ProviderSelection sut = ProviderSelection.userSelected().build();

        assertDoesNotThrow(sut::asUserSelected);
    }

    @Test
    @DisplayName("It should throw an error when converting to UserSelectedProviderSelection")
    public void shouldNotConvertToUserSelectedProviderSelection() {
        ProviderSelection sut =
                ProviderSelection.preselected().providerId("a-provider-id").build();

        Throwable thrown = assertThrows(TrueLayerException.class, sut::asUserSelected);

        assertEquals(
                String.format(
                        "Provider selection is of type %s.", sut.getClass().getSimpleName()),
                thrown.getMessage());
    }

    @Test
    @DisplayName("It should yield true if instance is of type PreselectedProviderSelection")
    public void shouldYieldTrueIfPreselectedProviderSelection() {
        ProviderSelection sut =
                ProviderSelection.preselected().providerId("a-provider-id").build();

        assertTrue(sut.isPreselected());
    }

    @Test
    @DisplayName("It should convert to an instance of class PreselectedProviderSelection")
    public void shouldConvertToPreselectedProviderSelection() {
        ProviderSelection sut =
                ProviderSelection.preselected().providerId("a-provider-id").build();

        assertDoesNotThrow(sut::asPreselected);
    }

    @Test
    @DisplayName("It should throw an error when converting to PreselectedProviderSelection")
    public void shouldNotConvertToPreselectedProviderSelection() {
        ProviderSelection sut = ProviderSelection.userSelected().build();

        Throwable thrown = assertThrows(TrueLayerException.class, sut::asPreselected);

        assertEquals(
                String.format(
                        "Provider selection is of type %s.", sut.getClass().getSimpleName()),
                thrown.getMessage());
    }
}
