package com.truelayer.java.payments.entities.verification;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Value;

@Getter
@Value
@EqualsAndHashCode(callSuper = false)
public class AutomatedVerification extends Verification {
    Verification.Type type = Type.AUTOMATED;

    boolean remitterName;

    boolean remitterDateOfBirth;

    @JsonIgnore
    public static AutomatedVerificationBuilder builder() {
        return new AutomatedVerificationBuilder();
    }

    /**
     * Custom builder for the AutomatedVerification class that prevents setting remitter flags to false
     */
    public static class AutomatedVerificationBuilder {
        private boolean remitterName;

        private boolean remitterDateOfBirth;

        public AutomatedVerification.AutomatedVerificationBuilder withRemitterName() {
            this.remitterName = true;
            return this;
        }

        public AutomatedVerification.AutomatedVerificationBuilder withRemitterDateOfBirth() {
            this.remitterDateOfBirth = true;
            return this;
        }

        public AutomatedVerification build() {
            return new AutomatedVerification(remitterName, remitterDateOfBirth);
        }
    }
}
