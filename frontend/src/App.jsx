import './App.css';
import "leaflet/dist/leaflet.css"

import DataLoader from './components/DataLoader';
import Map from './components/Map';
import Header from './components/Header';
import Footer from './components/Footer';
import { useState } from "react";
import Survey from './components/Survey';

function App() {

const [markerArray, setMarkerArray] = useState([])
const [SurveyData, setSurveyData] = useState([])

const handleMapData = (data) =>
{
  setMarkerArray(data)
  // console.log(data)
}

return (
  <div className="App">
    
    <Header />
    <DataLoader handleMapData={handleMapData} setSurveyData={setSurveyData}/>

    <Map markerArray={markerArray}/>

    <Survey markerArray={markerArray} SurveyData={SurveyData}/>

    <Footer />
  </div >
);
}

export default App;
