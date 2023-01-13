package com.example.putinder.content_screen.map_screen

import android.widget.Toast
import com.yandex.mapkit.ScreenPoint


//class GeoCodeOverlay(mapController: MapController?) : Overlay(mapController),
//    GeoCodeListener {
//    fun onFinishGeoCode(geoCode: GeoCode?): Boolean {
//        if (geoCode != null) {
//            getMapController().getMapView().post(Runnable { // show display name of the point
//                Toast.makeText(
//                    getMapController().getContext(),
//                    geoCode.getDisplayName(), Toast.LENGTH_LONG
//                ).show()
//            })
//        }
//        return true
//    }
//
//    fun onSingleTapUp(x: Float, y: Float): Boolean {
//        getMapController().getDownloader()
//            .getGeoCode(this, getMapController().getGeoPoint(ScreenPoint(x, y)))
//        return true
//    }
//}