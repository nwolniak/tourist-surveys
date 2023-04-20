import { marker } from 'leaflet';
import './App.css';
import "leaflet/dist/leaflet.css"

import { MapContainer, TileLayer, Marker, Popup } from 'react-leaflet';
import { Icon } from 'leaflet';

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


  return (
    <div className="App">
      <h1>Tourist surveys</h1>

      {/* KRK */}
      <MapContainer center={[50.0614, 19.9365]} zoom={13} >

        <TileLayer
          url="https:/{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
        />

        {markers.map(marker => (
          <Marker position={marker.geocode} icon={customIcon}>
            <Popup position={marker.geocode}> <h1>{marker.name}</h1></Popup>
          </Marker>
        ))

        }

      </MapContainer>
    </div>
  );
}

export default App;
