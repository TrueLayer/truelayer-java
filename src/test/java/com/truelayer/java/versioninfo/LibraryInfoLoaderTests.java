package com.truelayer.java.versioninfo;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class LibraryInfoLoaderTests {

    @Test
    @DisplayName("It should load version info from resources abd build a version info object")
    public void itShouldBuildALiveConfiguration() {
        LibraryInfoLoader sut = new LibraryInfoLoader();

        VersionInfo versionInfo = sut.load();

g        assertNotNull(versionInfo);
    }
}
