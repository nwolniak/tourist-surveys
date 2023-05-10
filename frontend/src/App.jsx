import './App.css';
import "leaflet/dist/leaflet.css"

import DataLoader from './DataLoader';
import Map from './Map';
import { useState } from "react";

function App() {

const [markerArray, setMarkerArray] = useState([])

const hadleMapData = (data) =>
{
  setMarkerArray(data)
  // console.log(data)
}

return (
  <div className="App">
    <h1>Tourist surveys</h1>

    <DataLoader hadleMapData={hadleMapData}/>

    <Map markerArray={markerArray}/>
  </div >
);
}

export default App;
