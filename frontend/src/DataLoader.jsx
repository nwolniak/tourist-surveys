import { marker } from "leaflet";
import React, { useEffect, useState } from "react";

function DataLoader(props) {
  const [file, setFile] = useState();
  const [array, setArray] = useState([]);

  const handleOnChange = (e) => {
    setFile(e.target.files[0]);
  };

  const csvFileToArray = (string) => {
    const csvHeader = string.slice(0, string.indexOf("\n")).split(";");
    const csvRows = string.slice(string.indexOf("\n") + 1).split("\n");

    const array = csvRows.map((i) => {
      const values = i.split(";");
      const obj = csvHeader.reduce((object, header, index) => {
        object[header] = values[index];
        return object;
      }, {});
      return obj;
    });

    setArray(array);
  };

  const handleOnSubmit = (e) => {
    e.preventDefault();

    if (file) {
      const fileReader = new FileReader();
      fileReader.onload = function (event) {
        const text = event.target.result;
        csvFileToArray(text);
      };

      fileReader.readAsText(file);
    }
  };

  const prepareMarkerArray = () => {
    const markers = array.map(item => {
      const lattitude = item["latitude"]
      const longitude = item["longitude"]

      const newLattitude = lattitude ? parseFloat(lattitude.replace(",", ".")) : ""
      const newLongitude = longitude ? parseFloat(longitude.replace(",", ".")) : ""

      if (item["timestamp"] !== undefined) {
        return {
          geocode: [newLattitude, newLongitude],
          timestamp: item["timestamp"]
        }
      } else {
        return null
      }
    }).filter(item => item !== null)
    console.log(markers)
    props.hadleMapData(markers)
  }

  useEffect(() => {
    prepareMarkerArray()
  }, [array])

  // const headerKeys = Object.keys(Object.assign({}, ...array));

  return (
    <div style={{ textAlign: "center" }}>
      <form>
        <input
          type="file"
          id="csvFileInput"
          accept=".csv"
          onChange={handleOnChange}
        />

        <button onClick={handleOnSubmit}>IMPORT CSV</button>
      </form>
{/* 
      <br />

      <table>
        <thead>
          <tr key="header">
            {headerKeys.map((key) => (
              <th key={key}>{key}</th>
            ))}
          </tr>
        </thead>

        <tbody>
          {array.map((item, index) => (
            <tr key={index}>
              {Object.values(item).map((val, index) => (
                <td key={index}>{val}</td>
              ))}
            </tr>
          ))}
        </tbody>
      </table> */}
    </div>
  );
}

export default DataLoader;
