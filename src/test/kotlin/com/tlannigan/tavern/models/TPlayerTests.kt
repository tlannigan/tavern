package com.tlannigan.tavern.models

import be.seeseemelk.mockbukkit.MockBukkit
import be.seeseemelk.mockbukkit.ServerMock
import be.seeseemelk.mockbukkit.entity.PlayerMock
import com.tlannigan.tavern.extensions.toTPlayer
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class TPlayerTests {
    private lateinit var server: ServerMock
    private lateinit var player: PlayerMock
    private lateinit var tPlayer: TPlayer

    @BeforeEach
    fun setUp() {
        server = MockBukkit.mock()
        player = server.addPlayer()
        tPlayer = player.toTPlayer()
    }

    @AfterEach
    fun tearDown() {
        MockBukkit.unmock()
    }

    @Test
    fun whenCampaignCreated_AddCampaignToTPlayer() {
        tPlayer.createCampaign("Test")
        assertEquals(1, tPlayer.campaigns.size)
    }

    @Test
    fun whenCampaignCreatedWithDuplicateName_DontCreateCampaign() {
        tPlayer.createCampaign("Test")
        tPlayer.createCampaign("Test")
        assertEquals(1, tPlayer.campaigns.size)
    }

    @Test
    fun whenCampaignCreatedByPlayerInActiveCampaign_DontCreateCampaign() {
        tPlayer.createCampaign("Test")
        tPlayer.enterCampaign("Test")
        tPlayer.createCampaign("Test 2")
        assertEquals(1, tPlayer.campaigns.size)
    }

    @Test
    fun whenCampaignEnteredByPlayerInActiveCampaign_DontEnterCampaign() {
        tPlayer.createCampaign("Test")
        val testCampaign1 = tPlayer.campaigns[0]
        tPlayer.createCampaign("Test 2")
        tPlayer.enterCampaign("Test")
        tPlayer.enterCampaign("Test 2")
        assertEquals(testCampaign1, tPlayer.activeCampaign)
    }

    @Test
    fun whenCampaignEnteredByPlayerWithoutAnyCampaigns_DontEnterCampaign() {
        tPlayer.enterCampaign("Test")
        assertNull(tPlayer.activeCampaign)
    }

    @Test
    fun whenCampaignEnteredWithWrongName_DontEnterCampaign() {
        tPlayer.createCampaign("Test")
        tPlayer.enterCampaign("Test 2")
        assertNull(tPlayer.activeCampaign)
    }

    @Test
    fun whenCampaignEnteredWithDuplicateNames_DontEnterCampaignImmediately() {
        tPlayer.createCampaign("Test")
        tPlayer.campaigns.add(tPlayer.campaigns[0])
        tPlayer.enterCampaign("Test")
        assertNull(tPlayer.activeCampaign)
    }
}