import './App.css';
import "leaflet/dist/leaflet.css"

import DataLoader from './components/DataLoader';
import Map from './components/Map';
import Header from './components/Header';
import Footer from './components/Footer';
import { useState } from "react";

function App() {

const [markerArray, setMarkerArray] = useState([])

const handleMapData = (data) =>
{
  setMarkerArray(data)
  // console.log(data)
}

return (
  <div className="App">
    
    <Header />
    <DataLoader handleMapData={handleMapData}/>

    <Map markerArray={markerArray}/>

    <Footer />
  </div >
);
}

export default App;
