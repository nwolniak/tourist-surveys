import './App.css';
import "leaflet/dist/leaflet.css"

import DataLoader from './DataLoader';
import Map from './Map';

function App() {

const hadleMapData = (data) =>
{
  console.log(data)
}

return (
  <div className="App">
    <h1>Tourist surveys</h1>

    <DataLoader hadleMapData={hadleMapData}/>

    <Map/>
  </div >
);
}

export default App;
