package truelayer.java.versioninfo;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Unit test class for the mechanism that loads the library version information.
 * As the version info file is created at build as part of a Gradle task, we're not
 * testing the case where we miss that file inside the resources' directory.
 */
class VersionInfoLoaderTests {

    @Test
    @DisplayName("It should load version info from resources abd build a version info object")
    public void itShouldBuildALiveConfiguration() {
        VersionInfoLoader sut = new VersionInfoLoader();

        VersionInfo versionInfo = sut.load();

        Assertions.assertNotNull(versionInfo);
    }
}
