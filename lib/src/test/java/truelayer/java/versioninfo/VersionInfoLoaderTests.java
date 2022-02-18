package truelayer.java.versioninfo;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class VersionInfoLoaderTests {

    @Test
    @DisplayName("It should load version info from resources abd build a version info object")
    public void itShouldBuildALiveConfiguration() {
        VersionInfoLoader sut = new VersionInfoLoader();

        VersionInfo versionInfo = sut.load();

        assertNotNull(versionInfo);
    }
}
