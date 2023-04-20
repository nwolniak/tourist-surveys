import './App.css';
import "leaflet/dist/leaflet.css"

import { MapContainer, TileLayer } from 'react-leaflet';

function App() {
  return (
    <div className="App">
      <h1>Tourist surveys</h1>

      {/* KRK */}
      <MapContainer center={[50.0614, 19.9365]} zoom={13} >

        <TileLayer
          url="https:/{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
        />

      </MapContainer>
    </div>
  );
}

export default App;
