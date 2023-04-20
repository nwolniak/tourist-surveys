import { marker } from 'leaflet';
import './App.css';
import "leaflet/dist/leaflet.css"

import { MapContainer, TileLayer, Marker, Popup } from 'react-leaflet';
import { Icon, divIcon, point } from 'leaflet';
import MarkerClousterGroup from 'react-leaflet-markercluster';

function App() {

  const markers = [
    {
      geocode: [50.0614, 19.9365],
      name: "Kraków"
    },
    {
      geocode: [50.0654, 19.9395],
      name: "Kraków 2"
    },
    {
      geocode: [50.0714, 19.9465],
      name: "Kraków 3"
    }
  ]

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
  <div className="App">
    <h1>Tourist surveys</h1>

    {/* KRK */}
    <MapContainer center={[50.0614, 19.9365]} zoom={13} >

      <TileLayer
        url="https:/{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
      />

      <MarkerClousterGroup
        chunkedLoading={true}
        iconCreateFunction={createCustomClusterIcon}
      >
        {markers.map(marker => (
          <Marker position={marker.geocode} icon={customIcon}>
            <Popup position={marker.geocode}> <h1>{marker.name}</h1></Popup>
          </Marker>
        ))
        }

      </MarkerClousterGroup>

    </MapContainer>

  </div >
);
}

export default App;
