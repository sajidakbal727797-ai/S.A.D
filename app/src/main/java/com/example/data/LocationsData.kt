package com.example.data

import com.example.model.LocationPoint

object LocationsData {
  val defaultLocations = listOf(
    LocationPoint(
      name = "Hazratganj Metro Station",
      address = "MG Marg, Hazratganj, Lucknow",
      latitude = 26.8525,
      longitude = 80.9462,
      category = "Hub",
    ),
    LocationPoint(
      name = "Charbagh Railway Station",
      address = "Lucknow Junction, Charbagh",
      latitude = 26.8322,
      longitude = 80.9202,
      category = "Transit",
    ),
    LocationPoint(
      name = "Chaudhary Charan Singh Airport (Amausi)",
      address = "VIP Road, Amausi Airport Terminal",
      latitude = 26.7606,
      longitude = 80.8893,
      category = "Airport",
    ),
    LocationPoint(
      name = "Gomti Nagar Tech Hub",
      address = "Vibhuti Khand, Gomti Nagar",
      latitude = 26.8668,
      longitude = 81.0028,
      category = "Tech Park",
    ),
    LocationPoint(
      name = "Phoenix Palassio Mall",
      address = "Sector 7, Amar Shaheed Path",
      latitude = 26.8041,
      longitude = 80.9995,
      category = "Shopping",
    ),
    LocationPoint(
      name = "Lulu Mall & Hypermarket",
      address = "Shaheed Path, Sushant Golf City",
      latitude = 26.7725,
      longitude = 81.0112,
      category = "Shopping",
    ),
    LocationPoint(
      name = "Chowk Heritage Gate",
      address = "Chowk Old City, Victoria Street",
      latitude = 26.8687,
      longitude = 80.9069,
      category = "Heritage",
    ),
    LocationPoint(
      name = "S.A.D Enterprises Corporate Office",
      address = "Ring Road Business Park, Sector B",
      latitude = 26.8841,
      longitude = 80.9634,
      category = "HQ",
    )
  )

  fun calculateDistanceKm(loc1: LocationPoint, loc2: LocationPoint): Double {
    val dLat = Math.toRadians(loc2.latitude - loc1.latitude)
    val dLon = Math.toRadians(loc2.longitude - loc1.longitude)
    val a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
        Math.cos(Math.toRadians(loc1.latitude)) * Math.cos(Math.toRadians(loc2.latitude)) *
        Math.sin(dLon / 2) * Math.sin(dLon / 2)
    val c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a))
    val distance = 6371 * c // Earth radius in km
    val roadCurvatureFactor = 1.35 // Road vs straight line factor
    val estimatedRoadDist = distance * roadCurvatureFactor
    return Math.max(1.5, Math.round(estimatedRoadDist * 10.0) / 10.0)
  }
}
