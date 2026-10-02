package com.majinnaibu.monstercards.data;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.majinnaibu.monstercards.data.enums.GameSystem;
import com.majinnaibu.monstercards.models.ImportSource;

import org.junit.Test;

import java.util.List;

public class CompendiumSourceManagerTest {

    @Test
    public void testAvailableCompendiumSources() {
        List<ImportSource> sources = ImportConfig.SOURCES;
        assertNotNull(sources);
        assertTrue(sources.size() >= 3);

        ImportSource open5e = null;
        ImportSource pf2e = null;
        ImportSource sf2e = null;

        for (ImportSource source : sources) {
            if ("open5e".equals(source.id)) open5e = source;
            if ("pf2e_foundry".equals(source.id)) pf2e = source;
            if ("sf2e_foundry".equals(source.id)) sf2e = source;
        }

        assertNotNull(open5e);
        assertEquals(GameSystem.DND_5E, open5e.gameSystem);
        assertEquals(ImportSource.ImportType.OPEN5E_API, open5e.importType);

        assertNotNull(pf2e);
        assertEquals(GameSystem.PF_2E, pf2e.gameSystem);
        assertEquals(ImportSource.ImportType.GIT_ARCHIVE, pf2e.importType);
        assertNotNull(pf2e.downloadUrl);

        assertNotNull(sf2e);
        assertEquals(GameSystem.SF_2E, sf2e.gameSystem);
        assertEquals(ImportSource.ImportType.GIT_ARCHIVE, sf2e.importType);
        assertNotNull(sf2e.downloadUrl);
    }
}
