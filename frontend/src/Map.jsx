import "leaflet/dist/leaflet.css"

import { MapContainer, TileLayer, Marker, Popup, Polyline } from 'react-leaflet';
import { Icon, divIcon, point } from 'leaflet';
import MarkerClousterGroup from '@changey/react-leaflet-markercluster';
import { useState, useEffect } from "react";

export default function Map(props) {


    const [markers, setMarkers] = useState(props.markerArray);

    useEffect(() => {
        setMarkers(props.markerArray);
    }, [props.markerArray]);

    const customIcon = new Icon({
        iconUrl: 'https://cdn0.iconfinder.com/data/icons/small-n-flat/24/678111-map-marker-512.png',
        iconSize: [38, 38],
    })

    const createCustomClusterIcon = (cluster) => {
        return new divIcon({
            html: `<div class="cluster-icon">${cluster.getChildCount()}</div>`,
            className: 'marker-cluster-custom',
            iconSize: point(33, 33, true),
        })
    }


    return (
        <div className="map">
            {/* KRK */}
            <MapContainer center={[50.0614, 19.9365]} zoom={8} >

                <TileLayer
                    url="https:/{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
                />

                <MarkerClousterGroup
                    chunkedLoading={true}
                    iconCreateFunction={createCustomClusterIcon}
                >
                    {markers.map(marker => (
                        <Marker position={marker.geocode} icon={customIcon} key={marker.timestamp}>
                            <Popup position={marker.geocode}> <h1>{marker.timestamp}</h1></Popup>
                        </Marker>
                    ))
                    }

                    <Polyline pathOptions={{ color: 'red' }} positions={markers.map(marker => marker.geocode)} />


                </MarkerClousterGroup>

            </MapContainer>
        </div>
    );
}
