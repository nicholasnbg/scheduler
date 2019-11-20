package com.nbgdev.scheduler.repository.Zones

import java.util.UUID

import com.nbgdev.scheduler.model.{Commute, Zone}
import org.specs2.mutable.Specification

class CommuteTransformerTest extends Specification {
  "CommuteTransformer" should {
    "validate new zone" in {
      val existingZone1 = Zone(UUID.randomUUID(), "Zone1", Vector.empty)
      val existingZone2 = Zone(UUID.randomUUID(), "Zone2", Vector.empty)

      "with valid commutes" in {
        val commute1 = Commute(existingZone1.id, 30)
        val commute2 = Commute(existingZone2.id, 30)
        val newZone = Zone(UUID.randomUUID(), "Some Zone", Vector(commute1, commute2))

        val result = CommuteTransformer.validCommutes(newZone, Vector(existingZone1, existingZone2))
        result must beRight(newZone)
      }

      "with invalid commutes" in {
        val newZone = Zone(UUID.randomUUID(), "Some Zone", Vector.empty)

        val result = CommuteTransformer.validCommutes(newZone, Vector(existingZone1, existingZone2))
        result must beLeft
      }
    }
  }
}
